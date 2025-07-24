package org.xiaoxingbomei.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson配置类
 * 
 * 【什么是Redisson】
 * Redisson是一个在Redis基础上实现的Java驻内存数据网格(In-Memory Data Grid)。
 * 它不仅提供了一系列的分布式的Java常用对象，还提供了许多分布式服务。
 * 
 * 【Redisson vs RedisTemplate】
 * 
 * RedisTemplate（基础操作）：
 * - 基本的Redis命令封装
 * - 需要手动处理分布式场景
 * - 适合简单的缓存操作
 * 
 * Redisson（高级功能）：
 * - 提供分布式锁、分布式集合、分布式对象
 * - 自动处理分布式一致性问题
 * - 适合复杂的分布式场景
 * 
 * 【Redisson核心功能】
 * 1. 分布式锁：RLock、ReadWriteLock、Semaphore等
 * 2. 分布式集合：RMap、RSet、RList、RQueue等
 * 3. 分布式对象：RAtomicLong、RBitSet等
 * 4. 分布式服务：RScheduledExecutorService、RRemoteService等
 * 5. 布隆过滤器：RBloomFilter
 * 6. 限流器：RRateLimiter
 * 
 * 【配置方式】
 * Redisson支持多种配置方式：
 * - 单节点模式：适合开发和测试
 * - 集群模式：适合生产环境
 * - 哨兵模式：适合高可用场景
 * - 主从模式：适合读写分离场景
 */
@Configuration
public class RedissonConfig {

    /**
     * Redis连接地址
     * 从配置文件中读取，支持单节点和集群配置
     */
    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    @Value("${spring.data.redis.database:0}")
    private int database;

    /**
     * 配置Redisson客户端
     * 
     * 【单节点配置】
     * 适用于开发环境和单机Redis部署
     * 
     * 【配置参数说明】
     * - address：Redis服务器地址
     * - password：Redis密码（可选）
     * - database：数据库索引
     * - connectionPoolSize：连接池大小
     * - connectionMinimumIdleSize：最小空闲连接数
     * - timeout：命令执行超时时间
     * - retryAttempts：命令重试次数
     * - retryInterval：重试间隔时间
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        
        /**
         * 【单节点配置】
         * 使用单节点模式连接Redis
         * 
         * 地址格式：redis://host:port
         * 注意：Redisson要求地址必须以redis://开头
         */
        String address = "redis://" + redisHost + ":" + redisPort;
        config.useSingleServer()
                .setAddress(address)
                .setDatabase(database)
                /**
                 * 【连接池配置】
                 * 
                 * connectionPoolSize：连接池大小，默认64
                 * - 作用：控制最大连接数，避免连接过多
                 * - 调优：根据并发量调整，一般设置为CPU核数的2-4倍
                 */
                .setConnectionPoolSize(10)
                
                /**
                 * connectionMinimumIdleSize：最小空闲连接数，默认32
                 * - 作用：保持一定数量的空闲连接，减少连接建立时间
                 * - 调优：一般设置为connectionPoolSize的1/4到1/2
                 */
                .setConnectionMinimumIdleSize(5)
                
                /**
                 * timeout：命令执行超时时间（毫秒），默认3000
                 * - 作用：防止命令执行时间过长
                 * - 调优：根据网络环境和业务需求调整
                 */
                .setTimeout(3000)
                
                /**
                 * retryAttempts：命令重试次数，默认3
                 * - 作用：网络异常时自动重试
                 * - 注意：重试次数不宜过多，避免雪崩
                 */
                .setRetryAttempts(3)
                
                /**
                 * retryInterval：重试间隔时间（毫秒），默认1500
                 * - 作用：控制重试频率
                 * - 建议：使用指数退避或固定间隔
                 */
                .setRetryInterval(1500);

        /**
         * 【密码配置】
         * 如果Redis设置了密码，需要配置密码
         */
        if (redisPassword != null && !redisPassword.trim().isEmpty()) {
            config.useSingleServer().setPassword(redisPassword);
        }

        /**
         * 【其他可选配置】
         */
        
        // 看门狗超时时间，默认30秒
        // 当获取锁后，在锁自动超时前，如果线程还在执行，会自动延长锁的过期时间
        config.setLockWatchdogTimeout(30000);
        
        // 编码配置，默认使用Jackson编码
        // config.setCodec(new JsonJacksonCodec());

        return Redisson.create(config);
    }
    
    /**
     * 【生产环境集群配置示例】
     * 
     * 在生产环境中，建议使用集群模式：
     * 
     * @Bean(destroyMethod = "shutdown")
     * public RedissonClient redissonClusterClient() {
     *     Config config = new Config();
     *     config.useClusterServers()
     *         .addNodeAddress("redis://redis1:6379")
     *         .addNodeAddress("redis://redis2:6379")
     *         .addNodeAddress("redis://redis3:6379")
     *         .setPassword("password")
     *         .setMasterConnectionPoolSize(100)
     *         .setSlaveConnectionPoolSize(100);
     *     return Redisson.create(config);
     * }
     */
} 