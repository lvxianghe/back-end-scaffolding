#!/bin/bash

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

# 项目配置
COMPOSE_PROJECT_NAME=${COMPOSE_PROJECT_NAME:-scaffolding}
NETWORK_NAME=${NETWORK_NAME:-scaffolding-network}

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DOCKER_DIR="$(dirname "$SCRIPT_DIR")"

# ==================== 通用函数 ====================

print_header() {
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE}    🐳 Docker 基础设施管理工具${NC}"
    echo -e "${BLUE}================================${NC}"
}

print_usage() {
    echo "用法: $0 <命令> [选项]"
    echo ""
    echo -e "${CYAN}核心命令:${NC}"
    echo "  start          启动基础设施服务"
    echo "  stop           停止所有服务"
    echo "  restart        重启服务"
    echo "  status         查看服务状态"
    echo ""
    echo -e "${CYAN}管理命令:${NC}"
    echo "  clean          停止服务并删除所有数据"
    echo "  logs           查看服务日志"
    echo "  backup         备份数据"
    echo ""
    echo -e "${CYAN}选项说明:${NC}"
    echo "  start [选项]:"
    echo "    -a, --all         启动所有服务"
    echo "    -s, --select      选择特定服务启动"
    echo "    -i, --interactive 交互式选择"
    echo ""
    echo "  logs [服务名] [选项]:"
    echo "    -n <数量>         显示最近N行日志"
    echo "    -f, --follow      实时跟踪日志"
    echo ""
    echo -e "${CYAN}示例:${NC}"
    echo "  $0 start -a              # 启动所有服务"
    echo "  $0 start -s mysql,redis  # 启动指定服务"
    echo "  $0 status                # 查看状态"
    echo "  $0 logs mysql -n 100     # 查看MySQL最近100行日志"
    echo "  $0 stop                  # 停止所有服务"
    echo "  $0 clean                 # 清理所有数据"
    echo ""
    echo -e "${YELLOW}💡 提示: 首次运行建议使用 '$0 start -i' 进行交互式配置${NC}"
}

check_prerequisites() {
    # 检查Docker是否运行
    if ! docker info >/dev/null 2>&1; then
        echo -e "${RED}❌ Docker 未运行，请先启动 Docker${NC}"
        exit 1
    fi

    # 检查docker-compose文件是否存在
    if [ ! -f "$DOCKER_DIR/docker-compose.yml" ]; then
        echo -e "${RED}❌ 未找到 docker-compose.yml 文件${NC}"
        echo "预期位置: $DOCKER_DIR/docker-compose.yml"
        exit 1
    fi
}

setup_network() {
    echo "🌐 设置Docker网络: $NETWORK_NAME"
    if docker network ls --format "{{.Name}}" | grep -q "^${NETWORK_NAME}$"; then
        echo "✅ 网络 $NETWORK_NAME 已存在"
    else
        docker network create $NETWORK_NAME >/dev/null 2>&1
        echo "✅ 网络 $NETWORK_NAME 创建完成"
    fi
}

get_running_containers() {
    docker ps --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Names}}" 2>/dev/null | sort
}

get_all_containers() {
    docker ps -a --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Names}}" 2>/dev/null | sort
}

get_available_services() {
    cd "$DOCKER_DIR"
    docker-compose config --services 2>/dev/null | sort
}

# ==================== 启动相关函数 ====================

get_service_info() {
    local choice=$1
    case $choice in
        1) echo "mysql MySQL数据库" ;;
        2) echo "redis Redis缓存" ;;
        3) echo "nacos Nacos注册中心" ;;
        4) echo "mongodb MongoDB文档数据库" ;;
        5) echo "elasticsearch Elasticsearch搜索引擎+Kibana可视化" ;;
        6) echo "kafka Kafka消息队列+Zookeeper+WebUI" ;;
        7) echo "milvus Milvus向量数据库+Attu管理界面+Minio存储" ;;
        8) echo "monitoring Prometheus监控+Grafana可视化面板" ;;
        *) echo "" ;;
    esac
}

