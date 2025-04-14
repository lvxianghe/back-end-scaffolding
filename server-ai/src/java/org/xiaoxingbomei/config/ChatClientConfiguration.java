package org.xiaoxingbomei.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfiguration
{
    @Bean
    public ChatClient chatClient(OllamaChatModel model)
    {
        return ChatClient
                .builder(model)
                .defaultSystem("你是一个热心的智能助手，你的名字是小型博美，请以小型博美的身份回答问题")
                .build();
    }


}
