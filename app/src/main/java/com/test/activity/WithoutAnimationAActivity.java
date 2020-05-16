package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.test.R;
import com.test.app.LogMgr;
import com.test.event.OnWithoutAnimationBActivityOnCreateEvent;
import com.test.fragment.ProgressBarDemoFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/5/16<br>
 * Time: 19:12<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class WithoutAnimationAActivity extends BaseActivity {

    private FrameLayout mFlRootContainer;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, WithoutAnimationAActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_a_animation_without;
    }

    @Override
    protected void initView() {
        mFlRootContainer = findViewById(R.id.aaaw_fl_root_container);
    }

    @Override
    protected void initData() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.aaaw_fl_root_container, new ProgressBarDemoFragment());
        ft.commit();
        startNextActivityDelay();
    }

    protected void startNextActivityDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    WithoutAnimationBActivity.start(WithoutAnimationAActivity.this);
                    //clear the animations.
                    overridePendingTransition(0, 0);
                }
            }
        }, 3000);
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWithoutAnimationBActivityOnCreateEvent(OnWithoutAnimationBActivityOnCreateEvent e) {
        LogMgr.i().logI(TAG, "onWithoutAnimationBActivityOnCreateEvent -> finish self!");
        finish();
    }

    @Override
    protected void onFinish() {
        super.onFinish();
        EventBus.getDefault().unregister(this);
    }

}
