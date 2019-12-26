package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import com.test.R;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/12/9<br>
 * Time: 10:47<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TextViewMarqueeActivity extends BaseActivity {

    public static final void start(Activity a) {
        a.startActivity(new Intent(a, TextViewMarqueeActivity.class));
    }

    private TextView mTvMarquee;

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_marquee_view_text;
    }

    @Override
    protected void initView() {
        mTvMarquee = findViewById(R.id.amvt_tv_marquee);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mTvMarquee.setSelected(true);
    }
}
