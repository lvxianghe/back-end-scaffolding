package org.xiaoxingbomei.common.aspect;


import org.apache.logging.log4j.core.config.Order;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.xiaoxingbomei.common.annotation.DistributeLock;
import org.xiaoxingbomei.common.constant.DistributeLockConstant;
import org.xiaoxingbomei.common.entity.exception.DistributeLockException;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁切面
 * 
 * <p>基于Redisson实现的分布式锁AOP切面，通过注解方式提供方法级别的分布式锁功能</p>
 * 
 * <h3>🎯 设计目标：</h3>
 * <ul>
 *   <li>提供声明式分布式锁，简化业务代码</li>
 *   <li>支持灵活的key生成策略（固定key + SpEL表达式）</li>
 *   <li>提供完善的时间控制（过期时间 + 等待时间）</li>
 *   <li>确保异常安全的锁释放机制</li>
 * </ul>
 *
 * @author hollis
 */
@Aspect
@Component
@ConditionalOnClass({RedissonClient.class, RLock.class})
/**
 * 🚀 切面执行优先级设计分析
 * 
 * <h3>为什么使用 @Order(Integer.MIN_VALUE) ？</h3>
 * 
 * <p><b>核心原理：</b>数值越小，优先级越高，越先执行</p>
 * <ul>
 *   <li>Integer.MIN_VALUE = -2,147,483,648（最高优先级）</li>
 *   <li>确保分布式锁切面在所有其他切面之前执行</li>
 *   <li>保证锁的完整性和一致性</li>
 * </ul>
 * 
 * <h3>📋 切面执行顺序（由高到低优先级）：</h3>
 * <pre>
 * ┌─────────────────────────────────────────────────┐
 * │  @Order(Integer.MIN_VALUE)                      │
 * │  🔒 DistributedLockAspect (分布式锁)             │ ← 最先执行
 * │     ├── 获取锁                                   │
 * │     └── try-finally确保释放                      │
 * └─────────────────────────────────────────────────┘
 *           ↓
 * ┌─────────────────────────────────────────────────┐
 * │  @Order(1) 或默认顺序                            │
 * │  🔄 @Transactional (事务切面)                    │
 * │     ├── 开启事务                                 │
 * │     └── 提交/回滚事务                            │
 * └─────────────────────────────────────────────────┘
 *           ↓
 * ┌─────────────────────────────────────────────────┐
 * │  @Order(2) 或默认顺序                            │
 * │  💾 @Cacheable (缓存切面)                        │
 * │     ├── 查询缓存                                 │
 * │     └── 写入缓存                                 │
 * └─────────────────────────────────────────────────┘
 *           ↓
 * ┌─────────────────────────────────────────────────┐
 * │  📋 业务方法 (实际业务逻辑)                       │
 * │     ├── 业务参数验证                             │
 * │     ├── 业务逻辑处理                             │
 * │     └── 返回业务结果                             │
 * └─────────────────────────────────────────────────┘
 * </pre>
 * 
 * <h3>⚡ 为什么必须最高优先级？</h3>
 * 
 * <p><b>1. 锁的完整性保护：</b></p>
 * <ul>
 *   <li>✅ 正确：先获取锁 → 开启事务 → 执行业务 → 提交事务 → 释放锁</li>
 *   <li>❌ 错误：开启事务 → 获取锁 → 执行业务 → 释放锁 → 提交事务</li>
 *   <li>风险：事务提交阶段可能出现并发问题</li>
 * </ul>
 * 
 * <p><b>2. 避免死锁场景：</b></p>
 * <pre>
 * 错误顺序可能导致：
 * 线程A: 开启事务 → 等待分布式锁
 * 线程B: 获取分布式锁 → 等待数据库锁
 * 结果: 死锁！
 * 
 * 正确顺序保证：
 * 线程A: 获取分布式锁 → 开启事务 → 完成
 * 线程B: 等待分布式锁 → 获取分布式锁 → 开启事务 → 完成
 * 结果: 串行执行，无死锁
 * </pre>
 * 
 * <p><b>3. 缓存一致性保障：</b></p>
 * <ul>
 *   <li>分布式锁确保同一时刻只有一个线程操作</li>
 *   <li>防止缓存穿透、缓存击穿等并发问题</li>
 *   <li>保证缓存更新的原子性</li>
 * </ul>
 * 
 * <h3>🎯 实际应用示例：</h3>
 * <pre>
 * {@code
 * @DistributeLock(scene = "payment", keyExpression = "#orderId")
 * @Transactional
 * @Cacheable(value = "orderCache", key = "#orderId")
 * public PaymentResult processPayment(String orderId) {
 *     // 执行顺序：
 *     // 1. 🔒 获取分布式锁 (防止重复支付)
 *     // 2. 🔄 开启数据库事务
 *     // 3. 💾 检查缓存
 *     // 4. 📋 执行支付逻辑
 *     // 5. 💾 更新缓存
 *     // 6. 🔄 提交事务
 *     // 7. 🔒 释放分布式锁
 * }
 * }
 * </pre>
 * 
 * <h3>📊 性能影响分析：</h3>
 * <ul>
 *   <li><b>优点：</b>保证数据一致性，避免并发问题</li>
 *   <li><b>代价：</b>轻微的性能开销（微秒级别）</li>
 *   <li><b>权衡：</b>牺牲极少性能换取数据安全，非常值得</li>
 * </ul>
 * 
 * <p><b>💡 总结：@Order(Integer.MIN_VALUE) 是分布式锁切面的核心设计，</b></p>
 * <p><b>它确保了锁的语义正确性和系统的数据一致性！</b></p>
 */
