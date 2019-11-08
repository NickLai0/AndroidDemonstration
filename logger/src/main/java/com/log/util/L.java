package com.log.util;

import android.util.Log;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/6/20<br>
 * Time: 13:14<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class L {

    private static boolean sIsDebug = false;

    public static void setIsDebug(boolean isDebug) {
        sIsDebug = isDebug;
    }

    public static void d(String tag, String msg) {
        if (sIsDebug) {
            Log.d(tag,msg);
        }
    }

    public static void i(String tag, String msg) {
        if (sIsDebug) {
            Log.i(tag,msg);
        }
    }

    public static void w(String tag, String msg) {
        if (sIsDebug) {
            Log.w(tag,msg);
        }
    }

    public static void e(String tag, String msg) {
        if (sIsDebug) {
            Log.e(tag,msg);
        }
    }

}
