package com.test.activity;

import android.app.Activity;
import android.content.Intent;

import com.test.R;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/11/8<br>
 * Time: 10:06<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class CoordinateLayoutDemoActivity extends BaseActivity {

    public static void start(Activity a) {
        a.startActivity(new Intent(a, CoordinateLayoutDemoActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_demo_layout_coordinator;
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
