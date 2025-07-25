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
    echo "  logs          查看服务日志"
    echo "  backup        备份数据"
    echo "  restart       重启服务"
    echo ""
    echo "选项:"
    echo "  logs [service]     查看指定服务日志"
    echo "  logs [service] -n  查看指定行数日志"
    echo ""
    echo "示例:"
    echo "  $0                    # 停止所有服务"
    echo "  $0 clean              # 停止服务并删除数据"
    echo "  $0 logs mysql         # 查看MySQL日志"
    echo "  $0 logs mysql -n 200  # 查看MySQL最近200行日志"
    echo "  $0 backup             # 备份数据"
    echo "  $0 restart            # 重启所有服务"
    echo ""
    echo "💡 提示: 查看服务状态请使用 ./scripts/start.sh status"
}

# 检查前置条件
check_prerequisites() {
    # 检查Docker是否运行
    if ! docker info >/dev/null 2>&1; then
        echo -e "${RED}❌ Docker 未运行，请先启动 Docker${NC}"
        exit 1
    fi

    # 检查docker-compose文件是否存在
    if [ ! -f "$DOCKER_DIR/docker-compose.yml" ]; then
        echo -e "${RED}❌ 未找到 docker-compose.yml 文件${NC}"
        echo "请确保在正确的目录运行脚本"
        exit 1
    fi
}

# 获取可用的服务列表
get_available_services() {
    cd "$DOCKER_DIR"
    docker-compose config --services 2>/dev/null || echo ""
}

# 验证服务名是否有效
validate_service() {
    local service=$1
    local available_services=$(get_available_services)

    if [ -z "$service" ]; then
        return 0  # 空服务名表示所有服务
    fi

    if echo "$available_services" | grep -q "^${service}$"; then
        return 0
    else
        echo -e "${RED}❌ 未知服务: $service${NC}"
        echo ""
        echo "可用服务:"
        echo "$available_services" | sed 's/^/  /'
        return 1
    fi
}

show_logs() {
    local service=$1
    local lines=${2:-50}

    # 验证行数参数
    if [[ "$lines" =~ ^-n$ ]] && [ -n "$3" ]; then
        lines=$3
    elif [[ "$lines" =~ ^-n[0-9]+$ ]]; then
        lines=${lines#-n}
    elif [[ ! "$lines" =~ ^[0-9]+$ ]]; then
        lines=50
    fi

    cd "$DOCKER_DIR"

    if [ -z "$service" ]; then
        echo "📖 查看所有服务日志 (最近 $lines 行，Ctrl+C 退出)..."
        docker-compose logs -f --tail="$lines"
    else
        if ! validate_service "$service"; then
            return 1
        fi
        echo "📖 查看 $service 服务日志 (最近 $lines 行，Ctrl+C 退出)..."
        docker-compose logs -f --tail="$lines" "$service"
    fi
}

stop_services() {
    echo -e "${YELLOW}🛑 停止服务...${NC}"

    cd "$DOCKER_DIR"

    # 获取运行中的容器
    running_containers=$(docker ps --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Names}}")

    if [ -z "$running_containers" ]; then
        echo -e "${YELLOW}⚠️  没有运行中的服务${NC}"
        return 0
    fi

    echo "正在停止以下服务:"
    echo "$running_containers" | sed 's/^/  /'
    echo ""

    if docker-compose down --timeout 30; then
        echo -e "${GREEN}✅ 所有服务已停止${NC}"
    else
        echo -e "${RED}❌ 停止服务时出现错误${NC}"
        return 1
    fi
}

