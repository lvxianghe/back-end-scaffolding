package org.xiaoxingbomei.constant;

public class ApiConstant
{


    public class Chat
    {
        public static final String chat                  = "/ai/chat";                       // 对话
        public static final String chat_for_string       = "/chat/chat_for_string";       // 普通对话
        public static final String chat_for_stream       = "/ai/chat/chat_for_stream";       // 流式对话


    }


    public class Prompt
    {
        public static final String getAllSystemPrompt   = "/ai/prompt/getAllSystemPrompt";    // 获取所有系统提示词
        public static final String getSystemPromptById  = "/ai/prompt/getSystemPromptById";   // 根据ID获取系统提示词
        public static final String addSystemPrompt      = "/ai/prompt/addSystemPrompt";       // 添加系统提示词
        public static final String deleteSystemPrompt   = "/ai/prompt/deleteSystemPrompt";    // 删除系统提示词
    }
    
    public class Model
    {
        public static final String getAllModels       = "/ai/model/getAllModels";           // 获取所有模型
    }



}
