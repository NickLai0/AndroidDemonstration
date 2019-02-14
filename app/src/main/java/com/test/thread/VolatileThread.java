package com.test.thread;

import com.test.data.VolatileData;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2018/12/19<br>
 * Time: 0:10<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class VolatileThread extends Thread {

    private final VolatileData data;

    public VolatileThread(VolatileData data) {
        this.data = data;
    }

    @Override
    public void run() {
        int oldValue = data.getCounter();
        System.out.println("[Thread " + Thread.currentThread().getId() + "]: Old value = " + oldValue);

        data.increaseCounter();

        int newValue = data.getCounter();
        System.out.println("[Thread " + Thread.currentThread().getId() + "]: New value = " + newValue);
    }
}
