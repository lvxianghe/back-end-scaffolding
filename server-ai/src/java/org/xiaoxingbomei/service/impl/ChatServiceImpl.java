package org.xiaoxingbomei.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.xiaoxingbomei.dao.localhost.ChatMapper;
import org.xiaoxingbomei.entity.response.ResponseEntity;
import org.xiaoxingbomei.service.ChatService;
import org.xiaoxingbomei.utils.Request_Utils;
import org.xiaoxingbomei.vo.LlmChatHistory;
import org.xiaoxingbomei.vo.LlmChatHistoryList;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ChatServiceImpl implements ChatService
{
    @Autowired
    @Qualifier("ollamaChatClient")
    private ChatClient ollamaChatClient;

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    ChatMemory chatMemory;

    private Map<String,List<String>> chatHistory;

    // ===================================================================

    @Override
    public ResponseEntity chat_for_string(String paramString)
    {
        log.info("chat_for_string");
        // 1、获取前端参数
        String prompt = Request_Utils.getParam(paramString, "prompt");

        // 2、普通模式
        String resultContent = ollamaChatClient
                .prompt()
                .user(prompt)
                .call()
                .content();

        //  3、封装返回体
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("resultContent", resultContent);
        resultMap.put("prompt", prompt);
        return ResponseEntity.success(resultMap);
    }
    @Override
    public Flux<String> chat_for_stream(String prompt,String chatId)
    {

        // 1、获取前端参数
//        String prompt = Request_Utils.getParam(paramStringparamString, "prompt");
//        String chatId = Request_Utils.getParam(paramStringparamString, "chatId");

        log.info("chat_for_stream with prompt: {}", prompt);
        // 使用OpenAI进行流式响应
        Flux<String> resultContent = ollamaChatClient
                .prompt()
                .user(prompt)
                .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .stream()
                .content();

        // 持久化对话信息
        List<Message> messages = chatMemory.get(chatId, Integer.MAX_VALUE);

        List<LlmChatHistory> llmChatHistorys = new ArrayList<>(); // 存放全部的聊天记录

        // 查询之前的聊天记录
//        List<LlmChatHistory> lastChatHistorys = chatMapper.getChatHistoryById(chatId);
//        if (!lastChatHistorys.isEmpty())
//        {
//            llmChatHistorys.addAll(lastChatHistorys);
//        }

        // 增加新的聊天记录
        for (Message message : messages)
        {
            llmChatHistorys.add(new LlmChatHistory(message,chatId));
        }

        if (!llmChatHistorys.isEmpty())
        {
            // 先把所有的历史记录删除 再插入新的记录
            chatMapper.deleteChatHistory(chatId);
            chatMapper.insertChatHistory(llmChatHistorys);
        }

        // 返回响应
        return resultContent;
    }

    @Override
    public ResponseEntity getAllChatHistoryList()
    {
        // 1、获取前端参数

        // 2、操作
        List<LlmChatHistoryList> allChatHistoryList = chatMapper.getAllChatHistoryList();
        log.info("getAllChatHistoryList:"+allChatHistoryList.get(0).toString());

        // 3、封装响应体
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("allChatHistoryList", allChatHistoryList.toString());
        return ResponseEntity.success(resultMap,"获取全部会话历史成功");
    }

    @Override
    public ResponseEntity insertChatHistoryList(String paramString)
    {
        // 1、获取前端参数
        String chatId       = Request_Utils.getParam(paramString, "chatId");
        String chatTittle   = Request_Utils.getParam(paramString, "chatTittle");
        String chatTag      = Request_Utils.getParam(paramString, "chatTag");
        String chatType     = Request_Utils.getParam(paramString, "chatType");
        String createTime   = Request_Utils.getParam(paramString, "createTime");
        String updateTime   = Request_Utils.getParam(paramString, "updateTime");

        // 2、插入操作
        LlmChatHistoryList llmChatHistoryList = new LlmChatHistoryList();
        llmChatHistoryList.setChatId(chatId);
        llmChatHistoryList.setChatTittle(chatTittle);
        llmChatHistoryList.setChatTag(chatTag);
        llmChatHistoryList.setCreateTime(createTime);
        llmChatHistoryList.setUpdateTime(updateTime);

        chatMapper.insertChatHistoryList(llmChatHistoryList);

        // 3、封装响应体
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("chatId", chatId);
        resultMap.put("chatTittle", chatTittle);
        return ResponseEntity.success(resultMap,"插入新的会话历史成功");
    }

    @Override
    public ResponseEntity deleteChatHistoryList(String paramString)
    {
        // 1、获取前端参数
        String chatId = Request_Utils.getParam(paramString, "chatId");

        chatMapper.deleteChatHistoryList(chatId);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("chatId", chatId);
        return ResponseEntity.success(resultMap,"删除历史会话成功");
    }

    @Override
    public ResponseEntity updateChatHistoryList(String paramString)
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
        return ResponseEntity.success(resultMap,"更新历史会话成功");
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
        //        return messages.stream().map(LlmChatHistory::new).toList();
        return messages;
    }

    /**
     * 初始化
     */
    @PostConstruct
    private void init()
    {

    }
}
