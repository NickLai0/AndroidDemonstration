package com.test.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.test.R;
import com.test.app.LogMgr;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/1/8<br>
 * Time: 16:24<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestObjectAnimatorActivity extends BaseActivity {

    private Button mBtnTestAlphaAnimationStart;
    private Button mBtnTestAlphaAnimationStop;
    private Button mBtnShakeAnimationStart;
    private Button mBtnShakeAnimationStop;

    private TextView mTvWeakNetTip;

    private View mTvShakeAnimation;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestObjectAnimatorActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_animator_object_test;
    }

    @Override
    protected void initView() {
        mBtnTestAlphaAnimationStart = findViewById(R.id.aaot_btn_test_alpha_animation_start);
        mBtnTestAlphaAnimationStop = findViewById(R.id.aaot_btn_test_alpha_animation_stop);
        mTvWeakNetTip = findViewById(R.id.aaot_tv_weak_net_tip);
        mBtnShakeAnimationStart = findViewById(R.id.aaot_btn_test_shake_animation_start);
        mBtnShakeAnimationStop = findViewById(R.id.aaot_btn_test_shake_animation_stop);
        mTvShakeAnimation = findViewById(R.id.aaot_tv_shake_animation);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mBtnTestAlphaAnimationStart.setOnClickListener(this);
        mBtnTestAlphaAnimationStop.setOnClickListener(this);
        mBtnShakeAnimationStart.setOnClickListener(this);
        mBtnShakeAnimationStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aaot_btn_test_alpha_animation_start:
                weakNetAnimationStart();
                break;
            case R.id.aaot_btn_test_alpha_animation_stop:
                weakNetAnimationStop();
                break;
            case R.id.aaot_btn_test_shake_animation_start:
                shakeAnimationStart();
                break;
            case R.id.aaot_btn_test_shake_animation_stop:
                shakeAnimationStop();
                break;
        }
    }

    private ObjectAnimator mObjectAnimator4Shake;

    private void shakeAnimationStart() {
        if (mObjectAnimator4Shake == null) {
            mObjectAnimator4Shake = ObjectAnimator.ofFloat(mTvShakeAnimation, View.ROTATION
                    , 0, 3         //start
                    , 3, -3, -3, 3
                    , 3, -3, -3, 3
                    , 3, -3, -3, 3
                    , 3, -3, -3, 3
                    , 3, 0                 //end
            );
            mObjectAnimator4Shake.setDuration(1000);
        }
        if (!mObjectAnimator4Shake.isStarted()) {
            mObjectAnimator4Shake.start();
        }
    }

    private void shakeAnimationStop() {
        if (mObjectAnimator4Shake != null) {
            mObjectAnimator4Shake.cancel();
        }
    }

    private ObjectAnimator mObjectAnimator4WeakNetTip;

    private void weakNetAnimationStop() {
        if (mObjectAnimator4WeakNetTip != null) {
            mObjectAnimator4WeakNetTip.cancel();
            LogMgr.i().logT(TAG, "weakNetAnimationStop -> cancel animation.");
        }
        mTvWeakNetTip.setAlpha(0);
    }

    private void weakNetAnimationStart() {
        if (mObjectAnimator4WeakNetTip == null) {
            mObjectAnimator4WeakNetTip = ObjectAnimator.ofFloat(mTvWeakNetTip, View.ALPHA, 0, 1);
            mObjectAnimator4WeakNetTip.setDuration(1000);
            mObjectAnimator4WeakNetTip.setRepeatCount(ValueAnimator.INFINITE);
        }
        if (!mObjectAnimator4WeakNetTip.isStarted()) {
            mTvWeakNetTip.setAlpha(1);
            LogMgr.i().logT(TAG, "weakNetAnimationStart -> start animation.");
            mObjectAnimator4WeakNetTip.start();
        }
    }
}
