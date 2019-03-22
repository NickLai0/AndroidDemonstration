package com.test.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

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

    public static boolean isActivityOnTop(Context c, String activityClassName) {
        ActivityManager am = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
        boolean isOnTop = false;
        if (runningTasks != null && runningTasks.size() > 0) {
            ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
            if (runningTaskInfo == null || runningTaskInfo.topActivity == null) {
                //to logging in file.
            } else {
                isOnTop = runningTaskInfo.topActivity.getClassName().equals(activityClassName);
            }
        } else {
            //to logging in file.
        }
        return isOnTop;
    }

}
