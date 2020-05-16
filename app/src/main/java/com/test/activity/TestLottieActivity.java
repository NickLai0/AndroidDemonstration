package com.test.activity;

import android.app.Activity;
import android.content.Intent;

import com.airbnb.lottie.LottieAnimationView;
import com.test.R;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/4/1<br>
 * Time: 14:55<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestLottieActivity extends BaseActivity {

    private LottieAnimationView mLavAnimation1;
    private LottieAnimationView mLavAnimation2;
    private LottieAnimationView mLavAnimation3;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestLottieActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_lottie_test;
    }

    @Override
    protected void initView() {
        mLavAnimation1 = findViewById(R.id.alt_lav_animation_1);
        mLavAnimation2 = findViewById(R.id.alt_lav_animation_2);
        mLavAnimation3 = findViewById(R.id.alt_lav_animation_3);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {

    }
}
