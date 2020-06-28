package com.test.handler;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/6/22<br>
 * Time: 19:47<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class WeakHandler extends Handler {

    final public void handleMessage(Message msg) {
        IMessageHandler iMessageHandler = getMessageHandler();
        if (iMessageHandler != null) {
            iMessageHandler.handleMessage(msg);
        }
    }

    private IMessageHandler getMessageHandler() {
        return mWeakReference == null ? null : mWeakReference.get();
    }

    private WeakReference<IMessageHandler> mWeakReference;

    public void setMessageHandler(@Nullable IMessageHandler handler) {
        if (handler == null) {
            mWeakReference = null;
        } else {
            mWeakReference = new WeakReference<>(handler);
        }
    }

    public interface IMessageHandler {
        void handleMessage(Message msg);
    }

}
