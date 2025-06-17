package org.xiaoxingbomei.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import org.xiaoxingbomei.config.llm.ChatClientFactory;
import org.xiaoxingbomei.service.ChatService;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService
{
    @Autowired
    ChatMemory chatMemory;

    @Autowired
    private ChatClientFactory chatClientFactory;

    // ==============================================================


    @Override
    public Flux<String> chat(String prompt, String chatId, String isStream, String modelProvider, String modelName, String systemPrompt)
    {
        List<Advisor> advisors = List.of
                (
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                );

        ChatClient chatClient      = chatClientFactory.getClient(modelProvider, modelName);
        Boolean    isStreamBoolean = Boolean.valueOf(isStream);

        // 如果是普通对话，路由是否流式响应
        return null;

    }

    @Override
    public GlobalResponse chat_for_string(String prompt)
    {
        log.info("chat_for_string");

        ChatClient chatClient = chatClientFactory.getClient("ollama", "qwen3:14b");

        String resultContent = chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("resultContent", resultContent);
        resultMap.put("prompt", prompt);
        return GlobalResponse.success(resultMap);
    }

    @Override
    public Flux<String> chat_for_stream(String prompt, String chatId) {
        return null;
    }
}
