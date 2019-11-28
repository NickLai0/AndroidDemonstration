package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.test.R;
import com.test.app.LogMgr;
import com.test.view.CustomImageView;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/11/22<br>
 * Time: 16:54<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestCustomImageViewActivity extends BaseActivity {

    private CustomImageView mCiv;
    private Button mBtnJustSetBackground;
    private Button mBtnJustSetPhoto;
    private Button mBtnSetBackgroundAndPhoto;
    private Button mBtnSetCornerRadius;
    private Button mBtnClearCornerRadius;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestCustomImageViewActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_view_image_custom_test;
    }

    @Override
    protected void initView() {
        mCiv = findViewById(R.id.avict_civ);
        mBtnJustSetBackground = findViewById(R.id.avict_btn_just_set_background);
        mBtnJustSetPhoto = findViewById(R.id.avict_btn_just_set_photo);
        mBtnSetBackgroundAndPhoto = findViewById(R.id.avict_btn_set_background_and_photo);
        mBtnSetCornerRadius = findViewById(R.id.avict_btn_set_corner_radius);
        mBtnClearCornerRadius = findViewById(R.id.avict_btn_clear_corner_radius);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mBtnJustSetBackground.setOnClickListener(this);
        mBtnJustSetPhoto.setOnClickListener(this);
        mBtnSetBackgroundAndPhoto.setOnClickListener(this);
        mBtnSetCornerRadius.setOnClickListener(this);
        mBtnClearCornerRadius.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avict_btn_just_set_background:
                mCiv.setBackgroundColor(getResources().getColor(R.color.red));
                mCiv.setImageDrawable(null);
                LogMgr.i().logT(TAG, "onClick -> just set background!");
                break;

            case R.id.avict_btn_just_set_photo:
                mCiv.setBackground(null);
                mCiv.setImageResource(R.drawable.photo);
                LogMgr.i().logT(TAG, "onClick -> just set photo!");
                break;

            case R.id.avict_btn_set_background_and_photo:
                mCiv.setBackgroundColor(getResources().getColor(R.color.red));
                mCiv.setImageResource(R.drawable.photo);
                LogMgr.i().logT(TAG, "onClick -> set background and photo!");
                break;

            case R.id.avict_btn_set_corner_radius:
                mCiv.setCornerRadius(10000);
                break;

            case R.id.avict_btn_clear_corner_radius:
                mCiv.setCornerRadius(0);
                break;
        }
    }
}
