package org.xiaoxingbomei.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * http通用返回体
 */
@Data
public class GlobalEntity<T> implements Serializable
{
    private static final long serialVersionUID  = 1L;

    public static final String SUCCESS = "0";

    public static final String ERROR = "-1";

    private T data;                 // 响应主体
    private String code;            // 响应码
    private String message;         // 技术message
    private String userMessage;     // 用户message
    private String businessMessage; // 业务message

    // 无参构造
    public GlobalEntity() {}

    // 有参构造
    public GlobalEntity(T data, String code, String message, String userMessage, String businessMessage)
    {
        this.data = data;
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
        this.businessMessage = businessMessage;
    }

    // success-data
    public GlobalEntity<T> success(T data)
    {
        this.data = data;
        this.code = SUCCESS;
        return this;
    }

    // error-data
    public GlobalEntity<T> error(T data)
    {
        this.data = data;
        this.code = ERROR;
        return this;
    }

    // success-all
    public static GlobalEntity success(String data, String code, String message, String userMessage, String businessMessage)
    {
        GlobalEntity globalEntity = new GlobalEntity(data, code, message, userMessage, businessMessage);
        return globalEntity;
    }

    // error-all
    public static GlobalEntity error(String data, String code, String message, String userMessage, String businessMessage)
    {
        GlobalEntity globalEntity = new GlobalEntity(data, code, message, userMessage, businessMessage);
        return globalEntity;
    }

}
