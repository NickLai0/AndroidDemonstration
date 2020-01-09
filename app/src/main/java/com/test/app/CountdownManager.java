package com.test.app;

import android.os.Looper;

import com.cd.CountdownHelper;
import com.cd.CountdownInfo;
import com.cd.OnCountdownListener;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/1/9<br>
 * Time: 17:59<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public final class CountdownManager {

    private static final CountdownManager sfInstance = new CountdownManager();

    public static CountdownManager i() {
        return sfInstance;
    }

    private CountdownHelper mCountdownHelper;

    private CountdownManager() {
    }


    void init(Looper countdownLooper) {
        if (mCountdownHelper == null) {
            mCountdownHelper = new CountdownHelper(countdownLooper);
            mCountdownHelper.init();
        }
    }

    void uninit() {
        if (mCountdownHelper != null) {
            mCountdownHelper.uninit();
            mCountdownHelper = null;
        }
    }

    public CountdownInfo add(long startMillis, long endMillis, int frameFrequency, OnCountdownListener subThreadListener, OnCountdownListener mainThreadListener) {
        return mCountdownHelper.add(startMillis, endMillis, frameFrequency, subThreadListener, mainThreadListener);
    }

    public CountdownInfo add(long startMillis, long endMillis, int frameFrequency, Object tag, OnCountdownListener subThreadListener, OnCountdownListener mainThreadListener) {
        return mCountdownHelper.add(startMillis, endMillis, frameFrequency, tag, subThreadListener, mainThreadListener);
    }

    public boolean remove(CountdownInfo ci) {
        return mCountdownHelper.remove(ci);
    }

    public void removeAll(CountdownInfo ci) {
        mCountdownHelper.removeAll();
    }

}
