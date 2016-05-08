package com.king.applock.proxy;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 * @创建者     Administrator
 * @创建时间   2016/2/27 19:28
 * @描述	      线程的代理
 *
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public class ThreadPoolProxy {
    ThreadPoolExecutor mExecutor;
    private int mCorePoolSize;//核心池大小
    private int mMaximumPoolSize;//最大线程池数

    /**
     * 外界通过传入不同 核心池大小,最大线程池数就能生成不同的线程池执行器
     *
     * @param maximumPoolSize
     * @param corePoolSize
     */
    public ThreadPoolProxy(int maximumPoolSize, int corePoolSize) {
        mMaximumPoolSize = maximumPoolSize;
        mCorePoolSize = corePoolSize;
    }

    /**
     * 初始化ThreadPoolExecutor
     */
    private void initThreadPoolExecutor() {
        //双重检查加锁-->只有在第一次实例化的时候才启用同步机制,提高了性能
        if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {

            synchronized (ThreadPoolProxy.class) {

                if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
                    //初始化
                    long keepAliveTime = 5000;//保持时间
                    TimeUnit unit = TimeUnit.MILLISECONDS;//单位
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();//任务队列
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();//线程池工厂
                    mExecutor = new ThreadPoolExecutor(mCorePoolSize, mMaximumPoolSize, keepAliveTime, unit, workQueue,
                            threadFactory);
                }
            }

        }
    }

    /*
    执行任务和提交任务的区别?
     1.是否有返回值
        execute-->没有返回值
        submit-->有返回值
     2.submit返回回来的Future,干嘛的,有啥用?
           Future-->接收任务执行完成之后的结果
            1.可以使用其中的get方法接收结果
            2.其中get方法还是一个阻塞方法
            3.得到任务执行过程中抛出的异常
     */

    /**
     * 执行任务
     */
    public void execute(Runnable task) {
        initThreadPoolExecutor();
        mExecutor.execute(task);
    }


    /**
     * 提交任务
     */
    public Future<?> submit(Runnable task) {
        initThreadPoolExecutor();
        Future<?> submit = mExecutor.submit(task);
        return submit;
    }

    /**
     * 移除任务
     */
    public void remove(Runnable task) {
        initThreadPoolExecutor();
        mExecutor.remove(task);
    }
}
