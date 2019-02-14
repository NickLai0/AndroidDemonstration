package com.test.data;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2018/12/19<br>
 * Time: 0:09<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class VolatileData {

    private volatile int counter = 0;

    public int getCounter() {
        return counter;
    }

    public void increaseCounter() {
        ++counter;
    }
}
