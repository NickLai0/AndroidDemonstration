package com.cd;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/1/9<br>
 * Time: 11:52<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class CountdownHelper {

    private Looper mSubThreadLooper;

    private Handler mCountdownHandler;
    private Handler mMainThreadHandler;

    private HandlerThread mCountdownHandlerThread;

    private List<CountdownInfo> mCountdownInfoList = new ArrayList();

    private boolean mInitialized;

    public CountdownHelper() {
        this(null);
    }

    public CountdownHelper(Looper subThreadLooper) {
        mSubThreadLooper = subThreadLooper;
    }

    public void init() {
        if (mInitialized) {
            return;
        }

        Looper subThreadLooper = mSubThreadLooper;
        if (subThreadLooper == null) {
            mCountdownHandlerThread = new HandlerThread("countdown thread");
            mCountdownHandlerThread.start();
            subThreadLooper = mCountdownHandlerThread.getLooper();
        }

        mCountdownHandler = new Handler(subThreadLooper) {
            @Override
            public void handleMessage(Message msg) {
                handleCountdownMessage(msg);
            }
        };

        mMainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                handleMainThreadMessage(msg);
            }
        };

        mInitialized = true;
    }

    public void uninit() {
        if (mInitialized) {
            if (mCountdownHandlerThread != null) {
                mCountdownHandlerThread.quit();
                mCountdownHandlerThread = null;
            }
            removeAll();
            mCountdownHandler = null;
            mMainThreadHandler.removeCallbacksAndMessages(null);
            mInitialized = false;
        }
    }

    private void handleCountdownMessage(Message msg) {
        for (int i = 0; i < mCountdownInfoList.size(); i++) {
            CountdownInfo ci = mCountdownInfoList.get(i);
            long currentMillis = System.currentTimeMillis();
            int lastFrameCount = ci.frameCount;
            if (currentMillis >= ci.endMillis) {
                ci.spentMillis = ci.duration;
                ci.spentMillisPercent = 1;
                ci.frameCount++;
            } else {
                ci.spentMillis = currentMillis - ci.startMillis;
                ci.spentMillisPercent = ci.spentMillis * 1.0 / ci.duration;
                ci.frameCount = (int) (ci.spentMillis / ci.frameFrequency);
            }

            if (lastFrameCount == ci.frameCount) {
                continue;
            }

            if (ci.subThreadListener != null) {
                ci.subThreadListener.onCountdownUpdate(ci);
            }

            if (ci.mainThreadListener != null) {
                Message.obtain(mMainThreadHandler, 0, ci).sendToTarget();
            }

            if (ci.spentMillisPercent == 1) {
                //The countdown ended.
                mCountdownInfoList.remove(ci);
                i--;
            }
        }
        if (mCountdownInfoList.size() > 0) {
            mCountdownHandler.sendEmptyMessage(0);
        }
    }

    private void handleMainThreadMessage(Message msg) {
        CountdownInfo ci = (CountdownInfo) msg.obj;
        if (ci.mainThreadListener != null) {
            ci.mainThreadListener.onCountdownUpdate(ci);
        }
    }

    public CountdownInfo add(long startMillis, long endMillis, int frameFrequency, OnCountdownListener subThreadListener, OnCountdownListener mainThreadListener) {
        return add(startMillis, endMillis, frameFrequency, null, subThreadListener, mainThreadListener);
    }

    public CountdownInfo add(long startMillis, long endMillis, int frameFrequency, Object tag, OnCountdownListener subThreadListener, OnCountdownListener mainThreadListener) {
        check();
        if (subThreadListener == null && mainThreadListener == null) {
            throw new IllegalArgumentException("Either subThreadListener and mainThreadListener can not be null.");
        }
        CountdownInfo ci = new CountdownInfo();
        ci.startMillis = startMillis;
        ci.endMillis = endMillis;
        ci.duration = endMillis - startMillis;
        ci.frameFrequency = frameFrequency;
        ci.tag = tag;
        ci.subThreadListener = subThreadListener;
        ci.mainThreadListener = mainThreadListener;

        mCountdownInfoList.add(ci);

        mCountdownHandler.removeCallbacksAndMessages(null);
        mCountdownHandler.sendEmptyMessage(0);
        return ci;
    }

    public boolean remove(CountdownInfo ci) {
        check();
        return mCountdownInfoList.remove(ci);
    }

    public void removeAll() {
        check();
        mCountdownInfoList.clear();
    }

    private void check() {
        if (mCountdownHandler == null) {
            throw new IllegalStateException("Please invoke init() method first.");
        }
    }

}
