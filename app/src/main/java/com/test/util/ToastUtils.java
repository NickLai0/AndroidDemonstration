package com.test.util;

import android.content.Context;
import android.widget.Toast;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/2/16<br>
 * Time: 17:50<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class ToastUtils {

    public static void showShort(Context c, String msg) {
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context c, String msg) {
        Toast.makeText(c, msg, Toast.LENGTH_LONG).show();
    }
}
