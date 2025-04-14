package org.xiaoxingbomei.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.stereotype.Service;
import java.util.concurrent.*;

/**
 * java 并发编程学习
 */
@Service
@Slf4j
public class Java_Multi
{

    /**
     * java thread 的本质是os的内核线程，jvm对其进行封装，jvm 对thread的管理方式依赖于底层os
     */


    /**
     * 1.1 线程创建方式-Thread
     * 原理：Thread本质是对Runnable的封装，run方法默认调用Runnable任务，start方法最终调用jvm的start0本地方法，
     *      由jvm通过os的各自的方式创建新的操作系统线程
     * 适用：少量线程，不需要线程池管理的情况
     */
    class Mythread extends Thread
    {
        @Override
        public void run()
        {
            log.info("通过Thread方式创建线程 start:{}", Thread.currentThread().getName());
        }
    }
    @Test
    public void createThreadByThread() throws InterruptedException
    {
        Mythread mythread = new Mythread();
        mythread.start();
        mythread.join();  // 主线程等待子线程结束
    }

    /**
     * 1.1 线程创建-实现Runnable接口
     * 原理：Runnable是函数式接口，只有run方法，Thread的构造方法可以接Runnable对象，并在run方法中调用Runnable的run
     * 适用：避免了单继承限制，可以集成其他类；也适合多个线程共享同一任务
     */
    class MyRunnable implements Runnable
    {
        @Override
        public void run()
        {
            log.info("通过Runnable方式创建线程 start:{}", Thread.currentThread().getName());
        }
    }
    @Test
    public void createThreadByRunnable() throws InterruptedException {
        Thread thread = new Thread(new MyRunnable());
        thread.start();
        thread.join();
    }

    /**
     * 1.1 线程创建-实现 Callable 接口+Future
     * 原理：Callable支持返回值，是Runnable的增强版，FutureTask适配Callable,管理线程返回结果，线程池submit任务会封装为FutureTask执行
     * 适用：并行计算，大规模数据处理（其实没人这么用）
     */
    class MyCallable implements Callable<String>
    {
        @Override
        public String call() throws Exception
        {
            return "通过Callable创建线程："+Thread.currentThread().getName();
        }
    }
    @Test
    public void createThreadByCallable() throws InterruptedException, ExecutionException
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new MyCallable());
        log.info("执行结果：{}",future.get());
        executor.shutdown();
    }

    /**
     * 1.1 线程创建-通过线程池
     * 原理：Executors创建的线程池，底层是ThreadPoolExecutor，worker+queue
     * 适用：最佳实践
     */
    @Test
    public void createThreadByThreadpool() throws InterruptedException
    {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        executor.execute(()-> log.info("通过线程池创建线程执行{}",Thread.currentThread().getName()));
        executor.shutdown();
        Thread.sleep(100);
    }



}
