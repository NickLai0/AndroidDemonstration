package com.test.util;

import android.hardware.Camera;

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
            int minDifference = Integer.MAX_VALUE;
            int tempArea = 0;
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
}
