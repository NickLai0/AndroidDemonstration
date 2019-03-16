package com.test.service;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.test.activity.TopActivityMonitorActivity;
import com.test.util.AppUtils;
import com.test.util.ToastUtils;

import java.util.List;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/3/16<br>
 * Time: 15:56<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TopActivityMonitorService extends BaseService {

    private boolean mIsDestroy;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (AppUtils.isActivityOnTop(getApplicationContext(), TopActivityMonitorActivity.class.getName())) {
                ToastUtils.showShort(getApplicationContext(), "TopActivityMonitorActivity on top.");
            } else {
                ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
                if (runningTasks != null && runningTasks.size() > 0 && runningTasks.get(0) != null) {
                    ToastUtils.showShort(getApplicationContext(), runningTasks.get(0).topActivity + " on top");
                } else {
                    ToastUtils.showShort(getApplicationContext(), "Get running tasks failed.");
                }
            }
            if (!mIsDestroy) {
                sendEmptyMessageDelayed(0, 500);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public void onDestroy() {
        mIsDestroy = true;
        super.onDestroy();
    }
}
