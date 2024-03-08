package org.xiaoxingbomei.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * http通用返回体
 */
@Getter
@Setter
public class GlobalEntity
{

    String data;            // 响应主体
    String code;               // 响应码
    String message;         // 技术message
    String userMessage;     // 用户message
    String businessMessage; // 业务message

    // 无参构造
    public GlobalEntity() {}

    // 有参构造
    public GlobalEntity(String data, String code, String message, String userMessage, String businessMessage)
    {
        this.data = data;
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
        this.businessMessage = businessMessage;
    }

    // success
    public static GlobalEntity success(String data, String code, String message, String userMessage, String businessMessage)
    {
        GlobalEntity globalEntity = new GlobalEntity(data, code, message, userMessage, businessMessage);
        return globalEntity;
    }

    // error
    public static GlobalEntity error(String data, String code, String message, String userMessage, String businessMessage)
    {
        GlobalEntity globalEntity = new GlobalEntity(data, code, message, userMessage, businessMessage);
        return globalEntity;
    }

}
