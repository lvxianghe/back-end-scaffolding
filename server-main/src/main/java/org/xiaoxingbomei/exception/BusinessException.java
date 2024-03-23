package org.xiaoxingbomei.exception;

import lombok.Getter;
import lombok.Setter;
import org.xiaoxingbomei.Enum.GlobalCodeEnum;

/**
 * 业务异常
 * @Author lvxianghe
 */
@Getter
@Setter
public class BusinessException extends RuntimeException
{
    private static final long seriaVersionUID = 1L;


    private GlobalCodeEnum errorCode;


    private String errorMessage;



    public BusinessException(GlobalCodeEnum errorCode)
    {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getMessage();
    }


    public BusinessException(GlobalCodeEnum errorCode, String errorMessage)
    {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }






}