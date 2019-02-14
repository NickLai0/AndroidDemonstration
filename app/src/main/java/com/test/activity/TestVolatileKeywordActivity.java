package com.test.activity;

import android.app.Activity;
import android.content.Intent;

import com.test.R;
import com.test.data.VolatileData;
import com.test.thread.VolatileThread;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2018/12/18<br>
 * Time: 23:46<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestVolatileKeywordActivity extends BaseActivity {

    private volatile int count;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestVolatileKeywordActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_keyword_volatile_test;
    }

    @Override
    protected void initView() {

    }

    private final static int TOTAL_THREADS = 10000;

    @Override
    protected void initData() {
        VolatileData volatileData = new VolatileData();
        Thread[] threads = new Thread[TOTAL_THREADS];
        for (int i = 0; i < TOTAL_THREADS; ++i)
            threads[i] = new VolatileThread(volatileData);

        //Start all reader threads.
        for (int i = 0; i < TOTAL_THREADS; ++i)
            threads[i].start();

        //Wait for all threads to terminate.
//        for (int i = 0; i < TOTAL_THREADS; ++i) {
//            try {
//                threads[i].join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    protected void initListener() {

    }
}
