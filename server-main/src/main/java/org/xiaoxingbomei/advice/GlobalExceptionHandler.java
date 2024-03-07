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
//    // 拦截：Exception
//    @ExceptionHandler(Exception.class)
//    public GlobalEntity handlerException(Exception e) {
//        e.printStackTrace();
//        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(), e.getMessage(), "当前功能不可用，请稍后再试", "", null);
//    }
//
//
//    // Mysql:DataAccessException Spring 的数据访问异常，可能由数据库操作引起，如 SQL 语法错误、数据库连接问题等
//
//    // Mysql:MyBatisSystemException MyBatis 的系统异常，可能由 MyBatis 的配置错误或使用问题引起
//
//    // Mysql:PersistenceException MyBatis 的持久化异常，可能由数据库操作异常引起，如插入、更新、删除操作失败
//
//    // Mysql:SQLException 数据库操作异常，可能由数据库连接问题、事务处理问题等引起
//
//    // Mysql:TransactionSystemException Spring 的事务系统异常，可能由于事务管理配置错误或使用问题引起
//
//    // Mysql:BeanInstantiationException Spring 的 Bean 实例化异常，可能由于无法实例化 Mapper 接口引起
//
//    // Spring:NestedIOException Spring 的 IO 异常，可能由于读取 Mapper XML 文件失败引起
//
//
//    // 拦截：Throwable
//    @ExceptionHandler(Throwable.class)
//    public GlobalEntity handlerException(Throwable e) {
//        e.printStackTrace();
//        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(), e.getMessage(), "出大问题啦", "", null);
//    }
//
//
//}
