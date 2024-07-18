package org.xiaoxingbomei.annotation;

import org.xiaoxingbomei.Enum.OperateTypeEnum;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 */
@Target(ElementType.METHOD) // 注解只能用于方法
@Retention(RetentionPolicy.RUNTIME) // 修饰注解的生命周期
@Documented
public @interface AsyncLogRecord
{

    String value() default "";

    // 请求描述
    String description() default "";

    // 操作类型
    OperateTypeEnum operationType() default OperateTypeEnum.OTHER;

}