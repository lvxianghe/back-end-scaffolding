package org.xiaoxingbomei.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import org.xiaoxingbomei.common.utils.Request_Utils;
import org.xiaoxingbomei.config.llm.ChatClientFactory;
import org.xiaoxingbomei.constant.SystemPromptConstant;
import org.xiaoxingbomei.dao.localhost.ChatMapper;
import org.xiaoxingbomei.service.ChatService;
import org.xiaoxingbomei.vo.LlmChatHistory;
import org.xiaoxingbomei.vo.LlmChatHistoryList;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService
{
    @Autowired
    ChatMemory chatMemory;

    @Autowired
    private ChatMapper chatMapper;


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
            StringBuilder fullResponse = new StringBuilder();
            return promptBuilder.stream().content()
                .doOnNext(chunk -> {
                    fullResponse.append(chunk);
                })
                .doOnComplete(() -> {
                    // 流式调用完成后保存对话历史
                    saveChatHistoryToDatabase(chatId, prompt, fullResponse.toString());
                })
                .doOnError(error -> {
                    log.error("流式对话发生错误, chatId: {}", chatId, error);
                });
        }
        else
        {
            // 非流式调用：获取完整结果后包装成Flux
            String result = promptBuilder.call().content();
            
            // 保存对话历史到数据库
            saveChatHistoryToDatabase(chatId, prompt, result);
            
            return Flux.just(result);
        }
    }
    
    /**
     * 保存对话历史到数据库
     */
    private void saveChatHistoryToDatabase(String chatId, String userMessage, String assistantMessage) {
        try {
            List<LlmChatHistory> chatHistories = new ArrayList<>();
            
            // 保存用户消息
            LlmChatHistory userHistory = new LlmChatHistory();
            userHistory.setChatId(chatId);
            userHistory.setChatRole("user");
            userHistory.setChatContent(userMessage);
            chatHistories.add(userHistory);
            
            // 保存AI回复
            LlmChatHistory assistantHistory = new LlmChatHistory();
            assistantHistory.setChatId(chatId);
            assistantHistory.setChatRole("assistant");
            assistantHistory.setChatContent(assistantMessage);
            chatHistories.add(assistantHistory);
            
            // 批量插入数据库
            chatMapper.insertChatHistory(chatHistories);
            log.info("成功保存对话历史到数据库, chatId: {}, 用户消息: {}, AI回复长度: {}", 
                chatId, userMessage.length() > 50 ? userMessage.substring(0, 50) + "..." : userMessage, 
                assistantMessage.length());
            
        } catch (Exception e) {
            log.error("保存对话历史到数据库失败, chatId: {}", chatId, e);
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

    @Override
    public GlobalResponse getAllChatHistoryList()
    {
        try {
            // 1、获取前端参数
            List<LlmChatHistoryList> allChatHistoryList = chatMapper.getAllChatHistoryList();
            
            // 2、检查空列表
            if (allChatHistoryList == null || allChatHistoryList.isEmpty()) {
                log.info("getAllChatHistoryList: 暂无会话历史记录");
                return GlobalResponse.success(new ArrayList<>(), "暂无会话历史记录");
            }
            
            log.info("getAllChatHistoryList: 获取到 {} 条会话记录", allChatHistoryList.size());
            
            // 3、直接返回列表，不要转换为字符串
            return GlobalResponse.success(allChatHistoryList, "获取全部会话历史成功");
            
        } catch (Exception e) {
            log.error("获取会话历史列表失败", e);
            return GlobalResponse.error("获取会话历史列表失败：" + e.getMessage());
        }
    }

    @Override
    public GlobalResponse insertChatHistoryList(String paramString)
    {
        try {
            // 1、获取前端参数
            String chatId       = Request_Utils.getParam(paramString, "chatId");
            String chatTittle   = Request_Utils.getParam(paramString, "chatTittle");
            String chatTag      = Request_Utils.getParam(paramString, "chatTag");
            // 移除未使用的chatType参数
            
            // 参数校验
            if (StringUtils.isEmpty(chatId) || StringUtils.isEmpty(chatTittle)) {
                return GlobalResponse.error("会话ID和标题不能为空");
            }

            // 2、插入操作
            LlmChatHistoryList llmChatHistoryList = new LlmChatHistoryList();
            llmChatHistoryList.setChatId(chatId);
            llmChatHistoryList.setChatTittle(chatTittle);
            llmChatHistoryList.setChatTag(StringUtils.isEmpty(chatTag) ? "默认" : chatTag);
            // createTime和updateTime由数据库自动生成

            int result = chatMapper.insertChatHistoryList(llmChatHistoryList);
            
            if (result > 0) {
                // 3、封装响应体
                HashMap<String, Object> resultMap = new HashMap<>();
                resultMap.put("chatId", chatId);
                resultMap.put("chatTittle", chatTittle);
                return GlobalResponse.success(resultMap, "插入新的会话历史成功");
            } else {
                return GlobalResponse.error("插入会话历史失败");
            }
            
        } catch (Exception e) {
            log.error("插入会话历史失败", e);
            return GlobalResponse.error("插入会话历史失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GlobalResponse deleteChatHistoryList(String paramString)
    {
        // 1、获取前端参数
        String chatId = Request_Utils.getParam(paramString, "chatId");

        chatMapper.deleteChatHistoryList(chatId);
        chatMapper.deleteChatHistory(chatId);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("chatId", chatId);
        return GlobalResponse.success(resultMap,"删除历史会话成功");
    }

    @Override
    public GlobalResponse updateChatHistoryList(String paramString)
    {
        // 1、获取前端参数
        String chatId       = Request_Utils.getParam(paramString, "chatId");
        String chatTittle   = Request_Utils.getParam(paramString, "chatTittle");
        String chatTag      = Request_Utils.getParam(paramString, "chatTag");
        String chatType     = Request_Utils.getParam(paramString, "chatType");
        String updateTime   = Request_Utils.getParam(paramString, "updateTime");
        if (!chatId.isEmpty() && !chatTittle.isEmpty())
        {
            LlmChatHistoryList llmChatHistoryList = new LlmChatHistoryList();
            llmChatHistoryList.setChatId(chatId);
            llmChatHistoryList.setChatTittle(chatTittle);
            llmChatHistoryList.setChatTag(chatTag);
            llmChatHistoryList.setUpdateTime(updateTime);

            chatMapper.updateChatHistoryList(llmChatHistoryList);
        }

        // 3、分装响应体
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("chatId", chatId);
        return GlobalResponse.success(resultMap,"更新历史会话成功");
    }

    @Override
    public List<LlmChatHistory> getChatHistoryById(String chatId)
    {

        // 1、根据chatId获取历史会话

        List<LlmChatHistory> messages = chatMapper.getChatHistoryById(chatId);
        if(messages.isEmpty())
        {
            return List.of();
        }
        return messages;
    }

    /**
     * 将所有的聊天记录初始化到chatMemory中
     */
    @PostConstruct
    private void init()
    {
        System.out.println("OllamaChatClientConfiguration init");
        // 获取全部聊天记录
        List<LlmChatHistory> allChatHistory = chatMapper.getAllChatHistory();
        // 遍历聊天记录，根据chatId分组，将聊天记录转换为Message对象，并添加到chatMemory中
        allChatHistory.stream().collect(
                // 分组，根据chatId分组
                Collectors.groupingBy(LlmChatHistory::getChatId)
        ).forEach(
                // 遍历分组
                (chatId, llmChatHistoryList) ->
                {
                    // 将聊天记录转换为Message对象
                    List<Message> messages = llmChatHistoryList.stream().map(LlmChatHistory::toMessage).toList();
                    log.info("init chatMemory chatId:{}-->chatHistory:{}", chatId,messages);
                    // 将Message对象添加到chatMemory中
                    chatMemory.add(chatId, messages);
                }
        );
    }
}
