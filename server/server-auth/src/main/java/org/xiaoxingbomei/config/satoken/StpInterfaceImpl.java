package org.xiaoxingbomei.config.satoken;

import cn.dev33.satoken.stp.StpInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xiaoxingbomei.service.auth.UserService;

import java.util.List;

/**
 * Sa-Token权限验证接口实现
 * 
 * @author xiaoxingbomei
 * @date 2024-01-01
 */
@Component
@Slf4j
public class StpInterfaceImpl implements StpInterface
{

    @Autowired
    private UserService userService;

    // ============================================================================================================

    /**
     * 返回指定账号id所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType)
    {
        log.debug("获取用户权限: userId={}, loginType={}", loginId, loginType);

        try
        {
            Long userId = Long.valueOf(loginId.toString());
            List<String> permissions = userService.getUserPermissions(userId);
            log.debug("用户 {} 的权限列表: {}", userId, permissions);
            return permissions;
        } catch (Exception e)
        {
            log.error("获取用户权限失败: userId={}", loginId, e);
            return List.of();
        }
    }

    /**
     * 返回指定账号id所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType)
    {
        log.debug("获取用户角色: userId={}, loginType={}", loginId, loginType);

        try
        {
            Long userId = Long.valueOf(loginId.toString());
            List<String> roles = userService.getUserRoles(userId);
            log.debug("用户 {} 的角色列表: {}", userId, roles);
            return roles;
        } catch (Exception e)
        {
            log.error("获取用户角色失败: userId={}", loginId, e);
            return List.of();
        }
    }



}