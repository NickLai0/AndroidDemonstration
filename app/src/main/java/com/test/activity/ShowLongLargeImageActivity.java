package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

import com.test.R;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/3/22<br>
 * Time: 18:47<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class ShowLongLargeImageActivity extends BaseActivity {

    private ImageView mIvLongImage;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, ShowLongLargeImageActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_image_large_long_show;
    }

    @Override
    protected void initView() {
        mIvLongImage =(ImageView) findViewById(R.id.aills_iv_long_img);
    }

    @Override
    protected void initData() {
        mIvLongImage.setImageResource(R.mipmap.img_long_temp);
    }

    @Override
    protected void initListener() {

    }

}