clean_all() {
    echo -e "${RED}🗑️  清理所有数据${NC}"
    echo ""

    # 显示将要删除的内容
    cd "$DOCKER_DIR"
    containers=$(docker ps -a --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Names}}")
    volumes=$(docker volume ls --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Name}}")

    echo -e "${RED}⚠️  警告: 这将删除以下内容:${NC}"

    if [ -n "$containers" ]; then
        echo "📦 容器:"
        echo "$containers" | sed 's/^/    /'
    fi

    if [ -n "$volumes" ]; then
        echo "💾 数据卷:"
        echo "$volumes" | sed 's/^/    /'
    fi

    echo "🌐 网络: $NETWORK_NAME (如果存在)"
    echo ""

    read -p "确认删除所有数据? (输入 'DELETE_ALL' 确认): " confirm

    if [ "$confirm" = "DELETE_ALL" ]; then
        echo "开始清理..."

        # 1. 停止并删除容器
        echo "🛑 停止并删除容器..."
        docker-compose down --volumes --remove-orphans --timeout 30

        # 2. 强制删除剩余容器
        if [ -n "$containers" ]; then
            echo "💥 强制删除剩余容器..."
            echo "$containers" | xargs -r docker rm -f 2>/dev/null || true
        fi

        # 3. 删除数据卷
        if [ -n "$volumes" ]; then
            echo "💾 删除数据卷..."
            echo "$volumes" | xargs -r docker volume rm 2>/dev/null || true
        fi

        # 4. 删除网络
        echo "🌐 删除网络..."
        if docker network ls --format "{{.Name}}" | grep -q "^${NETWORK_NAME}$"; then
            docker network rm "${NETWORK_NAME}" 2>/dev/null || echo "  网络可能仍有其他容器连接"
        fi

        echo ""
        echo -e "${GREEN}✅ 清理完成${NC}"

    else
        echo "❌ 清理操作已取消"
    fi
}

backup_data() {
    echo "💾 备份数据..."

    # 检查是否有数据卷
    volumes=$(docker volume ls --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Name}}")
    if [ -z "$volumes" ]; then
        echo -e "${YELLOW}⚠️  没有找到需要备份的数据卷${NC}"
        return 0
    fi

    TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
    BACKUP_DIR="./backups"
    backup_name="manual_backup_${TIMESTAMP}"
    backup_path="$BACKUP_DIR/$backup_name"

    # 创建备份目录
    if ! mkdir -p "$backup_path"; then
        echo -e "${RED}❌ 无法创建备份目录: $backup_path${NC}"
        return 1
    fi

    echo "备份到: $backup_path"
    echo ""

    # 备份所有数据卷
    echo "$volumes" | while read volume; do
        if [ -n "$volume" ]; then
            echo "  📦 备份卷: $volume"
            vol_name=$(echo $volume | sed "s/${COMPOSE_PROJECT_NAME}-//")

            if docker run --rm \
                -v "$volume":/data \
                -v "$(pwd)/$backup_path":/backup \
                alpine:latest \
                tar czf "/backup/${vol_name}.tar.gz" -C /data . 2>/dev/null; then
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
备份内容: $(echo "$volumes" | wc -l) 个数据卷
EOF

    echo ""
    echo -e "${GREEN}✅ 备份完成: $backup_path${NC}"

    # 显示备份大小
    if command -v du >/dev/null 2>&1; then
        backup_size=$(du -sh "$backup_path" 2>/dev/null | cut -f1)
        echo "📊 备份大小: $backup_size"
    fi
}

restart_services() {
    echo "🔄 重启服务..."

    cd "$DOCKER_DIR"

    # 检查是否有运行的服务
    running_containers=$(docker ps --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Names}}")

    if [ -z "$running_containers" ]; then
        echo -e "${YELLOW}⚠️  没有运行中的服务可以重启${NC}"
        echo "💡 使用 './scripts/start.sh' 启动服务"
        return 0
    fi

    echo "正在重启以下服务:"
    echo "$running_containers" | sed 's/^/  /'
    echo ""

    # 优雅重启
    if docker-compose restart; then
        echo -e "${GREEN}✅ 服务重启完成${NC}"
    else
        echo -e "${RED}❌ 重启失败，尝试停止后重新启动...${NC}"
        docker-compose down && docker-compose up -d
    fi
}

# 主函数
main() {
    local command=${1:-stop}
    shift

    # 检查前置条件
    check_prerequisites

    case $command in
        stop)
            stop_services
            ;;
        clean)
            clean_all
            ;;
        logs)
            show_logs "$@"
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
            echo -e "${RED}❌ 未知命令: $command${NC}"
            echo ""
            print_usage
            exit 1
            ;;
    esac
}

# 运行主函数
main "$@"