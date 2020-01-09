package com.bezier.bean;

import java.util.ArrayList;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/12/31<br>
 * Time: 10:50<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public final class Coordinate {

    private static final int FLAG_IN_USE = 1 << 0;

    private static final int POOL_MAX = 30;

    private static final Object sfPoolSync = new Object();

    private static Coordinate sPool;

    private static int sPoolSize;

    private int flags;

    public int x;
    public int y;

    private Coordinate next;

    public Coordinate calculate(Coordinate end, double t) {
        Coordinate finalCoordinate = obtain();
        finalCoordinate.x = (int) ((1 - t) * x + t * end.x + 0.5);
        finalCoordinate.y = (int) ((1 - t) * y + t * end.y + 0.5);
        return finalCoordinate;
    }

    public static Coordinate obtain() {
        synchronized (sfPoolSync) {
            if (sPool != null) {
                Coordinate head = sPool;
                sPool = sPool.next;
                sPoolSize--;
                head.next = null;
                //clear the flags.
                head.flags = 0;
                return head;
            }
        }
        return new Coordinate();
    }

    public static Coordinate obtain(int x,int y) {
        Coordinate c = obtain();
        c.x = x;
        c.y = y;
        return c;
    }

    public static Coordinate obtain(Coordinate coordinate) {
        Coordinate c = obtain();
        c.x = coordinate.x;
        c.y = coordinate.y;
        return c;
    }

    public void recycle() {
        synchronized (sfPoolSync) {
            if ((FLAG_IN_USE & flags) == FLAG_IN_USE) {
                throw new IllegalStateException("This coordinate cannot be recycled because it is still in use.");
            }

            if (sPoolSize < POOL_MAX) {
                //Mark in use.
                flags = FLAG_IN_USE;
                //clear the coordinate;
                x = 0;
                y = 0;

                next = sPool;
                //Make self become the head node.
                sPool = this;
                sPoolSize++;
            }

        }
    }

    public static void recycle(ArrayList<Coordinate> bezierCoordinates) {
        for (Coordinate coor : bezierCoordinates) {
            coor.recycle();
        }
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

}