check_existing_data() {
    echo "🔍 检查现有数据..."

    local existing_volumes=$(docker volume ls --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Name}}" | wc -l)

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
            1) echo "✅ 保留现有数据" ;;
            2) backup_data && clean_all_silent ;;
            3)
                echo -e "${RED}⚠️  警告: 这将删除所有现有数据！${NC}"
                read -p "确认删除? (yes/no): " confirm
                if [ "$confirm" = "yes" ]; then
                    clean_all_silent
                else
                    echo "❌ 启动已取消"; exit 1
                fi
                ;;
            4) echo "❌ 启动已取消"; exit 1 ;;
            *) echo "默认保留现有数据" ;;
        esac
    else
        echo "✅ 没有发现现有数据，可以直接启动"
    fi
}

interactive_service_selection() {
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

parse_service_list() {
    local services=$1
    local profiles=()

    # 分割服务列表
    IFS=',' read -ra service_array <<< "$services"

    for service in "${service_array[@]}"; do
        service=$(echo "$service" | xargs)  # 去除空格
        case $service in
            mysql) profiles+=("mysql") ;;
            redis) profiles+=("redis") ;;
            nacos) profiles+=("nacos") ;;
            mongodb) profiles+=("mongodb") ;;
            elasticsearch|elastic) profiles+=("elasticsearch") ;;
            kafka) profiles+=("kafka") ;;
            milvus) profiles+=("milvus") ;;
            monitoring|prometheus|grafana) profiles+=("monitoring") ;;
            all) profiles=("all"); break ;;
            *)
                echo -e "${RED}❌ 未知服务: $service${NC}"
                echo "可用服务: mysql, redis, nacos, mongodb, elasticsearch, kafka, milvus, monitoring"
                exit 1
                ;;
        esac
    done

    if [ ${#profiles[@]} -eq 0 ]; then
        echo -e "${RED}❌ 没有选择任何有效服务${NC}"
        exit 1
    fi

    # 构建profile参数
    selected_profiles=""
    for profile in "${profiles[@]}"; do
        if [ "$profile" = "all" ]; then
            selected_profiles="--profile all"
            break
        else
            selected_profiles="$selected_profiles --profile $profile"
        fi
    done
}

start_services() {
    echo ""
    echo "🚀 开始启动服务..."

    cd "$DOCKER_DIR"

    echo "📂 工作目录: $(pwd)"
    echo "🏗️  执行命令: docker-compose $selected_profiles up -d"

    if docker-compose $selected_profiles up -d; then
        echo -e "${GREEN}✅ 服务启动成功！${NC}"
        return 0
    else
        echo -e "${RED}❌ 服务启动失败${NC}"
        return 1
    fi
}

# ==================== 状态相关函数 ====================

show_status() {
    cd "$DOCKER_DIR"

    echo ""
    echo "📊 服务状态："
    echo "================================"

    # 显示容器状态
    echo "🐳 容器状态:"

    local containers=$(get_all_containers)

    if [ -n "$containers" ]; then
        docker ps -a --filter "name=${COMPOSE_PROJECT_NAME}-" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

        echo ""
        echo "📈 运行状态统计:"
        local running=$(docker ps --filter "name=${COMPOSE_PROJECT_NAME}-" --filter "status=running" | wc -l)
        local stopped=$(docker ps -a --filter "name=${COMPOSE_PROJECT_NAME}-" --filter "status=exited" | wc -l)
        local total=$(docker ps -a --filter "name=${COMPOSE_PROJECT_NAME}-" | wc -l)

        # 减去表头行
        running=$((running - 1))
        stopped=$((stopped - 1))
        total=$((total - 1))

        echo -e "  ${GREEN}✅ 运行中: $running${NC}"
        echo -e "  ${RED}❌ 已停止: $stopped${NC}"
        echo -e "  📊 总计: $total"

        # 显示资源使用情况
        local running_containers=$(get_running_containers)
        if [ -n "$running_containers" ]; then
            echo ""
            echo "💻 资源使用:"
            docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.MemPerc}}" $running_containers 2>/dev/null || echo "  无法获取资源使用信息"
        fi
    else
        echo -e "${YELLOW}⚠️  没有发现运行的容器${NC}"
        echo ""
        echo "💡 提示: 使用以下命令启动服务"
        echo "  $0 start -i"
        return
    fi

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
    echo "  查看日志:   $0 logs [service]"
    echo "  停止服务:   $0 stop"
    echo "  重启服务:   $0 restart"
    echo "  清理数据:   $0 clean"
    echo "  备份数据:   $0 backup"
}

# ==================== 停止相关函数 ====================

