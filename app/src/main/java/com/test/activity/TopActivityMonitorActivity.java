package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.test.R;
import com.test.service.TopActivityMonitorService;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/3/16<br>
 * Time: 14:59<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TopActivityMonitorActivity extends BaseActivity {

    private View mTvStartMonitor;
    private View mTvStopMonitor;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TopActivityMonitorActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_monitor_activity_top;
    }

    @Override
    protected void initView() {
        mTvStartMonitor = findViewById(R.id.amat_tv_start_monitor);
        mTvStopMonitor = findViewById(R.id.amat_tv_stop_monitor);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mTvStartMonitor.setOnClickListener(this);
        mTvStopMonitor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.amat_tv_start_monitor:
                startService(new Intent(this, TopActivityMonitorService.class));
                break;

            case R.id.amat_tv_stop_monitor:
                stopService(new Intent(this, TopActivityMonitorService.class));
                break;
        }
    }
}
