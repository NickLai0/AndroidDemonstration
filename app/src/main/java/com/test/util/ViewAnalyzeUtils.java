package com.test.util;

import android.view.View;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/1/3<br>
 * Time: 10:47<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public final class ViewAnalyzeUtils {

    private ViewAnalyzeUtils() {
    }

    public static String getDimensionMode(int dimensionMeasureSpec) {
        String modeDesc = "unknown";
        int mode = View.MeasureSpec.getMode(dimensionMeasureSpec);
        switch (mode) {
            case View.MeasureSpec.UNSPECIFIED:
                modeDesc = "unspecified";
                break;

            case View.MeasureSpec.AT_MOST:
                modeDesc = "at_most";
                break;

            case View.MeasureSpec.EXACTLY:
                modeDesc = "exactly";
                break;
        }
        return modeDesc;
    }

}
