package com.test.view;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/2/2<br>
 * Time: 15:31<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.test.util.CameraUtils;

import java.io.IOException;
import java.util.List;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private final String TAG = CameraPreview.class.getName();

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.Size mOptimalSize;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.setFixedSize(0,0);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            preview();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        try {
            preview();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public Camera.Size getOptimalSize() {
        return mOptimalSize;
    }

    private void preview() throws IOException {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        mOptimalSize = CameraUtils.getOptimalSize(mCamera, getWidth(), getHeight());
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(mOptimalSize.width, mOptimalSize.height);
        mCamera.setParameters(parameters);

        // set preview size and make any resize, rotate or
        // reformatting changes here
        mCamera.setDisplayOrientation(90);
        mCamera.setPreviewDisplay(mHolder);

        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes != null) {
            for (String mode : focusModes) {
                if (mode.contains("continuous-video")) {
                    parameters.setFocusMode("continuous-video");
                    break;
                }
            }
        }

        // start preview with new settings
        mCamera.startPreview();
    }
}