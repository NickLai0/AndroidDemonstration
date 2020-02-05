package com.wav.util;


import com.wav.interfaces.Logger;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/2/4<br>
 * Time: 14:33<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public final class LogUtils {

    private static Logger mLogger;

    private LogUtils() {
    }

    public static void setLogger(Logger logger) {
        mLogger = logger;
    }


    public static void logT(String tag, String msg) {
        if (mLogger != null) {
            mLogger.logT(tag, msg);
        }
    }

    public static void logI(String tag, String msg) {
        if (mLogger != null) {
            mLogger.logI(tag, msg);
        }
    }

    public static void logE(String tag, String msg) {
        if (mLogger != null) {
            mLogger.logE(tag, msg);
        }
    }

}
