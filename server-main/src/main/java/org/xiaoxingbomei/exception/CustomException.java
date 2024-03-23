package org.xiaoxingbomei.exception;

import lombok.Getter;
import lombok.Setter;
import org.xiaoxingbomei.Enum.GlobalCodeEnum;

@Getter
@Setter
public class CustomException extends Throwable
{
    private static final long seriaVersionUID = 1L;


    private String errorCode;


    private String errorMessage;

    public CustomException(String errorCode,String errorMessage)
    {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }


}
