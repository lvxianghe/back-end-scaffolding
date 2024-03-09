package org.xiaoxingbomei.advice;

import cn.dev33.satoken.exception.*;
import cn.dev33.satoken.util.SaResult;

import org.slf4j.*;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.xiaoxingbomei.Enum.GlobalCodeEnum;
import org.xiaoxingbomei.entity.GlobalEntity;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{

    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * 0、Sa-token
     *  - NotLoginException
     *  - NotPermissionException
     *  - NotRoleException
     *  - NotSafeException
     *  - DisableServiceException
     *  - NotBasicAuthException
     * 1、服务调用
     *  - http超时
     *  - http缺少入参
     *  - http异常
     *  - feign超时
     *  - feign异常
     *  - redis超时
     *  - redis异常
     *  - kafka超时
     *  - kafka异常
     *  - rabbitmq超时
     *  - rabbitmq异常
     *  - es超时
     *  - es异常
     *  - mongodb超时
     *  - mongodb异常
     *  - hbase超时
     *  - hbase异常
     *  - eureka超时
     *  - eureka异常
     *  - oss超时
     *  - oss异常
     *  - minio超时
     *  - minio异常
     *  - swagger超时
     *  - swagger异常
     *  - caffeine超时
     *  - caffeine异常
     *  - mybatis超时
     *  - mybatis异常
     * 2、数据库
     *  - 语法错误
     *  - 数据库连接失败
     *  - 数据库异常
     *  - 数据库超时
     *  - mybatis错误
     *  - mysql错误
     *  - oracle错误
     *  - db2错误
     * 3、Java
     *  - 空指针
     *  - 类型转换
     *  - 数组越界
     *  - 数字格式
     *  - 日期格式
     *  - 文件上传
     *  - 文件下载
     *  - 网络异常
     *  - 线程异常
     *  - 死锁
     *  - 内存溢出
     *  - 栈溢出
     *  - 运行时异常
     *  - 编译时异常
     *  - Exception
     *  - Throwable
     *
     */


    /**
     * 0、sa-token
     */

    // 拦截：未登录异常
    @ExceptionHandler(NotLoginException.class)
    public GlobalEntity handlerException(NotLoginException e)
    {
        // 打印堆栈，以供调试
        e.printStackTrace();
        // 返回给前端
        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(), e.getMessage(),"权限不足，请联系管理员xxx","",null);
    }


    // 拦截：缺少权限异常
    @ExceptionHandler(NotPermissionException.class)
    public SaResult handlerException(NotPermissionException e)
    {
        e.printStackTrace();
        return SaResult.error("缺少权限：" + e.getMessage()); // e.getPermission()
    }


    // 拦截：缺少角色异常
    @ExceptionHandler(NotRoleException.class)
    public SaResult handlerException(NotRoleException e)
    {
        e.printStackTrace();
        return SaResult.error("缺少角色：" + e.getRole());
    }


    // 拦截：二级认证校验失败异常
    @ExceptionHandler(NotSafeException.class)
    public SaResult handlerException(NotSafeException e)
    {
        e.printStackTrace();
        return SaResult.error("二级认证校验失败：" +e.getMessage());  //  e.getService()  目前不支持这个版本
    }


    // 拦截：服务封禁异常
    @ExceptionHandler(DisableServiceException.class)
    public SaResult handlerException(DisableServiceException e)
    {
        e.printStackTrace();
        return SaResult.error("当前账号 " + e.getService() + " 服务已被封禁 (level=" + e.getLevel() + ")：" + e.getDisableTime() + "秒后解封");
    }






    /**
     * 1、服务调用
     */

    // 拦截：http超时
    @ExceptionHandler(value = ResourceAccessException.class)
    public GlobalEntity exceptionHandler(ResourceAccessException e)
    {
        e.printStackTrace();
        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(), e.getMessage(), "ResourceAccessException,当前功能不可用，请稍后再试", "", null);
    }

    // 拦截：http缺少入参
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public GlobalEntity exceptionHandler(HttpMessageNotReadableException e)
    {
        e.printStackTrace();
        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(), e.getMessage(), "HttpMessageNotReadableException,当前功能不可用，请稍后再试", "", null);
    }


    //




    /**
     * 2、数据库
     */

    // 拦截：数据库连接失败异常
    @ExceptionHandler(value = DataAccessException.class)
    public GlobalEntity exceptionHandler(DataAccessException e) {
        e.printStackTrace();
        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(), e.getMessage(), ",当前功能不可用，请稍后再试", "", null);
    }









    /**
     * Java
     */

    // 拦截：空指针异常
    @ExceptionHandler(NullPointerException.class)
    public GlobalEntity handlerException(NullPointerException e) {
        e.printStackTrace();
        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(), e.getMessage(), ",当前功能不可用，请稍后再试", "", null);
    }

    // 拦截：类型转换异常
    @ExceptionHandler(ClassCastException.class)
    public GlobalEntity handlerException(ClassCastException e) {
        e.printStackTrace();
        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(), e.getMessage(), ",当前功能不可用，请稍后再试", "", null);
    }

    // 拦截：数组越界异常
    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public GlobalEntity handlerException(ArrayIndexOutOfBoundsException e) {
        e.printStackTrace();
        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(), e.getMessage(), ",当前功能不可用，请稍后再试", "", null);
    }

    // 拦截：数字格式异常
    @ExceptionHandler(NumberFormatException.class)
    public GlobalEntity handlerException(NumberFormatException e) {
        e.printStackTrace();
        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(), e.getMessage(), ",当前功能不可用，请稍后再试", "", null);
    }

    // 拦截：日期格式异常
    @ExceptionHandler(IllegalArgumentException.class)
    public GlobalEntity handlerException(IllegalArgumentException e) {
        e.printStackTrace();
        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(), e.getMessage(), ",当前功能不可用，请稍后再试", "", null);
    }

    // 拦截：其它所有异常
    @ExceptionHandler(Exception.class)
    public GlobalEntity handlerException(Exception e) {
        e.printStackTrace();
        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(), e.getMessage(), ",当前功能不可用，请稍后再试", "", null);
    }


    // 拦截：顶级
    @ExceptionHandler(Throwable.class)
    public GlobalEntity handlerException(Throwable e) {
        e.printStackTrace();
        return GlobalEntity.error(GlobalCodeEnum.ERROR.getCode(), e.getMessage(), ",当前功能不可用，请稍后再试", "", null);
    }


}
