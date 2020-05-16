package com.test.mvp.model;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/4/21<br>
 * Time: 14:10<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class MessageModel {

    public boolean sendMessage(String msg) {
        return System.currentTimeMillis() % 2 == 0;
    }

}
