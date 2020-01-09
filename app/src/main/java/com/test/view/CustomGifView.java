package com.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.test.app.LogMgr;
import com.test.util.IoUtil;
import com.test.util.ViewAnalyzeUtils;

import java.io.InputStream;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/1/3<br>
 * Time: 10:15<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class CustomGifView extends View {

    private static final String TAG = CustomGifView.class.getSimpleName();

    private int mResId;
    private InputStream mInputStream;
    private Movie mMovie;
    private int mMovieWidth;
    private int mMovieHeight;
    private long mStart;

    public CustomGifView(Context context) {
        super(context);
    }

    public CustomGifView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImageResource(int resId) {
        release();
        setFocusable(true);
        init(resId);
        requestLayout();
        invalidate();
    }

    private void init(int resId) {
        mResId = resId;
        mInputStream = getResources().openRawResource(mResId);
        mMovie = Movie.decodeStream(mInputStream);
        mMovieWidth = mMovie.width();
        mMovieHeight = mMovie.height();
        mStart = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMovie != null) {
            setMeasuredDimension(mMovie.width(), mMovie.height());
        } else {
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        }
        LogMgr.i().logT(TAG, "onMeasure -> mMovieWidth=" + mMovieWidth + ", mMovieHeight=" + mMovieHeight
                + ", widthMode=" + ViewAnalyzeUtils.getDimensionMode((widthMeasureSpec)) + ", widthSize=" + MeasureSpec.getSize(widthMeasureSpec)
                + ", heightMode=" + ViewAnalyzeUtils.getDimensionMode((heightMeasureSpec)) + ", heightSize=" + MeasureSpec.getSize(heightMeasureSpec)
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mMovie != null) {
            long now = SystemClock.uptimeMillis();
            if (mStart == 0) {
                mStart = now;
            }
            int relativeMilliseconds = (int) ((now - mStart) % mMovie.duration());
            mMovie.setTime(relativeMilliseconds);
            mMovie.draw(canvas, 0, 0);
            LogMgr.i().logT(TAG, "onDraw -> relativeMilliseconds : " + relativeMilliseconds);
            invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogMgr.i().logT(TAG, "onDetachedFromWindow -> ");
        if (mResId != 0) {
            setImageResource(mResId);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LogMgr.i().logT(TAG, "onAttachedToWindow -> ");
        release();
    }

    private void release() {
        if (mInputStream != null) {
            IoUtil.close(mInputStream);
        }
        mMovie = null;
    }

}
