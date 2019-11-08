package com.test.app;

import android.content.Context;
import android.os.HandlerThread;
import android.os.Looper;

import com.log.BuildConfig;
import com.log.LoggerImpl;
import com.log.interfaces.Logger;
import com.test.util.ExceptionUtil;

import java.io.File;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/11/8<br>
 * Time: 11:57<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class LogManager {

    private static final String TAG = LogManager.class.getSimpleName();
    private Context mContext;

    public static LogManager i() {
        return SingletonHolder.sfLogUploadManager;
    }

    private static final class SingletonHolder {
        private static final LogManager sfLogUploadManager = new LogManager();
    }

    private HandlerThread mHandlerThread;

    private Logger mLogger;

    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    void start() {
        mLogger.start();
        logI(TAG, "start -> Logs must logging after this method be called");
    }

    void stop() {
        logI(TAG, "stop -> this should never be called.");
        mLogger.stop();
        mHandlerThread.quit();
    }

    void init(Context c) {
        if (mContext != null) {
            //Has already initialized.
            return;
        }
        mContext = c.getApplicationContext();
        mHandlerThread = new HandlerThread(TAG);
        mHandlerThread.start();
        Looper looper = mHandlerThread.getLooper();
        boolean isDebug = BuildConfig.DEBUG;
        String logDir = mContext.getFilesDir() + File.separator + "logRepo";
        String logZipDir = logDir + File.separator + "zip";
        String logFileName = "log";

        mLogger = new LoggerImpl.Builder()
                .setContext(mContext)
                .setLooper(looper)
                .setLogDir(logDir)
                .setLogFileName(logFileName)
                .setZipLogDir(logZipDir)
                .setLogRefreshBytes(1024)
                .setZipLogBytes(1024 * 1024 * 5)
                .setMaxZips(3)
                .setIsDebug(isDebug)
                .setOpenInsideLog(isDebug)
                .setIsRecordRefreshLogStartTag(isDebug)
                .setIsRecordRefreshLogEndTag(isDebug)
//                .setHeaderInfo()
//                .setRefreshLogStartTag()
//                .setRefreshLogEndTag()
                .build();

        //start logger.
        mLogger.start();
        logI(TAG, "init -> start logger.");

        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                logE(TAG, "uncaughtException -> thread name : " + t.getName() + ", thread id : " + t.getId() + ", Exception : " + ExceptionUtil.getStackTrace(e));
                //Refresh all of logs.
                mLogger.refreshLog();
                if (mDefaultUncaughtExceptionHandler != null) {
                    mDefaultUncaughtExceptionHandler.uncaughtException(t, e);
                }
            }
        });

    }

    private long logStartMillis;

    public void logStart() {
        logStartMillis = System.currentTimeMillis();
    }

    public void logEnd(String tag, String msg) {
        logI(tag, "spent time : " + (System.currentTimeMillis() - logStartMillis) + " ; " + msg);
    }
    public void logT(String tag, String msg) {
        mLogger.logT(tag, msg);
    }
    public void logI(String tag, String msg) {
        mLogger.logI(tag, msg);
    }

    public void logE(String tag, String s) {
        mLogger.logE(tag, s);
    }

    public void flushAsync() {
        mLogger.notifyRefreshLog();
    }

    public void flush() {
        mLogger.refreshLog();
    }
}
