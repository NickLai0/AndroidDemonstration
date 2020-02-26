package com.test.app;

import android.content.Context;
import android.os.HandlerThread;
import android.os.Looper;

import com.log.BuildConfig;
import com.log.LoggerImpl;
import com.log.interfaces.Logger;
import com.log.interfaces.Uploader;
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
public class LogMgr {

    private static final String TAG = LogMgr.class.getSimpleName();

    private HandlerThread mHandlerThread;

    private Logger mLogger;

    private Uploader mUploader;

    private Context mContext;

    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    public static LogMgr i() {
        return SingletonHolder.sfLogMgr;
    }

    private static final class SingletonHolder {
        private static final LogMgr sfLogMgr = new LogMgr();
    }

    private LogMgr() {
    }

    void init(Context c, Looper loggingLooper) {
        if (mContext != null) {
            //Has already initialized.
            return;
        }
        mContext = c.getApplicationContext();
        if (loggingLooper == null) {
            mHandlerThread = new HandlerThread(TAG);
            mHandlerThread.start();
            loggingLooper = mHandlerThread.getLooper();
        }
        boolean isDebug = BuildConfig.DEBUG;
        String logDir = mContext.getFilesDir() + File.separator + "logRepo";
        String logZipDir = logDir + File.separator + "zip";
        String logFileName = "log";

        mLogger = new LoggerImpl.Builder()
                .setContext(mContext)
                .setLooper(loggingLooper)
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
        logI(TAG, "start -> Logs must logging after this method be called");
    }

    void stop() {
        logI(TAG, "stop -> this should never be called.");
        mLogger.stop();
        if (mHandlerThread != null) {
            mHandlerThread.quit();
        }
    }

    private long logStartMillis;

    public void logStart() {
        logStartMillis = System.currentTimeMillis();
    }

    public void logEnd(String tag, String msg) {
        logI(TAG, "spent time : " + (System.currentTimeMillis() - logStartMillis) + " ; " + msg);
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
