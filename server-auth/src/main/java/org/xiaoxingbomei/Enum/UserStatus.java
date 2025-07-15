package org.xiaoxingbomei.Enum;

/**
 * 用户状态枚举
 */
public enum UserStatus
{
    ACTIVE("正常"),
    INACTIVE("禁用"),
    LOCKED("锁定"),
    PENDING("待激活");

    private final String description;

    UserStatus(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
}