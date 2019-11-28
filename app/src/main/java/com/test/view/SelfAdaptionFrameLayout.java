package com.test.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.test.R;
import com.test.app.LogMgr;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/11/22<br>
 * Time: 21:47<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class SelfAdaptionFrameLayout extends FrameLayout {

    private final String TAG = getClass().getSimpleName();

    public static final int ADAPTION_MODE_NON = 1 << 0;
    public static final int ADAPTION_MODE_WIDTH_BASE_ON_HEIGHT = 1 << 1;
    public static final int ADAPTION_MODE_HEIGHT_BASE_ON_WIDTH = 1 << 2;

    private int mAdaptionMode = ADAPTION_MODE_NON;

    private float mScaleRatio = 0;
    private int mCornerRadius;
    private Path mPath;

    public SelfAdaptionFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public SelfAdaptionFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelfAdaptionFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs);
    }

    private void initData(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelfAdaptionFrameLayout);
        if (typedArray.hasValue(R.styleable.SelfAdaptionFrameLayout_widthSelfAdaptionBasedOnHeight)) {
            mScaleRatio = typedArray.getFloat(R.styleable.SelfAdaptionFrameLayout_widthSelfAdaptionBasedOnHeight, 0);
            mAdaptionMode = ADAPTION_MODE_WIDTH_BASE_ON_HEIGHT;
        } else if (typedArray.hasValue(R.styleable.SelfAdaptionFrameLayout_heightSelfAdaptionBasedOnWidth)) {
            mScaleRatio = typedArray.getFloat(R.styleable.SelfAdaptionFrameLayout_heightSelfAdaptionBasedOnWidth, 0);
            mAdaptionMode = ADAPTION_MODE_HEIGHT_BASE_ON_WIDTH;
        }
        if (mScaleRatio < 0) {
            mScaleRatio = 0;
        }
        mCornerRadius = typedArray.getDimensionPixelSize(R.styleable.SelfAdaptionFrameLayout_cornerRadius, 0);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogMgr.i().logT(TAG, "onMeasure -> ");
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        switch (mAdaptionMode) {
            case ADAPTION_MODE_WIDTH_BASE_ON_HEIGHT:
                width = (int) (height * mScaleRatio);
                widthMeasureSpec = View.MeasureSpec.EXACTLY | width;
                break;

            case ADAPTION_MODE_HEIGHT_BASE_ON_WIDTH:
                height = (int) (width * mScaleRatio);
                heightMeasureSpec = View.MeasureSpec.EXACTLY | height;
                break;

            case ADAPTION_MODE_NON:
            default:
                break;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        LogMgr.i().logT(TAG, "onLayout -> ");
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setAdaptionMode(int width, int height, int adaptionMode, float scaleRatio) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(0, 0);
        }
        if (width == layoutParams.width && height == layoutParams.height && adaptionMode == mAdaptionMode && scaleRatio == mScaleRatio) {
            //Ignore the same parameters.
            return;
        }
        mAdaptionMode = adaptionMode;
        mScaleRatio = scaleRatio;
        layoutParams.width = width;
        layoutParams.height = height;
        //Request layout automatically.
        setLayoutParams(layoutParams);
    }

    private String getModeDesc(int mode) {
        String desc = null;
        switch (mode) {
            case View.MeasureSpec.EXACTLY:
                desc = "exactly";
                break;

            case View.MeasureSpec.AT_MOST:
                desc = "at_most";
                break;

            case View.MeasureSpec.UNSPECIFIED:
                desc = "unspecified";
                break;
        }
        return desc;
    }

    @Override
    public void draw(Canvas canvas) {
        LogMgr.i().logT(TAG, "draw -> ");
        canvas.save();
        canvas.clipPath(getRoundPath());
        super.draw(canvas);
        canvas.restore();
    }

    private Path getRoundPath() {
        modifyPath(getWidth(), getHeight(), mCornerRadius);
        return mPath;
    }

    public void setCornerRadius(int radius) {
        LogMgr.i().logT(TAG, "setCornerRadius -> new radius : " + radius + ", old radius : " + mCornerRadius);
        if (mCornerRadius == radius || radius < 0) {
            return;
        }
        mCornerRadius = radius;
        invalidate();
    }

    private int mLastPathWidth = -1;
    private int mLastPathHeight = -1;
    private int mLastCornerRadius = -1;

    private void modifyPath(int w, int h, int cornerRadius) {
        //Avoid repetition.
        if (mLastPathWidth == w && mLastPathHeight == h && cornerRadius == mLastCornerRadius) {
            return;
        }
        //record the parameters.
        mLastPathWidth = w;
        mLastPathHeight = h;
        mLastCornerRadius = cornerRadius;
        //Initial Path object.
        RectF r = new RectF(0, 0, w, h);
        mPath = new Path();
        mPath.addRoundRect(r, cornerRadius, cornerRadius, Path.Direction.CW);
        mPath.close();
    }

    public int getCornerRadius() {
        return mCornerRadius;
    }
}
