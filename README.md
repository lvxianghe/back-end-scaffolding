# back-end-scaffolding
# 概述

- 工程简介：

|      工程       |     备注     |      默认端口      |
|:-------------:|:----------:|:--------------:|
| server-parent | 父工程，全局依赖管理 |       /        |
| server-common |    公共模块    |       /        |
|  server-main  | 主工程，脚手架核心  |     28920      |
| server-eureka |    注册中心    |     28921      |
|  server-api   |     网关     |     28922      |
|   server-es   |    搜索服务    |     28923      |
|   server-mq   |    消息服务    |     28924      |
|  server-sql   |   sql服务    |     28925      |
|    apollo     |    三个服务    | 8070、8080、8090 |
|     minio     |    对象存储    |      9000      |
|     redis     |   缓存数据库    |      6379      |
|     mysql     |    数据库     |      3306      |
|    mongodb    |    数据库     |     27017      |
| elasticsearch |    搜索引擎    |      9200      |
|    kibanna    |   es可视化    |      5601      |
|     kafka     |    消息队列    |      9092      |

- 技术简介：

| 技术                  | 说明           | 官网                                            |
|:--------------------| -------------- | ----------------------------------------------- |
| springboot          | web框架        | https://spring.io/projects/spring-boot          |
| springcloud netflix | 分布式框架     | https://spring.io/projects/spring-cloud-netflix |
| mybatis             | orm框架        | https://blog.mybatis.org/                       |
| mybatis-plus        | orm框架        | https://baomidou.com/                           |
| redis               | 缓存           | https://redis.io/                               |
| mongodb             | 缓存           | https://www.mongodb.com/zh-cn                   |
| caffeine            | 本地缓存       | https://github.com/ben-manes/caffeine           |
| kafka               | 消息队列       | https://kafka.apache.org/                       |
| hutool              | 工具类         | https://hutool.cn/                              |
| sa-token            | 权限框架       | https://sa-token.cc/doc.html#/                  |
| oss                 | 对象存储       | https://www.aliyun.com/product/oss              |
| minio               | 自建对象存储   | https://min.io/                                 |
| mysql               | 数据库         | https://www.mysql.com/cn/                       |
| swagger             | api文档框架    | https://swagger.io/                             |
| docker              | 容器           | https://www.docker.com/                         |
| nginx               | 代理服务器     | https://nginx.org/cn/                           |
| elasticsearch       | 搜索引擎       |                                                 |
| filebeat            | 采集agent      |                                                 |
| logstash            | 采集agent      |                                                 |
| kibanna             | 可视化分析工具 |                                                 |
|                     |                |                                                 |




# 架构设计
## 零、基础架构
1. 全局异常捕捉
2. 全局统一响应
3. 统一异常处理
4. 统一日志管理


## 一、异常体系架构

## 二、权限体系架构

## 三、日志体系架构

## 四、网关架构

## 五、熔断&限流&降级体系架构

## 六、线程池架构

## 七、多级缓存架构

## 八、统一调度架构

## 九、推荐系统架构

## 十、消息队列架构

## 十一、搜索引擎架构

