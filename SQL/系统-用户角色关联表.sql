create table sys_user_role
(
    login_id   varchar(36)  not null comment '用户id',
    role_key  varchar(36)  not null comment '角色id',
    primary key (login_id,role_key)
)
    comment '系统-用户角色关联表';