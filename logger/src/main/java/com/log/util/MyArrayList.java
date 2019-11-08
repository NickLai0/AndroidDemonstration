package com.log.util;

import java.util.ArrayList;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2018/12/19<br>
 * Time: 1:37<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class MyArrayList<E> extends ArrayList<E> {

    @Override
    public E get(int index) {
        try {
            return super.get(index);
        } catch (Exception e) {
            return null;
        }
    }

}