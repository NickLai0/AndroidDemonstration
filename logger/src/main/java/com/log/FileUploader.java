package com.log;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.CallSuper;
import android.text.TextUtils;

import com.log.interfaces.Logger;
import com.log.interfaces.Uploader;
import com.log.listener.OnUploadListener;
import com.log.util.FileUtils;
import com.log.util.MyArrayList;

import java.io.File;
import java.util.ArrayList;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/6/18<br>
 * Time: 21:17<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class FileUploader implements Uploader {

    protected final String TAG = getClass().getSimpleName();

    private final int MSG_UPLOAD_FILE = 1;

    protected UploadParameters mUP = new UploadParameters();

    protected FileUploader() {
    }

    protected void log(String msg) {
        if (mUP.mLogger != null) {
            mUP.mLogger.logI(TAG, msg);
        }
    }

    protected void logE(String msg) {
        if (mUP.mLogger != null) {
            mUP.mLogger.logE(TAG, msg);
        }
    }

    @Override
    public void notifyUpload(String entrance) {
        log(entrance);
        sendMessage(MSG_UPLOAD_FILE);
    }

    private void sendMessage(int what) {
        if (mUP.mHandler != null && !mUP.mHandler.hasMessages(what)) {
            mUP.mHandler.sendEmptyMessage(what);
        }
    }

    private void initHandler() {
        if (mUP.mHandler != null || mUP.mHandlerThread != null) {
            log("initHandler -> Has already been initialed.");
            return;
        }
        if (mUP.mLooper != null) {
            mUP.mHandler = new UploadHandler(mUP.mLooper);
        } else {
            mUP.mHandlerThread = new HandlerThread(TAG) {
                @Override
                protected void onLooperPrepared() {
                    super.onLooperPrepared();
                    mUP.mHandler = new UploadHandler(getLooper());
                }
            };
            mUP.mHandlerThread.start();
        }
    }

    private class UploadHandler extends Handler {

        public UploadHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPLOAD_FILE:
                    upload("inside handleMessage");
                    break;
            }
        }

    }

    /**
     * Subclass must override this method and
     * implement the check logic.
     */
    @Override
    public void check(String msg) {
        log("check -> " + msg);
        //Start next check alarm.
        startAlarm();
    }

    /**
     * Subclass must override this method and
     * implement the upload logic.
     */
    @CallSuper
    @Override
    public void upload(String entrance) {
        log("upload -> Invoked.entrance : " + entrance);
        publishOnBeforeFileUpload();
    }

    protected File[] getAllowedUploadFiles() {
        File[] files = new File(mUP.mUploadDir).listFiles();
        ArrayList<File> fileList = new ArrayList<>(files.length);
        for (File f : files) {
            if (f == null || !f.exists() || !f.isFile()) {
                continue;
            }
            fileList.add(f);
        }
        return fileList.toArray(new File[fileList.size()]);
    }

    protected void publishOnBeforeFileUpload() {
        for (int i = 0; i < mUP.mUploadListenerList.size(); i++) {
            OnUploadListener l = mUP.mUploadListenerList.get(i);
            if (l != null) {
                l.onBeforeFileUpload();
            }
        }
    }

    protected void publishOnHasNotLogFile() {
        for (int i = 0; i < mUP.mUploadListenerList.size(); i++) {
            OnUploadListener l = mUP.mUploadListenerList.get(i);
            if (l != null) {
                l.onHasNoLogFile();
            }
        }
    }

    protected void publishOnLogFileUploadSuccess(String urlSuffix, String dir, String fileName, int failedCount, int successCount, int total) {
        for (int i = 0; i < mUP.mUploadListenerList.size(); i++) {
            OnUploadListener l = mUP.mUploadListenerList.get(i);
            if (l != null) {
                l.onFileUploadSuccess(urlSuffix,dir, fileName, failedCount, successCount, total);
            }
        }
        if (mUP.mIsDeleteUploadedFile) {
            FileUtils.deleteFile(new File(dir, fileName));
        }
    }

    protected void publishOnLogFileUploadFailed(String dir, String fileName, int failedCount, int successCount, int total) {
        for (int i = 0; i < mUP.mUploadListenerList.size(); i++) {
            OnUploadListener l = mUP.mUploadListenerList.get(i);
            if (l != null) {
                l.onFileUploadFailed(dir, fileName, failedCount, successCount, total);
            }
        }
    }

    @Override
    public void addOnUploadListener(OnUploadListener listener) {
        if (listener != null && !mUP.mUploadListenerList.contains(listener)) {
            mUP.mUploadListenerList.add(listener);
        }
    }

    @Override
    public void removeOnUploadListener(OnUploadListener listener) {
        if (mUP.mUploadListenerList.contains(listener)) {
            mUP.mUploadListenerList.remove(listener);
        }
    }

    @Override
    public synchronized void start() {
        log("start -> Invoked.");
        if (!mUP.mIsStarted) {
            log("start -> start success.");
            initHandler();
            registerReceiver();
            startAlarm();
            mUP.mIsStarted = true;
        }
    }

    @Override
    public synchronized void stop() {
        log("stop -> Invoked.");
        if (mUP.mIsStarted) {
            log("stop -> stop stop.");
            stopHandler();
            unregisterReceiver();
            stopAlarm();
            mUP.mIsStarted = false;
        }
    }

    private void stopHandler() {
        if (mUP.mHandler != null) {
            mUP.mHandler.removeCallbacksAndMessages(null);
            mUP.mHandler = null;
        }
        if (mUP.mHandlerThread != null) {
            mUP.mHandlerThread.quitSafely();
            mUP.mHandlerThread = null;
        }
    }

    private void registerReceiver() {
        log("registerReceiver -> Invoked.");
        if (mUP.mIsStarted) {
            //Has already been started.
            return;
        }
        IntentFilter intentFilter = new IntentFilter(mUP.mCheckLogAction);
        mUP.mContext.registerReceiver(mLogBroadcastReceiver, intentFilter);
        log("registerReceiver -> Receiver registered.");
    }

    private void unregisterReceiver() {
        log("unregisterReceiver -> Invoked.");
        if (mUP.mIsStarted) {
            mUP.mContext.unregisterReceiver(mLogBroadcastReceiver);
            log("unregisterReceiver -> Receiver unregistered.");
        }
    }

    protected void startAlarm() {
        //log("startAlarm -> Invoked.");
        AlarmManager alarmManager = mUP.mAlarmManager;
        if (alarmManager == null) {
            return;
        }
        log("startAlarm -> Set alarm. It would triggered in " + mUP.mCheckIntervalMillis + " milliseconds.");
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + mUP.mCheckIntervalMillis, mUP.mPendingIntent);
    }

    private void stopAlarm() {
        //log("stopAlarm -> Invoked.");
        AlarmManager alarmManager = mUP.mAlarmManager;
        if (alarmManager == null) {
            return;
        }
        log("stopAlarm -> Cancel pending intent.");
        mUP.mAlarmManager.cancel(mUP.mPendingIntent);
    }

    private BroadcastReceiver mLogBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            log("onReceive -> action : " + intent.getAction());
            if (mUP.mCheckLogAction.equals(intent.getAction())) {
                check(TAG + "#onReceive");
            }
        }
    };

    public static class Builder {

        private Context mContext;

        private Logger mLogger;

        private boolean mIsDeleteUploadedFile;

        private int mCheckIntervalMillis;

        private String mUploadDir;

        private Looper mLooper;

        public Uploader build() {
            checkParams();
            FileUploader fileUploader = generateFileUploader();
            apply(fileUploader.mUP);
            return fileUploader;
        }

        protected FileUploader generateFileUploader() {
            return new FileUploader();
        }

        protected void checkParams() {
            if (mContext == null) {
                throw new IllegalArgumentException("Context parameter cannot be null.");
            }
            if (mCheckIntervalMillis <= 0) {
                throw new IllegalArgumentException("Expected > 0 check interval milliseconds.");
            }
            if (TextUtils.isEmpty(mUploadDir) || !new File(mUploadDir).exists()) {
                throw new IllegalArgumentException("Expected exists log directory but the path is empty or the directory doesn't exist.");
            }
        }

        protected void apply(UploadParameters up) {
            up.mContext = mContext.getApplicationContext();
            up.mUploadListenerList = new MyArrayList<>();
            up.mLooper = mLooper;
            up.mLogger = mLogger;
            up.mIsDeleteUploadedFile = mIsDeleteUploadedFile;
            up.mCheckIntervalMillis = mCheckIntervalMillis;
            up.mCheckLogAction = mContext.getPackageName() + ".CHECK_LOG";
            up.mUploadDir = mUploadDir;
            up.mAlarmManager = (AlarmManager) up.mContext.getSystemService(Context.ALARM_SERVICE);
            if (up.mAlarmManager != null) {
                up.mPendingIntent = PendingIntent.getBroadcast(up.mContext, 0, new Intent(up.mCheckLogAction), PendingIntent.FLAG_UPDATE_CURRENT);
            }
        }

        public Builder setContext(Context context) {
            mContext = context;
            return this;
        }

        public Builder setLogger(Logger logger) {
            mLogger = logger;
            return this;
        }

        public Builder setIsDeleteUploadedFile(boolean isDelete) {
            mIsDeleteUploadedFile = isDelete;
            return this;
        }

        public Builder setCheckIntervalMillis(int millis) {
            mCheckIntervalMillis = millis;
            return this;
        }

        public Builder setUploadDir(String uploadDir) {
            mUploadDir = uploadDir;
            return this;
        }

        public Builder setLooper(Looper looper) {
            mLooper = looper;
            return this;
        }
    }

}
