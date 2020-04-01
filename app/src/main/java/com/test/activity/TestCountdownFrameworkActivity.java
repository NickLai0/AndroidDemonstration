package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import com.cd.CountdownHelper;
import com.cd.CountdownInfo;
import com.cd.OnCountdownListener;
import com.test.R;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/4/1<br>
 * Time: 10:51<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestCountdownFrameworkActivity extends BaseActivity {

    private TextView mTvCountdown;
    private CountdownHelper mCountdownHelper;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestCountdownFrameworkActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_framework_countdown_test;
    }

    @Override
    protected void initView() {
        mTvCountdown = findViewById(R.id.afct_tv_countdown);
    }

    @Override
    protected void initData() {
        startCountdown();
    }

    private void startCountdown() {
        if (mCountdownHelper == null) {
            mCountdownHelper = new CountdownHelper();
            mCountdownHelper.init();
            int countdownSeconds = 2000;
            if (countdownSeconds == 0) {
                countdownSeconds = 30;
            }
            mCountdownHelper.add(
                    System.currentTimeMillis()
                    , System.currentTimeMillis() + (countdownSeconds * 1000)
                    , 1000
                    , null
                    , mOnCountdownListener
            );
            refreshCountdown(countdownSeconds);
        }
    }

    private void refreshCountdown(int countdownSeconds) {
        mTvCountdown.setText(String.format("剩余%d秒", countdownSeconds));
    }

    private OnCountdownListener mOnCountdownListener = new OnCountdownListener() {
        @Override
        public void onCountdownUpdate(CountdownInfo ci) {
            int leftSeconds = (int) ((ci.getDuration() - ci.getSpentMillis()) / 1000);
            refreshCountdown(leftSeconds);
        }
    };

    @Override
    protected void initListener() {

    }

    @Override
    protected void onFinish() {
        if(mOnCountdownListener!=null) {
            mCountdownHelper.uninit();
        }
    }

}
