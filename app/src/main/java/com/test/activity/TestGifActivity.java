package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.test.R;
import com.test.view.CustomGifView;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/1/3<br>
 * Time: 10:03<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestGifActivity extends BaseActivity {

    private Button mBtnShowOrHideTestView;
    private LinearLayout mLlTestView;
    private Button mBtnGifAnimationStart;
    private Button mBtnGifAnimationStartByGlide;
    private CustomGifView mCgv;
    private ImageView mIv;
    private ImageView mIvOnlineGif;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestGifActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_gif_test;
    }

    @Override
    protected void initView() {
        mBtnShowOrHideTestView = findViewById(R.id.agt_btn_show_or_hide_test_view);
        mLlTestView = findViewById(R.id.agt_ll_test_view);
        mBtnGifAnimationStart = findViewById(R.id.agt_btn_gif_animation_start);
        mBtnGifAnimationStartByGlide = findViewById(R.id.agt_btn_gif_animation_start_by_glide);
        mCgv = findViewById(R.id.agt_cgv);
        mIv = findViewById(R.id.agt_iv);
        mIvOnlineGif = findViewById(R.id.agt_iv_online_gif);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
        mBtnShowOrHideTestView.setOnClickListener(this);
        mBtnGifAnimationStart.setOnClickListener(this);
        mBtnGifAnimationStartByGlide.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.agt_btn_gif_animation_start:
                mCgv.setImageResource(R.drawable.gif_img_superlion_flying);
                break;

            case R.id.agt_btn_show_or_hide_test_view:
                mLlTestView.setVisibility(mLlTestView.isShown() ? View.GONE : View.VISIBLE);
                break;

            case R.id.agt_btn_gif_animation_start_by_glide:
                Glide.with(this).load(R.drawable.gif_img_superlion_flying).into(mIv);
                Glide.with(this).load("http://5b0988e595225.cdn.sohucs.com/images/20180405/33729b05958f453c8b25474c3ae1c4ff.gif").into(mIvOnlineGif);
                break;
        }
    }
}
