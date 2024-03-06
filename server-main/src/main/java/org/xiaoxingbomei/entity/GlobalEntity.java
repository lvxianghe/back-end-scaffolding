package org.xiaoxingbomei.entity;

/**
 * http通用返回体
 */
public class GlobalEntity
{

    String data;            // 响应主体
    int code;               // 响应码
    String message;         // 技术message
    String userMessage;     // 用户message
    String businessMessage; // 业务message

    // 无参构造
    public GlobalEntity() {}

    // 有参构造
    public GlobalEntity(String data, int code, String message, String userMessage, String businessMessage)
    {
        this.data = data;
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
        this.businessMessage = businessMessage;
    }

    // success

    // error


}
