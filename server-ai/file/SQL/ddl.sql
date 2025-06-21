-- 重新创建会话历史列表表
CREATE TABLE llm_chat_history_list
(
    chat_id      VARCHAR(64)  PRIMARY KEY COMMENT '会话id',
    chat_tittle  VARCHAR(100) NOT NULL COMMENT '会话标题',
    chat_tag     VARCHAR(40)  NOT NULL COMMENT '会话标签',
    create_time  VARCHAR(50)  DEFAULT '今天' COMMENT '创建时间（支持中文描述）',
    update_time  VARCHAR(50)  DEFAULT '今天' COMMENT '更新时间（支持中文描述）'
) COMMENT '大模型会话历史列表';

-- 重新创建会话历史详情表（修复主键问题）
CREATE TABLE llm_chat_history
(
    id           BIGINT       AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
    chat_id      VARCHAR(64)  NOT NULL COMMENT '会话id',
    chat_role    VARCHAR(20)  NOT NULL COMMENT '会话角色（user/assistant/system）',
    chat_content TEXT         NOT NULL COMMENT '会话内容',
    create_time  VARCHAR(50)  DEFAULT '刚刚' COMMENT '创建时间（支持中文描述）',

    -- 添加索引
    INDEX idx_chat_id (chat_id),
    INDEX idx_create_time (create_time),

    -- 外键约束（可选）
    FOREIGN KEY (chat_id) REFERENCES llm_chat_history_list(chat_id) ON DELETE CASCADE
) COMMENT '大模型会话历史详情表';

create table llm_functioncalling_programmer
(
    programmer_name varchar(64) primary key comment '编程者名称',
    education       varchar(5)     not null comment '程序员的学历  0-无 1-初中 2-高中 3-大专 4-本科 5-硕士 6-博士',
    programmerType  varchar(5)     not null comment '程序员类型    0-全栈 1-前端 2-后端 3-数据 4-测试 5-运维 6-其他',
    salary          varchar(64)    not null comment '程序员薪',
    experience      varchar(64)    not null comment '程序员经验（年）'
)
    comment '大模型智能客服-程序员表';

create table llm_system_prompt
(
    prompt_id varchar(64) primary key comment '主键',
    prompt_name             varchar(40)    not null comment '主键ID',
    prompt_type             varchar(10)    not null comment '提示词名字',
    prompt_description      varchar(100)    not null comment '提示词类型',
    prompt_tag              varchar(100)    not null comment '提示词描述',
    prompt_content          varchar(2000)    not null comment '提示词标签（A,B,C）',
    status                 varchar(5)    not null comment '提示词内容'
)
    comment '大模型-系统提示词表';

CREATE TABLE llm_model_resource
(
    model_provider       VARCHAR(50)  NOT NULL COMMENT '模型提供者（ollama/openai）',
    model_name          VARCHAR(100) NOT NULL COMMENT '大模型名称（如 grok-3, deepseek-r1:14B）',
    model_description   VARCHAR(500) NOT NULL COMMENT '大模型描述',
    model_tag          VARCHAR(200) NOT NULL COMMENT '提示词标签（推理,创作,多模态）',
    embedding_model_name VARCHAR(100) NULL COMMENT '嵌入模型名称（如 text-embedding-v3）',
    embedding_dimensions INT          NULL COMMENT '嵌入模型维度（如 1024）',
    create_time         TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time         TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (model_provider, model_name)
) COMMENT '大模型-模型来源表';

-- 创建索引（可选，提高查询性能）
CREATE INDEX idx_model_provider ON llm_model_resource(model_provider);
CREATE INDEX idx_create_time ON llm_model_resource(create_time);