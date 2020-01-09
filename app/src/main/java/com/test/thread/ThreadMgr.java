package com.test.thread;

import android.os.Looper;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/1/7<br>
 * Time: 10:09<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public final class ThreadMgr {

    private static final String TAG = ThreadMgr.class.getSimpleName();

    private ThreadPoolExecutor mThreadPoolExecutor;

    private Executor mMainThreadExecutor;
    private Executor mSubThreadExecutor;

    private UnquitableHandlerThread mUnquitableHandlerThread;

    private ThreadMgr() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int corePoolSize = availableProcessors / 2;
        int maximumPoolSize = corePoolSize;
        long keepAliveTime = 30 * 1000;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
        ThreadFactory threadFactory = new MyThreadFactory();
        mThreadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize
                , maximumPoolSize
                , keepAliveTime
                , unit
                , workQueue
                , threadFactory
        );

        mUnquitableHandlerThread = new UnquitableHandlerThread("UnquitableHandlerThread");
        mUnquitableHandlerThread.start();
        mSubThreadExecutor = new MyThreadExecutor(mUnquitableHandlerThread.getLooper());
        mMainThreadExecutor = new MyThreadExecutor(Looper.getMainLooper());

        Log.v(TAG, "availableProcessors=" + availableProcessors + ", corePoolSize=" + corePoolSize + ", maximumPoolSize=" + maximumPoolSize);
    }

    private static final ThreadMgr sfInstance = new ThreadMgr();

    public static ThreadMgr i() {
        return sfInstance;
    }

    /**
     * Execute a command on sub-thread.
     *
     * @param runnable command
     */
    public void execute(Runnable runnable) {
        mThreadPoolExecutor.execute(runnable);
    }

    /**
     * Execute a command on main-thread.
     *
     * @param runnable command
     */
    public void executeOnMainThread(Runnable runnable) {
        mMainThreadExecutor.execute(runnable);
    }

    /**
     * Execute a command on a single sub-thread.
     *
     * @param runnable command
     */
    public void executeOnSubThread(Runnable runnable) {
        mSubThreadExecutor.execute(runnable);
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return mThreadPoolExecutor;
    }

    public Executor getMainThreadExecutor() {
        return mMainThreadExecutor;
    }

    public Executor getSubThreadExecutor() {
        return mSubThreadExecutor;
    }

    public UnquitableHandlerThread getUnquitableHandlerThread() {
        return mUnquitableHandlerThread;
    }

}
