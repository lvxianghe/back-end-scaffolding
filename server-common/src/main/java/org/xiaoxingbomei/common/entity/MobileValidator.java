package org.xiaoxingbomei.common.entity;


import cn.hutool.core.lang.Validator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.xiaoxingbomei.common.annotation.IsMobile;

/**
 * 手机号码格式校验器
 * 
 * <p>本类实现了{@link ConstraintValidator}接口，为{@link IsMobile}注解提供具体的验证逻辑。
 * 当Spring容器检测到带有{@code @IsMobile}注解的字段或参数时，会自动调用本验证器进行手机号码格式校验。</p>
 * 
 * <h3>验证规则:</h3>
 * <ul>
 *   <li>使用Hutool工具库的{@code Validator.isMobile()}方法进行验证</li>
 *   <li>支持中国大陆手机号码格式(11位数字，以1开头)</li>
 *   <li>空值(null)会通过验证，如需验证非空请配合{@code @NotBlank}注解使用</li>
 *   <li>空字符串("")会通过验证</li>
 * </ul>
 * 
 * <h3>支持的手机号码格式示例:</h3>
 * <ul>
 *   <li>13812345678 ✓</li>
 *   <li>15987654321 ✓</li>
 *   <li>18666666666 ✓</li>
 *   <li>1234567890 ✗ (不以1开头)</li>
 *   <li>138123456789 ✗ (超过11位)</li>
 * </ul>
 * 
 * @author Hollis
 * @see IsMobile
 * @see ConstraintValidator
 * @see cn.hutool.core.lang.Validator#isMobile(CharSequence)
 * @since 1.0
 */
public class MobileValidator implements ConstraintValidator<IsMobile, String>
{

    /**
     * 执行手机号码格式验证
     * 
     * <p>此方法会在Spring进行Bean Validation时被自动调用。
     * 当验证通过时返回true，验证失败时返回false。</p>
     * 
     * @param value 待验证的手机号码字符串，可能为null
     * @param context 约束验证上下文，可用于自定义错误消息等
     * @return {@code true} 如果手机号码格式正确或为null/空字符串；{@code false} 如果格式不正确
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 空值通过验证，如需验证非空请配合@NotBlank使用
        if (value == null || value.isEmpty()) {
            return true;
        }
        
        // 使用Hutool工具库进行手机号码格式验证
        return Validator.isMobile(value);
    }
}
