package org.xiaoxingbomei.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xiaoxingbomei.entity.auth.SysPermission;
import org.xiaoxingbomei.Enum.PermissionType;
import org.xiaoxingbomei.Enum.PermissionStatus;

import java.util.List;

/**
 * RBAC-权限数据访问层
 */
@Repository
public interface PermissionRepository extends JpaRepository<SysPermission, String>
{

    /**
     * 根据权限类型查找权限
     */
    List<SysPermission> findByPermissionType(PermissionType permissionType);

    /**
     * 根据父权限ID查找子权限
     */
    List<SysPermission> findByParentId(String parentId);

    /**
     * 根据状态查找权限
     */
    List<SysPermission> findByStatus(PermissionStatus status);

    /**
     * 检查权限ID是否存在
     */
    boolean existsByPermissionId(String permissionId);

    /**
     * 获取权限树结构
     */
    @Query("SELECT p FROM SysPermission p WHERE p.status = 'ACTIVE' ORDER BY p.permissionId")
    List<SysPermission> findPermissionTree();

    /**
     * 获取指定权限的所有子权限
     */
    @Query("SELECT p FROM SysPermission p WHERE p.parentId = :parentId AND p.status = 'ACTIVE'")
    List<SysPermission> findChildPermissions(@Param("parentId") String parentId);
}