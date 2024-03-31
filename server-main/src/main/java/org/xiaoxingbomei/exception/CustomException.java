package org.xiaoxingbomei.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomException extends ParamException
{
    private static final long seriaVersionUID = 1L;


    private String errorCode;


    private String errorMessage;


    public CustomException(List<String> fieldList, List<String> msgList, String errorCode, String errorMessage) {
        super(fieldList, msgList);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
