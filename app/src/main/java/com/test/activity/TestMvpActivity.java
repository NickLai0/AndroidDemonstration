package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.test.R;
import com.test.mvp.presenter.IPresenter;
import com.test.mvp.presenter.TestPresenter;
import com.test.mvp.view.ITestView;
import com.test.util.ToastUtils;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/4/21<br>
 * Time: 11:41<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestMvpActivity extends MvpBaseActivity implements ITestView {

    private TestPresenter mTestPresenter;

    private EditText mEd;
    private Button mBtnSendMessage;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestMvpActivity.class));
    }

    @Override
    protected IPresenter getPresenter() {
        return mTestPresenter = new TestPresenter(this);
    }


    @Override
    protected void initView() {
        mEd = findViewById(R.id.amt_ed);
        mBtnSendMessage = findViewById(R.id.amt_btn_send_message);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mBtnSendMessage.setOnClickListener(this);
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_mvp_test;
    }

    @Override
    public void clearMessage() {
        mEd.setText(null);
    }

    @Override
    public void notifyMessageFailed() {
        ToastUtils.showShort(this, "Send message failed.");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.amt_btn_send_message:
                Editable text = mEd.getText();
                String msg = null;
                if (text == null || TextUtils.isEmpty(msg = text.toString())) {
                    ToastUtils.showShort(this, "The message can not be empty!");
                }
                mTestPresenter.sendMessage(msg);
                break;
        }
    }

}
