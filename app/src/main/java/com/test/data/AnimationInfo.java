package com.test.data;

import android.view.View;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/12/25<br>
 * Time: 14:15<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class AnimationInfo implements Cloneable {

    public View v;
    public int startX;
    public int startY;
    public int currentX;
    public int currentY;
    public int endX;
    public int endY;
    public long startMillis;
    public int durationMillis;
    public double spentMillisPercent;

    public void set(AnimationInfo ai) {
        if (ai == null) {
            return;
        }
        this.v = ai.v;
        this.startX = ai.startX;
        this.startY = ai.startY;
        this.currentX = ai.currentX;
        this.currentY = ai.currentY;
        this.endX = ai.endX;
        this.endY = ai.endY;
        this.startMillis = ai.startMillis;
        this.durationMillis = ai.durationMillis;
        this.spentMillisPercent = ai.spentMillisPercent;
    }

}
