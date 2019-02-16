package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.test.R;
import com.test.util.CameraUtils;
import com.test.util.ToastUtils;
import com.test.view.CameraPreview;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/2/16<br>
 * Time: 17:34<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class CameraOpenOrCloseActivity extends BaseActivity {

    private final String TEXT_CAMERA_OPEN = "open front of camera";
    private final String TEXT_CAMERA_CLOSE = "close camera";

    private TextView mTvOpenOrClose;
    private Camera mCamera;
    private CameraPreview mCameraPreview;
    private FrameLayout mFlRootContainer;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, CameraOpenOrCloseActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_close_or_open_camera;
    }

    @Override
    protected void initView() {
        mFlRootContainer = (FrameLayout) findViewById(R.id.acooc_fl_root_container);
        mTvOpenOrClose = (TextView) findViewById(R.id.acooc_tv_open_or_close_camera);
    }

    @Override
    protected void initData() {
        mTvOpenOrClose.setText(TEXT_CAMERA_OPEN);
    }

    @Override
    protected void initListener() {
        mTvOpenOrClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acooc_tv_open_or_close_camera:
                if (TEXT_CAMERA_OPEN.equals(mTvOpenOrClose.getText().toString())) {
                    mCamera = CameraUtils.getFrontCameraInstance();
                    if (mCamera == null) {
                        ToastUtils.showLong(this, "打开前置摄像头失败， 请检查是否损坏或被其它应用占用。");
                        return;
                    }
                    // Create our Preview view and set it as the content of our activity.
                    mCameraPreview = new CameraPreview(this, mCamera);
                    mCameraPreview.setIsRecordingPreview(false);
                    mFlRootContainer.addView(mCameraPreview, 0);
                    mTvOpenOrClose.setText(TEXT_CAMERA_CLOSE);
                } else {
                    mFlRootContainer.removeView(mCameraPreview);
                    releaseCamera();
                    mTvOpenOrClose.setText(TEXT_CAMERA_OPEN);
                }
                break;
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

}
