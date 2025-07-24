package org.xiaoxingbomei.common.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.xiaoxingbomei.common.entity.MobileValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 手机号码格式校验注解
 * 
 * <h3>原理说明:</h3>
 * <p>本注解基于Jakarta Bean Validation规范实现，通过自定义验证器{@link MobileValidator}
 * 对标注的字段或参数进行手机号码格式校验。验证器内部使用Hutool工具库的{@code Validator.isMobile()}
 * 方法进行手机号码格式验证，支持中国大陆手机号码格式。</p>
 * 
 * <h3>使用场景:</h3>
 * <ul>
 *   <li>实体类字段验证：在DO、DTO、VO等实体类的字段上标注</li>
 *   <li>方法参数验证：在Controller层或Service层的方法参数上标注</li>
 *   <li>方法返回值验证：在方法上标注，验证返回值</li>
 * </ul>
 * 
 * <h3>使用示例:</h3>
 * <pre>{@code
 * // 1. 在实体类字段上使用
 * public class UserDTO {
 *     @IsMobile(message = "请输入正确的手机号码")
 *     private String mobile;
 * }
 * 
 * // 2. 在Controller方法参数上使用
 * @PostMapping("/register")
 * public Result register(@IsMobile @RequestParam String phone) {
 *     // 业务逻辑
 * }
 * 
 * // 3. 在方法上使用(验证返回值)
 * @IsMobile
 * public String getUserMobile() {
 *     return userService.getMobile();
 * }
 * }</pre>
 * 
 * <h3>注意事项:</h3>
 * <ul>
 *   <li>需要在控制器类或方法上添加{@code @Validated}注解来启用验证</li>
 *   <li>空值(null)和空字符串("")会通过验证，如需验证非空请配合{@code @NotBlank}使用</li>
 *   <li>验证失败时会抛出{@code ConstraintViolationException}异常</li>
 * </ul>
 *
 * @author Hollis
 * @see MobileValidator
 * @see jakarta.validation.Constraint
 * @since 1.0
 */
@Constraint(validatedBy = MobileValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsMobile {
    /**
     * 验证失败时的错误信息
     * 
     * @return 错误消息，默认为"手机号格式不正确"
     */
    String message() default "手机号格式不正确";

    /**
     * 验证分组，用于在不同场景下应用不同的验证规则
     * 
     * @return 验证分组数组
     */
    Class<?>[] groups() default {};

    /**
     * 负载信息，可用于传递额外的元数据
     * 
     * @return 负载数组
     */
    Class<? extends Payload>[] payload() default {};
}
