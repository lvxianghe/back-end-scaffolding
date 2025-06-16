package org.xiaoxingbomei.service;


import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatService
{

    GlobalResponse chat_for_string(String paramString);
    Flux<String>   chat_for_stream(String prompt,String chatId);

}
