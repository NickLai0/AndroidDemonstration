package com.test.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

import com.test.app.LogMgr;

import java.util.List;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/2/14<br>
 * Time: 10:34<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class CameraUtils {

    public static Camera.Size getOptimalSize(Camera camera, int viewW, int viewH) {
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> supportedVideoSizes = parameters.getSupportedVideoSizes();
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        return getOptimalSize(supportedVideoSizes, supportedPreviewSizes, viewW, viewH);
    }

    private static Camera.Size getOptimalSize(List<Camera.Size> videoSizes, List<Camera.Size> previewSizes, int viewW, int viewH) {
        if (videoSizes == null) {
            videoSizes = previewSizes;
        }
        Camera.Size optimalSize = null;

        if (videoSizes != null && previewSizes != null) {
            //the pixels of view
            int area = viewW * viewH;
            int tempArea = 0;
            int minDifference = Integer.MAX_VALUE;
            for (Camera.Size s : videoSizes) {
                if (
                        (tempArea = s.width * s.height) >= area
                                && (tempArea - area) <= minDifference
                                && previewSizes.contains(s)
                ) {
                    optimalSize = s;
                    minDifference = tempArea - area;
                }
            }
        }
        return optimalSize;
    }


    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getBackCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public static Camera getFrontCameraInstance() {
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            try {
                Camera.getCameraInfo(i, cameraInfo);
            } catch (Exception e) {
                LogMgr.i().logE("CameraUtils", "getFrontCameraInstance -> " + ExceptionUtil.getStackTrace(e));
                continue;
            }
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    return Camera.open(i); // attempt to get a Camera instance
                } catch (Exception e) {
                    LogMgr.i().logE("CameraUtils", "getFrontCameraInstance -> " + ExceptionUtil.getStackTrace(e));
                }
            }
        }
        return null;
    }

}
