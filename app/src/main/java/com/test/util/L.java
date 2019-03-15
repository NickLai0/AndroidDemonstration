package com.test.util;

import android.util.Log;

import com.test.BuildConfig;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/3/15<br>
 * Time: 16:32<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class L {

    private static final String TAG = "TEMP";

    private static boolean sCanLog = BuildConfig.DEBUG;

    public void setCanLog(boolean canLog) {
        sCanLog = canLog;
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (sCanLog) {
            Log.i(tag, msg);
        }
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void w(String tag, String msg) {
        if (sCanLog) {
            Log.w(tag, msg);
        }
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (sCanLog) {
            Log.e(tag, msg);
        }
    }

}
