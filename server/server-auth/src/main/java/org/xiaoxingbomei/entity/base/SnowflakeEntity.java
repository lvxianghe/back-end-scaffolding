package org.xiaoxingbomei.entity.base;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 雪花算法主键基类
 * 适用于分布式环境，主键使用雪花算法生成
 *
 * @author xiaoxingbomei
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class SnowflakeEntity extends FlexibleBaseEntity
{

    /**
     * 主键ID - 使用雪花算法
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;


}