package com.test.activity;

import android.view.View;
import android.widget.TextView;

import com.test.R;

public class MainActivity extends BaseActivity {

    private TextView mTvToTestMemoryActivity;
    private TextView mTvToTestTemperatureSensorActivity;
    private TextView mTvToTestReentrantLock;
    private TextView mTvToTestSynchronizedKeyword;
    private TextView mTvToTestVolatileKeyword;
    private TextView mTvToTestCustomArrayList;
    private TextView mTvToTestCollapsingToolbarLayout;
    private TextView mTvToTestCollapsingToolbarLayoutTwo;
    private TextView mTvToRecordVideo;
    private TextView mTvToCameraOpenOrClose;
    private TextView mTvPringDoubleDataType;

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mTvToTestMemoryActivity = (TextView) findViewById(R.id.am_tv_to_test_alloc_memory_activity);
        mTvToTestTemperatureSensorActivity = (TextView) findViewById(R.id.am_tv_to_test_temperature_sensor_activity);
        mTvToTestReentrantLock = (TextView) findViewById(R.id.am_tv_to_test_reentrant_lock);
        mTvToTestSynchronizedKeyword = (TextView) findViewById(R.id.am_tv_to_test_synchronized_keyword);
        mTvToTestVolatileKeyword = (TextView) findViewById(R.id.am_tv_to_test_volatile_keyword);
        mTvToTestCustomArrayList = (TextView) findViewById(R.id.am_tv_to_test_custom_array_list);
        mTvToTestCollapsingToolbarLayout = (TextView) findViewById(R.id.am_tv_to_test_collapsing_toolbar_layout);
        mTvToTestCollapsingToolbarLayoutTwo = (TextView) findViewById(R.id.am_tv_to_test_collapsing_toolbar_layout_two);
        mTvToRecordVideo = (TextView) findViewById(R.id.am_tv_to_record_video);
        mTvToCameraOpenOrClose = (TextView) findViewById(R.id.am_tv_to_camera_open_or_close);
        mTvPringDoubleDataType = (TextView) findViewById(R.id.am_tv_print_double_data_type);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mTvToTestMemoryActivity.setOnClickListener(this);
        mTvToTestTemperatureSensorActivity.setOnClickListener(this);
        mTvToTestReentrantLock.setOnClickListener(this);
        mTvToTestSynchronizedKeyword.setOnClickListener(this);
        mTvToTestVolatileKeyword.setOnClickListener(this);
        mTvToTestCustomArrayList.setOnClickListener(this);
        mTvToTestCollapsingToolbarLayout.setOnClickListener(this);
        mTvToTestCollapsingToolbarLayoutTwo.setOnClickListener(this);
        mTvToRecordVideo.setOnClickListener(this);
        mTvToCameraOpenOrClose.setOnClickListener(this);
        mTvPringDoubleDataType.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.am_tv_print_double_data_type:
                PrintDoubleDataTypeActivity.start(this);
                break;

            case R.id.am_tv_to_camera_open_or_close:
                CameraOpenOrCloseActivity.start(this);
                break;

            case R.id.am_tv_to_record_video:
                RecordVideoActivity.start(this);
                break;

            case R.id.am_tv_to_test_alloc_memory_activity:
                TestAllocMemoryActivity.start(this);
                break;

            case R.id.am_tv_to_test_temperature_sensor_activity:
                TestTemperatureSensorActivity.start(this);
                break;

            case R.id.am_tv_to_test_reentrant_lock:
                TestReentrantLockActivity.start(this);
                break;

            case R.id.am_tv_to_test_synchronized_keyword:
                TestSynchronizedKeywordActivity.start(this);
                break;

            case R.id.am_tv_to_test_volatile_keyword:
                TestVolatileKeywordActivity.start(this);
                break;

            case R.id.am_tv_to_test_custom_array_list:
                TestCustomArrayListActivity.start(this);
                break;

            case R.id.am_tv_to_test_collapsing_toolbar_layout:
                TestCollapsingToolbarLayoutActivity.start(this);
                break;

            case R.id.am_tv_to_test_collapsing_toolbar_layout_two:
                TestCollapsingToolbarLayoutActivityTwo.start(this);
                break;
        }
    }
}
