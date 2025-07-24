package org.xiaoxingbomei.Enum;

/**
 * 权限类型枚举
 */
public enum PermissionType
{
    MENU("菜单"),
    BUTTON("按钮"),
    API("接口");

    private final String description;

    PermissionType(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
}