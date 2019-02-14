package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.test.R;

import java.util.LinkedList;
import java.util.List;

public class TestAllocMemoryActivity extends BaseActivity implements View.OnClickListener {

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestAllocMemoryActivity.class));
    }

    private TextView mTvMemoryCount;
    private TextView mTvActivityCount;

    private static List sMemoryList = new LinkedList();

    private static int sActivityCount = 0;

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_memory_alloc_test;
    }

    protected void initView() {
        mTvActivityCount = (TextView)findViewById(R.id.amat_tv_activity_count);
        mTvMemoryCount = (TextView)findViewById(R.id.amat_tv_memory_count);
    }

    protected void initData() {
        showHeapMemoryCount();
        showActivityCount(++sActivityCount);
    }

    protected void initListener() {
        mTvMemoryCount.setOnClickListener(this);
        mTvActivityCount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.amat_tv_memory_count:
                sMemoryList.add(new byte[10 * 1024 * 1024]);
                showHeapMemoryCount();
                break;
            case R.id.amat_tv_activity_count:
                startActivity(new Intent(this, TestAllocMemoryActivity.class));
                break;
        }
    }

    private void showHeapMemoryCount() {
        mTvMemoryCount.setText("自己申请的堆内存 : " + sMemoryList.size() * 10 + "MB");
    }

    private void showActivityCount(int activityCount) {
        mTvActivityCount.setText("当前是第" + activityCount + "个activity");
    }
}
