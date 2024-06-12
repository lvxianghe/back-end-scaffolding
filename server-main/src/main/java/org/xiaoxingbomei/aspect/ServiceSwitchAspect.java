package org.xiaoxingbomei.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.xiaoxingbomei.Enum.GlobalCodeEnum;
import org.xiaoxingbomei.annotation.ServiceSwitch;
import org.xiaoxingbomei.constant.Constant;
import org.xiaoxingbomei.entity.GlobalEntity;
import org.xiaoxingbomei.exception.BusinessException;

import java.lang.reflect.Method;

/**
 *
 */
@Aspect
@Component
@Log4j2
public class ServiceSwitchAspect
{
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 定义切点，使用了@ServiceSwitch注解的类或方法都拦截
     */
    @Pointcut("@annotation(org.xiaoxingbomei.annotation.ServiceSwitch)")
    public void aspect() {}

    /**
     * 环绕切面
     */
    @Around("aspect()")
    public Object around(ProceedingJoinPoint point) throws Throwable
    {

        // 获取被代理的方法的参数
        Object[] args = point.getArgs();
        // 获取被代理的对象
        Object target = point.getTarget();
        // 获取通知签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        try
        {
            // 获取被代理的方法
            Method method = target.getClass().getMethod(signature.getName(), signature.getParameterTypes());
            // 获取方法上的注解
            ServiceSwitch annotation = method.getAnnotation(ServiceSwitch.class);
            // 核心业务逻辑
            if (annotation != null)
            {
                String switchVal = annotation.switchVal();
                String message = annotation.message();
                // 从redis中取，使用中大家可以按照意愿自行修改。
                String configVal = redisTemplate.opsForValue().get(Constant.ConfigCode.XX_SWITCH);
                if (switchVal.equals(configVal))
                {
                    // 开关打开，则返回提示。
                    return new GlobalEntity("",String.valueOf(HttpStatus.FORBIDDEN.value()), message,"","");
                }
            }
            // 放行
            return point.proceed(args);
        } catch (Throwable e)
        {
            throw new BusinessException(GlobalCodeEnum.ERROR,e.getMessage());
        }
    }

}
