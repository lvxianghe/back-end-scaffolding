package org.xiaoxingbomei.service.auth;

import org.xiaoxingbomei.entity.auth.SysPermission;
import org.xiaoxingbomei.Enum.PermissionType;
import org.xiaoxingbomei.Enum.PermissionStatus;

import java.util.List;
import java.util.Optional;

/**
 * RBAC-权限服务接口
 */
public interface PermissionService
{

    /**
     * 创建权限
     */
    SysPermission createPermission(SysPermission permission);

    /**
     * 更新权限
     */
    SysPermission updatePermission(SysPermission permission);

    /**
     * 删除权限
     */
    void deletePermission(String permissionId);

    /**
     * 根据ID查找权限
     */
    Optional<SysPermission> findById(String permissionId);

    /**
     * 获取所有权限
     */
    List<SysPermission> findAllPermissions();

    /**
     * 根据类型获取权限
     */
    List<SysPermission> findPermissionsByType(PermissionType type);

    /**
     * 根据父ID获取子权限
     */
    List<SysPermission> findChildPermissions(String parentId);

    /**
     * 获取权限树
     */
    List<SysPermission> getPermissionTree();

    /**
     * 更新权限状态
     */
    boolean updatePermissionStatus(String permissionId, PermissionStatus status);

    /**
     * 检查权限是否存在
     */
    boolean existsByPermissionId(String permissionId);

}