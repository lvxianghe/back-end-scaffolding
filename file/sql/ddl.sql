-- RBAC 用户表
CREATE TABLE sys_user (
                          user_id BIGINT PRIMARY KEY COMMENT '用户ID(雪花算法)',
                          username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
                          password VARCHAR(100) NOT NULL COMMENT '密码(加密)',
                          nickname VARCHAR(50) COMMENT '昵称',
                          email VARCHAR(100) COMMENT '邮箱',
                          phone VARCHAR(20) COMMENT '手机号',
                          avatar VARCHAR(200) COMMENT '头像URL',
                          status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE:正常 INACTIVE:禁用 LOCKED:锁定)',
                          create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '权限-RBAC-用户表';

-- RBAC 角色表
CREATE TABLE sys_role (
                          role_id VARCHAR(50) PRIMARY KEY COMMENT '角色标识(admin,user等)',
                          role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
                          description VARCHAR(200) COMMENT '角色描述',
                          status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE:正常 INACTIVE:禁用)'
) COMMENT '权限-RBAC-角色表';

-- RBAC 权限表
CREATE TABLE sys_permission (
                                permission_id VARCHAR(100) PRIMARY KEY COMMENT '权限标识',
                                parent_id VARCHAR(100) DEFAULT '0' COMMENT '父权限ID',
                                permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
                                permission_type VARCHAR(20) COMMENT '权限类型(MENU:菜单 BUTTON:按钮 API:接口)',
                                status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE:正常 INACTIVE:禁用)'
) COMMENT '权限-RBAC-权限表';

-- RBAC 用户角色关联表
CREATE TABLE sys_user_role (
                               user_id BIGINT NOT NULL COMMENT '用户ID',
                               role_id VARCHAR(50) NOT NULL COMMENT '角色ID',
                               PRIMARY KEY (user_id, role_id)
) COMMENT '权限-RBAC-用户角色关联表';

-- RBAC 角色权限关联表
CREATE TABLE sys_role_permission (
                                     role_id VARCHAR(50) NOT NULL COMMENT '角色ID',
                                     permission_id VARCHAR(100) NOT NULL COMMENT '权限ID',
                                     PRIMARY KEY (role_id, permission_id)
) COMMENT '权限-RBAC-角色权限关联表';