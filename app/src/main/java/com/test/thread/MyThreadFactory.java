package com.test.thread;

import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/1/7<br>
 * Time: 11:38<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class MyThreadFactory implements ThreadFactory {

    private static final String NAME = "my-thread-";
    private final AtomicInteger atomicInteger = new AtomicInteger(1);

    @Override
    public Thread newThread(@NonNull Runnable runnable) {
        return new Thread(runnable, NAME + atomicInteger.getAndIncrement());
    }
}