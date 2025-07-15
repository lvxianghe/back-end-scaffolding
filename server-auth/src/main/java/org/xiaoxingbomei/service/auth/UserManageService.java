package org.xiaoxingbomei.service.auth;

import org.xiaoxingbomei.dto.auth.CreateUserRequest;
import org.xiaoxingbomei.entity.auth.SysUser;
import org.xiaoxingbomei.Enum.UserStatus;

import java.util.List;

/**
 * 用户管理服务接口
 * 
 * @author xiaoxingbomei
 * @date 2024-01-01
 */
public interface UserManageService {

    /**
     * 创建用户
     */
    SysUser createUser(CreateUserRequest request);

    /**
     * 批量创建用户
     */
    List<SysUser> batchCreateUsers(List<CreateUserRequest> requests);

    /**
     * 重置用户密码
     */
    boolean resetUserPassword(Long userId, String newPassword);

    /**
     * 启用/禁用用户
     */
    boolean toggleUserStatus(Long userId, UserStatus status);

    /**
     * 为用户分配角色
     */
    boolean assignRolesToUser(Long userId, List<String> roleIds);

    /**
     * 移除用户角色
     */
    boolean removeRolesFromUser(Long userId, List<String> roleIds);
} 