package org.xiaoxingbomei.config.llm;

import org.checkerframework.checker.index.qual.PolyIndex;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.xiaoxingbomei.constant.SystemPromptConstant;

/**
 * 大模型应用对话的配置
 */
@Configuration
public class LlmChatClientConfiguration
{

    @Autowired
    ChatMemory chatMemory;

    /**
     * 配置ollama的chatClient
     */
//    @Bean
//    @Primary
//    public ChatClient ollamaChatClient(OllamaChatModel model)
//    {
//        return ChatClient
//                .builder(model)
//                .defaultSystem(SystemPromptConstant.XIAOXINGBOMEI_SYSTEM_PROMPT)
//                .defaultAdvisors(new SimpleLoggerAdvisor()) // 先只用日志增强
//                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
//                .build();
//    }

    /**
     * 配置openai的chatClient
     */
    @Bean
    public ChatClient openAiChatClient(OpenAiChatModel model)
    {
        return ChatClient
                .builder(model)
                .defaultSystem(SystemPromptConstant.GAME_SYSTEM_PROMPT)
                .defaultAdvisors(new SimpleLoggerAdvisor()) // 先只用日志增强
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
}
