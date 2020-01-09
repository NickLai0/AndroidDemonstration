package com.test.util;

import java.io.Closeable;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/1/3<br>
 * Time: 11:17<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public final class IoUtil {

    public static void close(Closeable... closeableArr) {
        if (closeableArr == null) {
            return;
        }
        for (Closeable c : closeableArr) {
            if (c == null) {
                continue;
            }
            try {
                c.close();
            } catch (Exception ignore) {
            }
        }
    }

}
