package org.xiaoxingbomei.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xiaoxingbomei.entity.auth.SysUser;
import org.xiaoxingbomei.Enum.UserStatus;

import java.util.List;
import java.util.Optional;

/**
 * RBAC-用户数据访问层
 */
@Repository
public interface UserRepository extends JpaRepository<SysUser, Long>
{

    /**
     * 根据用户名查找用户
     */
    Optional<SysUser> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    Optional<SysUser> findByEmail(String email);

    /**
     * 根据手机号查找用户
     */
    Optional<SysUser> findByPhone(String phone);

    /**
     * 根据状态查找用户
     */
    List<SysUser> findByStatus(UserStatus status);

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
    @Query("SELECT u FROM SysUser u LEFT JOIN FETCH u.roles WHERE u.userId = :userId")
    Optional<SysUser> findByIdWithRoles(@Param("userId") Long userId);

    /**
     * 获取用户的所有权限
     */
    @Query("SELECT DISTINCT p.permissionId FROM SysUser u " +
            "LEFT JOIN u.roles r " +
            "LEFT JOIN r.permissions p " +
            "WHERE u.userId = :userId AND u.status = 'ACTIVE' AND r.status = 'ACTIVE' AND p.status = 'ACTIVE'")
    List<String> findUserPermissions(@Param("userId") Long userId);

    /**
     * 获取用户的所有角色标识
     */
    @Query("SELECT DISTINCT r.roleId FROM SysUser u " +
            "LEFT JOIN u.roles r " +
            "WHERE u.userId = :userId AND u.status = 'ACTIVE' AND r.status = 'ACTIVE'")
    List<String> findUserRoles(@Param("userId") Long userId);
}