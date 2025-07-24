#!/bin/bash

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 项目配置
COMPOSE_PROJECT_NAME=${COMPOSE_PROJECT_NAME:-scaffolding}
NETWORK_NAME=${NETWORK_NAME:-scaffolding-network}

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DOCKER_DIR="$(dirname "$SCRIPT_DIR")"

print_usage() {
    echo "用法: $0 [命令] [选项]"
    echo ""
    echo "命令:"
    echo "  stop          停止所有服务 (默认)"
    echo "  clean         停止服务并删除所有数据"
    echo "  status        查看服务状态"
    echo "  logs          查看服务日志"
    echo "  backup        备份数据"
    echo "  restart       重启服务"
    echo ""
    echo "示例:"
    echo "  $0                    # 停止所有服务"
    echo "  $0 clean              # 停止服务并删除数据"
    echo "  $0 status             # 查看状态"
    echo "  $0 logs mysql         # 查看MySQL日志"
    echo "  $0 backup             # 备份数据"
    echo "  $0 restart            # 重启所有服务"
}

show_status() {
    echo -e "${BLUE}📊 服务状态${NC}"
    echo "================================"

    cd "$DOCKER_DIR"

    # 显示容器状态
    echo "🐳 容器状态:"
    docker-compose ps

    echo ""
    echo "🌐 网络状态:"
    docker network ls --filter "name=${NETWORK_NAME}"

    echo ""
    echo "💾 数据卷状态:"
    docker volume ls --filter "name=${COMPOSE_PROJECT_NAME}-" --format "table {{.Driver}}\t{{.Name}}\t{{.Size}}"

    echo ""
    echo "📈 资源使用:"
    running_containers=$(docker ps --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Names}}")
    if [ -n "$running_containers" ]; then
        docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.MemPerc}}" $running_containers
    else
        echo "没有运行中的容器"
    fi
}

show_logs() {
    local service=$1

    cd "$DOCKER_DIR"

    if [ -z "$service" ]; then
        echo "📖 查看所有服务日志 (Ctrl+C 退出)..."
        docker-compose logs -f --tail=50
    else
        echo "📖 查看 $service 服务日志 (Ctrl+C 退出)..."
        docker-compose logs -f --tail=100 "$service"
    fi
}

stop_services() {
    echo -e "${YELLOW}🛑 停止服务...${NC}"

    cd "$DOCKER_DIR"

    if docker-compose down; then
        echo -e "${GREEN}✅ 所有服务已停止${NC}"
    else
        echo -e "${RED}❌ 停止服务时出现错误${NC}"
    fi
}

