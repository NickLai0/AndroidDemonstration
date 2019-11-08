package com.log;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.log.interfaces.Logger;
import com.log.util.ExceptionUtils;
import com.log.util.FileUtils;
import com.log.util.ZipUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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

    private final int MSG_CHECK_LOG = 1;
    private final int MSG_NOTIFY_REFRESH_LOG = 2;

    protected LoggerParameters mLP = new LoggerParameters();

    @Override
    public synchronized void start() {
        logInside(TAG, "Start -> Invoke start method by outside.");
        if (mLP.mIsStarted) {
            logInside(TAG, "Start -> Invoke start method by outside.But has already started.");
            return;
        }
        mLP.mIsStarted = true;
        if (mLP.mLooper != null) {
            mLP.mLogHandler = new LogHandler(mLP.mLooper);
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
                case MSG_CHECK_LOG:
                    checkLog();
                    break;

                case MSG_NOTIFY_REFRESH_LOG:
                    refreshLog();
                    boolean isZipSuccessful = zipLog(true);
                    if (isZipSuccessful) {
                        deleteLogFileIfOutOfLimited();
                        deleteOldestZipIfOutOfLimited();
                    }
                    break;
            }
        }
    }

    protected boolean zipLog(boolean isCareLogSize) {
//        if (mLP.mMaxZips < 1) {
//            logInside(TAG, "zipLog -> Max zips smaller than 1");
//            return;
//        }
        boolean isLogZipSuccessful = false;
        boolean needZip = true;
        File logFile = getLogFile();
        if (isCareLogSize) {
            needZip = logFile.length() != 0 && logFile.length() >= mLP.mZipLogBytes;
        }
        if (needZip) {
            try {
                zipFile(logFile, genZipFileName(logFile.getName()));
                isLogZipSuccessful = true;
            } catch (Exception e) {
                logInside(TAG, ExceptionUtils.getStackTrace(e));
            }
        }
        return isLogZipSuccessful;
    }

    protected void zipFile(File srcLogFile, String destZipName) throws Exception {
        ZipUtils.zipFile(srcLogFile, new File(mLP.mZipLogDir, destZipName));
    }

    protected void zipDir(String dir, String destZipName) throws Exception {
        ZipUtils.zipFolder(dir, mLP.mZipLogDir + File.separator + destZipName + ".zip");
    }

    protected String genLogFileName(String prefix) {
        return prefix + mLP.mLogFileNameSimpleDateFormat.format(new Date());
    }

    protected String genZipFileName(String prefix) {
        return prefix + LoggerParameters.ZIP_FILE_RULE + mLP.mLogFileNameSimpleDateFormat.format(new Date());
    }

    protected void deleteLogFileIfOutOfLimited() {
        File logFile = getLogFile();
        if (logFile.length() >= mLP.mZipLogBytes) {
            boolean isDeleteFileSuccessful = FileUtils.deleteFile(logFile);
            logInside(TAG, "deleteLogFileIfOutOfLimited -> isDeleteFileSuccessful : " + isDeleteFileSuccessful);
        }
    }

    protected File[] getZips() {
        return new File(mLP.mZipLogDir).listFiles();
    }

    protected void deleteOldestZipIfOutOfLimited() {
        File[] files = getZips();
        if (files == null || files.length == 0) {
            logInside(TAG, "deleteOldestZipIfOutOfLimited -> Files are empty. directory path : " + mLP.mZipLogDir);
            return;
        }
        List<File> fileList = new ArrayList<>(files.length);
        //Filter my zip files.
        for (File f : files) {
            if (f == null || !f.isFile()) {
                continue;
            }
            if (isMyZip(f)) {
                fileList.add(f);
            }
        }

        for (File oldestMultipleZip; (oldestMultipleZip = findOlderMultipleZip(fileList)) != null; ) {
            boolean isDeleted = FileUtils.deleteFile(oldestMultipleZip);
            logInside(TAG, "deleteOldestZipIfOutOfLimited ->  delete multiple zip. file name : " + oldestMultipleZip.getName() + ", is deleted : " + isDeleted);
        }

        while (fileList.size() != 0 && fileList.size() > mLP.mMaxZips) {
            File oldestZip = findMyOldestZip(fileList);
            if (oldestZip == null) {
                logE(TAG, "deleteOldestZipIfOutOfLimited -> cannot find the oldest zip! files : " + getAllOfPath(fileList));
                break;
            }
            FileUtils.deleteFile(oldestZip);
            fileList.remove(oldestZip);
        }

    }

    private String getAllOfPath(List<File> fileList) {
        if (fileList != null && fileList.size() > 0) {
            StringBuilder sb = new StringBuilder(fileList.size() * 32);
            final String SEPARATOR = "@:@";
            for (File f : fileList) {
                if (f == null) {
                    sb.append(f);
                } else {
                    sb.append(f.getAbsolutePath());
                }
                sb.append(SEPARATOR);
            }
            sb.setLength(sb.length() - SEPARATOR.length());
            return sb.toString();
        }
        return null;
    }

    private File findOlderMultipleZip(List<File> fileList) {
        if (fileList == null || fileList.size() == 0) {
            return null;
        }
        for (int i = 0; i < fileList.size() - 1; i++) {
            File zipFile = fileList.get(i);
            String zipPrefix = getZipFilePrefix(zipFile);
            if (TextUtils.isEmpty(zipPrefix)) {
                //Exception situation.
                logI(TAG, "findOlderMultipleZip -> Excepted zip file. zip file : " + fileList.get(i));
                continue;
            }
            for (int j = i + 1; j < fileList.size(); j++) {
                File anotherZipFile = fileList.get(j);
                String anotherZipPrefix = getZipFilePrefix(anotherZipFile);
                if (zipPrefix.equals(anotherZipPrefix)) {
                    List<File> multipleZipList = new ArrayList<>();
                    multipleZipList.add(zipFile);
                    multipleZipList.add(anotherZipFile);
                    logI(TAG, "findOlderMultipleZip -> found the same prefix files. zipPrefix : " + zipPrefix + ", anotherZipFile : " + anotherZipFile);
                    return findMyOldestZip(multipleZipList);
                }
            }
        }

        return null;
    }

    private String getZipFilePrefix(File zipFile) {
        if (zipFile != null && zipFile.isFile()) {
            String[] prefixAndSuffix = zipFile.getName().split(LoggerParameters.ZIP_FILE_RULE);
            if (prefixAndSuffix != null && prefixAndSuffix.length == 2) {
                return prefixAndSuffix[0];
            }
        }
        return null;
    }

    private File findMyOldestZip(List<File> fileList) {
        if (fileList == null || fileList.size() == 0) {
            return null;
        }
        File oldestZip = null;
        for (int i = 0; i < fileList.size(); i++) {
            if ((oldestZip = fileList.get(i)) != null) {
                break;
            }
        }
        if (oldestZip != null) {
            for (int i = 0; i < fileList.size(); i++) {
                File tempFile = fileList.get(i);
                if (tempFile == null) {
                    continue;
                }
                String oldestZipName = oldestZip.getName();
                String tempZipName = tempFile.getName();
                if (oldestZipName.compareTo(tempZipName) > 0) {
                    //Save the oldest zip file.
                    oldestZip = tempFile;
                }
            }
        }
        return oldestZip;
    }

    protected boolean isMyZip(File f) {
        boolean isMyZip = false;
        if (f != null) {
            String fileName = f.getName();
            isMyZip = !TextUtils.isEmpty(fileName) && fileName.contains(LoggerParameters.ZIP_FILE_RULE);
        }
        return isMyZip;
    }

    @Override
    public synchronized void stop() {
        if (!mLP.mIsStarted) {
            logInside(TAG, "stop -> Haven't started yet.Do nothing.");
            return;
        }
        mLP.mIsStarted = false;
        logInside(TAG, "stop -> Invoke stop method by outside.");
        if (mLP.mLogHandler != null) {
            logInside(TAG, "stop -> remove all of messages.");
            mLP.mLogHandler.removeCallbacksAndMessages(null);
            mLP.mLogHandler = null;
        }
        if (mLP.mLogHandlerThread != null) {
            logInside(TAG, "stop -> quit handler thread.");
            mLP.mLogHandlerThread.quit();
            mLP.mLogHandlerThread = null;
        }
    }

    /**
     * For modify logging permission dynamically.
     * It's just like a secret door.
     *
     * @return
     */
    protected boolean isOpenLoggingForce() {
        return false;
    }

    private void notifyCheckLog() {
        if (mLP.mLogHandler == null) {
            logInside(TAG, "checkLog -> checkLog method invoked but the log handler haven't initiated yet!");
        } else {
            if (!mLP.mLogHandler.hasMessages(MSG_CHECK_LOG)) {
                mLP.mLogHandler.sendEmptyMessage(MSG_CHECK_LOG);
            }
        }
    }

    private void checkLog() {
        int logSize = 0;
        try {
            synchronized (mLP.mMsgQueue) {
                for (String msg : mLP.mMsgQueue) {
                    logSize += msg == null ? 0 : msg.length();
                }
            }
        } catch (Exception e) { //avoid ConcurrentModifyException.
            logInside(TAG, ExceptionUtils.getStackTrace(e));
        }
        if (logSize >= mLP.mLogRefreshBytes) {
            notifyRefreshLog();
        }
    }


    @Override
    public void notifyRefreshLog() {
        if (mLP.mLogHandler == null) {
            logInside(TAG, "notifyRefreshLog -> notifyRefreshLog method invoked but the log handler haven't initiated yet!");
        } else {
            if (!mLP.mLogHandler.hasMessages(MSG_NOTIFY_REFRESH_LOG)) {
                mLP.mLogHandler.sendEmptyMessage(MSG_NOTIFY_REFRESH_LOG);
            }
        }
    }

    @Override
    public void refreshLog() {
        File logFile = null;
        synchronized (mLP.mMsgQueue) {
            if (mLP.mMsgQueue.isEmpty()) {
                //Ignore this event, because there is no any log.
                return;
            }
            logFile = getLogFile();
            if (!logFile.exists()) {
                FileUtils.createNewFile(logFile);
                if (!TextUtils.isEmpty(mLP.mHeaderInfo)) {
                    mLP.mMsgQueue.addFirst(mLP.mHeaderInfo);
                }
            }
        }
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new FileOutputStream(logFile, true));
            if (mLP.mIsRecordRefreshLogStartTag) {
                printWriter.println(mLP.mRefreshLogStartTag);
            }
            String log = null;
            while ((log = msgDequeue()) != null) {
                printWriter.println(log);
            }
            if (mLP.mIsRecordRefreshLogEndTag) {
                printWriter.println(mLP.mRefreshLogEndTag);
            }
            printWriter.flush();
        } catch (Exception e) {
            logInside(TAG, ExceptionUtils.getStackTrace(e));
        } finally {
            FileUtils.close(printWriter);
        }
    }

    private File getLogFile() {
        File logDir = new File(mLP.mLogDir);
        FileUtils.checkDir(logDir);
        final String fLogFileName = mLP.mLogFileName;

        File[] mLogFiles = logDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                boolean isMyLog = !TextUtils.isEmpty(name) && name.startsWith(fLogFileName);
                //logInside(TAG, "getLogFile - accept name : " + name + ", logFileName : " + fLogFileName + ", isMyLog : " + isMyLog);
                return isMyLog;
            }
        });

        File myLogFile = null;
        if (mLogFiles != null && mLogFiles.length > 0) {
            for (File logFile : mLogFiles) {
                if (logFile != null && logFile.isFile()) {
                    myLogFile = logFile;
                    //logInside(TAG, "getLogFile -> Get the old log file.My old log file name ：" + myLogFile.getName());
                    break;
                }
            }
        }

        //If myLogFile is still null, It mean's that it's first time to get the myLogFile.
        if (myLogFile == null) {
            myLogFile = new File(logDir, genLogFileName(fLogFileName));
            logInside(TAG, "getLogFile -> There is no older log file. So create a new one; New log's file name ：" + myLogFile.getName());
        }
        return myLogFile;
    }

    protected void logInside(String tag, String msg) {
        if (mLP.mIsOpenInsideLog) {
            //Just let the message enqueue here.
            msgEnqueue("[inside]", tag, msg);
        }
    }

    @Override
    public void logMethodStackTrack(String tag, String msg) {
        logException(tag, new RuntimeException(msg));
    }

    @Override
    public void logException(String tag, Exception exception) {
        log("[exception]", tag, ExceptionUtils.getStackTrace(exception), true);
    }

    @Override
    public void logE(String tag, String msg) {
        log("[error]", tag, msg, true);
    }

    @Override
    public void logT(String tag, String msg) {
        if (mLP.mIsDebug || isOpenLoggingForce()) {
            log("[temp]", tag, msg, false);
        }
    }

    @Override
    public void logI(String tag, String msg) {
        log("", tag, msg, false);
    }

    private void log(String infoMark, String tag, String msg, boolean notifyRefreshLogImmediately) {
        if (mLP.mIsDebug || isOpenLoggingForce()) {
            Log.i(tag, msg);
        }
        msgEnqueue(infoMark, tag, msg);
        if (notifyRefreshLogImmediately) {
            notifyRefreshLog();
        } else {
            notifyCheckLog();
        }
    }

    private void msgEnqueue(String infoMark, String tag, String msg) {
        String date = mLP.mLogContentSimpleDateFormat.format(new Date());
        String formattedMsg = null;
        if (TextUtils.isEmpty(infoMark)) {
            formattedMsg = String.format(Locale.getDefault(), "%s %s: %s", date, tag, msg);
        } else {
            formattedMsg = String.format(Locale.getDefault(), "%s %s %s: %s", date, infoMark, tag, msg);
        }
        synchronized (mLP.mMsgQueue) {
            mLP.mMsgQueue.addLast(formattedMsg);
        }
    }

    private String msgDequeue() {
        synchronized (mLP.mMsgQueue) {
            return mLP.mMsgQueue.pollFirst();
        }
    }

    public static class Builder {

        private Context mContext;
        private Looper mLooper;

        SimpleDateFormat mLogFileNameSimpleDateFormat;
        SimpleDateFormat mLogContentSimpleDateFormat;

        private boolean mIsOpenInsideLog = true;
        private boolean mIsDebug;
        private boolean mIsRecordRefreshLogStartTag;
        private boolean mIsRecordRefreshLogEndTag;

        private String mHeaderInfo;
        private String mRefreshLogStartTag = "@refresh log start@";
        private String mRefreshLogEndTag = "@refresh log end@";
        private String mLogDir;
        private String mLogFileName;
        private String mZipLogDir;

        private int mLogRefreshBytes = 1024 * 2;//Default value is 2 KB.
        private int mZipLogBytes = 1024 * 1024 * 8; //Default value is 8 MB.
        private int mMaxZips = 2;

        public Logger build() {
            checkParams();
            LoggerImpl localLogger = generateLogger();
            apply(localLogger.mLP);
            return localLogger;
        }

        /**
         * Lets the subclass can reuse parent's code.
         *
         * @return LoggerImpl object or LoggerImpl's children.
         */
        protected LoggerImpl generateLogger() {
            return new LoggerImpl();
        }

        protected void checkParams() {
            if (mContext == null) {
                throw new NullPointerException("The context object can not be null!");
            }
            if (TextUtils.isEmpty(mLogDir)) {
                throw new IllegalArgumentException("The log directory path cannot be empty.");
            } else if (!FileUtils.checkDir(new File(mLogDir))) {
                throw new IllegalArgumentException("The log directory cannot be create. log dir : " + mLogDir);
            }
            if (TextUtils.isEmpty(mLogFileName)) {
                throw new IllegalArgumentException("The log file name cannot be empty!");
            }
            if (TextUtils.isEmpty(mZipLogDir)) {
                throw new IllegalArgumentException("The zip directory path cannot be empty.");
            } else if (!FileUtils.checkDir(new File(mZipLogDir))) {
                throw new IllegalArgumentException("The zip directory cannot be create. zip dir : " + mZipLogDir);
            }
        }

        protected void apply(LoggerParameters lp) {
            lp.mContext = mContext;

            if (mLogFileNameSimpleDateFormat == null) {
                SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.applyPattern("yyyy-MM-dd_HH-mm-ss-sss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                lp.mLogFileNameSimpleDateFormat = sdf;
            } else {
                lp.mLogFileNameSimpleDateFormat = mLogFileNameSimpleDateFormat;
            }

            if (mLogContentSimpleDateFormat == null) {
                SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.applyPattern("MM-dd HH:mm:ss.sss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                lp.mLogContentSimpleDateFormat = sdf;
            } else {
                lp.mLogContentSimpleDateFormat = mLogContentSimpleDateFormat;
            }

            //lp.mMsgQueue = new MyLinkedList<>();
            lp.mMsgQueue = new LinkedList<>();
            lp.mLooper = mLooper;
            lp.mIsOpenInsideLog = mIsOpenInsideLog;
            lp.mIsDebug = mIsDebug;
            lp.mIsRecordRefreshLogStartTag = mIsRecordRefreshLogStartTag;
            lp.mIsRecordRefreshLogEndTag = mIsRecordRefreshLogEndTag;
            lp.mHeaderInfo = mHeaderInfo;
            lp.mRefreshLogStartTag = mRefreshLogStartTag;
            lp.mRefreshLogEndTag = mRefreshLogEndTag;
            lp.mLogDir = mLogDir;
            lp.mLogFileName = mLogFileName;
            lp.mZipLogDir = mZipLogDir;
            lp.mZipLogBytes = mZipLogBytes;
            lp.mLogRefreshBytes = mLogRefreshBytes;
            lp.mMaxZips = mMaxZips;
        }

        public void setLogFileNameSimpleDateFormat(SimpleDateFormat logFileNameSimpleDateFormat) {
            mLogFileNameSimpleDateFormat = logFileNameSimpleDateFormat;
        }

        public void setLogContentSimpleDateFormat(SimpleDateFormat logContentSimpleDateFormat) {
            mLogContentSimpleDateFormat = logContentSimpleDateFormat;
        }

        public Builder setOpenInsideLog(boolean openInsideLog) {
            mIsOpenInsideLog = openInsideLog;
            return this;
        }

        public Builder setIsDebug(boolean isDebug) {
            mIsDebug = isDebug;
            return this;
        }

        public Builder setIsRecordRefreshLogStartTag(boolean recordRefreshLogStartTag) {
            mIsRecordRefreshLogStartTag = recordRefreshLogStartTag;
            return this;
        }

        public Builder setIsRecordRefreshLogEndTag(boolean recordRefreshLogEndTag) {
            mIsRecordRefreshLogEndTag = recordRefreshLogEndTag;
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

        public Builder setLogFileName(String logFileName) {
            mLogFileName = logFileName;
            return this;
        }

        public Builder setZipLogDir(String zipLogDir) {
            mZipLogDir = zipLogDir;
            return this;
        }

        public Builder setLogRefreshBytes(int logRefreshBytes) {
            mLogRefreshBytes = logRefreshBytes;
            return this;
        }

        public Builder setZipLogBytes(int zipLogBytes) {
            mZipLogBytes = zipLogBytes;
            return this;
        }

        public Builder setMaxZips(int maxZips) {
            mMaxZips = maxZips;
            return this;
        }

        public Builder setLooper(Looper looper) {
            mLooper = looper;
            return this;
        }

        public Builder setContext(Context c) {
            mContext = c;
            return this;
        }

    }

}
