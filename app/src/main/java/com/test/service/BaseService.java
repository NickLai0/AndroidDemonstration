package com.test.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.test.BuildConfig;
import com.test.util.L;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/3/16<br>
 * Time: 15:56<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
class BaseService extends Service {

    private final String TAG = getClass().getSimpleName();

    protected boolean mLifecycleLogging = BuildConfig.DEBUG;

    @Override
    public void onCreate() {
        super.onCreate();
        lifeCycleLog("onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        lifeCycleLog("onBind");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        lifeCycleLog("onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        lifeCycleLog("onStart");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        lifeCycleLog("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        lifeCycleLog("onDestroy");
        super.onDestroy();
    }

    public void lifeCycleLog(String msg) {
        if (mLifecycleLogging) {
            L.i(TAG /*+ hashCode()*/, msg);
        }
    }

}
