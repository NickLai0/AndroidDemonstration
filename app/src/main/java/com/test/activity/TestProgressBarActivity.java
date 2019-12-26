package com.test.activity;

import android.app.Activity;
import android.content.Intent;

import com.test.R;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/11/28<br>
 * Time: 17:05<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestProgressBarActivity extends BaseActivity {

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestProgressBarActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_bar_progress_test;
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
