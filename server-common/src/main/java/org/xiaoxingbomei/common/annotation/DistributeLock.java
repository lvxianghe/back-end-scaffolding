package org.xiaoxingbomei.common.annotation;

import org.xiaoxingbomei.common.constant.DistributeLockConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁注解
 * 
 * <p>基于Redis Redisson实现的分布式锁，支持自动加锁、释放锁，防止并发操作冲突</p>
 * 
 * <h3>🎯 使用场景：</h3>
 * <ul>
 *   <li>防止重复提交：支付、下单等关键操作</li>
 *   <li>资源竞争：库存扣减、账户余额操作</li>
 *   <li>定时任务：防止多实例重复执行</li>
 *   <li>缓存更新：防止缓存击穿</li>
 * </ul>
 * 
 * <h3>📖 基础用法：</h3>
 * <pre>{@code
 * // 1. 简单固定key锁
 * @DistributeLock(scene = "payment", key = "order123")
 * public void processPayment() {
 *     // 业务逻辑...
 * }
 * 
 * // 2. 使用方法参数作为key
 * @DistributeLock(scene = "user", keyExpression = "#userId")
 * public void updateUser(String userId) {
 *     // 业务逻辑...
 * }
 * 
 * // 3. 使用对象属性作为key  
 * @DistributeLock(scene = "order", keyExpression = "#order.id")
 * public void processOrder(Order order) {
 *     // 业务逻辑...
 * }
 * 
 * // 4. 复杂SpEL表达式
 * @DistributeLock(scene = "inventory", keyExpression = "#product.id + '_' + #warehouse")
 * public void reduceStock(Product product, String warehouse) {
 *     // 业务逻辑...
 * }
 * }</pre>
 * 
 * <h3>⏰ 时间控制：</h3>
 * <pre>{@code
 * // 设置锁过期时间（防止死锁）
 * @DistributeLock(scene = "task", key = "dailyReport", expireTime = 30000) // 30秒
 * public void generateDailyReport() {
 *     // 长时间任务...
 * }
 * 
 * // 设置等待时间（非阻塞获取锁）
 * @DistributeLock(scene = "flash", keyExpression = "#productId", waitTime = 1000) // 等待1秒
 * public boolean flashSale(String productId) {
 *     // 秒杀逻辑...
 *     return true;
 * }
 * 
 * // 同时设置过期时间和等待时间
 * @DistributeLock(
 *     scene = "transfer", 
 *     keyExpression = "#fromAccount + '_' + #toAccount",
 *     expireTime = 60000,  // 锁持有60秒
 *     waitTime = 5000      // 最多等待5秒
 * )
 * public void transferMoney(String fromAccount, String toAccount, BigDecimal amount) {
 *     // 转账逻辑...
 * }
 * }</pre>
 * 
 * <h3>🔧 参数说明：</h3>
 * <ul>
 *   <li><b>scene</b>: 锁的业务场景，用于区分不同业务的锁</li>
 *   <li><b>key</b>: 固定的锁key，优先级高于keyExpression</li>
 *   <li><b>keyExpression</b>: SpEL表达式，动态生成锁key</li>
 *   <li><b>expireTime</b>: 锁过期时间(毫秒)，-1表示自动续期</li>
 *   <li><b>waitTime</b>: 等待获取锁的时间(毫秒)，-1表示阻塞等待</li>
 * </ul>
 * 
 * <h3>⚠️ 注意事项：</h3>
 * <ul>
 *   <li>最终锁key格式：scene + "#" + key，确保全局唯一</li>
 *   <li>建议设置合理的expireTime，防止死锁</li>
 *   <li>waitTime=-1时会阻塞等待，可能影响性能</li>
 *   <li>SpEL表达式支持方法参数、对象属性、运算符等</li>
 *   <li>锁释放在方法执行完成后自动进行</li>
 * </ul>
 * 
 * <h3>🚀 高级用法：</h3>
 * <pre>{@code
 * // 结合返回值的SpEL表达式（需要在方法执行后才能获取）
 * @DistributeLock(scene = "cache", keyExpression = "#result.id")
 * public User createUser(CreateUserRequest request) {
 *     // 注意：这种用法keyExpression在方法执行前无法解析
 *     // 建议使用输入参数: keyExpression = "#request.email"
 *     return userService.create(request);
 * }
 * 
 * // 多个参数组合
 * @DistributeLock(scene = "booking", keyExpression = "#date + '_' + #roomId + '_' + #userId")
 * public void bookRoom(String date, String roomId, String userId) {
 *     // 预订逻辑...
 * }
 * }</pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributeLock
{
    /**
     * 锁的场景标识
     * 
     * <p>用于区分不同业务场景的锁，最终锁key格式为：scene + "#" + key</p>
     * <p>建议使用有意义的业务场景名称，如：payment、order、user、inventory等</p>
     *
     * @return 场景标识
     */
    public String scene();

    /**
     * 固定的锁key
     * 
     * <p>如果设置了key，则直接使用此值作为锁key，忽略keyExpression</p>
     * <p>适用于固定key的场景，如定时任务、全局配置更新等</p>
     *
     * @return 锁key，默认为NONE_KEY表示使用keyExpression
     */
    public String key() default DistributeLockConstant.NONE_KEY;

    /**
     * SpEL表达式动态生成锁key
     * 
     * <p>当key为NONE_KEY时，使用此表达式动态生成锁key</p>
     * <p>支持的SpEL语法：</p>
     * <ul>
     *   <li>#参数名：获取方法参数值，如 #userId</li>
     *   <li>#对象.属性：获取对象属性，如 #user.id</li>
     *   <li>字符串拼接：使用 + 连接，如 #userId + '_' + #type</li>
     *   <li>条件表达式：#status == 1 ? 'active' : 'inactive'</li>
     * </ul>
     * 
     * <p>示例：</p>
     * <pre>
     * #id                           → 直接使用id参数
     * #user.id                      → 使用user对象的id属性  
     * #orderId + '_' + #status      → 拼接多个值
     * #user.type + '_' + #user.id   → 组合对象属性
     * </pre>
     *
     * @return SpEL表达式，默认为NONE_KEY
     */
    public String keyExpression() default DistributeLockConstant.NONE_KEY;

    /**
     * 锁的过期时间（毫秒）
     * 
     * <p>锁的最大持有时间，超过此时间后锁会自动释放，防止死锁</p>
     * <p>设置建议：</p>
     * <ul>
     *   <li>-1（默认）：使用Redisson看门狗机制，自动续期</li>
     *   <li>短任务：3000-10000毫秒（3-10秒）</li>
     *   <li>中等任务：30000-60000毫秒（30秒-1分钟）</li>
     *   <li>长任务：根据实际业务时间设置，建议不超过5分钟</li>
     * </ul>
     *
     * @return 过期时间（毫秒），-1表示自动续期
     */
    public int expireTime() default DistributeLockConstant.DEFAULT_EXPIRE_TIME;

    /**
     * 等待获取锁的时间（毫秒）
     * 
     * <p>当锁被其他线程持有时，当前线程的等待时间</p>
     * <p>设置建议：</p>
     * <ul>
     *   <li>-1（默认）：阻塞等待直到获取锁</li>
     *   <li>0：立即返回，不等待（适合秒杀、抢购场景）</li>
     *   <li>1000-5000：等待1-5秒（适合一般业务场景）</li>
     *   <li>较大值：适合允许长时间等待的场景</li>
     * </ul>
     * 
     * <p>注意：waitTime=-1时会一直阻塞，可能影响系统性能</p>
     *
     * @return 等待时间（毫秒），-1表示阻塞等待
     */
    public int waitTime() default DistributeLockConstant.DEFAULT_WAIT_TIME;
}
