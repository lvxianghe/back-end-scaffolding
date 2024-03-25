package org.xiaoxingbomei.exception;

import lombok.Getter;
import lombok.Setter;
import org.xiaoxingbomei.Enum.GlobalCodeEnum;

/**
 * 用户异常
 */
@Getter
@Setter
public class UserException extends RuntimeException
{
    private static final long serialVersionUID = 1L;


    private GlobalCodeEnum errorCode;


    private String errorMsg;


    public UserException(GlobalCodeEnum errorCode) {
        super();
        this.errorCode = errorCode;
        this.errorMsg = errorCode.getMessage();
    }


    public UserException(GlobalCodeEnum errorCode, String errorMsg) {
        super();
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}