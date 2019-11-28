package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.test.R;
import com.test.view.SelfAdaptionFrameLayout;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/11/22<br>
 * Time: 22:00<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestSelfAdaptionFrameViewActivity extends BaseActivity {

    private SelfAdaptionFrameLayout mSafl;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestSelfAdaptionFrameViewActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_view_frame_adaption_self_test;
    }

    @Override
    protected void initView() {
        mSafl = findViewById(R.id.avfast_safl_first);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSafl.setCornerRadius(10000);
            }
        }, 5 * 1000);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

}
