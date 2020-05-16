package com.test.activity;

import android.app.Activity;
import android.content.Intent;

import com.test.event.OnWithoutAnimationBActivityOnCreateEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/5/16<br>
 * Time: 19:39<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class WithoutAnimationBActivity extends WithoutAnimationAActivity {

    public static void start(Activity a) {
        a.startActivity(new Intent(a, WithoutAnimationBActivity.class));
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().post(new OnWithoutAnimationBActivityOnCreateEvent());
    }

    @Override
    protected void startNextActivityDelay() {
        //does not start next activity.
    }
}
