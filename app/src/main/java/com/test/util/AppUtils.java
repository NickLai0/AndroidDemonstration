package com.test.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/3/7<br>
 * Time: 18:29<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class AppUtils {

    public static boolean isOnTop(Context c, String activityClassName) {
        ActivityManager am = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return cn.getClassName().equals(activityClassName);
    }

}
