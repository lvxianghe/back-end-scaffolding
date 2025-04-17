package org.xiaoxingbomei.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.Message;

/**
 * 大模型会话历史记录
 */
@NoArgsConstructor
@Data
public class LlmChatHistory
{
    private String chatId;      // 会话id
    private String chatRole;    // 会话角色
    private String chatContent; // 会话内容

    /**
     * Message 转 MessageVO
     * MessageType是枚举，我们只需要 user 和 assistant 两种
     */
    public LlmChatHistory(Message message,String chatId)
    {
        switch (message.getMessageType())
        {
            case USER:
                this.chatRole = "user";
                break;
            case ASSISTANT:
                this.chatRole = "assistant";
                break;
            // case SYSTEM:
            //     this.chatRole = "system";
            //     break;
            // case TOOL:
            //     this.chatRole = "tool";
            //     break;
            default:
                this.chatRole = "unknown";
                break;
        }
        this.chatContent = message.getText();
        this.chatId      = chatId;
    }

}
