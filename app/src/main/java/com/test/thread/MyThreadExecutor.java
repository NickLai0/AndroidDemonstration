package com.test.thread;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/1/7<br>
 * Time: 11:41<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class MyThreadExecutor implements Executor {

    private Handler mHandler;

    public MyThreadExecutor(Looper looper) {
        mHandler = new Handler(looper);
    }

    @Override
    public void execute(@NonNull Runnable command) {
        mHandler.post(command);
    }

}
