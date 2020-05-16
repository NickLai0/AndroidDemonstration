package com.test.app;

import android.content.Context;
import android.os.HandlerThread;
import android.os.Looper;

import com.log.BuildConfig;
import com.log.LoggerImpl;
import com.log.interfaces.Logger;
import com.log.listener.OnLogRefreshListener;
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
public final class LogMgr {

    private static final String TAG = LogMgr.class.getSimpleName();

    private HandlerThread mHandlerThread;

    private Logger mLogger;

    private Context mContext;

    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;
    private String mLogZipDir;
    private String mLogDir;
    private String mLogFileName;

    public static LogMgr i() {
        return SingletonHolder.sfLogMgr;
    }

    private static final class SingletonHolder {
        private static final LogMgr sfLogMgr = new LogMgr();
    }

    private LogMgr() {
    }

    void init(Context c) {
        mContext = c.getApplicationContext();
        mHandlerThread = new HandlerThread(TAG);
        mHandlerThread.start();
        Looper looper = mHandlerThread.getLooper();
        mLogDir = mContext.getFilesDir() + File.separator + "logs";
        mLogFileName = "log";
        mLogZipDir = mLogDir + File.separator + "zip";
        boolean isDebug = BuildConfig.DEBUG;

        mLogger = new LoggerImpl.Builder()
                .setLooper(looper)
                .setLogDir(mLogDir)
                .setLogFileNamePrefix(mLogFileName)
                .setLogFileMax(3)
                .setLogFileBytesThreshold(1024 * 1024 * 5)
                .setLogInQueueBytesThreshold(1024)
                .setIsDebug(isDebug)
                //.setHeaderInfo()
                .setRefreshLogStartTag(isDebug ? "@refresh log start@" : null)
                .setRefreshLogEndTag(null)
                .build();

        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                logE(TAG, "uncaughtException -> thread name : " + t.getName() + ", thread id : " + t.getId() + ", Exception : " + ExceptionUtil.getStackTrace(e));
                //Refresh all of logs.
                mLogger.refreshLogRequest(new OnLogRefreshListener() {
                    @Override
                    public void onLogRefreshed() {
                        if (mDefaultUncaughtExceptionHandler != null) {
                            mDefaultUncaughtExceptionHandler.uncaughtException(t, e);
                        }
                    }
                });

            }
        });

    }

    void start() {
        mLogger.start();
    }

    void stop() {
        logI(TAG, "stop -> this should never be called.");
        mLogger.stop();
        mHandlerThread.quit();
    }

    public void logT(String tag, String msg) {
        mLogger.logT(tag, msg);
    }

    public void logI(String tag, String msg) {
        mLogger.logI(tag, msg);
    }

    public void logE(String tag, String msg) {
        mLogger.logE(tag, msg);
    }

    public void flushAsync() {
        mLogger.refreshLogRequest(new OnLogRefreshListener() {
            @Override
            public void onLogRefreshed() {
                LogMgr.i().logI(TAG, "flushAsync -> onLogRefreshed callback.");
            }
        });
    }


}