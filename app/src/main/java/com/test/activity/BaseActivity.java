package com.test.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.test.BuildConfig;
import com.test.handler.WeakHandler;
import com.test.util.L;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2018/11/24<br>
 * Time: 14:23<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public abstract class BaseActivity extends AppCompatActivity implements
        View.OnClickListener,
        WeakHandler.IMessageHandler {

    protected boolean mLifecycleLogging = BuildConfig.DEBUG;

    protected final String TAG = getClass().getSimpleName();

    private static boolean isTest = true;
    private static int testCount = 0;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        lifeCycleLog("onSaveInstanceState -> ");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        if (isTest) {
//            if (testCount % 2 == 0) {
//                ActivityUtils.fullScreen(this);
//            } else {
//                ActivityUtils.immersiveNavigation(this);
//            }
//            testCount++;
//        }
        super.onCreate(savedInstanceState);
        setContentView(provideLayoutId());
        lifeCycleLog("onCreate");
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifeCycleLog("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifeCycleLog("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifeCycleLog("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        lifeCycleLog("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifeCycleLog("onDestroy");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        lifeCycleLog("onNewIntent");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        lifeCycleLog("onConfigurationChanged");
    }

    @Override
    public void onBackPressed() {
        lifeCycleLog("onBackPressed");
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        lifeCycleLog("onRestart");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lifeCycleLog("onRestoreInstanceState");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        lifeCycleLog("onLowMemory");
    }

    @Override
    public void finish() {
        lifeCycleLog("finish");
        if (!isFinishing()) {
            onFinish();
            super.finish();
        }
    }

    protected void onFinish() {
        lifeCycleLog("onFinish");
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    protected abstract int provideLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    @Override
    public void onClick(View v) {
    }

    public void lifeCycleLog(String msg) {
        if (mLifecycleLogging) {
            L.i(TAG, msg);
        }
    }

    protected WeakHandler mHandler;

    public void initHandler() {
        mHandler = new WeakHandler();
        mHandler.setMessageHandler(this);
    }

    @Override
    public void handleMessage(Message msg) {
    }

}
