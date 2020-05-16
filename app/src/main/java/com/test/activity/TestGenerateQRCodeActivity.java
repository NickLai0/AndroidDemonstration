package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.test.R;
import com.test.util.QRCodeUtils;
import com.test.util.ToastUtils;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/4/3<br>
 * Time: 16:18<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestGenerateQRCodeActivity extends BaseActivity {

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestGenerateQRCodeActivity.class));
    }

    private EditText mEt;
    private Button mBtnGenerateQRCode;
    private ImageView mIvQRCode;

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_code_qr_generate_test;
    }

    @Override
    protected void initView() {
        mEt = findViewById(R.id.acqgt_et);
        mBtnGenerateQRCode = findViewById(R.id.acqgt_btn_generate_qr_code);
        mIvQRCode = findViewById(R.id.acqgt_iv_qr_code);
    }

    @Override
    protected void initData() {
        mBtnGenerateQRCode.setOnClickListener(this);
    }

    @Override
    protected void initListener() {
        mBtnGenerateQRCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acqgt_btn_generate_qr_code:
                String content = mEt.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showLong(this, "content string shouldn't be empty.");
                    return;
                }
                mIvQRCode.setImageBitmap(QRCodeUtils.createQRCodeBitmap(content, mIvQRCode.getWidth(), mIvQRCode.getHeight()));
                break;
        }
    }

}
