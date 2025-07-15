package org.xiaoxingbomei.Enum;

/**
 * 权限状态枚举
 */
public enum PermissionStatus {
    ACTIVE("正常"),
    INACTIVE("禁用");

    private final String description;

    PermissionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}