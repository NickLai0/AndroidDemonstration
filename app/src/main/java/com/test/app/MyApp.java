package com.test.app;

import android.app.Application;
import android.os.HandlerThread;

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

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread handlerThread = ThreadMgr.i().getUnquitableHandlerThread();
        LogMgr.i().init(this, handlerThread.getLooper());
        LogMgr.i().start();
        CountdownManager.i().init(handlerThread.getLooper());
    }

}
