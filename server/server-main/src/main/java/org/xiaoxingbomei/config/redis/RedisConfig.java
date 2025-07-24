package org.xiaoxingbomei.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类
 * 
 * 【什么是RedisTemplate】
 * RedisTemplate是Spring Data Redis提供的Redis操作模板类，
 * 类似于JdbcTemplate，提供了丰富的Redis操作方法。
 * 
 * 【为什么需要配置RedisTemplate】
 * 1. 序列化问题：默认使用JDK序列化，不便于调试和跨语言
 * 2. 性能优化：JSON序列化更紧凑，可读性更好
 * 3. 类型安全：通过泛型提供类型安全的操作
 * 4. 统一配置：统一管理Redis的连接和序列化策略
 * 
 * 【RedisTemplate vs StringRedisTemplate】
 * - RedisTemplate<Object, Object>：通用模板，支持任意类型
 * - StringRedisTemplate：专门处理String类型，继承自RedisTemplate<String, String>
 */
@Configuration
public class RedisConfig {

    /**
     * 配置通用RedisTemplate
     * 
     * 【配置要点】
     * 1. 使用JSON序列化器：便于调试和跨语言互操作
     * 2. 配置key和value的序列化方式：key用String，value用JSON
     * 3. 启用默认的序列化：简化配置
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        /**
         * 【JSON序列化器配置】
         * 
         * Jackson2JsonRedisSerializer：使用Jackson进行JSON序列化
         * 优点：
         * - 可读性好：存储的是JSON格式
         * - 跨语言：其他语言也能读取
         * - 紧凑性：比JDK序列化更省空间
         * - 调试友好：可以直接查看Redis中的数据
         */
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        
        /**
         * 【ObjectMapper配置】
         * 
         * 1. setVisibility：设置字段可见性，允许访问所有字段
         * 2. activateDefaultTyping：启用类型信息保存
         *    - LaissezFaireSubTypeValidator：宽松的类型验证器
         *    - DefaultTyping.NON_FINAL：对非final类型保存类型信息
         * 
         * 作用：确保序列化时保存对象的类型信息，反序列化时能正确还原对象类型
         */
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        /**
         * 【String序列化器】
         * 
         * StringRedisSerializer：处理String类型的序列化
         * 用于key的序列化，确保key在Redis中是可读的字符串格式
         */
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        /**
         * 【设置各种类型的序列化器】
         * 
         * 1. key序列化：String格式，便于查看和管理
         * 2. value序列化：JSON格式，支持复杂对象
         * 3. hash key序列化：String格式
         * 4. hash value序列化：JSON格式
         * 
         * 这样配置后的存储格式：
         * - 普通key-value：key为字符串，value为JSON
         * - hash结构：hash key为字符串，field为字符串，value为JSON
         */
        template.setKeySerializer(stringRedisSerializer);           // key采用String的序列化方式
        template.setValueSerializer(jackson2JsonRedisSerializer);   // value序列化方式采用jackson
        template.setHashKeySerializer(stringRedisSerializer);       // hash的key也采用String的序列化方式
        template.setHashValueSerializer(jackson2JsonRedisSerializer); // hash的value序列化方式采用jackson

        /**
         * 启用默认序列化：对于没有指定序列化器的操作，使用上面配置的序列化器
         */
        template.setDefaultSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        
        return template;
    }

    /**
     * 配置StringRedisTemplate
     * 
     * 【使用场景】
     * 当你确定只操作String类型数据时，使用StringRedisTemplate更简单：
     * - 缓存简单字符串值
     * - 存储序列化后的JSON字符串
     * - 计数器操作
     * - 简单的key-value存储
     * 
     * 【与RedisTemplate的区别】
     * StringRedisTemplate专门针对String类型优化，避免了类型转换的开销
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
} 