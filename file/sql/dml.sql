-- 权限系统初始化数据

-- 插入超级管理员用户 (密码: admin123)
INSERT IGNORE INTO sys_user (user_id, username, password, nickname, email, status, create_time, update_time) VALUES
(1, 'admin', '$2a$10$7JB720yubVSeLVsqsP8XAuY/tE6JGxr9yGQXhJT2Y1EIf2PtmCWV.', '超级管理员', 'admin@example.com', 'ACTIVE', NOW(), NOW()),
(2, 'user', '$2a$10$7JB720yubVSeLVsqsP8XAuY/tE6JGxr9yGQXhJT2Y1EIf2PtmCWV.', '测试用户', 'user@example.com', 'ACTIVE', NOW(), NOW());

-- 插入默认角色
INSERT IGNORE INTO sys_role (role_id, role_name, description, status) VALUES
('super_admin', '超级管理员', '系统超级管理员，拥有所有权限', 'ACTIVE'),
('admin', '管理员', '系统管理员，拥有管理权限', 'ACTIVE'),
('user', '普通用户', '普通用户，拥有基本权限', 'ACTIVE');

-- 插入默认权限
INSERT IGNORE INTO sys_permission (permission_id, parent_id, permission_name, permission_type, status) VALUES
('system', '0', '系统管理', 'MENU', 'ACTIVE'),
('system:user', 'system', '用户管理', 'MENU', 'ACTIVE'),
('system:role', 'system', '角色管理', 'MENU', 'ACTIVE'),
('system:permission', 'system', '权限管理', 'MENU', 'ACTIVE'),
('system:user:list', 'system:user', '用户查询', 'API', 'ACTIVE'),
('system:user:add', 'system:user', '用户新增', 'API', 'ACTIVE'),
('system:user:edit', 'system:user', '用户编辑', 'API', 'ACTIVE'),
('system:user:delete', 'system:user', '用户删除', 'API', 'ACTIVE'),
('system:role:list', 'system:role', '角色查询', 'API', 'ACTIVE'),
('system:role:add', 'system:role', '角色新增', 'API', 'ACTIVE'),
('system:role:edit', 'system:role', '角色编辑', 'API', 'ACTIVE'),
('system:role:delete', 'system:role', '角色删除', 'API', 'ACTIVE');

-- 分配用户角色
INSERT IGNORE INTO sys_user_role (user_id, role_id) VALUES
(1, 'super_admin'),
(2, 'user');

-- 分配角色权限
INSERT IGNORE INTO sys_role_permission (role_id, permission_id) VALUES
('super_admin', 'system'),
('super_admin', 'system:user'),
('super_admin', 'system:role'),
('super_admin', 'system:permission'),
('super_admin', 'system:user:list'),
('super_admin', 'system:user:add'),
('super_admin', 'system:user:edit'),
('super_admin', 'system:user:delete'),
('super_admin', 'system:role:list'),
('super_admin', 'system:role:add'),
('super_admin', 'system:role:edit'),
('super_admin', 'system:role:delete'),
('admin', 'system'),
('admin', 'system:user'),
('admin', 'system:role'),
('admin', 'system:user:list'),
('admin', 'system:user:add'),
('admin', 'system:user:edit'),
('admin', 'system:role:list'),
('user', 'system:user:list');