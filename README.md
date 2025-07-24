



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


### 后端