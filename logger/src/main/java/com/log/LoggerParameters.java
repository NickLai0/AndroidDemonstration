package com.log;

import android.os.Handler;
import android.os.HandlerThread;

import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/6/20<br>
 * Time: 10:39<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class LoggerParameters extends BaseParameters {

    static final String ZIP_FILE_RULE = "_zip_";

    SimpleDateFormat mLogFileNameSimpleDateFormat;
    SimpleDateFormat mLogContentSimpleDateFormat;

    LinkedList<String> mMsgQueue;

    HandlerThread mLogHandlerThread;

    Handler mLogHandler;

    boolean mIsOpenInsideLog;
    boolean mIsRecordRefreshLogStartTag;
    boolean mIsRecordRefreshLogEndTag;

    String mHeaderInfo;
    String mRefreshLogStartTag;
    String mRefreshLogEndTag;
    String mLogDir;
    String mLogFileName;
    String mZipLogDir;

    int mLogRefreshBytes;
    int mZipLogBytes;
    int mMaxZips;

    public SimpleDateFormat getLogFileNameSimpleDateFormat() {
        return mLogFileNameSimpleDateFormat;
    }

    public SimpleDateFormat getLogContentSimpleDateFormat() {
        return mLogContentSimpleDateFormat;
    }

    public LinkedList<String> getMsgQueue() {
        return mMsgQueue;
    }

    public HandlerThread getLogHandlerThread() {
        return mLogHandlerThread;
    }

    public Handler getLogHandler() {
        return mLogHandler;
    }

    public boolean isOpenInsideLog() {
        return mIsOpenInsideLog;
    }

    public boolean isRecordRefreshLogStartTag() {
        return mIsRecordRefreshLogStartTag;
    }

    public boolean isRecordRefreshLogEndTag() {
        return mIsRecordRefreshLogEndTag;
    }

    public String getHeaderInfo() {
        return mHeaderInfo;
    }

    public String getRefreshLogStartTag() {
        return mRefreshLogStartTag;
    }

    public String getRefreshLogEndTag() {
        return mRefreshLogEndTag;
    }

    public String getLogDir() {
        return mLogDir;
    }

    public String getLogFileName() {
        return mLogFileName;
    }

    public String getZipLogDir() {
        return mZipLogDir;
    }

    public int getLogRefreshBytes() {
        return mLogRefreshBytes;
    }

    public int getZipLogBytes() {
        return mZipLogBytes;
    }

    public int getMaxZips() {
        return mMaxZips;
    }
}
