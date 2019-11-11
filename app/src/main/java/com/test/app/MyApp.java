package com.test.app;

import android.app.Application;

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
        //init log manager.
        LogMgr.i().init(this);
        LogMgr.i().start();
    }
}
