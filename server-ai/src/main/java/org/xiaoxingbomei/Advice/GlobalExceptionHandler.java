package org.xiaoxingbomei.Advice;


import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xiaoxingbomei.common.Enum.GlobalCodeEnum;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import org.xiaoxingbomei.common.utils.Exception_Utils;


/**
 * 全局异常处理
 */
@EqualsAndHashCode
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler
{


    // 拦截：数组越界异常
    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public GlobalResponse handlerArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException e)
    {
        Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        return GlobalResponse.error(null, GlobalCodeEnum.ERROR.getCode(), e.getMessage(), "ArrayIndexOutOfBoundsException,数组访问越界", "");
    }

    // 拦截：空指针异常
    @ExceptionHandler(NullPointerException.class)
    public GlobalResponse handlerNullPointerException(NullPointerException e)
    {
        Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        return GlobalResponse.error(null, GlobalCodeEnum.ERROR.getCode(), e.getMessage(), "NullPointerException,空指针异常", "");
    }

    // 拦截：非法参数异常
    @ExceptionHandler(IllegalArgumentException.class)
    public GlobalResponse handlerIllegalArgumentException(IllegalArgumentException e)
    {
        Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        return GlobalResponse.error(null, GlobalCodeEnum.ERROR.getCode(), e.getMessage(), "IllegalArgumentException,参数不合法", "");
    }

    // 拦截：数字格式异常
    @ExceptionHandler(NumberFormatException.class)
    public GlobalResponse handlerNumberFormatException(NumberFormatException e)
    {
        Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        return GlobalResponse.error(null, GlobalCodeEnum.ERROR.getCode(), e.getMessage(), "NumberFormatException,数字格式错误", "");
    }

    // 拦截：类型转换异常
    @ExceptionHandler(ClassCastException.class)
    public GlobalResponse handlerClassCastException(ClassCastException e)
    {
        Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        return GlobalResponse.error(null, GlobalCodeEnum.ERROR.getCode(), e.getMessage(), "ClassCastException,类型转换异常", "");
    }

    // 拦截：算术异常
    @ExceptionHandler(ArithmeticException.class)
    public GlobalResponse handlerArithmeticException(ArithmeticException e)
    {
        Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        return GlobalResponse.error(null, GlobalCodeEnum.ERROR.getCode(), e.getMessage(), "ArithmeticException,算术运算异常", "");
    }

    // 拦截：字符串越界异常
    @ExceptionHandler(StringIndexOutOfBoundsException.class)
    public GlobalResponse handlerStringIndexOutOfBoundsException(StringIndexOutOfBoundsException e)
    {
        Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        return GlobalResponse.error(null, GlobalCodeEnum.ERROR.getCode(), e.getMessage(), "StringIndexOutOfBoundsException,字符串访问越界", "");
    }

    // 拦截：索引越界异常
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public GlobalResponse handlerIndexOutOfBoundsException(IndexOutOfBoundsException e)
    {
        Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        return GlobalResponse.error(null, GlobalCodeEnum.ERROR.getCode(), e.getMessage(), "IndexOutOfBoundsException,集合索引越界", "");
    }

    // 拦截：非法状态异常
    @ExceptionHandler(IllegalStateException.class)
    public GlobalResponse handlerIllegalStateException(IllegalStateException e)
    {
        Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        return GlobalResponse.error(null, GlobalCodeEnum.ERROR.getCode(), e.getMessage(), "IllegalStateException,非法状态异常", "");
    }

    // 拦截：不支持的操作异常
    @ExceptionHandler(UnsupportedOperationException.class)
    public GlobalResponse handlerUnsupportedOperationException(UnsupportedOperationException e)
    {
        Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        return GlobalResponse.error(null, GlobalCodeEnum.ERROR.getCode(), e.getMessage(), "UnsupportedOperationException,不支持的操作", "");
    }

    // 拦截：其它所有异常
    @ExceptionHandler(Exception.class)
    public GlobalResponse handlerException(Exception e)
    {
        Exception_Utils.recursiveReversePrintStackCauseCommon(e);
        return GlobalResponse.error(null, GlobalCodeEnum.ERROR.getCode(), e.getMessage(), "Exception,当前功能不可用，请稍后再试", "");
    }




}
