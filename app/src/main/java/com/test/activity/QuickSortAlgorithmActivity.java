package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import com.test.R;

import java.util.Random;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/3/13<br>
 * Time: 12:12<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class QuickSortAlgorithmActivity extends BaseActivity {

    private TextView mTv;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, QuickSortAlgorithmActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_algorithm_sort_quick;
    }

    @Override
    protected void initView() {
        mTv = (TextView) findViewById(R.id.aasq_tv);
    }

    @Override
    protected void initData() {
        final int SIZE = 10;
        int[] ints = new int[SIZE];
        Random random = new Random(31);
        StringBuffer sb = new StringBuffer(512);
        sb.append("Before sort : ").append('{');
        for (int i = 0; i < ints.length; i++) {
            ints[i] = random.nextInt(100);
            sb.append(ints[i]).append(',');
        }
        sb.append('}').append('\n');
        //Sort of the integer array.
        quickSort(ints, 0, ints.length - 1);
        sb.append("After sort : ").append('{');
        for (int i = 0; i < ints.length; i++) {
            sb.append(ints[i]).append(',');
        }
        sb.append('}').append('\n');

        mTv.setText(sb.toString());
    }

    @Override
    protected void initListener() {

    }

    private void quickSort(int[] ints, int start, int end) {
        //todo:xxx
    }

}
