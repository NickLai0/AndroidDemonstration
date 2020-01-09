package com.test.thread;

import android.os.HandlerThread;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/1/7<br>
 * Time: 15:45<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class UnquitableHandlerThread extends HandlerThread {

    public UnquitableHandlerThread(String name) {
        super(name);
    }

    @Override
    public boolean quit() {
        throw new UnsupportedOperationException("Unable to quit this handler thread.");
    }

    @Override
    public boolean quitSafely() {
        throw new UnsupportedOperationException("Unable to quit this handler thread.");
    }

}
