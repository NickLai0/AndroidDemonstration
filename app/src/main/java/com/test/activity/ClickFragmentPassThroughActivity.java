package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.test.R;
import com.test.app.LogMgr;
import com.test.fragment.NormalFragment;
import com.test.fragment.ProgressBarDemoFragment;
import com.test.util.ToastUtils;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/6/22<br>
 * Time: 19:38<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class ClickFragmentPassThroughActivity extends BaseActivity {

    public static void start(Activity a) {
        a.startActivity(new Intent(a, ClickFragmentPassThroughActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_stop_on_after_fragment_remove;
    }

    @Override
    protected void initView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.replace(android.R.id.content, new ProgressBarDemoFragment());
        ft.replace(android.R.id.content, new NormalFragment());
        ft.commit();
        LogMgr.i().logI(TAG, "initView -> replace a fragment!");
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {

    }

    public void onBackgroundClick(View v) {
        ToastUtils.showShort(this, "onBackgroundClick -> on activity background click.");
    }

}
