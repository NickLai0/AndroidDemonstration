package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.test.R;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2018/12/21<br>
 * Time: 16:46<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestCollapsingToolbarLayoutActivityTwo extends BaseActivity {

    private FloatingActionButton mFab;

    private AppBarLayout mAppBarLayout;

    private Menu menu;
    private LinearLayout mLlImageContianer;
    private LinearLayout mLlTopLayout;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestCollapsingToolbarLayoutActivityTwo.class));
    }

    @Override
    protected int provideLayoutId() {
        Log.i(TAG, "provideLayoutId -> return layout ID.");
        return R.layout.activity_layout_toolbar_collapsing_test_two;
    }

    @Override
    protected void initView() {
        Log.i(TAG, "initView");
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mLlImageContianer = (LinearLayout) findViewById(R.id.altctt_ll_imaeg_container);
        mLlTopLayout = (LinearLayout) findViewById(R.id.altctt_ll_top_layout);
    }

    @Override
    protected void initData() {
        Log.i(TAG, "initData");
    }

    @Override
    protected void initListener() {
        Log.i(TAG, "initListener");
        mFab.setOnClickListener(this);
        if (mAppBarLayout != null) {
            mAppBarLayout.addOnOffsetChangedListener(mOnOffsetChangedListener);
        }
    }

    private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
        boolean isShow = false;

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            Log.i(TAG, "onOffsetChanged -> verticalOffset : " + verticalOffset + ", appBarLayout.getTotalScrollRange()" + appBarLayout.getTotalScrollRange());

//            if ((appBarLayout.getTotalScrollRange() + verticalOffset) == mLlImageContianer.getHeight()) {
//                if (mLlImageContianer.getVisibility() != View.INVISIBLE) {
//                    mLlImageContianer.setVisibility(View.INVISIBLE);
//                }
//            } else {
//                if (mLlImageContianer.getVisibility() != View.VISIBLE) {
//                    mLlImageContianer.setVisibility(View.VISIBLE);
//                }
//            }

//            if (scrollRange == -1) {
//                Log.i(TAG, "onOffsetChanged -> scrollRange is -1");
//                scrollRange = appBarLayout.getTotalScrollRange();
//            }
//            if (scrollRange + verticalOffset == 0) {
//                Log.i(TAG, "onOffsetChanged -> scrollRange + verticalOffset is 0");
//                isShow = true;
//                showOption(R.id.action_info);
//            } else if (isShow) {
//                Log.i(TAG, "onOffsetChanged -> scrollRange + verticalOffset is not 0");
//                isShow = false;
//                hideOption(R.id.action_info);
//            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu -> on create options menu.");
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        hideOption(R.id.action_info);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.i(TAG, "onOptionsItemSelected -> on setting item click");
            return true;
        } else if (id == R.id.action_info) {
            Log.i(TAG, "onOptionsItemSelected -> on info item click");
            return true;
        }
        Log.i(TAG, "onOptionsItemSelected -> on unknown action item click");
        return super.onOptionsItemSelected(item);
    }

    private void hideOption(int id) {
        Log.i(TAG, "hideOption -> id ： " + id);
//        MenuItem item = menu.findItem(id);
//        item.setVisible(false);
    }

    private void showOption(int id) {
        Log.i(TAG, "showOption -> id ： " + id);
//        MenuItem item = menu.findItem(id);
//        item.setVisible(true);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action", this).show();
                break;

            default:
                Snackbar
                        .make(view, "On action click.", Snackbar.LENGTH_SHORT)
                        .setAction("Action", this).show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        mAppBarLayout.removeOnOffsetChangedListener(mOnOffsetChangedListener);
    }

}
