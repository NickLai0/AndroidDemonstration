package com.test.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.R;
import com.test.app.LogMgr;
import com.test.util.ExceptionUtil;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/5/16<br>
 * Time: 19:33<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class ProgressBarDemoFragment extends BaseFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogMgr.i().logT(TAG, "onAttach -> " + ExceptionUtil.getStackTrace());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogMgr.i().logT(TAG, "onAttach -> " + ExceptionUtil.getStackTrace());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogMgr.i().logT(TAG, "onAttach -> " + ExceptionUtil.getStackTrace());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogMgr.i().logT(TAG, "onAttach -> " + ExceptionUtil.getStackTrace());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_demo_bar_progress;
    }

}
