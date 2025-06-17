package org.xiaoxingbomei.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xiaoxingbomei.dao.localhost.ChatMapper;
import org.xiaoxingbomei.vo.LlmChatHistory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class ChatClient_Utils
{
    // 静态实例，用于在静态方法中访问实例成员
    private static ChatClient_Utils instance;

    // 构造函数，在Spring创建Bean时将实例保存到静态变量中
    public ChatClient_Utils() {
        instance = this;
    }

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private ChatMemory chatMemory;


    // =========================================

}
