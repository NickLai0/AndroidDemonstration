package com.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/4/15<br>
 * Time: 20:20<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class DrawableCenterTextView extends AppCompatTextView {
    private boolean mFeaturesEnable = true;

    public DrawableCenterTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableCenterTextView(Context context) {
        super(context);
        setGravity(Gravity.CENTER);
    }

    public void toggleFeatures(boolean enable) {
        mFeaturesEnable = enable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mFeaturesEnable) {
            Drawable[] drawables = getCompoundDrawables();
            if (drawables != null) {
                Drawable drawableLeft = drawables[0];
                if (drawableLeft != null) {
                    float textWidth = getPaint().measureText(getText().toString());
                    int drawablePadding = getCompoundDrawablePadding();
                    int drawableWidth = 0;
                    drawableWidth = drawableLeft.getIntrinsicWidth();
                    float bodyWidth = textWidth + drawableWidth + drawablePadding;
                    canvas.translate((getWidth() - bodyWidth) / 2, 0);
                }
                Drawable drawableTop = drawables[1];
                if (drawableTop != null) {
                    float textHeight = getPaint().getTextSize();
                    int drawablePadding = getCompoundDrawablePadding();
                    int drawableHeight = drawableTop.getIntrinsicHeight();
                    float bodyHeight = textHeight + drawableHeight + drawablePadding;
                    canvas.translate(0, (getHeight() - bodyHeight) / 2);
                }
                Drawable drawableRight = drawables[2];
                //使用drawableRight这个属性时 button的android:gravity属性要设置为right
                if (drawableRight != null) {
                    float textWidth = getPaint().measureText(getText().toString());
                    int drawablePadding = getCompoundDrawablePadding();
                    int drawableWidth = 0;
                    drawableWidth = drawableRight.getIntrinsicWidth();
                    float bodyWidth = textWidth + drawableWidth + drawablePadding;
                    canvas.translate(-((getWidth() - bodyWidth) / 2), 0);
                }
            }
        }
        super.onDraw(canvas);
    }
}