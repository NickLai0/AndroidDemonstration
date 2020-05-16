package com.log;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.log.interfaces.Logger;
import com.log.listener.OnLogRefreshListener;
import com.log.util.ExceptionUtils;
import com.log.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.TimeZone;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/6/18<br>
 * Time: 21:17<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class LoggerImpl implements Logger {

    protected final String TAG = getClass().getSimpleName();

    private static final String INFO_MARK_TEMP = "[temp]";
    private static final String INFO_MARK_INFO = "[info]";
    private static final String INFO_MARK_ERROR = "[error]";

    private final int MSG_REFRESH_LOG_REQUEST = 2;
    private final int MSG_LOG_ENQUEUE = 3;

    protected LoggerParameters mLP = new LoggerParameters();

    private LoggerImpl() {
    }

    @Override
    public synchronized void start() {
        if (mLP.mIsStarted) {
            return;
        }
        mLP.mIsStarted = true;
        if (mLP.mLooper != null) {
            mLP.mLogHandler = new LogHandler(mLP.mLooper);
            logInside(TAG, "start -> Use the looper of outside.");
            return;
        }
        if (mLP.mLogHandlerThread == null) {
            mLP.mLogHandlerThread = new HandlerThread(TAG) {
                @Override
                protected void onLooperPrepared() {
                    logInside(TAG, "start -> onLooperPrepared -> On sub thread looper prepared.");
                    if (mLP.mLogHandler == null) {
                        mLP.mLogHandler = new LogHandler(getLooper());
                    }
                }
            };
            mLP.mLogHandlerThread.start();
        }
    }


    private class LogHandler extends Handler {

        public LogHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOG_ENQUEUE:
                    if (msg.obj instanceof String) {
                        String formattedMsg = (String) msg.obj;
                        mLP.mMsgQueue.addLast(formattedMsg);
                        mLP.mLogInQueueCurrentBytes += formattedMsg.length();
                    }
                    boolean refreshLogImmediately = msg.arg1 != 0;
                    if (refreshLogImmediately || mLP.mLogInQueueCurrentBytes >= mLP.mLogInQueueBytesThreshold) {
                        //if (!mLP.mLogHandler.hasMessages(MSG_REFRESH_LOG_REQUEST)) {
                        refreshLogRequest(null);
                        //}
                    }
                    break;

                case MSG_REFRESH_LOG_REQUEST:
                    //msg.obj might be null.
                    refreshLog((OnLogRefreshListener) msg.obj);
                    mLP.mLogInQueueCurrentBytes = 0;
                    break;
            }
        }
    }

    @Override
    public synchronized void stop() {
        if (!mLP.mIsStarted) {
            return;
        }
        mLP.mIsStarted = false;
        if (mLP.mLogHandler != null) {
            mLP.mLogHandler.removeCallbacksAndMessages(null);
            mLP.mLogHandler = null;
        }
        if (mLP.mLogHandlerThread != null) {
            mLP.mLogHandlerThread.quit();
            mLP.mLogHandlerThread = null;
        }
    }

    @Override
    public void refreshLogRequest(OnLogRefreshListener l) {
        if (mLP.mLogHandler == null) {
            logInside(TAG, "notifyRefreshLog -> notifyRefreshLog method invoked but the log handler haven't initiated yet!");
        } else {
            Message.obtain(mLP.mLogHandler, MSG_REFRESH_LOG_REQUEST, l).sendToTarget();
        }
    }

    private void refreshLog(OnLogRefreshListener l) {
        if (mLP.mMsgQueue.isEmpty()) {
            //Ignore this event, because there is no any log.
            if (l != null) {
                l.onLogRefreshed();
            }
            return;
        }
        File logFile = getLogFile();
        if (!logFile.exists()) {
            FileUtils.createNewFile(logFile);
            if (!TextUtils.isEmpty(mLP.mHeaderInfo)) {
                mLP.mMsgQueue.addFirst(mLP.mHeaderInfo);
            }
        }
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new FileOutputStream(logFile, true));
            if (!TextUtils.isEmpty(mLP.mRefreshLogStartTag)) {
                printWriter.println(mLP.mRefreshLogStartTag);
            }
            String log;
            while ((log = mLP.mMsgQueue.pollFirst()) != null) {
                printWriter.println(log);
            }
            if (!TextUtils.isEmpty(mLP.mRefreshLogEndTag)) {
                printWriter.println(mLP.mRefreshLogEndTag);
            }
        } catch (Exception e) {
            logInside(TAG, ExceptionUtils.getStackTrace(e));
        } finally {
            FileUtils.close(printWriter);
            if (l != null) {
                l.onLogRefreshed();
            }
        }
    }

    private File getLogFile() {
        File logDir = new File(mLP.mLogDir);
        FileUtils.checkDir(logDir);
        File[] logFileArr = logDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches(mLP.mLogFileNamePrefix + "\\d+") && new File(dir, name).isFile();
            }
        });
        File logFile = null;
        if (logFileArr == null || logFileArr.length == 0) {
            logFile = new File(logDir, mLP.mLogFileNamePrefix + 1);
            logInside(TAG, "getLogFile -> There is no older log file. So create a new one; New log's file name ：" + logFile.getName());
        } else if (logFileArr.length > mLP.mLogFileMax) {
            for (int i = 0, size = logFileArr.length; size > mLP.mLogFileMax; i++) {
                File tempFile = logFileArr[i];
                if (tempFile != null && tempFile.delete()) {
                    logInside(TAG, "getLogFile delete -> fileName=" + tempFile.getName());
                    logFileArr[i] = null;
                }
                size--;
            }
            for (int i = 0, fileNumber = 0; i < logFileArr.length; i++) {
                File tempFile = logFileArr[i];
                if (tempFile != null) {
                    String newFileName = mLP.mLogFileNamePrefix + (++fileNumber);
                    logInside(TAG, "getLogFile rename from " + tempFile.getName() + " to " + newFileName);
                    tempFile.renameTo(new File(tempFile.getParent(), newFileName));
                }
            }
        }

        if (logFile == null) {
            int fileNumber = 1;
            for (int i = 0; i < logFileArr.length; i++) {
                File tempFile = logFileArr[i];
                if (tempFile == null || !tempFile.isFile()) {
                    continue;
                }
                if (tempFile.length() < mLP.mLogFileBytesThreshold) {
                    logFile = tempFile;
                    break;
                }
                fileNumber++;
            }
            if (logFile == null) {
                logFile = new File(mLP.mLogDir, mLP.mLogFileNamePrefix + fileNumber);
            }
        }

        return logFile;
    }

    protected void logInside(String tag, String msg) {
        log("[inside]", tag, msg, false);
    }

    @Override
    public void logE(String tag, String msg) {
        log(INFO_MARK_ERROR, tag, msg, true);
    }

    @Override
    public void logT(String tag, String msg) {
        if (mLP.mIsDebug) {
            log(INFO_MARK_TEMP, tag, msg, false);
        }
    }

    @Override
    public void logI(String tag, String msg) {
        log(INFO_MARK_INFO, tag, msg, false);
    }

    private void log(String infoMark, String tag, String msg, boolean refreshLogImmediately) {
        logcatLogging(infoMark, tag, msg);
        String date = mLP.mLogContentSimpleDateFormat.format(new Date());
        String formattedMsg = String.format(Locale.getDefault(), "%s %s %s: %s", date, infoMark, tag, msg);
        Message.obtain(mLP.mLogHandler, MSG_LOG_ENQUEUE, refreshLogImmediately ? 1 : 0, 0, formattedMsg).sendToTarget();
    }

    private void logcatLogging(String infoMark, String tag, String msg) {
        if (TextUtils.isEmpty(infoMark) || TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
            return;
        }
        if (mLP.mIsDebug) {
            switch (infoMark) {
                case INFO_MARK_ERROR:
                    Log.e(tag, msg);
                    break;

                case INFO_MARK_INFO:
                    Log.i(tag, msg);
                    break;

                case INFO_MARK_TEMP:
                default:
                    Log.i(tag, msg);
                    //Log.v(tag, msg);
                    break;
            }
        }
    }

    public static class Builder {

        SimpleDateFormat mLogContentSimpleDateFormat;

        private Looper mLooper;

        private boolean mIsDebug;

        private String mHeaderInfo;
        private String mRefreshLogStartTag = "@refresh log start@";
        private String mRefreshLogEndTag = "@refresh log end@";
        private String mLogDir;
        private String mLogFileNamePrefix;

        private int mLogFileMax = 3;
        private int mLogFileBytesThreshold;
        private int mLogInQueueBytesThreshold = 1024 * 2;//Default value is 2 KB.

        public LoggerImpl build() {
            checkParams();
            LoggerImpl localLogger = new LoggerImpl();
            apply(localLogger.mLP);
            return localLogger;
        }

        protected void checkParams() {
            if (!FileUtils.checkDir(new File(mLogDir))) {
                throw new IllegalArgumentException("The log directory cannot be create. log dir : " + mLogDir);
            }
            if (TextUtils.isEmpty(mLogFileNamePrefix)) {
                throw new IllegalArgumentException("The log file name cannot be empty!");
            }
            if (mLogFileMax < 1) {
                throw new IllegalArgumentException("mLogFileMax shouldn't smaller than 1.");
            }
            if (mLogFileBytesThreshold < 1024) {
                throw new IllegalArgumentException("mLogFileBytesThreshold shouldn't smaller than 1KB.");
            }
        }

        protected void apply(LoggerParameters lp) {
            if (mLogContentSimpleDateFormat == null) {
                SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.applyPattern("yyyy-MM-dd HH:mm:ss.sss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                lp.mLogContentSimpleDateFormat = sdf;
            } else {
                lp.mLogContentSimpleDateFormat = mLogContentSimpleDateFormat;
            }

            lp.mLooper = mLooper;
            lp.mLogDir = mLogDir;
            lp.mLogFileNamePrefix = mLogFileNamePrefix;
            lp.mMsgQueue = new LinkedList<>();
            lp.mLogInQueueBytesThreshold = mLogInQueueBytesThreshold;
            lp.mIsDebug = mIsDebug;
            lp.mHeaderInfo = mHeaderInfo;
            lp.mRefreshLogStartTag = mRefreshLogStartTag;
            lp.mRefreshLogEndTag = mRefreshLogEndTag;
            lp.mLogFileMax = mLogFileMax;
            lp.mLogFileBytesThreshold = mLogFileBytesThreshold;
        }

        public void setLogContentSimpleDateFormat(SimpleDateFormat logContentSimpleDateFormat) {
            mLogContentSimpleDateFormat = logContentSimpleDateFormat;
        }

        public Builder setIsDebug(boolean isDebug) {
            mIsDebug = isDebug;
            return this;
        }

        public Builder setHeaderInfo(String headerInfo) {
            mHeaderInfo = headerInfo;
            return this;
        }

        public Builder setRefreshLogStartTag(String refreshLogStartTag) {
            mRefreshLogStartTag = refreshLogStartTag;
            return this;
        }

        public Builder setRefreshLogEndTag(String refreshLogEndTag) {
            mRefreshLogEndTag = refreshLogEndTag;
            return this;
        }

        public Builder setLogDir(String logDir) {
            mLogDir = logDir;
            return this;
        }

        public Builder setLogFileNamePrefix(String logFileNamePrefix) {
            mLogFileNamePrefix = logFileNamePrefix;
            return this;
        }

        public Builder setLogInQueueBytesThreshold(int logInQueueBytesThreshold) {
            mLogInQueueBytesThreshold = logInQueueBytesThreshold;
            return this;
        }

        public Builder setLooper(Looper looper) {
            mLooper = looper;
            return this;
        }

        public Builder setLogFileBytesThreshold(int logFileBytesThreshold) {
            mLogFileBytesThreshold = logFileBytesThreshold;
            return this;
        }

        public Builder setLogFileMax(int logFileMax) {
            mLogFileMax = logFileMax;
            return this;
        }
    }

}
