package org.xiaoxingbomei.entity.auth;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaoxingbomei.Enum.UserStatus;
import org.xiaoxingbomei.common.utils.IdGenerate_Utils;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户实体类
 */
@Entity
@Table(name = "sys_user")
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUser {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "avatar", length = 200)
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    // 用户角色关联 (多对多)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sys_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<SysRole> roles;

    @PrePersist
    protected void onCreate()
    {
        if (userId == null) {
            userId =  new IdGenerate_Utils().nextId();
        }
        LocalDateTime now = LocalDateTime.now();
        createTime = now;
        updateTime = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}