package com.test.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.BuildConfig;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/5/16<br>
 * Time: 19:18<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public abstract class BaseFragment extends Fragment {

    protected final String TAG = getClass().getSimpleName();

    protected boolean mLifecycleLogging = BuildConfig.DEBUG;

    private void lifecycleLog(String msg) {
        if (mLifecycleLogging) {
            Log.i(TAG, msg);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        lifecycleLog("onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleLog("onCreate");
    }

    protected View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lifecycleLog("onCreateView");
        if (mView == null) {
            mView = inflater.inflate(getLayoutId(), null);
        }
        return mView;
    }

    protected abstract int getLayoutId();

    private boolean initialized;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifecycleLog("onViewCreated");
        if (!initialized) {
            initialized = true;
            initView();
            initData();
            initListener();
        }
    }

    protected <T extends View> T findViewById(int resId) {
        if (mView == null) {
            return null;
        }
        return mView.findViewById(resId);
    }

    protected void initView() {
    }

    protected void initData() {
    }

    protected void initListener() {
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleLog("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleLog("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        lifecycleLog("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycleLog("onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycleLog("onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lifecycleLog("onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        lifecycleLog("onDetach");
    }

}
