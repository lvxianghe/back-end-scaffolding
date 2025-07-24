package org.xiaoxingbomei.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xiaoxingbomei.entity.auth.SysRole;
import org.xiaoxingbomei.Enum.RoleStatus;

import java.util.List;
import java.util.Optional;

/**
 * RBAC-角色数据访问层
 */
@Repository
public interface RoleRepository extends JpaRepository<SysRole, String> {

    /**
     * 根据角色名查找角色
     */
    Optional<SysRole> findByRoleName(String roleName);

    /**
     * 根据状态查找角色
     */
    List<SysRole> findByStatus(RoleStatus status);

    /**
     * 检查角色ID是否存在
     */
    boolean existsByRoleId(String roleId);

    /**
     * 检查角色名是否存在
     */
    boolean existsByRoleName(String roleName);

    /**
     * 获取角色的所有权限
     */
    @Query("SELECT r FROM SysRole r LEFT JOIN FETCH r.permissions WHERE r.roleId = :roleId")
    Optional<SysRole> findByIdWithPermissions(@Param("roleId") String roleId);

    /**
     * 获取角色的所有用户
     */
    @Query("SELECT r FROM SysRole r LEFT JOIN FETCH r.users WHERE r.roleId = :roleId")
    Optional<SysRole> findByIdWithUsers(@Param("roleId") String roleId);
}