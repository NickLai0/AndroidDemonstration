package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.test.R;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2018/12/18<br>
 * Time: 10:41<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestReentrantLockActivity extends BaseActivity {

    private static final String TAG = TestReentrantLockActivity.class.getSimpleName();

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestReentrantLockActivity.class));
    }

    private ReentrantLock mReentrantLock = new ReentrantLock();

    private int mCount = 0;

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_lock_reentrant_test;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        new Thread("Thread A") {
            @Override
            public void run() {
                try {
                    while (true) {
                        mReentrantLock.lock();
                        Log.i(TAG, getName() + " locking this thread.");
                        if (++mCount > 10) {
                            Log.i(TAG, "Thrown an Exception that it'll skip unlock step.");
                            throw new RuntimeException("Count's value is ten. so throw a RuntimeException for test ReentrantLock");
                        }
                        mReentrantLock.unlock();
                        Log.i(TAG, getName() + " unlocked this thread and sleep a second and the count is " + mCount + " right now.");
                        sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread("Thread B") {
            @Override
            public void run() {
                try {
                    while (true) {
                        mReentrantLock.lock();
                        Log.i(TAG, getName() + " locking this thread.");
                        mReentrantLock.unlock();
                        Log.i(TAG, getName() + " unlocked this thread and sleep a second and the count is " + (++mCount) + " right now.");
                        sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void initListener() {

    }

}
