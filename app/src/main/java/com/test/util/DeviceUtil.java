package com.test.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2018/11/24<br>
 * Time: 20:12<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class DeviceUtil {

    private DeviceUtil() {
    }

    public static float getCpuTemp() {
        Process p;
        try {
            p = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = reader.readLine();
            float temp = Float.parseFloat(line) / 1000.0f;

            return temp;

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public static float getGpuTemp() {
        Process p;
        try {
            p = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone10/temp");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = reader.readLine();
            float temp = Float.parseFloat(line) / 10;

            return temp;

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

}
