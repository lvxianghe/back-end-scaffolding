package org.xiaoxingbomei.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import org.xiaoxingbomei.config.llm.ChatClientFactory;
import org.xiaoxingbomei.constant.SystemPromptConstant;
import org.xiaoxingbomei.service.ChatService;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
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

        // 1.根据模型选择获取对应的 ChatClient
        ChatClient chatClient      = chatClientFactory.getClient(modelProvider, modelName);
        Boolean    isStreamBoolean = Boolean.valueOf(isStream);

        // 2.系统提示词
        if(StringUtils.isEmpty(systemPrompt))
        {
            systemPrompt = SystemPromptConstant.XIAOXINGBOMEI_SYSTEM_PROMPT;
        }

        // 3.构建prompt builder
        var promptBuilder = chatClient
                .prompt()
                .user(prompt)         // 用户提示词
                .system(systemPrompt) // 覆盖默认系统提示词，null和""都会覆盖的
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId)); // 会话记忆与会话id进行关联

        // 4.是否流式调用,执行最终的对话调用
        if(isStreamBoolean)
        {
            // 流式调用：返回实时流
            return promptBuilder.stream().content();
        }
        else
        {
            // 非流式调用：获取完整结果后包装成Flux
            String result = promptBuilder.call().content();
            return Flux.just(result);
        }

    }

    // 构建 advisors 方法
    private static List<Advisor> buildAdvisors(List<Advisor> additionalAdvisors)
    {
        List<Advisor> advisors = new ArrayList<>();
        advisors.add(new SimpleLoggerAdvisor()); // 添加默认的 logger advisor
        if (additionalAdvisors != null)
        {
            advisors.addAll(additionalAdvisors); // 将传入的额外 advisor 加入
        }
        return advisors;
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
