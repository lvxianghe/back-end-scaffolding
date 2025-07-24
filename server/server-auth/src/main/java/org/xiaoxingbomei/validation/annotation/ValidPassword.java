package org.xiaoxingbomei.validation.annotation;

import org.xiaoxingbomei.validation.validator.PasswordValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 密码复杂度验证注解
 * 
 * @author xiaoxingbomei
 * @date 2024-01-01
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface ValidPassword {
    
    String message() default "密码不符合复杂度要求";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * 最小长度
     */
    int minLength() default 8;
    
    /**
     * 是否需要包含数字
     */
    boolean requireDigit() default true;
    
    /**
     * 是否需要包含字母
     */
    boolean requireLetter() default true;
    
    /**
     * 是否需要包含特殊字符
     */
    boolean requireSpecialChar() default false;
} 