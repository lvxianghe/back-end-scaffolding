package org.xiaoxingbomei.entity.vo;

import lombok.Data;

/**
 * 大模型信息
 */
@Data
public class LlmModel
{
    private String modelProvider;     // 模型提供者 如openai/ollama
    private String modelName;         // 模型名称 如 qwen3、deepseek
    private String modelDescription;  // 模型描述
    private String modelTag;          // 模型标签(tagA,tagB,tagC)
    
    private String embeddingModelName; // 嵌入模型名称，如 text-embedding-v3
    private Integer embeddingDimensions; // 嵌入模型维度，如 1024
}
