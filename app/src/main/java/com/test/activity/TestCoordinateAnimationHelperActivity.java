package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.bezier.bean.AnimationInfo;
import com.bezier.helper.CoordinateAnimationHelper;
import com.test.R;
import com.test.app.LogMgr;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/12/24<br>
 * Time: 17:32<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestCoordinateAnimationHelperActivity extends BaseActivity implements CoordinateAnimationHelper.OnAnimationRefreshListener {

    private View mV1;
    private View mV2;
    private View mV3;
    private Button mBtnStartTest;
    private Button mBtnStopFirstView;
    private CoordinateAnimationHelper mCoordinateAnimationHelper;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestCoordinateAnimationHelperActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_helper_animation_coordination_test;
    }

    @Override
    protected void initView() {
        mV1 = findViewById(R.id.ahact_v1);
        mV2 = findViewById(R.id.ahact_v2);
        mV3 = findViewById(R.id.ahact_v3);
        mBtnStartTest = findViewById(R.id.ahact_btn_start_test);
        mBtnStopFirstView = findViewById(R.id.ahact_btn_stop_first_view);
    }

    @Override
    protected void initData() {
        mCoordinateAnimationHelper = new CoordinateAnimationHelper();
        mCoordinateAnimationHelper.init();
    }

    @Override
    protected void initListener() {
        mBtnStartTest.setOnClickListener(this);
        mBtnStopFirstView.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCoordinateAnimationHelper.uninit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ahact_btn_start_test:
                animationTest(mV1);
                animationTest(mV2);
                animationTest(mV3);
                break;

            case R.id.ahact_btn_stop_first_view:
                mCoordinateAnimationHelper.stop(mV1);
                break;
        }
    }

    private void animationTest(View v) {
        int[] xyCoordinates = new int[2];
        v.getLocationOnScreen(xyCoordinates);
        int startX = xyCoordinates[0];
        int startY = xyCoordinates[1];
        mCoordinateAnimationHelper.start(v, startX, startY, startX + 200, startY + 200, 1000 * 30, this);
    }

    @Override
    public boolean onAnimationRefresh(AnimationInfo a) {
        LogMgr.i().logT(TAG, "onAnimationRefresh -> a.spentMillisPercent : " + a.spentMillisPercent);
        a.spentMillisPercent = 0;
        return false;
    }


}
