package com.test.activity;

import android.app.Activity;
import android.content.Intent;

import com.test.R;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/4/15<br>
 * Time: 20:26<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestDrawableCenterTextViewActivity extends BaseActivity{

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestDrawableCenterTextViewActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_view_text_center_drawable_test;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

}
