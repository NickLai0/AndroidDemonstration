package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.test.R;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/4/1<br>
 * Time: 11:23<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestHtmlCompactActivity extends BaseActivity {

    private EditText mEtHtmlInput;
    private Button mBtnVerify;
    private TextView mTvResult;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestHtmlCompactActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_compact_html_test;
    }

    @Override
    protected void initView() {
        mEtHtmlInput = findViewById(R.id.acht_et_html_input);
        mBtnVerify = findViewById(R.id.acht_btn_verify);
        mTvResult = findViewById(R.id.acht_tv_result);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mBtnVerify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acht_btn_verify:
                Editable text = mEtHtmlInput.getText();
                if (text != null) {
                    String html = text.toString();
                    mTvResult.setText(Html.fromHtml(html));
                }
                break;
        }
    }

}
