package org.xiaoxingbomei.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import org.xiaoxingbomei.constant.ApiConstant;
import org.xiaoxingbomei.service.ChatService;
import org.xiaoxingbomei.service.LlmModelService;
import org.xiaoxingbomei.vo.LlmModel;
import reactor.core.publisher.Flux;

@RestController
public class ChatController
{

    @Autowired
    private ChatService chatService;

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
