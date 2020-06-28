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

    SimpleDateFormat mLogContentSimpleDateFormat;

    LinkedList<String> mMsgQueue;

    HandlerThread mLogHandlerThread;

    Handler mLogHandler;

    String mHeaderInfo;
    String mRefreshLogStartTag;
    String mRefreshLogEndTag;
    String mLogDir;
    String mLogFileNamePrefix;

    int mLogInQueueBytesThreshold;
    int mLogInQueueCurrentBytes;
    int mLogFileMax;
    int mLogFileBytesThreshold;

    public long mLogRefreshDeadline;
}
