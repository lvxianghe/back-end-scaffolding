package org.xiaoxingbomei.config.validation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * 数据验证配置
 */
@Configuration
public class ValidationConfig
{

    @Bean
    public Validator validator()
    {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

} 