package org.xiaoxingbomei.service.auth;

import org.xiaoxingbomei.entity.auth.SysUser;
import org.xiaoxingbomei.Enum.UserStatus;

import java.util.List;
import java.util.Optional;

/**
 * RBAC-用户服务接口
 */
public interface UserService
{

    /**
     * 创建用户
     */
    SysUser createUser(SysUser user);

    /**
     * 更新用户
     */
    SysUser updateUser(SysUser user);

    /**
     * 删除用户
     */
    void deleteUser(Long userId);

    /**
     * 根据ID查找用户
     */
    Optional<SysUser> findById(Long userId);

    /**
     * 根据用户名查找用户
     */
    Optional<SysUser> findByUsername(String username);

    /**
     * 获取所有用户
     */
    List<SysUser> findAllUsers();

    /**
     * 根据状态获取用户
     */
    List<SysUser> findUsersByStatus(UserStatus status);

    /**
     * 修改用户密码
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 重置用户密码
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * 验证用户密码
     */
    boolean validatePassword(String username, String password);

    /**
     * 更新用户状态
     */
    boolean updateUserStatus(Long userId, UserStatus status);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 获取用户的所有角色
     */
    List<String> getUserRoles(Long userId);

    /**
     * 获取用户的所有权限
     */
    List<String> getUserPermissions(Long userId);

}