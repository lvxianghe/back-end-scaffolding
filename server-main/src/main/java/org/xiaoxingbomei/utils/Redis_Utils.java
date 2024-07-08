package org.xiaoxingbomei.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xiaoxingbomei.job.task.CacheTask;

/**
 * redis工具类
 * 1、检查redis连接状态
 */
@Component
public class Redis_Utils
{
    @Autowired
    private CacheTask cacheTask;

    // 1、检查redis连接状态
    public boolean isRedisConnected()
    {
        return cacheTask.isRedisConnected();
    }

    //

}
