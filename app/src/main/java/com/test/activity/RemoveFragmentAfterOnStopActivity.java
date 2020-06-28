package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;

import com.test.R;
import com.test.app.LogMgr;
import com.test.fragment.ProgressBarDemoFragment;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/6/22<br>
 * Time: 19:38<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class RemoveFragmentAfterOnStopActivity extends BaseActivity {

    public static void start(Activity a) {
        a.startActivity(new Intent(a, RemoveFragmentAfterOnStopActivity.class));
    }

    private final int MSG_REMOVE_FRAGMENT = 1;

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_stop_on_after_fragment_remove;
    }

    private ProgressBarDemoFragment mProgressBarDemoFragment;

    @Override
    protected void initView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(android.R.id.content, mProgressBarDemoFragment = new ProgressBarDemoFragment());
        ft.commit();
        LogMgr.i().logI(TAG, "initView -> replace a fragment!");
    }

    @Override
    protected void initData() {
        initHandler();
        mHandler.sendEmptyMessageDelayed(MSG_REMOVE_FRAGMENT, 1000 * 5);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_REMOVE_FRAGMENT:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.remove(mProgressBarDemoFragment);
                /**
                 * 06-22 20:21:07.934 14860-14874/com.test E/AndroidRuntime: FATAL EXCEPTION: main
                 *     Process: com.test, PID: 14860
                 *     java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
                 *         at android.support.v4.app.FragmentManagerImpl.checkStateLoss(FragmentManager.java:2053)
                 *         at android.support.v4.app.FragmentManagerImpl.enqueueAction(FragmentManager.java:2079)
                 *         at android.support.v4.app.BackStackRecord.commitInternal(BackStackRecord.java:678)
                 *         at android.support.v4.app.BackStackRecord.commit(BackStackRecord.java:632)
                 *         at com.test.activity.RemoveFragmentAfterOnStopActivity.handleMessage(RemoveFragmentAfterOnStopActivity.java:56)
                 *         at com.test.handler.WeakHandler.handleMessage(WeakHandler.java:23)
                 *         at android.os.Handler.dispatchMessage(Handler.java:102)
                 *         at android.os.Looper.loop(Looper.java:148)
                 *         at android.app.ActivityThread.main(ActivityThread.java:5619)
                 *         at java.lang.reflect.Method.invoke(Native Method)
                 *         at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:853)
                 *         at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:737)
                 */
                //activity
                //ft.commit();
                ft.commitAllowingStateLoss();
                LogMgr.i().logI(TAG, "handleMessage -> remove a fragment!");
                break;
        }
    }

    @Override
    protected void initListener() {

    }

}
