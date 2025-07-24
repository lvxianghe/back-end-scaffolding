package org.xiaoxingbomei.validation.validator;

import org.xiaoxingbomei.validation.annotation.ValidPassword;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 密码验证器
 * 
 * @author xiaoxingbomei
 * @date 2024-01-01
 */
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private int minLength;
    private boolean requireDigit;
    private boolean requireLetter;
    private boolean requireSpecialChar;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.requireDigit = constraintAnnotation.requireDigit();
        this.requireLetter = constraintAnnotation.requireLetter();
        this.requireSpecialChar = constraintAnnotation.requireSpecialChar();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }

        // 检查长度
        if (password.length() < minLength) {
            return false;
        }

        // 检查是否包含数字
        if (requireDigit && !Pattern.compile(".*\\d.*").matcher(password).matches()) {
            return false;
        }

        // 检查是否包含字母
        if (requireLetter && !Pattern.compile(".*[a-zA-Z].*").matcher(password).matches()) {
            return false;
        }

        // 检查是否包含特殊字符
        if (requireSpecialChar && !Pattern.compile(".*[!@#$%^&*(),.?\":{}|<>].*").matcher(password).matches()) {
            return false;
        }

        return true;
    }
} 