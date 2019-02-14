package com.test.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.test.R;
import com.test.util.DeviceUtil;
import com.test.util.TimeUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2018/11/24<br>
 * Time: 14:55<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestTemperatureSensorActivity extends BaseActivity implements SensorEventListener {

    private static final String CPU_TEMPERATURE_MAX = "cpu temperature max";
    private static final String CPU_TEMPERATURE_MIN = "cpu temperature min";
    private static final String GPU_TEMPERATURE_MAX = "GUP temperature max";
    private static final String GPU_TEMPERATURE_MIN = "GUP temperature min";
    private static final String BATTERY_TEMPERATURE_MAX = "battery temperature max";
    private static final String BATTERY_TEMPERATURE_MIN = "battery temperature min";

    public static final String TAG = TestTemperatureSensorActivity.class.getSimpleName();

    private final static String NOT_SUPPORTED_MESSAGE = "Sorry, sensor not available for this device.";

    private TextView mTvTemperatureInfo;
    private TextView mTvTemperatureInfoReadFileWay;

    private SensorManager mSensorManager;

    private Sensor mTemperature;

    private Handler mHandler = new Handler();

    private BatteryInfoReceiver mBatteryInfoReceiver;

    private Map<String, Float> mTemperatureMap = new HashMap<>();

    StringBuilder mStringBuilder = new StringBuilder();

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_sensor_temperature_test;
    }

    @Override
    protected void initView() {
        mTvTemperatureInfo = (TextView)findViewById(R.id.ttsa_tv_temperature_info);
        mTvTemperatureInfoReadFileWay = (TextView)findViewById(R.id.ttsa_tv_temperature_info_read_file_way);
    }

    @Override
    protected void initData() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // requires API level 14.
            mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }
        if (mTemperature == null) {
            mTvTemperatureInfo.setText(NOT_SUPPORTED_MESSAGE);
        }

        mBatteryInfoReceiver = new BatteryInfoReceiver();
        registerReceiver(mBatteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                float currentCpuTemp = DeviceUtil.getCpuTemp();
                float currentGPUTemp = DeviceUtil.getGpuTemp();
                float currentBatteryTemp = mBatteryInfoReceiver.getTemp();

                temperatureRecord(currentCpuTemp, currentGPUTemp, currentBatteryTemp);

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

                String info = mStringBuilder.toString();
                mStringBuilder.setLength(0);
                Log.i(TAG, info);
                Log.i(TAG, "-----------------------------------------");
                mTvTemperatureInfoReadFileWay.setText(info);
                mHandler.postDelayed(this, 0);
            }
        }, 0);

    }

    private void temperatureRecord(float currentCpuTemp, float currentGPUTemp, float currentBatteryTemp) {

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBatteryInfoReceiver);
    }

    @Override
    protected void initListener() {

    }

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestTemperatureSensorActivity.class));
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("TAG", "onSensorChanged");
        map.put("current time", TimeUtil.formatMillsTimes(System.currentTimeMillis(), TimeUtil.NORMAL_TIME_FORMAT_WITH_MILLS));
        map.put("event.accuracy", event.accuracy + "");
        map.put("event.timestamp", TimeUtil.formatMillsTimes(event.timestamp, TimeUtil.NORMAL_TIME_FORMAT_WITH_MILLS));
        map.put("event.sensor", event.sensor + "");
        putSensorString(map, event.sensor);
        if (event.values == null || event.values.length == 0) {
            map.put("event.values", event.values + "");
        } else {
            for (int i = 0; i < event.values.length; i++) {
                map.put("event.values[" + i + "]", event.values[i] + "");
            }
        }
        genStringAndShowToLast(map);
    }

    private void putSensorString(Map<String, String> map, Sensor sensor) {

        if (map == null) {
            return;
        }

        if (sensor == null) {
            map.put("sensor", "null");
            return;
        }

        map.put("sensor.getName()", sensor.getName() + "");
        map.put("sensor.getVendor()", sensor.getVendor() + "");
        map.put("sensor.getType()", sensor.getType() + "");
        map.put("sensor.getVersion()", sensor.getVersion() + "");
        map.put("sensor.getMaximumRange()", sensor.getMaximumRange() + "");
        map.put("sensor.getResolution()", sensor.getResolution() + "");
        map.put("sensor.getPower()", sensor.getPower() + "");
        map.put("sensor.getMinDelay()", sensor.getMinDelay() + "");
        map.put("sensor.getFifoReservedEventCount()", sensor.getFifoReservedEventCount() + "");
        map.put("sensor.getFifoMaxEventCount()", sensor.getFifoMaxEventCount() + "");
        map.put("sensor.getStringType()", sensor.getStringType() + "");
        map.put("sensor.getMaxDelay()", sensor.getMaxDelay() + "");
        map.put("sensor.isWakeUpSensor()", sensor.isWakeUpSensor() + "");

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("TAG", "onAccuracyChanged");
        map.put("current time", TimeUtil.formatMillsTimes(System.currentTimeMillis(), TimeUtil.NORMAL_TIME_FORMAT_WITH_MILLS));
        map.put("event.accuracy", accuracy + "");
        putSensorString(map, sensor);
        genStringAndShowToLast(map);
    }


    private void genStringAndShowToLast(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            mStringBuilder.append(entry.getKey()).append('=').append(entry.getValue()).append('\n');
        }
        mStringBuilder.append("\n\n");
        mTvTemperatureInfo.setText(mStringBuilder.toString());
    }


    private class BatteryInfoReceiver extends BroadcastReceiver {

        int temp;

        public int getTemp() {
            return temp;
        }

        @Override
        public void onReceive(Context c, Intent intent) {
            temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10;
        }

    }

}
