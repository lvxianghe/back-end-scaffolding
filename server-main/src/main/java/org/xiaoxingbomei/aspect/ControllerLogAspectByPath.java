package org.xiaoxingbomei.aspect;


import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 统一日志处理：Controller层 通过工程路径切面实现
 *
 * 在微服务中，为了更好的了解接口的运行状态，我们经常会通过日志查看接口的入参、出参、调用地址、调用方法和接口的响应时间。
 * 通过AOP面向切面编程，零侵入完成对接口信息的监控
 * 从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率。
 *
 * `@Aspect`:作用是把当前类标识为一个切面供容器读取
 *
 * `@Pointcut`：Pointcut是植入Advice的触发条件。每个Pointcut的定义包括2部分，一是表达式，二是方法签名
 *
 * `@Around`：环绕增强，相当于MethodInterceptor
 *
 * `@AfterReturning`：后置增强，相当于AfterReturningAdvice，方法正常退出时执行
 *
 * `@Before`：标识一个前置增强方法，相当于BeforeAdvice的功能，相似功能的还有
 *
 * `@AfterThrowing`：异常抛出增强，相当于ThrowsAdvice
 *
 * `@After`: final增强，不管是抛出异常或者正常退出都会执行
 */
@Aspect
@Component
@Log4j2
public class ControllerLogAspectByPath
{

    /**
     * 接口响应时间
     */
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 切入点
     */
    @Pointcut("execution(public * org.xiaoxingbomei.controller..*.*(..))")
    public void ControllerLogAspectByPath() {}

    /**
     * 在切入点开始处切入内容
     */
    @Before("ControllerLogAspectByPath()")
    public void doBefore(JoinPoint joinPoint) throws Throwable
    {
        startTime.set(System.currentTimeMillis());

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        log.info("URL : " + request.getRequestURL().toString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));

    }

    /**
     * 在切入点return内容之后切入内容（输出返回参数及处理时间）
     */
    @AfterReturning(returning = "ret", pointcut = "ControllerLogAspectByPath()")
    public void doAfterReturning(Object ret) throws Throwable
    {
        // 处理完请求，返回内容
        log.info("RESPONSE : " + ret);
        log.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
    }




}