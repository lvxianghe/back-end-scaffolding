package org.xiaoxingbomei.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.xiaoxingbomei.constant.ApiConstant;
import org.xiaoxingbomei.entity.response.ResponseEntity;
import org.xiaoxingbomei.service.ChatService;
import org.xiaoxingbomei.vo.LlmChatHistory;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class AiController
{
    @Autowired
    private ChatService chatService;

    // =========================================================

    @RequestMapping(value = ApiConstant.Chat.chat_for_string, method = RequestMethod.POST)
    public ResponseEntity chat_for_string(@RequestBody String paramString)
    {
        ResponseEntity ret = null;

        ret = chatService.chat_for_string(paramString);

        return ret;
    }
    @RequestMapping(value = ApiConstant.Chat.chat_for_stream, method = RequestMethod.GET,produces ="text/html;charset=utf-8")
    public Flux<String> chat_for_stream(
            @RequestParam(value = "prompt") String prompt,
            @RequestParam(value = "chatId") String chatId
    )
    {
        Flux<String> ret = null;

        ret = chatService.chat_for_stream(prompt,chatId);

        return ret;
    }

    @RequestMapping(value = ApiConstant.Chat.getAllChatHistoryList, method = RequestMethod.GET)
    public ResponseEntity getAllChatHistoryList()
    {
        ResponseEntity ret = null;

        ret = chatService.getAllChatHistoryList();

        return ret;
    }

    @RequestMapping(value = ApiConstant.Chat.insertChatHistoryList, method = RequestMethod.POST)
    public ResponseEntity insertChatHistoryList(@RequestBody String paramString)
    {
        ResponseEntity ret = null;

        ret = chatService.insertChatHistoryList(paramString);

        return ret;
    }

    @RequestMapping(value = ApiConstant.Chat.deleteChatHistoryList, method = RequestMethod.POST)
    public ResponseEntity deleteChatHistoryList(@RequestBody String paramString)
    {
        ResponseEntity ret = null;

        ret = chatService.deleteChatHistoryList(paramString);

        return ret;
    }

    @RequestMapping(value = ApiConstant.Chat.updateChatHistoryList, method = RequestMethod.POST)
    public ResponseEntity updateChatHistoryList(@RequestBody String paramString)
    {
        ResponseEntity ret = null;

        ret = chatService.updateChatHistoryList(paramString);

        return ret;
    }

    @RequestMapping(value = ApiConstant.Chat.getChatHistoryById, method = RequestMethod.GET)
    public List<LlmChatHistory> getChatHistoryById(@RequestParam(value = "chatId") String chatId)
    {
        return chatService.getChatHistoryById(chatId);

    }


}
