package org.xiaoxingbomei.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import org.xiaoxingbomei.constant.ApiConstant;
import org.xiaoxingbomei.service.ChatService;

@RestController
public class ChatController
{

    @Autowired
    private ChatService chatService;

    @RequestMapping(value = ApiConstant.Chat.chat_for_string, method = RequestMethod.GET)
    public GlobalResponse chat_for_string(@RequestParam(value = "prompt") String prompt)
    {
        GlobalResponse ret = null;

        ret = chatService.chat_for_string(prompt);

        return ret;
    }

}
