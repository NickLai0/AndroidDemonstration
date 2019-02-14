package com.test.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2018/11/24<br>
 * Time: 14:23<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideLayoutId());
        initView();
        initData();
        initListener();
    }

    protected abstract int provideLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    @Override
    public void onClick(View v) {

    }
}
