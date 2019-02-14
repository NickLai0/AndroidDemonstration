package com.test.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.HandlerThread;

import com.test.util.DeviceUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2018/11/24<br>
 * Time: 22:04<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public final class DeviceTemperatureMonitor {

    private static final int CHECK_TEMPERATURE_INFO = 15 * 1000;

    private static final String CPU_TEMPERATURE_MAX = "cpu temperature max";
    private static final String CPU_TEMPERATURE_MIN = "cpu temperature min";
    private static final String GPU_TEMPERATURE_MAX = "GUP temperature max";
    private static final String GPU_TEMPERATURE_MIN = "GUP temperature min";
    private static final String BATTERY_TEMPERATURE_MAX = "battery temperature max";
    private static final String BATTERY_TEMPERATURE_MIN = "battery temperature min";

    private DeviceTemperatureMonitor sMe;

    private Context mContext;

    private HandlerThread mHandlerThread;

    private Handler mHandler;

    private BatteryInfoReceiver mBatteryInfoReceiver;

    StringBuilder mStringBuilder = new StringBuilder(128);

    private Map<String, Float> mTemperatureMap = new HashMap<>();

    private boolean isStart;

    private DeviceTemperatureMonitor getMe() {
        if (sMe == null) {
            synchronized (DeviceTemperatureMonitor.class) {
                if (sMe == null) {
                    sMe = new DeviceTemperatureMonitor();
                }
            }
        }
        return sMe;
    }

    private DeviceTemperatureMonitor() {

    }

    private Runnable mAutoGetDeviceTemperatureRunnable = new Runnable() {
        @Override
        public void run() {

            float currentCpuTemp = DeviceUtil.getCpuTemp();
            float currentGPUTemp = DeviceUtil.getGpuTemp();
            float currentBatteryTemp = mBatteryInfoReceiver.getBatteryTemperature();

            temperaturePutHandle(currentCpuTemp, currentGPUTemp, currentBatteryTemp);

            mStringBuilder.setLength(0);
            mStringBuilder
                    .append("Cpu当前温度 : ").append(currentCpuTemp)
                    .append(", 最高 : ").append(mTemperatureMap.get(CPU_TEMPERATURE_MAX))
                    .append(", 最低 : ").append(mTemperatureMap.get(CPU_TEMPERATURE_MIN))
                    .append("， 平均 : ").append((mTemperatureMap.get(CPU_TEMPERATURE_MAX) + mTemperatureMap.get(CPU_TEMPERATURE_MIN)) / 2)
                    .append('\n')
                    .append("GPU当前温度 : ").append(currentGPUTemp)
                    .append(", 最高 : ").append(mTemperatureMap.get(GPU_TEMPERATURE_MAX))
                    .append(", 最低 : ").append(mTemperatureMap.get(GPU_TEMPERATURE_MIN))
                    .append("， 平均 : ").append((mTemperatureMap.get(GPU_TEMPERATURE_MAX) + mTemperatureMap.get(GPU_TEMPERATURE_MIN)) / 2)
                    .append('\n')
                    .append("电池当前温度 : ").append(currentBatteryTemp)
                    .append(", 最高 : ").append(mTemperatureMap.get(BATTERY_TEMPERATURE_MAX))
                    .append(", 最低 : ").append(mTemperatureMap.get(BATTERY_TEMPERATURE_MIN))
                    .append("， 平均 : ").append((mTemperatureMap.get(BATTERY_TEMPERATURE_MAX) + mTemperatureMap.get(BATTERY_TEMPERATURE_MIN)) / 2)
                    .append('\n');

            if (mTemperatureListener != null) {
                DeviceTemperatureInfo dti = new DeviceTemperatureInfo();
                dti.event = DeviceTemperatureInfo.EVENT_TEMPERATURE_UPDATE;
                dti.curBattery = currentCpuTemp;
                dti.curCpuTemp = currentGPUTemp;
                dti.curGPUTemp = currentBatteryTemp;
                dti.info = mStringBuilder.toString();
                mTemperatureListener.onTemperatureEvent(dti);
            }

            mHandler.postDelayed(this, CHECK_TEMPERATURE_INFO);
        }
    };

    public synchronized void start(Context c) {
        if (c == null) {
            return;
        }
        mContext = c.getApplicationContext();
        if (!isStart) {
            isStart = true;

            if (mHandlerThread == null) {
                mHandlerThread = new HandlerThread("device temperature monitor");
                mHandlerThread.start();
            }

            if (mHandler == null) {
                mHandler = new Handler(mHandlerThread.getLooper());
            }

            mBatteryInfoReceiver = new BatteryInfoReceiver();
            mContext.registerReceiver(mBatteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            mHandler.post(mAutoGetDeviceTemperatureRunnable);
        }
    }

    public synchronized void stop() {
        if (isStart) {
            isStart = false;
            sMe = null;
            if (mHandler != null) {
                mHandler.removeCallbacksAndMessages(null);
            }
            if (mHandlerThread != null) {
                mHandlerThread.quit();
            }
            if (mContext != null && mBatteryInfoReceiver != null) {
                mContext.unregisterReceiver(mBatteryInfoReceiver);
            }
        }
    }

    private void temperaturePutHandle(float currentCpuTemp, float currentGPUTemp, float currentBatteryTemp) {

        String[] temperatureKeys = new String[]{
                CPU_TEMPERATURE_MAX, CPU_TEMPERATURE_MIN,
                GPU_TEMPERATURE_MAX, GPU_TEMPERATURE_MIN,
                BATTERY_TEMPERATURE_MAX, BATTERY_TEMPERATURE_MIN,
        };

        float[] temperatureValues = new float[]{
                currentCpuTemp, currentCpuTemp,
                currentGPUTemp, currentGPUTemp,
                currentBatteryTemp, currentBatteryTemp
        };

        for (int i = 0; i < temperatureKeys.length; i++) {
            String key = temperatureKeys[i];
            float curValue = temperatureValues[i];

            Float tempLastValue = mTemperatureMap.get(key);
            if (tempLastValue == null) {
                mTemperatureMap.put(key, curValue);
                continue;
            }

            float lastValue = tempLastValue;

            if (i % 2 == 0) { // max value handle
                mTemperatureMap.put(key, lastValue > curValue ? lastValue : curValue);
            } else {// min value handle
                //i % 2 == 1
                mTemperatureMap.put(key, lastValue < curValue ? lastValue : curValue);
            }
        }

    }

    public String getTemperatureInformation() {
        return mStringBuilder.toString();
    }

    private TemperatureListener mTemperatureListener;

    public void setTemperatureListener(TemperatureListener l) {
        mTemperatureListener = l;
    }

    public interface TemperatureListener {
        void onTemperatureEvent(DeviceTemperatureInfo dti);
    }

    public static final class DeviceTemperatureInfo {

        public static final byte EVENT_TEMPERATURE_UPDATE = 1;

        public byte event;

        public float curCpuTemp;
        public float curGPUTemp;
        public float curBattery;

        public String info;
    }

    private static final class BatteryInfoReceiver extends BroadcastReceiver {

        int mBatteryTemperature;

        public int getBatteryTemperature() {
            return mBatteryTemperature;
        }

        @Override
        public void onReceive(Context c, Intent intent) {
            mBatteryTemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10;
        }

    }
}
