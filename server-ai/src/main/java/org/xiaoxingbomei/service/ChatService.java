package org.xiaoxingbomei.service;


import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatService
{
    Flux<String>   chat
            (
                    String prompt,          // 用户提示词
                    String chatId,          // 会话id
                    String isStream,        // 是否流式打印
                    String modelProvider,   // 模型提供商
                    String modelName,       // 模型名称
                    String systemPrompt     // 系统提示词
            );

    GlobalResponse chat_for_string(String paramString);
    Flux<String>   chat_for_stream(String prompt,String chatId);

}
