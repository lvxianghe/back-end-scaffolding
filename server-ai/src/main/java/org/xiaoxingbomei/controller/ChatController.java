package org.xiaoxingbomei.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import org.xiaoxingbomei.constant.ApiConstant;
import org.xiaoxingbomei.service.ChatService;
import org.xiaoxingbomei.service.LlmModelService;
import org.xiaoxingbomei.service.PromptService;
import org.xiaoxingbomei.vo.LlmChatHistory;
import org.xiaoxingbomei.vo.LlmModel;
import org.xiaoxingbomei.vo.LlmSystemPrompt;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class ChatController
{

    @Autowired
    private ChatService chatService;

    @Autowired
    private PromptService promptService;

    @Autowired
    private LlmModelService llmModelService;

    // =========================================================

    @RequestMapping(value = ApiConstant.Chat.chat,method = RequestMethod.GET,produces ="text/html;charset=utf-8")
    public Flux<String> chat
            (
                    @RequestParam(value = "prompt")   String prompt,
                    @RequestParam(value = "chatId")   String chatId,
                    @RequestParam(value = "isStream") String isStream,
                    @RequestParam(value = "modelName") String modelName,
                    @RequestParam(value = "modelProvider") String modelProvider,
                    @RequestParam(value = "systemPrompt",required = false) String systemPrompt
            )
    {

        Flux<String> ret = chatService.chat(prompt,chatId,isStream,modelProvider,modelName,systemPrompt);

        return ret;
    }

    @RequestMapping(value = ApiConstant.Chat.chat_for_string, method = RequestMethod.GET)
    public GlobalResponse chat_for_string(@RequestParam(value = "prompt") String prompt)
    {
        GlobalResponse ret = null;

        ret = chatService.chat_for_string(prompt);

        return ret;
    }

    @RequestMapping(value = ApiConstant.Chat.getAllChatHistoryList, method = RequestMethod.GET)
    public GlobalResponse getAllChatHistoryList()
    {
        GlobalResponse ret = null;

        ret = chatService.getAllChatHistoryList();

        return ret;
    }

    @RequestMapping(value = ApiConstant.Chat.insertChatHistoryList, method = RequestMethod.POST)
    public GlobalResponse insertChatHistoryList(@RequestBody String paramString)
    {
        GlobalResponse ret = null;

        ret = chatService.insertChatHistoryList(paramString);

        return ret;
    }

    @RequestMapping(value = ApiConstant.Chat.deleteChatHistoryList, method = RequestMethod.POST)
    public GlobalResponse deleteChatHistoryList(@RequestBody String paramString)
    {
        GlobalResponse ret = null;

        ret = chatService.deleteChatHistoryList(paramString);

        return ret;
    }

    @RequestMapping(value = ApiConstant.Chat.updateChatHistoryList, method = RequestMethod.POST)
    public GlobalResponse updateChatHistoryList(@RequestBody String paramString)
    {
        GlobalResponse ret = null;

        ret = chatService.updateChatHistoryList(paramString);

        return ret;
    }

    @RequestMapping(value = ApiConstant.Chat.getChatHistoryById, method = RequestMethod.GET)
    public List<LlmChatHistory> getChatHistoryById(@RequestParam(value = "chatId") String chatId)
    {
        return chatService.getChatHistoryById(chatId);

    }
    
    
    /**
     * 获取所有系统提示词
     * @return 提示词列表
     */
    @RequestMapping(value = ApiConstant.Prompt.getAllSystemPrompt, method = RequestMethod.POST)
    public GlobalResponse getAllSystemPrompt()
    {
        GlobalResponse ret = null;

        ret = promptService.getAllSystemPrompt();

        return ret;
    }

    /**
     * 根据ID获取系统提示词
     * @param promptId 提示词ID
     * @return 提示词信息
     */
    @RequestMapping(value = ApiConstant.Prompt.getSystemPromptById, method = RequestMethod.POST)
    public GlobalResponse getSystemPromptById(@RequestBody String paramString)
    {
        GlobalResponse ret = null;

        ret = promptService.getSystemPromptById(paramString);

        return ret;
    }

    /**
     * 添加系统提示词
     * @param prompt 提示词信息
     * @return 添加结果
     */
    @RequestMapping(value = ApiConstant.Prompt.addSystemPrompt, method = RequestMethod.POST)
    public GlobalResponse addSystemPrompt(@RequestBody LlmSystemPrompt prompt)
    {
        GlobalResponse ret = null;

        ret = promptService.addSystemPrompt(prompt);

        return ret;
    }

    /**
     * 删除系统提示词
     */
    @RequestMapping(value = ApiConstant.Prompt.deleteSystemPrompt, method = RequestMethod.POST)
    public GlobalResponse deleteSystemPrompt(@RequestBody String paramString)
    {
        GlobalResponse ret = null;

        ret = promptService.deleteSystemPrompt(paramString);

        return ret;
    }
    

    /**
     * 获取所有模型
     * @return 模型列表
     */
    @RequestMapping(value = ApiConstant.Model.getAllModels, method = RequestMethod.POST)
    public GlobalResponse getAllModels()
    {
        GlobalResponse ret = llmModelService.getAllModels();

        return ret;
    }


}
