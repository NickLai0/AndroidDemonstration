package com.bezier.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bezier.bean.Coordinate;
import com.bezier.util.BezierCurveUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/12/31<br>
 * Time: 10:55<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class BezierCurveView extends View {

    private static final int DOT_RADIUS = 3;
    private static final int STROKE_WIDTH = 0;

    private final String TAG = BezierCurveView.class.getSimpleName();

    private Paint mPaintDot;
    private Paint mPaintCurve;

    private List<List<Coordinate>> mCoordinatesList;

    public BezierCurveView(Context context) {
        super(context, null);
    }

    public BezierCurveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    private void initData() {
        mPaintDot = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCurve = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaintDot.setColor(Color.RED);
        mPaintCurve.setColor(Color.BLUE);
        mPaintCurve.setStyle(Paint.Style.STROKE);
        mPaintCurve.setStrokeWidth(STROKE_WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCoordinatesList == null) {
            return;
        }
        Path pathCurve = new Path();
        for (int i = 0; i < mCoordinatesList.size(); i++) {

            List<Coordinate> coordinates = mCoordinatesList.get(i);
            if (coordinates == null) {
                continue;
            }

            for (Coordinate coor : coordinates) {
                canvas.drawCircle(coor.x, coor.y, DOT_RADIUS, mPaintDot);
            }

            Coordinate start = coordinates.get(0);
            pathCurve.moveTo(start.x, start.y);
            logI("onDraw -> moveTo start.x=" + start.x + ", start.y=" + start.y);
            for (double t = 0.01; t <= 1; t += 0.01) {
                Coordinate end = BezierCurveUtils.calculate4nOrder(coordinates, t);
                pathCurve.lineTo(end.x, end.y);
                logI("onDraw -> lineTo end.x=" + end.x + ", end.y=" + end.y);
                end.recycle();
            }
        }
        //pathCurve.close();
        canvas.drawPath(pathCurve, mPaintCurve);
    }

    private void logI(String msg) {
            Log.i(TAG, msg);
    }

    public void drawCurve(List<Coordinate> coordinateList) {
        List<List<Coordinate>> list = new ArrayList<>();
        list.add(coordinateList);
        drawCurve1(list);
    }

    public void drawCurve1(List<List<Coordinate>> coordinateList) {
        mCoordinatesList = coordinateList;
        invalidate();
    }

}
