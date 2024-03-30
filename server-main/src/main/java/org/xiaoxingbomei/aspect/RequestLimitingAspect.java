package org.xiaoxingbomei.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.xiaoxingbomei.Enum.GlobalCodeEnum;
import org.xiaoxingbomei.annotation.RequestLimiting;
import org.xiaoxingbomei.exception.UserException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;


/**
 * 用户粒度限流切面
 */
@Aspect
@Component
//@Log4j2

public class RequestLimitingAspect
{
    private static Logger log = LoggerFactory.getLogger(RequestLimitingAspect.class);

    @Resource
    private RedisTemplate redisTemplate;

    // 切点
    @Pointcut("@annotation(requestLimiting)")
    public void aspect(RequestLimiting requestLimiting) {}

    // 环绕切面
    @Around("aspect(requestLimiting)")
    public Object around(ProceedingJoinPoint joinPoint, RequestLimiting requestLimiting) throws Throwable
    {
        // 获取注解参数
        long period = requestLimiting.period();
        long limitCount = requestLimiting.count();

        ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = attributes.getRequest();

        // 请求ip
        String ip = request.getRemoteAddr();

        // 请求uri
        String uri = request.getRequestURI();

        // redis key
        String key = "req_limiting_" + uri + "_" + ip;

        // 获取ZSetOperations
        ZSetOperations<String,Long> zSetOperations = redisTemplate.opsForZSet();

        // 查询访问次数
        long count = zSetOperations.zCard(key);

        // 判断访问次数
        if (count > limitCount)
        {
            log.info("限流拦截接口：{},用户{},访问超过【{}次/{}s】限制",uri, ip, limitCount, period );
            throw new UserException(GlobalCodeEnum.USER_ACCESS_EXCEED, "您的请求太快啦，请稍后重试~");
        }

        // 获取当前时间
        long currentTimeMillis = System.currentTimeMillis();

        // 添加当前时间
        zSetOperations.add(key, currentTimeMillis, currentTimeMillis);

        // 设置过期时间
        redisTemplate.expire(key, period, TimeUnit.SECONDS);

        // 删除窗口外的值
        zSetOperations.removeRangeByScore(key, 0, currentTimeMillis - period * 1000);

        // 放行
        return joinPoint.proceed();
    }

}