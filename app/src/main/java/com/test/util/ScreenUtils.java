package com.test.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/12/31<br>
 * Time: 17:37<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class ScreenUtils {

    public static final int getWidth(Context c) {
        DisplayMetrics dm = c.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static final int getHeight(Context c) {
        DisplayMetrics dm = c.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

}
