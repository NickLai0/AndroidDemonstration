package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.test.R;

import static java.lang.Thread.sleep;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2018/12/19<br>
 * Time: 1:54<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestSynchronizedKeywordActivity extends BaseActivity {

    public static final String TAG = TestSynchronizedKeywordActivity.class.getSimpleName();

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestSynchronizedKeywordActivity.class));
    }

    private int mCount;

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_keyword_synchronized_test;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        new Thread() {

            @Override
            public void run() {
                try {
                    synchronized (TestSynchronizedKeywordActivity.class) {
                        while (true) {
                            if (++mCount >= 2) {
                                throw new RuntimeException("Count is " + mCount + " so I wanna throw an Exception.");
                            }
                            sleep(1000);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Caught a exception. it means through synchronized code block. msg : " + e.toString());
                }
            }
        }.start();

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    synchronized (TestSynchronizedKeywordActivity.class) {
                        Log.i(TAG, "the count is " + mCount);
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    @Override
    protected void initListener() {

    }
}