verify_stop_result() {
    echo "🔍 验证停止结果..."
    sleep 2

    local remaining_containers=$(get_running_containers)

    if [ -z "$remaining_containers" ]; then
        echo -e "${GREEN}✅ 所有服务已成功停止${NC}"
        return 0
    else
        echo -e "${RED}❌ 以下服务仍在运行:${NC}"
        echo "$remaining_containers" | sed 's/^/  /'
        echo ""
        echo "🔧 尝试强制停止..."

        echo "$remaining_containers" | while read container; do
            if [ -n "$container" ]; then
                echo "  强制停止: $container"
                docker stop "$container" --time 10 2>/dev/null || true
            fi
        done

        sleep 2
        local final_check=$(get_running_containers)
        if [ -z "$final_check" ]; then
            echo -e "${GREEN}✅ 强制停止成功${NC}"
            return 0
        else
            echo -e "${RED}❌ 部分容器无法停止:${NC}"
            echo "$final_check" | sed 's/^/  /'
            return 1
        fi
    fi
}

stop_services() {
    echo -e "${YELLOW}🛑 停止服务...${NC}"

    local running_containers=$(get_running_containers)

    if [ -z "$running_containers" ]; then
        echo -e "${YELLOW}⚠️  没有运行中的服务${NC}"
        return 0
    fi

    echo "正在停止以下服务:"
    echo "$running_containers" | sed 's/^/  /'
    echo ""

    cd "$DOCKER_DIR"
    echo "📂 当前工作目录: $(pwd)"

    echo "⏳ 执行 docker-compose down..."
    if docker-compose down --timeout 30; then
        echo "✅ docker-compose down 执行完成"
        verify_stop_result
    else
        echo -e "${RED}❌ docker-compose down 执行失败${NC}"
        echo "🔧 尝试强制停止..."

        local containers=$(get_running_containers)
        if [ -n "$containers" ]; then
            echo "$containers" | while read container; do
                if [ -n "$container" ]; then
                    echo "  强制停止: $container"
                    docker stop "$container" --time 10 2>/dev/null || true
                fi
            done
            verify_stop_result
        fi
    fi
}

# ==================== 清理相关函数 ====================

clean_all_silent() {
    echo "🗑️  静默清理数据..."

    cd "$DOCKER_DIR"

    # 停止并删除容器
    docker-compose down --volumes --remove-orphans --timeout 30 >/dev/null 2>&1

    # 删除剩余容器
    local containers=$(get_all_containers)
    if [ -n "$containers" ]; then
        echo "$containers" | xargs -r docker rm -f >/dev/null 2>&1 || true
    fi

    # 删除数据卷
    docker volume ls --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Name}}" | xargs -r docker volume rm >/dev/null 2>&1 || true

    echo "✅ 静默清理完成"
}

clean_all() {
    echo -e "${RED}🗑️  清理所有数据${NC}"
    echo ""

    cd "$DOCKER_DIR"
    local containers=$(get_all_containers)
    local volumes=$(docker volume ls --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Name}}")

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

        # 停止并删除容器
        echo "🛑 停止并删除容器..."
        docker-compose down --volumes --remove-orphans --timeout 30

        # 强制删除剩余容器
        if [ -n "$containers" ]; then
            echo "💥 强制删除剩余容器..."
            echo "$containers" | xargs -r docker rm -f 2>/dev/null || true
        fi

        # 删除数据卷
        if [ -n "$volumes" ]; then
            echo "💾 删除数据卷..."
            echo "$volumes" | xargs -r docker volume rm 2>/dev/null || true
        fi

        # 删除网络
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

# ==================== 日志相关函数 ====================

validate_service() {
    local service=$1
    local available_services=$(get_available_services)

    if [ -z "$service" ]; then
        return 0
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
    local service=""
    local lines=50
    local follow=false

    # 解析参数
    while [[ $# -gt 0 ]]; do
        case $1 in
            -n)
                lines="$2"
                shift 2
                ;;
            -f|--follow)
                follow=true
                shift
                ;;
            -*)
                echo -e "${RED}❌ 未知选项: $1${NC}"
                return 1
                ;;
            *)
                if [ -z "$service" ]; then
                    service="$1"
                fi
                shift
                ;;
        esac
    done

    cd "$DOCKER_DIR"

    if [ -z "$service" ]; then
        echo "📖 查看所有服务日志 (最近 $lines 行)..."
        if $follow; then
            docker-compose logs -f --tail="$lines"
        else
            docker-compose logs --tail="$lines"
        fi
    else
        if ! validate_service "$service"; then
            return 1
        fi
        echo "📖 查看 $service 服务日志 (最近 $lines 行)..."
        if $follow; then
            docker-compose logs -f --tail="$lines" "$service"
        else
            docker-compose logs --tail="$lines" "$service"
        fi
    fi
}

