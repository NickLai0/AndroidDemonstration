package com.test.app;

import android.app.Application;
import android.content.Context;
import android.os.HandlerThread;
import android.util.Log;

import com.test.thread.ThreadMgr;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2018/12/21<br>
 * Time: 16:33<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class MyApp extends Application {

    private static final String TAG = MyApp.class.getSimpleName();

    private static MyApp sInstance;

    public static Context i() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate -> ");
        sInstance = this;
        LogMgr.i().init(this);
        LogMgr.i().start();
        HandlerThread handlerThread = ThreadMgr.i().getUnquitableHandlerThread();
        CountdownManager.i().init(handlerThread.getLooper());
    }

}
