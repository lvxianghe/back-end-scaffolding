package org.xiaoxingbomei.exception;

import org.xiaoxingbomei.Enum.GlobalCodeEnum;

/**
 * 权限异常
 */
public class AuthException
{
    private static final long seriaVersionUID = 1L;


    private GlobalCodeEnum errorCode;


    private String errorMessage;



    public AuthException(GlobalCodeEnum errorCode)
    {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getMessage();
    }


    public AuthException(GlobalCodeEnum errorCode, String errorMessage)
    {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
