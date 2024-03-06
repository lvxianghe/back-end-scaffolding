//package org.xiaoxingbomei.advice;
//
//import cn.dev33.satoken.exception.*;
//import cn.dev33.satoken.util.SaResult;
//
//import org.slf4j.*;
//import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.xiaoxingbomei.Enum.GlobalCodeEnum;
//import org.xiaoxingbomei.entity.GlobalEntity;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler
//{
//
//
//    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//
//    // 拦截：未登录异常
//    @ExceptionHandler(NotLoginException.class)
//    public GlobalEntity handlerException(NotLoginException e)
//    {
//        // 打印堆栈，以供调试
//        e.printStackTrace();
//        // 返回给前端
//        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(), e.getMessage(),"权限不足，请联系管理员xxx","",null);
//    }
//
//
//    // 拦截：缺少权限异常
//    @ExceptionHandler(NotPermissionException.class)
//    public SaResult handlerException(NotPermissionException e)
//    {
//        e.printStackTrace();
//        return SaResult.error("缺少权限：" + e.getMessage()); // e.getPermission()
//    }
//
//
//    // 拦截：缺少角色异常
//    @ExceptionHandler(NotRoleException.class)
//    public SaResult handlerException(NotRoleException e)
//    {
//        e.printStackTrace();
//        return SaResult.error("缺少角色：" + e.getRole());
//    }
//
//
//    // 拦截：二级认证校验失败异常
//    @ExceptionHandler(NotSafeException.class)
//    public SaResult handlerException(NotSafeException e)
//    {
//        e.printStackTrace();
//        return SaResult.error("二级认证校验失败：" +e.getMessage());  //  e.getService()  目前不支持这个版本
//    }
//
//
//    // 拦截：服务封禁异常
//    @ExceptionHandler(DisableServiceException.class)
//    public SaResult handlerException(DisableServiceException e)
//    {
//        e.printStackTrace();
//        return SaResult.error("当前账号 " + e.getService() + " 服务已被封禁 (level=" + e.getLevel() + ")：" + e.getDisableTime() + "秒后解封");
//    }
//
//
//    // 拦截：Http Basic 校验失败异常
//    @ExceptionHandler(NotBasicAuthException.class)
//    public SaResult handlerException(NotBasicAuthException e)
//    {
//        e.printStackTrace();
//        return SaResult.error(e.getMessage());
//    }
//
//
//    // 拦截：Feigh
////    @ExceptionHandler(FeignException.class)
////    public GlobalEntity handlerException(FeignException e)
////    {
////        e.printStackTrace();
////        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(),e.getMessage(),"feign的问题，请稍后再试","",null);
////
////    }
//
//
//    // http缺少入参
//    @ExceptionHandler(value = HttpMessageNotReadableException.class)
//    public GlobalEntity exceptionHandler(Exception e) {
//        e.printStackTrace();
//        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(), "Required request body is missing", "页面当前不可用，请稍后重试", "", null);
//    }
//
//
//    // 拦截：其它所有异常
//    @ExceptionHandler(Exception.class)
//    public GlobalEntity handlerException(Exception e) {
//        e.printStackTrace();
//        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(), e.getMessage(), "当前功能不可用，请稍后再试", "", null);
//    }
//
//
//    // 拦截：顶级
//    @ExceptionHandler(Throwable.class)
//    public GlobalEntity handlerException(Throwable e) {
//        e.printStackTrace();
//        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(), e.getMessage(), "出大问题啦", "", null);
//    }
//
//
//}
