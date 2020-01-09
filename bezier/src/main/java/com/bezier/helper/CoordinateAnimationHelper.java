package com.bezier.helper;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.bezier.bean.AnimationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/1/2<br>
 * Time: 11:46<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class CoordinateAnimationHelper {

    private final String TAG = CoordinateAnimationHelper.class.getSimpleName();

    //The default frame frequency is 25 milliseconds per frame.
    private int frameFrequency = 25;

    private Handler mHandler;

    private AnimationInfo mAiTemp = new AnimationInfo();

    public void init() {
        if (mHandler != null) {
            throw new IllegalStateException("Has already been initialized!");
        }
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                handleAnimation((Info) msg.obj);
            }
        };
    }

    public void uninit() {
        if (mHandler == null) {
            throw new IllegalStateException("Haven't been initialized yet! Please invoke init method before you invoke uninit method.");
        }
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        mStopAnimationViewList.clear();
    }

    public void start(View v, int endX, int endY, int duration) {
        int[] xyCoordinate = new int[2];
        v.getLocationOnScreen(xyCoordinate);
        start(v, xyCoordinate[0], xyCoordinate[1], endX, endY, duration);
    }

    public void start(View v, int startX, int startY, int endX, int endY, int duration) {
        start(v, startX, startY, endX, endY, duration, null);
    }

    public void start(View v, int startX, int startY, int endX, int endY, int duration, OnAnimationRefreshListener l) {
        AnimationInfo animationInfo = new AnimationInfo();
        animationInfo.v = v;
        animationInfo.startX = startX;
        animationInfo.startY = startY;
        animationInfo.endX = endX;
        animationInfo.endY = endY;
        animationInfo.startMillis = System.currentTimeMillis();
        animationInfo.durationMillis = duration;

        Info info = new Info();
        info.ai = animationInfo;
        info.l = l;
        startLoop(info);
    }

    private List<View> mStopAnimationViewList = new ArrayList<>();

    public void stop(View v) {
        if (v != null && !mStopAnimationViewList.contains(v)) {
            mStopAnimationViewList.add(v);
        }
    }

    public int getFrameFrequency() {
        return frameFrequency;
    }

    public void setFrameFrequency(int frameFrequency) {
        this.frameFrequency = frameFrequency;
    }

    private void handleAnimation(Info info) {
        AnimationInfo ai = info.ai;
        if (mStopAnimationViewList.remove(ai.v)) {
            //stop this animation by outside.
            return;
        }
        long spentMillis = System.currentTimeMillis() - ai.startMillis;
        if (spentMillis > ai.durationMillis) {
            ai.spentMillisPercent = 1.0;
        } else {
            ai.spentMillisPercent = spentMillis * 1.0 / ai.durationMillis;
        }
        int offsetX = (int) ((ai.endX - ai.startX) * ai.spentMillisPercent);
        int offsetY = (int) ((ai.endY - ai.startY) * ai.spentMillisPercent);
        ai.currentX = ai.startX + offsetX;
        ai.currentY = ai.startY + offsetY;

        boolean consumed = notifyOnAnimationRefresh(info.l, ai);
        if (!consumed) {
            ai.v.setX(ai.currentX);
            ai.v.setY(ai.currentY);
        }

        if (spentMillis <= ai.durationMillis) {
            startLoop(info);
        }


    }

    private boolean notifyOnAnimationRefresh(OnAnimationRefreshListener l, AnimationInfo ai) {
        boolean consumed = false;
        if (l != null) {
            mAiTemp.set(ai);
            consumed = l.onAnimationRefresh(ai);
            //clear the reference for the view.
            mAiTemp.v = null;
        }
        return consumed;
    }

    private void startLoop(Info info) {
        Handler handler = mHandler;
        if (handler != null && info != null) {
            Message msg = Message.obtain(mHandler, -1, info);
            handler.sendMessageDelayed(msg, frameFrequency);
        }
    }

    public interface OnAnimationRefreshListener {
        boolean onAnimationRefresh(AnimationInfo a);
    }

    static class Info {
        AnimationInfo ai;
        OnAnimationRefreshListener l;
    }

}