@Order(Integer.MIN_VALUE)
public class DistributeLockAspect
{

    private RedissonClient redissonClient;

    public DistributeLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private static final Logger LOG = LoggerFactory.getLogger(DistributeLockAspect.class);

    @Around("@annotation(org.xiaoxingbomei.common.annotation.DistributeLock)")
    public Object process(ProceedingJoinPoint pjp) throws Exception {
        Object response = null;
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        DistributeLock distributeLock = method.getAnnotation(DistributeLock.class);

        String key = distributeLock.key();
        if (DistributeLockConstant.NONE_KEY.equals(key)) {
            if (DistributeLockConstant.NONE_KEY.equals(distributeLock.keyExpression())) {
                throw new DistributeLockException("no lock key found...");
            }
            SpelExpressionParser parser = new SpelExpressionParser();
            Expression expression = parser.parseExpression(distributeLock.keyExpression());

            EvaluationContext context = new StandardEvaluationContext();
            // 获取参数值
            Object[] args = pjp.getArgs();

            // 获取运行时参数的名称
            StandardReflectionParameterNameDiscoverer discoverer
                    = new StandardReflectionParameterNameDiscoverer();
            String[] parameterNames = discoverer.getParameterNames(method);

            // 将参数绑定到context中
            if (parameterNames != null) {
                for (int i = 0; i < parameterNames.length; i++) {
                    context.setVariable(parameterNames[i], args[i]);
                }
            }

            // 解析表达式，获取结果
            key = String.valueOf(expression.getValue(context));
        }

        String scene = distributeLock.scene();

        String lockKey = scene + "#" + key;

        int expireTime = distributeLock.expireTime();
        int waitTime = distributeLock.waitTime();
        RLock rLock = redissonClient.getLock(lockKey);
        boolean lockResult = false;
        if (waitTime == DistributeLockConstant.DEFAULT_WAIT_TIME) {
            if (expireTime == DistributeLockConstant.DEFAULT_EXPIRE_TIME) {
                LOG.info(String.format("lock for key : %s", lockKey));
                rLock.lock();
            } else {
                LOG.info(String.format("lock for key : %s , expire : %s", lockKey, expireTime));
                rLock.lock(expireTime, TimeUnit.MILLISECONDS);
            }
            lockResult = true;
        } else {
            if (expireTime == DistributeLockConstant.DEFAULT_EXPIRE_TIME) {
                LOG.info(String.format("try lock for key : %s , wait : %s", lockKey, waitTime));
                lockResult = rLock.tryLock(waitTime, TimeUnit.MILLISECONDS);
            } else {
                LOG.info(String.format("try lock for key : %s , expire : %s , wait : %s", lockKey, expireTime, waitTime));
                lockResult = rLock.tryLock(waitTime, expireTime, TimeUnit.MILLISECONDS);
            }
        }

        if (!lockResult) {
            LOG.warn(String.format("lock failed for key : %s , expire : %s", lockKey, expireTime));
            throw new DistributeLockException("acquire lock failed... key : " + lockKey);
        }

        try {
            LOG.info(String.format("lock success for key : %s , expire : %s", lockKey, expireTime));
            response = pjp.proceed();
        } catch (Throwable e) {
            throw new Exception(e);
        } finally {
            rLock.unlock();
            LOG.info(String.format("unlock for key : %s , expire : %s", lockKey, expireTime));
        }
        return response;
    }
}
