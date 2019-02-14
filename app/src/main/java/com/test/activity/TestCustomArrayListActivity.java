package com.test.activity;

import android.app.Activity;
import android.content.Intent;

import com.test.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2018/12/19<br>
 * Time: 1:14<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestCustomArrayListActivity extends BaseActivity {

    private List mList = new MyArrayList<>();

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestCustomArrayListActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_list_array_custom_test;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (!mList.contains(TestCustomArrayListActivity.this)) {
                        mList.add(TestCustomArrayListActivity.this);
                        /*
                         *When getting last element that I just added
                         *but another thread delete this element, then
                         *get logic would throw an IndexOutOfBoundsException.
                         *It's from ArrayList.
                         *
                         *
                         * But if you use MyArrayList it would have no
                         * throw an IndexOutOfBoundsException problem.
                         */
                        mList.get(0);
                    }
                }

            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (mList.contains(TestCustomArrayListActivity.this)) {
                        mList.remove(TestCustomArrayListActivity.this);
                    }
                }

            }
        }.start();

    }

    @Override
    protected void initListener() {

    }

    public class MyArrayList<E> extends ArrayList<E> {

        @Override
        public E get(int index) {
            try {
                return super.get(index);
            } catch (Exception e) {
                return null;
            }
        }

    }

}
