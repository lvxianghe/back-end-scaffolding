package org.xiaoxingbomei.entity.base;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 灵活的基础实体类
 * 使用 exist=false 让字段在表中不存在时不参与SQL
 */
@Data
public abstract class FlexibleBaseEntity
{

    /**
     * 创建时间 - 如果表中没有此字段，会被忽略
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT, exist = false)
    private LocalDateTime createTime;

    /**
     * 更新时间 - 如果表中没有此字段，会被忽略
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE, exist = false)
    private LocalDateTime updateTime;

    /**
     * 创建人 - 如果表中没有此字段，会被忽略
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT, exist = false)
    private String createBy;

    /**
     * 更新人 - 如果表中没有此字段，会被忽略
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE, exist = false)
    private String updateBy;

    /**
     * 逻辑删除标识 - 如果表中没有此字段，会被忽略
     */
    @TableLogic
    @TableField(value = "deleted", exist = false)
    private Integer deleted;

    /**
     * 版本号（乐观锁）- 如果表中没有此字段，会被忽略
     */
    @Version
    @TableField(value = "version", exist = false)
    private Integer version;
}