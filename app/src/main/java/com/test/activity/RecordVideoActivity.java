package com.test.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.test.R;
import com.test.view.CameraPreview;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/2/1<br>
 * Time: 14:29<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class RecordVideoActivity extends BaseActivity {

    private final String RECORDING_WORDING_START = "start recording video";
    private final String RECORDING_WORDING_STOP = "stop recording video";


    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private CameraPreview mCameraPreview;
    private FrameLayout mFlPreview;
    private TextView mTvRecord;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, RecordVideoActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_video_record;
    }

    @Override
    protected void initView() {
        mFlPreview = (FrameLayout) findViewById(R.id.avr_fl_root_container);
        mTvRecord = (TextView) findViewById(R.id.avr_tv_record);
    }

    @Override
    protected void initData() {
        mTvRecord.setText(RECORDING_WORDING_START);
    }

    @Override
    protected void initListener() {
        mTvRecord.setOnClickListener(this);
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }


    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private boolean prepareVideoRecorder(Camera c, Surface surface, Camera.Size optimalSize) {

        if (mMediaRecorder != null) {
            mMediaRecorder.release();
        }

//        mCamera = getCameraInstance();
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(c);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        profile.videoFrameWidth = optimalSize.width;
        profile.videoFrameHeight = optimalSize.height;
        profile.videoBitRate = 2 * optimalSize.width * optimalSize.height;
        mMediaRecorder.setProfile(profile);
        mMediaRecorder.setOrientationHint(90);
        // Step 4: Set output file
        mMediaRecorder.setOutputFile(getOutputMediaFile(this, MEDIA_TYPE_VIDEO).toString());

        // Step 5: Set the preview output
        mMediaRecorder.setPreviewDisplay(surface);

        // Step 6: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(Context c, int type) {
        return Uri.fromFile(getOutputMediaFile(c, type));
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(Context c, int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        final String SUB_DIR = "CameraVideos";
        File mediaStorageDir = new File(c.getExternalFilesDir(Environment.DIRECTORY_PICTURES), SUB_DIR);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory : " + SUB_DIR);
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avr_tv_record:
                if (RECORDING_WORDING_START.equals(mTvRecord.getText())) {
                    if (!prepareVideoRecorder(mCamera, mCameraPreview.getHolder().getSurface(), mCameraPreview.getOptimalSize())) {
                        Log.e(TAG, "onClick -> click record button, but prepare video failed.");
                        return;
                    }
                    mMediaRecorder.start();
                    mTvRecord.setText(RECORDING_WORDING_STOP);
                } else {
                    mMediaRecorder.stop();
                    mTvRecord.setText(RECORDING_WORDING_START);
                }
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();
        // take camera access back from MediaRecorder
        mCamera.lock();
        releaseCamera();
        if (mCameraPreview != null) {
            mFlPreview.removeView(mCameraPreview);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Create an instance of Camera
        mCamera = getCameraInstance();
        // Create our Preview view and set it as the content of our activity.
        mCameraPreview = new CameraPreview(this, mCamera);
        mFlPreview.addView(mCameraPreview, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaRecorder();
        releaseCamera();
    }
}
