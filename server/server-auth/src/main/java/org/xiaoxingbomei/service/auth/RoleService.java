package org.xiaoxingbomei.service.auth;

import org.xiaoxingbomei.entity.auth.SysRole;
import org.xiaoxingbomei.Enum.RoleStatus;

import java.util.List;
import java.util.Optional;

/**
 * RBAC-角色服务接口
 */
public interface RoleService
{

    /**
     * 创建角色
     */
    SysRole createRole(SysRole role);

    /**
     * 更新角色
     */
    SysRole updateRole(SysRole role);

    /**
     * 删除角色
     */
    void deleteRole(String roleId);

    /**
     * 根据ID查找角色
     */
    Optional<SysRole> findById(String roleId);

    /**
     * 根据角色名查找角色
     */
    Optional<SysRole> findByRoleName(String roleName);

    /**
     * 获取所有角色
     */
    List<SysRole> findAllRoles();

    /**
     * 根据状态获取角色
     */
    List<SysRole> findRolesByStatus(RoleStatus status);

    /**
     * 更新角色状态
     */
    boolean updateRoleStatus(String roleId, RoleStatus status);

    /**
     * 检查角色是否存在
     */
    boolean existsByRoleId(String roleId);

    /**
     * 检查角色名是否存在
     */
    boolean existsByRoleName(String roleName);

    /**
     * 为角色分配权限
     */
    boolean assignPermissions(String roleId, List<String> permissionIds);

    /**
     * 移除角色权限
     */
    boolean removePermissions(String roleId, List<String> permissionIds);

    /**
     * 获取角色的所有权限
     */
    List<String> getRolePermissions(String roleId);

}