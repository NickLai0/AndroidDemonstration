package com.test.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.test.R;
import com.test.app.LogMgr;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/11/22<br>
 * Time: 16:41<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class CustomImageView extends AppCompatImageView {

    private static final String TAG = CustomImageView.class.getSimpleName();
    private int mCornerRadius;

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs);
    }

    private void initData(Context c, @Nullable AttributeSet attrs) {
        TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        mCornerRadius = typedArray.getDimensionPixelSize(R.styleable.CustomImageView_cornerRadius, 0);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogMgr.i().logT(TAG, "onMeasure -> ");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        LogMgr.i().logT(TAG, "onLayout -> ");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null || mCornerRadius == 0 || !(drawable instanceof BitmapDrawable)) {
            LogMgr.i().logT(TAG, "onDraw -> drawable : " + drawable + " , mCornerRadius ：" + mCornerRadius);
            super.onDraw(canvas);
        } else {
            int width = getWidth() - getPaddingLeft() - getPaddingRight();
            int height = getHeight() - getPaddingTop() - getPaddingBottom();
            int left = getPaddingLeft();
            int top = getPaddingTop();
            //left means offset here.
            int right = width + left;
            //top means offset here.
            int bottom = height + top;

            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            float widthScale = (float) (width * 1.0 / bitmap.getWidth());
            float heightScale = (float) (height * 1.0 / bitmap.getHeight());

            Matrix matrix = new Matrix();
            matrix.setScale(widthScale, heightScale);

            BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            bitmapShader.setLocalMatrix(matrix);

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setShader(bitmapShader);

            //Draw the round corner.
            canvas.drawRoundRect(new RectF(left, top, right, bottom), mCornerRadius, mCornerRadius, paint);

            LogMgr.i().logT(TAG, "onDraw -> drawable : " + drawable + ", width : " + width + ", height : " + height + ", radius : " + mCornerRadius
                    + ", left : " + left
                    + ", top : " + top
                    + ", right : " + right
                    + ", bottom : " + bottom
                    + ", widthScale : " + widthScale
                    + ", heightScale : " + heightScale
            );
        }
    }

    public void setCornerRadius(int radius) {
        if (mCornerRadius == radius || radius < 0) {
            return;
        }
        mCornerRadius = radius;
        invalidate();
    }

    public int getCornerRadius() {
        return mCornerRadius;
    }

}
