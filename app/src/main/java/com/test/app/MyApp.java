package com.test.app;

import android.app.Application;
import android.util.Log;

import com.test.util.ExceptionUtil;

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

    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(mMyUncaughtExceptionHandler);
    }

    private Thread.UncaughtExceptionHandler mMyUncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            Log.e(TAG, ExceptionUtil.getStackTrace(e));
            mDefaultUncaughtExceptionHandler.uncaughtException(t, e);
        }

    };

}