# ==================== 备份相关函数 ====================

backup_data() {
    echo "💾 备份数据..."

    local volumes=$(docker volume ls --filter "name=${COMPOSE_PROJECT_NAME}-" --format "{{.Name}}")
    if [ -z "$volumes" ]; then
        echo -e "${YELLOW}⚠️  没有找到需要备份的数据卷${NC}"
        return 0
    fi

    local timestamp=$(date +"%Y%m%d_%H%M%S")
    local backup_dir="./backups"
    local backup_name="backup_${timestamp}"
    local backup_path="$backup_dir/$backup_name"

    if ! mkdir -p "$backup_path"; then
        echo -e "${RED}❌ 无法创建备份目录: $backup_path${NC}"
        return 1
    fi

    echo "备份到: $backup_path"
    echo ""

    echo "$volumes" | while read volume; do
        if [ -n "$volume" ]; then
            echo "  📦 备份卷: $volume"
            local vol_name=$(echo $volume | sed "s/${COMPOSE_PROJECT_NAME}-//")

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
备份内容: $(echo "$volumes" | wc -l) 个数据卷
EOF

    echo ""
    echo -e "${GREEN}✅ 备份完成: $backup_path${NC}"

    if command -v du >/dev/null 2>&1; then
        local backup_size=$(du -sh "$backup_path" 2>/dev/null | cut -f1)
        echo "📊 备份大小: $backup_size"
    fi
}

# ==================== 重启相关函数 ====================

restart_services() {
    echo "🔄 重启服务..."

    local running_containers=$(get_running_containers)

    if [ -z "$running_containers" ]; then
        echo -e "${YELLOW}⚠️  没有运行中的服务可以重启${NC}"
        echo "💡 使用 '$0 start' 启动服务"
        return 0
    fi

    echo "正在重启以下服务:"
    echo "$running_containers" | sed 's/^/  /'
    echo ""

    cd "$DOCKER_DIR"

    if docker-compose restart; then
        echo -e "${GREEN}✅ 服务重启完成${NC}"
    else
        echo -e "${RED}❌ 重启失败，尝试停止后重新启动...${NC}"
        docker-compose down && docker-compose up -d
    fi
}

# ==================== 主函数 ====================

main() {
    local command=${1:-help}
    shift

    print_header

    case $command in
        start)
            check_prerequisites
            setup_network

            # 解析启动参数
            local start_mode="interactive"
            local service_list=""

            while [[ $# -gt 0 ]]; do
                case $1 in
                    -a|--all)
                        start_mode="all"
                        shift
                        ;;
                    -s|--select)
                        start_mode="select"
                        service_list="$2"
                        shift 2
                        ;;
                    -i|--interactive)
                        start_mode="interactive"
                        shift
                        ;;
                    *)
                        echo -e "${RED}❌ 未知启动选项: $1${NC}"
                        print_usage
                        exit 1
                        ;;
                esac
            done

            echo ""

            # 根据模式选择服务
            case $start_mode in
                all)
                    selected_profiles="--profile all"
                    echo "✅ 启动所有服务"
                    ;;
                select)
                    if [ -z "$service_list" ]; then
                        echo -e "${RED}❌ 使用 -s 选项时必须指定服务列表${NC}"
                        exit 1
                    fi
                    parse_service_list "$service_list"
                    ;;
                interactive)
                    check_existing_data
                    echo ""
                    interactive_service_selection
                    ;;
            esac

            start_services && show_status
            echo ""
            echo "🎉 启动完成！"
            ;;

        stop)
            check_prerequisites
            stop_services
            ;;

        restart)
            check_prerequisites
            restart_services
            ;;

        status)
            check_prerequisites
            show_status
            ;;

        clean)
            check_prerequisites
            clean_all
            ;;

        logs)
            check_prerequisites
            show_logs "$@"
            ;;

        backup)
            check_prerequisites
            backup_data
            ;;

        help|-h|--help)
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