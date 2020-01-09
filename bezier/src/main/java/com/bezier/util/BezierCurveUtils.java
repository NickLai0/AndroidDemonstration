package com.bezier.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bezier.bean.Coordinate;

import java.util.LinkedList;
import java.util.List;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/12/31<br>
 * Time: 14:38<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */

/**
 * b1(t) = (1-t)p0 + tp1, t=[0,1]
 * <p>
 * <p>
 * b2(t)
 * = (1-t)((1-t)p0 + tp1)  +  t((1-t)p1 + tp2)
 * = (1-t)^2p0 + (1-t)tp1 + (1-t)tp1 + t^2p2
 * = (1-t)^2p0 + 2t(1-t)p1 + t^2p2
 * 即 b2(t) = (1-t)^2p0 + 2t(1-t)p1 + t^2p2, t=[0,1]
 * <p>
 * <p>
 * b3(t)
 * = (1-t)( (1-t) ( (1-t)p0+tp1) + t( (1-t)p1 + tp2 ) ) + t( (1-t) (( 1-t)p1 + tp2 ) + t( (1-t)p2+tp3) )
 * = (1-t)((1-t)^2p0+(1-t)tp1 + (1-t)tp1 + t^2p2) + t((1-t)^2p1 + (1-t)tp2 + (1-t)tp2 + t^2p3 )
 * = (1-t)^3p0 + (1-t)^2tp1 + (1-t)^2tp1 + (1-t)t^2p2 + (1-t)^2tp1 + (1-t)t^2p2 + (1-t)t^2p2 + t^3p3
 * = (1-t)^3p0 + (1-t)^2tp1 + (1-t)^2tp1 + (1-t)^2tp1 + (1-t)t^2p2 + (1-t)t^2p2 + (1-t)t^2p2 + t^3p3
 * = (1-t)^3p0 + 3t(1-t)^2p1 + 3t^2(1-t)p2 + t^3p3
 * 即 b3(t) = (1-t)^3p0 + 3t(1-t)^2p1 + 3t^2(1-t)p2 + t^3p3, t = [0,1]
 */

public final class BezierCurveUtils {

    private static final String TAG = BezierCurveUtils.class.getSimpleName();

    private BezierCurveUtils() {
    }

    public static Coordinate calculate4nOrder(@NonNull List<Coordinate> coorList, double t) {
        if (coorList.size() < 2) {
            throw new IllegalArgumentException("The size of the coordinate list cannot be smaller than two!");
        }
        LinkedList<Coordinate> resultCoorList = new LinkedList<>();
        for (int i = 0; i < coorList.size(); i++) {
            Coordinate coordinate = coorList.get(i);
            resultCoorList.addLast(Coordinate.obtain(coordinate));
        }
        Coordinate start = resultCoorList.pollFirst();
        int size = resultCoorList.size();
        for (int i = 0; i < size; i++) {
            Coordinate end = resultCoorList.pollFirst();
            Coordinate currentCoordinate = start.calculate(end, t);
            resultCoorList.addLast(currentCoordinate);

            Log.i(TAG, "Outside loop.start=" + start + ", end=" + end + ", currentCoordinate : " + currentCoordinate + ", t=" + t);

            start.recycle();
            //set the new start coordinate.
            start = end;

            if (i == (size - 1)) {
                end.recycle();
                i = -1;
                start = resultCoorList.pollFirst();
                size = resultCoorList.size();
            }
        }
        //the real final coordinate.
        Coordinate finalCoordinate = start;
        return start;
    }


}
