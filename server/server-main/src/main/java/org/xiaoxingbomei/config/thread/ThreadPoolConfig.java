package org.xiaoxingbomei.config.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 配置线程池并注册到管理器
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig implements AsyncConfigurer, SchedulingConfigurer
{

    @Autowired
    private ThreadPoolManager threadPoolManager;

    @Bean(name = "taskExecutorA")
    public ThreadPoolTaskExecutor taskExecutorA()
    {
        return buildExecutor("taskA-", 4, 8, 100);
    }

    @Bean(name = "asyncExecutor")
    @Override
    public Executor getAsyncExecutor()
    {
        return buildExecutor("async-", 5, 10, 200);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar)
    {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(4);
        scheduler.setThreadNamePrefix("schedule-");
        scheduler.setRejectedExecutionHandler(new RejectedHandler("schedule"));
        scheduler.initialize();
        registrar.setTaskScheduler(scheduler);
    }

    private ThreadPoolTaskExecutor buildExecutor(String prefix, int core, int max, int queue)
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(core);
        executor.setMaxPoolSize(max);
        executor.setQueueCapacity(queue);
        executor.setThreadNamePrefix(prefix);
        executor.setRejectedExecutionHandler(new RejectedHandler(prefix));
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();

        threadPoolManager.register(prefix, executor);
        return executor;
    }

    public static class RejectedHandler implements RejectedExecutionHandler
    {
        private final String poolName;

        public RejectedHandler(String poolName)
        {
            this.poolName = poolName;
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
        {
            System.err.println("【线程池拒绝任务】" + poolName + " - " + r.toString());
        }
    }


}

