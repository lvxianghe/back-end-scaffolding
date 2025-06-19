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


    /**
     * 添加模型
     * @param model 模型信息
     * @return 添加结果
     */
    @RequestMapping(value = ApiConstant.Model.addModel, method = RequestMethod.POST)
    public GlobalResponse addModel(@RequestBody LlmModel model)
    {
        GlobalResponse ret = null;

        ret = llmModelService.addModel(model);

        return ret;
    }

    /**
     * 更新模型
     * @param model 模型信息
     * @return 更新结果
     */
    @RequestMapping(value = ApiConstant.Model.updateModel, method = RequestMethod.POST)
    public GlobalResponse updateModel(@RequestBody LlmModel model)
    {
        GlobalResponse ret = null;

        ret = llmModelService.updateModel(model);

        return ret;
    }

    /**
     * 删除模型
     * @param modelProvider 模型提供者
     * @param modelName 模型名称
     * @return 删除结果
     */
    @RequestMapping(value = ApiConstant.Model.deleteModel, method = RequestMethod.POST)
    public GlobalResponse deleteModel(@RequestBody String paramString)
    {
        GlobalResponse ret = llmModelService.deleteModel(paramString);

        return ret;
    }

}
