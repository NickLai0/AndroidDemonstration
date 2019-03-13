package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.test.R;
import com.test.util.DoubleUtils;
import com.test.util.StringUtils;

import java.math.RoundingMode;

import static com.test.util.StringUtils.doubleToString;

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

    @Override
    protected void initData() {
        test01();
        //for calculate the test01's results
        //calculateTest01();
        //test02();
    }

    private void calculateTest01() {
        int[] a = new int[]{
                1841325, 2342608, 2046301, 2553701, 2197455, 1982720, 1812680, 1929833, 2011206, 2154887
        };
        int[] b = new int[]{
                1361302, 1708121, 1922659, 1967796, 1264200, 1597704, 1695699, 1824992, 1532471, 1583086
        };

        int aSummary = 0;
        int bSummary = 0;

        for (int i = 0; i < a.length; i++) {
            aSummary += a[i];
            bSummary += b[i];
        }

        int result = aSummary - bSummary;
        double ratio = DoubleUtils.divide(result * 1.0, bSummary * 1.0, 6, RoundingMode.HALF_DOWN);

        Log.i(TAG, aSummary + " - " + bSummary + " = " + result + ";  a more " + doubleToString(DoubleUtils.multiply(ratio, 100.0)) + "% than b ");
    }

    private void test02() {
        new Monitor().start();
        for (int i = 0; i < 5; i++) {
            new Counter().start();
        }
    }

    private void test01() {
        new ChangeListener().start();
        new ChangeMaker().start();
    }

    @Override
    protected void initListener() {

    }

    /**
     * volatile key word test summarize.
     * use volatile key word situation:
     * 01:1841325
     * 02:2342608
     * 03:2046301
     * 04:2553701
     * 05:2197455
     * 06:1982720
     * 07:1812680
     * 08:1929833
     * 09:2011206
     * 10:2154887
     * result is (1841325 + 2342608 +2046301 +2553701 + 2197455 +1982720 +1812680 +1929833 + 2011206 +2154887) = 20872716
     * <p>
     * without volatile key word situation:
     * 01:1361302
     * 02:1708121
     * 03:1922659
     * 04:1967796
     * 05:1264200
     * 06:1597704
     * 07:1695699
     * 08:1824992
     * 09:1532471
     * 10:1583086
     * <p>
     * result is（1361302 + 1708121 +1922659 + 1967796 +  1264200 +1597704 +1695699 +1824992 +1532471 +1583086） = 16458030
     * <p>
     * 20872716 - 16458030= 4414686;  a more 26.8239% than b
     *
     * so the summary of volatile is that the volatile key word
     * not guarantee the data correctness.But it more better than
     * not use volatile.
     */
    private volatile int mCount = 0;

    private class Counter extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                mCount++;
            }
        }
    }

    private final class Monitor extends Thread {
        @Override
        public void run() {
            while (!isFinishing()) {
                Log.i(TAG, "--mCount now : " + mCount);
            }
        }
    }

    private volatile static int MY_INT = 0;

    static class ChangeListener extends Thread {
        @Override
        public void run() {
            int local_value = MY_INT;
            while (local_value < 10) {
                if (local_value != MY_INT) {
                    Log.i(getClass().getSimpleName(), StringUtils.format("Got Change for MY_INT : %d", MY_INT));
                    local_value = MY_INT;
                }
            }
        }
    }

    static class ChangeMaker extends Thread {
        @Override
        public void run() {

            int local_value = MY_INT;
            while (MY_INT < 10) {
                Log.i(getClass().getSimpleName(), StringUtils.format("Incrementing MY_INT to %d", local_value + 1));
                MY_INT = ++local_value;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
