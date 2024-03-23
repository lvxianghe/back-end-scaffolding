package org.xiaoxingbomei.annotation;


import java.lang.annotation.*;

/**
 * 接口访问频率限制
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimiting
{

    // 窗口宽度 时间：秒 默认1分钟
    long period() default 60;

    /**
     * 在time时间段内允许访问的次数
     */
    long count() default 5;
}
