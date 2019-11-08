package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;

import com.test.R;
import com.test.app.LogManager;
import com.test.util.TabLayoutUtils;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/11/8<br>
 * Time: 11:46<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TabLayoutDemoActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    private TabLayout mTl;

    private final String[] fTabTextArray = new String[]{
            "课时卡", "课程包", "已过期", "Fourth",
            "Fifth", "Sixth", "七", "八",
    };

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TabLayoutDemoActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_demo_layout_tab;
    }

    @Override
    protected void initView() {
        mTl = (TabLayout) findViewById(R.id.adlt_tl);
    }

    @Override
    protected void initData() {
        for (int i = 0; i < fTabTextArray.length; i++) {
            if (i == 2) {
                mTl.addTab(mTl.newTab().setText(fTabTextArray[i]), true);
                break;
            } else {
                mTl.addTab(mTl.newTab().setText(fTabTextArray[i]));
            }
        }
        TabLayoutUtils.setIndicator(mTl, 20, 20);
    }

    @Override
    protected void initListener() {
        mTl.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        LogManager.i().logT(TAG, "onTabSelected -> text : " + tab.getText());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        LogManager.i().logT(TAG, "onTabUnselected -> text : " + tab.getText());
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        LogManager.i().logT(TAG, "onTabReselected -> text : " + tab.getText());
    }

}
