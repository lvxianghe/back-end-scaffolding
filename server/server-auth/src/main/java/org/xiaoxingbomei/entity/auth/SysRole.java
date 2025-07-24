package org.xiaoxingbomei.entity.auth;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaoxingbomei.Enum.RoleStatus;

import java.util.Set;

/**
 * 角色实体类
 */
@Entity
@Table(name = "sys_role")
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRole {

    @Id
    @Column(name = "role_id", length = 50)
    private String roleId;

    @Column(name = "role_name", nullable = false, length = 50)
    private String roleName;

    @Column(name = "description", length = 200)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private RoleStatus status = RoleStatus.ACTIVE;

    // 角色权限关联 (多对多)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sys_role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<SysPermission> permissions;

    // 用户角色关联 (多对多) - 反向关联
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<SysUser> users;
}