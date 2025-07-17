package org.xiaoxingbomei.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import org.xiaoxingbomei.config.thread.ThreadPoolConfig;
import org.xiaoxingbomei.config.thread.ThreadPoolManager;
import org.xiaoxingbomei.constant.ApiConstant;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 暴露状态接口和动态修改接口
 */
@RestController
public class ThreadPoolController
{

    @Autowired
    private ThreadPoolManager threadPoolManager;

    @Autowired
    private ThreadPoolConfig threadPoolConfig;

    // 查看线程池状态
    @PostMapping(ApiConstant.ThreadPool.getStatus)
    public GlobalResponse<Map<String, Object>> getStatus()
    {
        try {
            Map<String, Object> result = new LinkedHashMap<>();
            for (Map.Entry<String, ThreadPoolTaskExecutor> entry : threadPoolManager.getAllExecutors().entrySet()) {
                ThreadPoolExecutor pool = entry.getValue().getThreadPoolExecutor();
                Map<String, Object> stat = new HashMap<>();
                stat.put("corePoolSize", pool.getCorePoolSize());
                stat.put("maximumPoolSize", pool.getMaximumPoolSize());
                stat.put("activeCount", pool.getActiveCount());
                stat.put("poolSize", pool.getPoolSize());
                stat.put("queueSize", pool.getQueue().size());
                stat.put("largestPoolSize", pool.getLargestPoolSize());
                stat.put("completedTaskCount", pool.getCompletedTaskCount());
                result.put(entry.getKey(), stat);
            }
            return GlobalResponse.success(result, "获取线程池状态成功");
        } catch (Exception e) {
            return GlobalResponse.error("获取线程池状态失败: " + e.getMessage());
        }
    }

    // 动态修改线程池参数（重建方式）
    @PostMapping(ApiConstant.ThreadPool.updatePool)
    public GlobalResponse<String> updateThreadPool(@RequestBody Map<String, Object> request)
    {
        try {
            String name = (String) request.get("name");
            Integer coreSize = (Integer) request.get("coreSize");
            Integer maxSize = (Integer) request.get("maxSize");
            Integer queueCapacity = (Integer) request.get("queueCapacity");

            // 参数校验
            if (name == null || name.trim().isEmpty()) {
                return GlobalResponse.error("线程池名称不能为空");
            }
            if (coreSize == null || coreSize <= 0) {
                return GlobalResponse.error("核心线程数必须大于0");
            }
            if (maxSize == null || maxSize <= 0) {
                return GlobalResponse.error("最大线程数必须大于0");
            }
            if (queueCapacity == null || queueCapacity < 0) {
                return GlobalResponse.error("队列容量不能小于0");
            }
            if (coreSize > maxSize) {
                return GlobalResponse.error("核心线程数不能大于最大线程数");
            }

            ThreadPoolTaskExecutor old = threadPoolManager.getExecutor(name);
            if (old == null) {
                return GlobalResponse.error("线程池不存在: " + name);
            }

            // 重建新线程池
            ThreadPoolTaskExecutor newExecutor = new ThreadPoolTaskExecutor();
            newExecutor.setCorePoolSize(coreSize);
            newExecutor.setMaxPoolSize(maxSize);
            newExecutor.setQueueCapacity(queueCapacity);
            newExecutor.setThreadNamePrefix(name);
            newExecutor.setRejectedExecutionHandler(new ThreadPoolConfig.RejectedHandler(name));
            newExecutor.initialize();

            threadPoolManager.replace(name, newExecutor);
            return GlobalResponse.success("线程池 " + name + " 更新成功");
        } catch (Exception e) {
            return GlobalResponse.error("更新线程池失败: " + e.getMessage());
        }
    }

}
