



## 部署
### infra
infrastructure/
├── docker-compose.yml          # 主compose文件，包含所有服务
├── .env                        # 环境变量
├── mysql/
│   ├── init/
│   └── conf/
├── redis/
│   └── conf/
├── nacos/
│   └── conf/
├── mongodb/
│   ├── init/
│   └── conf/
├── elasticsearch/
├── kafka/
├── milvus/
│   └── conf/
├── monitoring/
│   ├── conf/
│   ├── dashboards/
│   └── datasources/
└── scripts/
├── start.sh
└── stop.sh

基本操作
./scripts/docker.sh start -i          # 交互式启动
./scripts/docker.sh start -a          # 启动所有服务  
./scripts/docker.sh start -s mysql,redis  # 启动指定服务
./scripts/docker.sh status            # 查看状态
./scripts/docker.sh stop              # 停止服务
./scripts/docker.sh restart           # 重启服务

管理操作
./scripts/docker.sh logs mysql -n 100 # 查看MySQL日志
./scripts/docker.sh backup            # 备份数据
./scripts/docker.sh clean             # 清理所有数据

### 后端