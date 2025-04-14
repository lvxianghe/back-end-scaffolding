package org.xiaoxingbomei.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xiaoxingbomei.constant.ApiConstant;
import org.xiaoxingbomei.entity.response.ResponseEntity;
import org.xiaoxingbomei.service.AiService;
import reactor.core.publisher.Flux;

@Controller
public class AiController
{
    @Autowired
    private AiService aiService;

    // =========================================================

    @RequestMapping(value = ApiConstant.Chat.chat_for_string, method = RequestMethod.POST)
    public ResponseEntity chat_for_string(@RequestBody String paramString)
    {
        ResponseEntity ret = null;

        ret = aiService.chat_for_string(paramString);

        return ret;
    }
    @RequestMapping(value = ApiConstant.Chat.chat_for_stream, method = RequestMethod.GET,produces ="text/html;charset=utf-8")
    public Flux<String> chat_for_stream(@RequestParam String prompt)
    {
        Flux<String> ret = null;

        ret = aiService.chat_for_stream(prompt);

        return ret;
    }
}
