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
        Random random = new Random(System.currentTimeMillis());
        StringBuffer sb = new StringBuffer(512);
        sb.append("Before quick sort : ").append('{');
        for (int i = 0; i < ints.length; i++) {
            ints[i] = random.nextInt(100);
            sb.append(ints[i]).append(',');
        }
        sb.append('}').append('\n');
        //Sort of the integer array.
        quickSort(ints, 0, ints.length - 1);
        sb.append("After quick sort : ").append('{');
        for (int i = 0; i < ints.length; i++) {
            sb.append(ints[i]).append(',');
        }
        sb.append('}').append('\n');

        mTv.setText(sb.toString());
    }

    @Override
    protected void initListener() {

    }

    private void quickSort(int[] arr, int start, int end) {
        if (start < end) {
            int pi = partition(arr, start, end);
            quickSort(arr, start, pi - 1);
            quickSort(arr, pi + 1, end);
        }
    }

    private int partition(int[] arr, int start, int end) {
        int pivot = arr[end];
        int i = start - 1;
        for (int j = start; j < end; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        i++;
        swap(arr, i, end);
        return i;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
