package org.xiaoxingbomei.Enum;

/**
 * 角色状态枚举
 */
public enum RoleStatus
{
    ACTIVE("正常"),
    INACTIVE("禁用");

    private final String description;

    RoleStatus(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
}