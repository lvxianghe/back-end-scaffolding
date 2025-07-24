package org.xiaoxingbomei.common.utils;

import cn.hutool.core.util.IdUtil;

/**
 * id 生成工具类
 */
public class IdGenerate_Utils
{

    /**
     * 生成雪花算法ID
     */
    public Long nextId()
    {
        return IdUtil.getSnowflakeNextId();
    }

    /**
     * 生成雪花算法ID字符串
     */
    public String nextIdStr()
    {
        return String.valueOf(IdUtil.getSnowflakeNextId());
    }

}
