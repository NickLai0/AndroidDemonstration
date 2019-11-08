package com.log;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Handler;
import android.os.HandlerThread;

import com.log.interfaces.Logger;
import com.log.listener.OnUploadListener;

import java.util.List;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/6/20<br>
 * Time: 17:00<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class UploadParameters extends BaseParameters {

    List<OnUploadListener> mUploadListenerList;

    HandlerThread mHandlerThread;

    Handler mHandler;

    Logger mLogger;

    boolean mIsDeleteUploadedFile;

    boolean mIsStarted;

    int mCheckIntervalMillis;

    String mCheckLogAction;

    String mUploadDir;

    PendingIntent mPendingIntent;

    AlarmManager mAlarmManager;

    public List<OnUploadListener> getUploadListenerList() {
        return mUploadListenerList;
    }

    public Logger getLogger() {
        return mLogger;
    }

    public boolean isDeleteUploadedFile() {
        return mIsDeleteUploadedFile;
    }

    public boolean isStarted() {
        return mIsStarted;
    }

    public int getCheckIntervalMillis() {
        return mCheckIntervalMillis;
    }

    public String getCheckLogAction() {
        return mCheckLogAction;
    }

    public String getUploadDir() {
        return mUploadDir;
    }

    public AlarmManager getAlarmManager() {
        return mAlarmManager;
    }

}
