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

print_header() {
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE}    🚀 基础设施启动工具${NC}"
    echo -e "${BLUE}================================${NC}"
}

setup_network() {
    echo "🌐 设置Docker网络: $NETWORK_NAME"
    if docker network ls --format "{{.Name}}" | grep -q "^${NETWORK_NAME}$"; then
        echo "✅ 网络 $NETWORK_NAME 已存在"
    else
        docker network create $NETWORK_NAME
        echo "✅ 网络 $NETWORK_NAME 创建完成"
    fi
}

check_existing_data() {
    echo "🔍 检查现有数据..."

    existing_volumes=$(docker volume ls --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Name}}" | wc -l)

    if [ "$existing_volumes" -gt 0 ]; then
        echo -e "${YELLOW}⚠️  发现 $existing_volumes 个现有数据卷${NC}"
        echo ""
        echo "数据处理选项："
        echo "1. 保留现有数据继续启动"
        echo "2. 备份现有数据后清理重新启动"
        echo "3. 直接清理现有数据重新启动"
        echo "4. 取消启动"

        read -p "请选择 (1-4): " data_choice

        case $data_choice in
            1)
                echo "✅ 保留现有数据"
                ;;
            2)
                backup_data
                clean_data
                ;;
            3)
                echo -e "${RED}⚠️  警告: 这将删除所有现有数据！${NC}"
                read -p "确认删除? (yes/no): " confirm
                if [ "$confirm" = "yes" ]; then
                    clean_data
                else
                    echo "❌ 启动已取消"
                    exit 1
                fi
                ;;
            4)
                echo "❌ 启动已取消"
                exit 1
                ;;
            *)
                echo "默认保留现有数据"
                ;;
        esac
    else
        echo "✅ 没有发现现有数据，可以直接启动"
    fi
}

backup_data() {
    echo "💾 备份现有数据..."

    TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
    BACKUP_DIR="./backups"
    backup_name="startup_backup_${TIMESTAMP}"
    backup_path="$BACKUP_DIR/$backup_name"

    mkdir -p "$backup_path"

    # 备份所有数据卷
    docker volume ls --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Name}}" | while read volume; do
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
    done

    # 创建备份信息
    cat > "$backup_path/backup_info.txt" << EOF
备份时间: $(date)
备份类型: 启动前自动备份
项目名称: $COMPOSE_PROJECT_NAME
备份原因: 启动前数据保护
EOF

    echo "✅ 数据备份完成: $backup_path"
}

clean_data() {
    echo "🗑️  清理现有数据..."

    cd "$DOCKER_DIR"

    # 停止所有服务
    docker-compose down

    # 删除数据卷
    docker volume ls --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Name}}" | xargs -r docker volume rm

    echo "✅ 数据清理完成"
}

get_service_info() {
    local choice=$1
    case $choice in
        1)
            echo "mysql MySQL数据库"
            ;;
        2)
            echo "redis Redis缓存"
            ;;
        3)
            echo "nacos Nacos注册中心"
            ;;
        4)
            echo "mongodb MongoDB文档数据库"
            ;;
        5)
            echo "elasticsearch Elasticsearch搜索引擎+Kibana可视化"
            ;;
        6)
            echo "kafka Kafka消息队列+Zookeeper+WebUI"
            ;;
        7)
            echo "milvus Milvus向量数据库+Attu管理界面+Minio存储"
            ;;
        8)
            echo "monitoring Prometheus监控+Grafana可视化面板"
            ;;
        *)
            echo ""
            ;;
    esac
}

select_services() {
    echo ""
    echo "📋 可用的基础设施服务："
    echo "  1. MySQL数据库"
    echo "  2. Redis缓存"
    echo "  3. Nacos注册中心"
    echo "  4. MongoDB文档数据库"
    echo "  5. Elasticsearch搜索引擎 + Kibana可视化"
    echo "  6. Kafka消息队列 + Zookeeper + WebUI"
    echo "  7. Milvus向量数据库 + Attu管理界面 + Minio存储"
    echo "  8. Prometheus监控 + Grafana可视化面板"

    echo ""
    echo "启动选项："
    echo "  a. 启动所有服务"
    echo "  s. 选择特定服务"
    echo "  q. 退出"

    read -p "请选择 (a/s/q): " choice

    case $choice in
        a|A)
            selected_profiles="--profile all"
            echo "✅ 选择启动所有服务"
            ;;
        s|S)
            echo "请选择要启动的服务 (输入数字，用空格分隔，如: 1 2 3):"
            read -r selection

            profiles=()
            for num in $selection; do
                service_info=$(get_service_info "$num")
                if [ -n "$service_info" ]; then
                    profile=$(echo $service_info | cut -d' ' -f1)
                    service_desc=$(echo $service_info | cut -d' ' -f2-)
                    profiles+=("$profile")
                    echo "✅ 选择: $service_desc"
                else
                    echo "❌ 无效选择: $num"
                fi
            done

            if [ ${#profiles[@]} -eq 0 ]; then
                echo "❌ 没有选择任何服务"
                exit 1
            fi

            # 构建profile参数
            selected_profiles=""
            for profile in "${profiles[@]}"; do
                selected_profiles="$selected_profiles --profile $profile"
            done
            ;;
        q|Q)
            echo "❌ 启动已取消"
            exit 0
            ;;
        *)
            echo "❌ 无效选择，启动已取消"
            exit 1
            ;;
    esac
}

start_services() {
    echo ""
    echo "🚀 开始启动服务..."

    cd "$DOCKER_DIR"

    # 使用profile启动选定的服务
    if docker-compose $selected_profiles up -d; then
        echo -e "${GREEN}✅ 服务启动成功！${NC}"
    else
        echo -e "${RED}❌ 服务启动失败${NC}"
        exit 1
    fi
}

show_status() {
    echo ""
    echo "📊 服务状态："
    echo "================================"

    cd "$DOCKER_DIR"

    # 显示容器状态
    echo "🐳 容器状态:"
    docker-compose ps

    echo ""
    echo "🔗 服务访问地址："
    echo "  MySQL:             localhost:3306 (root/root123456)"
    echo "  Redis:             localhost:6379"
    echo "  Nacos:             http://localhost:8848/nacos (nacos/nacos)"
    echo "  MongoDB:           localhost:27017 (admin/mongo123456)"
    echo "  Elasticsearch:     http://localhost:9200"
    echo "  Kibana:            http://localhost:5601"
    echo "  Kafka:             localhost:9092"
    echo "  Kafka UI:          http://localhost:8080"
    echo "  Milvus:            localhost:19530"
    echo "  Attu:              http://localhost:8000"
    echo "  Minio:             http://localhost:9001 (minioadmin/minioadmin123456)"
    echo "  Prometheus:        http://localhost:9090"
    echo "  Grafana:           http://localhost:3000 (admin/grafana123456)"

    echo ""
    echo "📝 常用命令："
    echo "  查看日志:   ./scripts/stop.sh logs [service]"
    echo "  查看状态:   ./scripts/stop.sh status"
    echo "  停止服务:   ./scripts/stop.sh"
    echo "  清理数据:   ./scripts/stop.sh clean"
}

# 主函数
main() {
    print_header

    # 设置网络
    setup_network

    echo ""

    # 检查现有数据
    check_existing_data

    echo ""

    # 选择服务
    select_services

    # 启动服务
    start_services

    # 显示状态
    show_status

    echo ""
    echo "🎉 启动完成！"
}

# 运行主函数
main "$@"