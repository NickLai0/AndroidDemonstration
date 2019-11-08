package com.log;

import android.content.Context;
import android.os.Looper;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/6/20<br>
 * Time: 10:42<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class BaseParameters {

    Context mContext;

    Looper mLooper;

    boolean mIsDebug;

    boolean mIsStarted;

    public Context getContext() {
        return mContext;
    }

    public Looper getLooper() {
        return mLooper;
    }
}
