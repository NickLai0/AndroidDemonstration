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
    private TextView mTvQuickSortAlgorithm;
    private TextView mTvTopActivityMonitor;
    private TextView mTvShowLongLargeImage;
    private TextView mTvCoordinateLayoutDemo;
    private TextView mTvTabLayoutDemo;
    private TextView mTvTestCustomImageView;
    private TextView mTvTestSelfAdaptionFrameLayout;
    private TextView mTvTestProgressBar;
    private TextView mTvTestTextViewMarquee;
    private TextView mTvTestCoordinateAnimationHelper;

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
        mTvQuickSortAlgorithm = (TextView) findViewById(R.id.am_tv_quick_sort_algorithm);
        mTvTopActivityMonitor = (TextView) findViewById(R.id.am_tv_top_activity_monitor);
        mTvShowLongLargeImage = (TextView) findViewById(R.id.am_tv_show_long_large_image);
        mTvCoordinateLayoutDemo = (TextView) findViewById(R.id.am_tv_coordinate_layout_demo);
        mTvTabLayoutDemo = (TextView) findViewById(R.id.am_tv_tab_layout_demo);
        mTvTestCustomImageView = (TextView) findViewById(R.id.am_tv_test_custom_image_view);
        mTvTestSelfAdaptionFrameLayout = (TextView) findViewById(R.id.am_tv_test_self_adaption_frame_layout);
        mTvTestProgressBar = (TextView) findViewById(R.id.am_tv_test_progress_bar);
        mTvTestTextViewMarquee = (TextView) findViewById(R.id.am_tv_test_text_view_marquee);
        mTvTestCoordinateAnimationHelper = (TextView) findViewById(R.id.am_tv_test_coordinate_animation_helper);
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
        mTvQuickSortAlgorithm.setOnClickListener(this);
        mTvTopActivityMonitor.setOnClickListener(this);
        mTvShowLongLargeImage.setOnClickListener(this);
        mTvCoordinateLayoutDemo.setOnClickListener(this);
        mTvTabLayoutDemo.setOnClickListener(this);
        mTvTestCustomImageView.setOnClickListener(this);
        mTvTestSelfAdaptionFrameLayout.setOnClickListener(this);
        mTvTestProgressBar.setOnClickListener(this);
        mTvTestTextViewMarquee.setOnClickListener(this);
        mTvTestCoordinateAnimationHelper.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.am_tv_show_long_large_image:
                ShowLongLargeImageActivity.start(this);
                break;

            case R.id.am_tv_top_activity_monitor:
                TopActivityMonitorActivity.start(this);
                break;

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

            case R.id.am_tv_quick_sort_algorithm:
                QuickSortAlgorithmActivity.start(this);
                break;

            case R.id.am_tv_coordinate_layout_demo:
                CoordinateLayoutDemoActivity.start(this);
                break;

            case R.id.am_tv_tab_layout_demo:
                TabLayoutDemoActivity.start(this);
                break;

            case R.id.am_tv_test_custom_image_view:
                TestCustomImageViewActivity.start(this);
                break;

            case R.id.am_tv_test_self_adaption_frame_layout:
                TestSelfAdaptionFrameViewActivity.start(this);
                break;

            case R.id.am_tv_test_progress_bar:
                TestProgressBarActivity.start(this);
                break;

            case R.id.am_tv_test_text_view_marquee:
                TextViewMarqueeActivity.start(this);
                break;

            case R.id.am_tv_test_coordinate_animation_helper:
                TestCoordinateAnimationHelperActivity.start(this);
                break;
        }
    }
}
