package org.xiaoxingbomei.security;

import org.apache.commons.lang3.ObjectUtils;

/**
 * 安全用户信息辅助类
 *
 * @author wl
 * @since 2022-03-03 21:22
 */
public final class SecurityUserHelper {

    /**
     * 私有化构造方法，确保不能实例化
     */
    private SecurityUserHelper() {
        throw new AssertionError("");
    }

    /**
     * 线程安全的用户
     */
    private static final ThreadLocal<SecurityUser> securityUserHolder = new ThreadLocal<>();

    /**
     * 设置线程安全的用户信息
     *
     * @param securityUser 用户信息
     */
    public static void set(SecurityUser securityUser) {
        if (ObjectUtils.isEmpty(securityUser)) {
            throw new IllegalArgumentException("HeaderSecurityUser is null");
        }
        securityUserHolder.set(securityUser);
    }

    /**
     * 获取线程安全的用户信息
     *
     * @return 安全用户
     */
    public static SecurityUser get() {
        return securityUserHolder.get();
    }

    /**
     * 移除线程安全的用户信息
     */
    public static void remove() {
        securityUserHolder.remove();
    }
}