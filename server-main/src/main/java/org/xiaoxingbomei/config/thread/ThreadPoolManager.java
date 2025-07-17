package org.xiaoxingbomei.config.thread;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程池注册与管理中心
 */
@Component
public class ThreadPoolManager
{

    private final Map<String, ThreadPoolTaskExecutor> poolMap = new ConcurrentHashMap<>();

    public void register(String name, ThreadPoolTaskExecutor executor)
    {
        poolMap.put(name, executor);
    }

    public ThreadPoolTaskExecutor getExecutor(String name)
    {
        return poolMap.get(name);
    }

    public Set<String> getPoolNames()
    {
        return poolMap.keySet();
    }

    public Map<String, ThreadPoolTaskExecutor> getAllExecutors()
    {
        return poolMap;
    }

    /**
     * 替换线程池（用于重建）
     */
    public synchronized void replace(String name, ThreadPoolTaskExecutor newExecutor)
    {
        ThreadPoolTaskExecutor old = poolMap.get(name);
        if (old != null)
        {
            old.shutdown();  // 优雅关闭旧线程池
        }
        poolMap.put(name, newExecutor);
    }


}

