package com.test.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;


/**
 * 创建者     许亮
 * 创建时间   2018/7/6 19:33
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class ActivityUtils {

//    public static void fullScreenAndKeepScreenOn(Activity a) {
//        Window window = a.getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
////        //取消状态栏
////        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
////
////
////        //隐藏Navigation bar
////        WindowManager.LayoutParams layoutParams = window.getAttributes();
////        layoutParams.systemUiVisibility = layoutParams.systemUiVisibility | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
////        window.setAttributes(layoutParams);
//        fullScreen(a);
//    }

    public static void hideNavigationBar(Window w) {
        int uiFlags =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

//        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_IMMERSIVE
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar

        if (android.os.Build.VERSION.SDK_INT >= 19) {
//            uiFlags |= 0x00001000;    //SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide navigation bars - compatibility: building API level is lower thatn 19, use magic number directly for higher API target level
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }

        w.getDecorView().setSystemUiVisibility(uiFlags);
    }

    /**
     * 判断是否有刘海凹槽
     *
     * @param context
     * @return
     */
    public static boolean isHasNotchInScreen(Context context) {
        boolean hasNotchInScreen = false;
        if (context != null) {
            //华为 可以在AndroidMenifest中适配就行
            ClassLoader classLoader = context.getClassLoader();
//            try {
//                Class hwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
//                Method method = hwNotchSizeUtil.getMethod("hasNotchInScreen");
//                hasNotchInScreen = (boolean) method.invoke(hwNotchSizeUtil);
//
//            } catch (Exception e) {
//
//            }
//            if (hasNotchInScreen) {
//                return true;
//            }
            //Oppo
            PackageManager pm = context.getPackageManager();
            hasNotchInScreen = pm.hasSystemFeature("com.oppo.feature.screen.heteromorphism");
            if (hasNotchInScreen) {
                return true;
            }
            //Vivo
            try {
                Class mayFtFeatureClass = classLoader.loadClass("android.util.FtFeature");
                Method hasTheMethod = mayFtFeatureClass.getMethod("isFeatureSupport", int.class);
                hasNotchInScreen = (boolean) hasTheMethod.invoke(mayFtFeatureClass, 0x00000020);
                if (hasNotchInScreen) {
                }
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
        return hasNotchInScreen;
    }

    public static void fullScreen(Activity a) {
        Window window = a.getWindow();
        if (window == null) {
            return;
        }
        //取消状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //隐藏Navigation bar
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.systemUiVisibility
                |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        ;
//        layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS;
        window.setAttributes(layoutParams);
    }

    public static void immersiveNavigation(Activity curActivity) {
        Window curWindow = curActivity.getWindow();
        if (curWindow != null) {
            //隐藏Navigation bar
            WindowManager.LayoutParams layoutParams = curWindow.getAttributes();
            layoutParams.systemUiVisibility
                    |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            ;
//        layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS;
            curWindow.setAttributes(layoutParams);
//            View decorView = curWindow.getDecorView();
//            if (decorView != null) {
//                int oldUiFlags = decorView.getSystemUiVisibility();
//                int uiFlags =
//                        oldUiFlags |
//                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
////                            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                                | View.SYSTEM_UI_FLAG_IMMERSIVE
//                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
////                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
////                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//                decorView.setSystemUiVisibility(uiFlags);
//            }
        }
    }
}
