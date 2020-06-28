package com.test.fragment;

import android.view.View;

import com.test.R;
import com.test.app.LogMgr;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/6/23<br>
 * Time: 15:03<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class NormalFragment extends BaseFragment implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_normal;
    }

    @Override
    protected void initView() {
        mView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        LogMgr.i().logI(TAG, "NormalFragment -> intercept onClick event.");
    }
}
