package org.xiaoxingbomei.advice;

import cn.dev33.satoken.exception.*;
import cn.dev33.satoken.util.SaResult;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.BindingException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xiaoxingbomei.common.Enum.GlobalCodeEnum;
import org.xiaoxingbomei.common.entity.GlobalEntity;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import org.xiaoxingbomei.common.utils.Exception_Utils;


/**
 * 全局异常处理
 */
@EqualsAndHashCode
@Slf4j
@RestControllerAdvice
public class ExceptionHandler
{


    /**
     * 数据库
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(BindingException.class)
    public GlobalResponse handlerException(BindingException e)
    {
        // 打印堆栈，以供调试
        Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        // 返回给前端
        return GlobalResponse.error(null,GlobalCodeEnum.ERROR.getCode(), e.getMessage(),"数据库有问题哦","");
    }


    /**
     * 0、sa-token
     */

    // 拦截：未登录异常

    // 拦截：未登录异常
    @org.springframework.web.bind.annotation.ExceptionHandler(NotLoginException.class)
    public GlobalResponse handlerException(NotLoginException e)
    {
        // 打印堆栈，以供调试
        Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        // 返回给前端
        return GlobalResponse.error(null,GlobalCodeEnum.ERROR.getCode(), e.getMessage(),"权限不足，请联系管理员xxx","");
    }


    // 拦截：缺少权限异常
    @org.springframework.web.bind.annotation.ExceptionHandler(NotPermissionException.class)
    public SaResult handlerException(NotPermissionException e)
    {
        Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        return SaResult.error("缺少权限：" + e.getMessage()); // e.getPermission()
    }


    // 拦截：缺少角色异常
    @org.springframework.web.bind.annotation.ExceptionHandler(NotRoleException.class)
    public SaResult handlerException(NotRoleException e)
    {
        Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        return SaResult.error("缺少角色：" + e.getRole());
    }


    // 拦截：二级认证校验失败异常
    @org.springframework.web.bind.annotation.ExceptionHandler(NotSafeException.class)
    public SaResult handlerException(NotSafeException e)
    {
        Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        return SaResult.error("二级认证校验失败：" +e.getMessage());  //  e.getService()  目前不支持这个版本
    }


    // 拦截：服务封禁异常
    @org.springframework.web.bind.annotation.ExceptionHandler(DisableServiceException.class)
    public SaResult handlerException(DisableServiceException e)
    {
        Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        return SaResult.error("当前账号 " + e.getService() + " 服务已被封禁 (level=" + e.getLevel() + ")：" + e.getDisableTime() + "秒后解封");
    }


    // 拦截：其它所有异常
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public GlobalEntity handlerException(Exception e)
    {
        Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        return GlobalEntity.error(null, GlobalCodeEnum.ERROR.getCode(), e.getMessage(), "Exception,当前功能不可用，请稍后再试", "");
    }


}
