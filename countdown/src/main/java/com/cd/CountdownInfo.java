package com.cd;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/1/9<br>
 * Time: 11:51<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class CountdownInfo {

     Object tag;

    long startMillis;
    long endMillis;
    long spentMillis;
    long duration;

    double spentMillisPercent;

    int frameFrequency;
    int frameCount;

    OnCountdownListener subThreadListener;
    OnCountdownListener mainThreadListener;

    public long getSpentMillis() {
        return spentMillis;
    }

    public long getDuration() {
        return duration;
    }

    public double getSpentMillisPercent() {
        return spentMillisPercent;
    }

    public Object getTag() {
        return tag;
    }
}
