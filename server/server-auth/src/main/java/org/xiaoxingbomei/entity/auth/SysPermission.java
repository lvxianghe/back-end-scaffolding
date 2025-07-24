package org.xiaoxingbomei.entity.auth;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaoxingbomei.Enum.PermissionType;
import org.xiaoxingbomei.Enum.PermissionStatus;

import java.util.Set;

/**
 * 权限实体类
 */
@Entity
@Table(name = "sys_permission")
@Data
@EqualsAndHashCode(callSuper = false)
public class SysPermission {

    @Id
    @Column(name = "permission_id", length = 100)
    private String permissionId;

    @Column(name = "parent_id", length = 100)
    private String parentId = "0";

    @Column(name = "permission_name", nullable = false, length = 50)
    private String permissionName;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_type", length = 20)
    private PermissionType permissionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private PermissionStatus status = PermissionStatus.ACTIVE;

    // 角色权限关联 (多对多) - 反向关联
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<SysRole> roles;
}