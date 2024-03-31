package org.xiaoxingbomei.exception;

import lombok.Data;

import java.util.List;

/**
 * 参数异常
 */
@Data
public class ParamException extends Exception
{

    private final List<String> fieldList;
    private final List<String> msgList;

    public ParamException(List<String> fieldList, List<String> msgList) {
        this.fieldList = fieldList;
        this.msgList = msgList;
    }
}