clean_all() {
    echo -e "${RED}🗑️  清理所有数据${NC}"
    echo ""
    echo -e "${RED}⚠️  警告: 这将删除以下内容:${NC}"
    echo "  - 所有容器"
    echo "  - 所有数据卷"
    echo "  - 所有网络"
    echo ""

    read -p "确认删除所有数据? (输入 'DELETE_ALL' 确认): " confirm

    if [ "$confirm" = "DELETE_ALL" ]; then
        echo "开始强制清理..."

        cd "$DOCKER_DIR"

        # 第1步：尝试正常停止
        echo "🛑 尝试正常停止服务..."
        docker-compose down --timeout 10 2>/dev/null || true

        # 第2步：强制停止所有相关容器
        echo "💥 强制停止所有相关容器..."
        docker ps -a --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Names}}" | while read container; do
            if [ -n "$container" ]; then
                echo "  强制停止: $container"
                docker stop "$container" --time 5 2>/dev/null || true
                docker rm -f "$container" 2>/dev/null || true
            fi
        done

        # 第3步：确保所有容器都被删除
        echo "🗑️  确保所有容器都被删除..."
        docker ps -a --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Names}}" | xargs -r docker rm -f 2>/dev/null || true

        # 第4步：等待一下，确保容器完全停止
        echo "⏳ 等待容器完全停止..."
        sleep 5

        # 第5步：删除数据卷
        echo "💾 删除数据卷..."
        docker volume ls --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Name}}" | while read volume; do
            if [ -n "$volume" ]; then
                echo "  删除卷: $volume"
                docker volume rm "$volume" 2>/dev/null || echo "    无法删除 $volume (可能仍在使用中)"
            fi
        done

        # 第6步：强制删除剩余数据卷
        echo "💥 强制删除剩余数据卷..."
        docker volume ls --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Name}}" | xargs -r docker volume rm -f 2>/dev/null || true

        # 第7步：删除网络
        echo "🌐 删除网络..."
        if docker network ls --format "{{.Name}}" | grep -q "^${NETWORK_NAME}$"; then
            docker network rm "${NETWORK_NAME}" 2>/dev/null || echo "  网络删除失败，可能仍有容器连接"
        fi

        # 第8步：清理孤立的容器和网络
        echo "🧹 清理孤立资源..."
        docker container prune -f 2>/dev/null || true
        docker network prune -f 2>/dev/null || true

        # 第9步：询问是否删除镜像
        read -p "是否删除相关镜像? (y/n): " delete_images
        if [ "$delete_images" = "y" ]; then
            echo "🗑️  删除镜像..."
            # 删除项目相关镜像
            docker images --format "{{.Repository}}:{{.Tag}}" | grep -E "(mysql|redis|nacos|mongo|elastic|kafka|milvus|prometheus|grafana)" | xargs -r docker rmi -f 2>/dev/null || true
            # 清理未使用的镜像
            docker image prune -f 2>/dev/null || true
        fi

        echo ""
        echo -e "${GREEN}✅ 强制清理完成${NC}"
        echo ""
        echo "📊 清理后状态："
        echo "容器数量: $(docker ps -a --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Names}}" | wc -l)"
        echo "数据卷数量: $(docker volume ls --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Name}}" | wc -l)"
        echo "网络状态: $(docker network ls --filter "name=${NETWORK_NAME}" --format "{{.Name}}" | wc -l)"

    else
        echo "❌ 清理操作已取消"
    fi
}

backup_data() {
    echo "💾 备份数据..."

    TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
    BACKUP_DIR="./backups"
    backup_name="manual_backup_${TIMESTAMP}"
    backup_path="$BACKUP_DIR/$backup_name"

    # 检查是否有数据卷
    volumes=$(docker volume ls --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Name}}")
    if [ -z "$volumes" ]; then
        echo "❌ 没有找到需要备份的数据卷"
        return
    fi

    mkdir -p "$backup_path"

    echo "备份到: $backup_path"

    # 备份所有数据卷
    echo "$volumes" | while read volume; do
        if [ -n "$volume" ]; then
            echo "  备份卷: $volume"
            vol_name=$(echo $volume | sed "s/${COMPOSE_PROJECT_NAME}-//")

            docker run --rm \
                -v "$volume":/data \
                -v "$(pwd)/$backup_path":/backup \
                alpine:latest \
                tar czf "/backup/${vol_name}.tar.gz" -C /data . 2>/dev/null

            if [ $? -eq 0 ]; then
                echo "    ✅ $vol_name 备份成功"
            else
                echo "    ❌ $vol_name 备份失败"
            fi
        fi
    done

    # 创建备份信息
    cat > "$backup_path/backup_info.txt" << EOF
备份时间: $(date)
备份类型: 手动备份
项目名称: $COMPOSE_PROJECT_NAME
备份说明: 用户手动执行的数据备份
EOF

    echo "✅ 备份完成: $backup_path"

    # 显示备份大小
    backup_size=$(du -sh "$backup_path" | cut -f1)
    echo "备份大小: $backup_size"
}

restart_services() {
    echo "🔄 重启所有服务..."

    cd "$DOCKER_DIR"

    docker-compose down
    sleep 2
    docker-compose up -d

    echo "✅ 服务重启完成"
}

# 主函数
main() {
    local command=${1:-stop}
    local option=$2

    case $command in
        stop)
            stop_services
            ;;
        clean)
            clean_all
            ;;
        status)
            show_status
            ;;
        logs)
            show_logs "$option"
            ;;
        backup)
            backup_data
            ;;
        restart)
            restart_services
            ;;
        -h|--help|help)
            print_usage
            ;;
        *)
            echo "❌ 未知命令: $command"
            echo ""
            print_usage
            exit 1
            ;;
    esac
}

# 运行主函数
main "$@"