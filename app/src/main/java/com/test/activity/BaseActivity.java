package com.test.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.test.BuildConfig;
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
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected boolean mLifecycleLogging = BuildConfig.DEBUG;

    protected final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        super.onBackPressed();
        lifeCycleLog("onBackPressed");
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
        super.finish();
        lifeCycleLog("finish");
    }

    protected abstract int provideLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    @Override
    public void onClick(View v) {

    }


    public void lifeCycleLog(String msg) {
        if(mLifecycleLogging) {
            L.i(TAG + hashCode(), msg);
        }
    }
}
