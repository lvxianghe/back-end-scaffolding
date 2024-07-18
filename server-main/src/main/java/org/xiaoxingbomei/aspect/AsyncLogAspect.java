//package org.xiaoxingbomei.aspect;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import org.xiaoxingbomei.annotation.AsyncLogRecord;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * 使用后置切入封装需要保存的日志，通过发布订阅模式，发布消息。
// * 说明一下这里可以对请求参数、响应参数以及一些异常信息进行保存，这个就需要根据自己的项目实际情况保存。
// */
//@Slf4j
//@Aspect
//@Component
//public class AsyncLogAspect
//{
//    private final SysLogEventPubListener eventPubListener;
//
//    public SysLogAspect(SysLogEventPubListener eventPubListener) {
//        this.eventPubListener = eventPubListener;
//    }
//
//    /**
//     * 以注解所标注的方法作为切入点
//     */
//    @Pointcut("@annotation(org.xiaoxingbomei.annotation.AsyncLogRecord)")
//    public void asyncLog() {}
//
//    /**
//     * 在切点之后织入
//     */
//    @After("sysLog()")
//    public void doAfter(JoinPoint joinPoint)
//    {
//        AsyncLogRecord logRecord = ((MethodSignature) joinPoint
//                .getSignature())
//                .getMethod()
//                .getAnnotation(AsyncLogRecord.class);
//
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        assert attributes != null;
//        HttpServletRequest request = attributes.getRequest();
//        String method = request.getMethod();
//        String url = request.getRequestURL().toString();
//        String ip = request.getRemoteAddr();
//        AsyncLog sysLog = new SysLog();
//        sysLog.setOperationType(logRecord.operationType().getCode());
//        sysLog.setDescription(logRecord.description());
//        sysLog.setRequestMethod(method);
//        sysLog.setOperationIp(ip);
//        sysLog.setOperationUrl(url);
//        // 从登录中token获取登录人员信息即可
//        sysLog.setOperationName("测试人员");
//        sysLog.setOperationTime(LocalDateTime.now());
//        // 发布消息
//        eventPubListener.pushListener(sysLog);
//        logger.info("=======日志发送成功，内容：{}", sysLog);
//    }
//}
