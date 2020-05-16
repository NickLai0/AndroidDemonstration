package com.test.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.test.mvp.presenter.IPresenter;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/4/21<br>
 * Time: 10:36<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public abstract class MvpBaseActivity extends BaseActivity {

    private IPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
        mPresenter.onCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    protected void onFinish() {
        super.onFinish();
        mPresenter.onFinish();
    }

    protected abstract IPresenter getPresenter();

}
