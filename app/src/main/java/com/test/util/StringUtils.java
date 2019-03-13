package com.test.util;

import java.util.Locale;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/3/13<br>
 * Time: 1:11<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class StringUtils {

    public static String format(String format, Object... args) {
        return String.format(Locale.getDefault(), format, args);
    }

    /**
     * double convert to string that like 1.0 -> 1, 1.10 -> 1.0 or 3.140 -> 3.14
     *
     * @param d double data type
     * @return remove last decimal point zero(s)
     */
    public static String doubleToString(double d) {
        String[] temp = (d + "").split("\\.");
        String result = null;
        if (temp.length == 1) {
            result = temp[0];
        } else {
            int zeroCount = 0;
            String decimalPoints = temp[1];
            for (int index = decimalPoints.length() - 1; index >= 0; index--) {
                if (decimalPoints.charAt(index) == '0') {
                    zeroCount++;
                } else {
                    break;
                }
            }
            if (zeroCount == 0) {
                result = temp[0] + "." + temp[1];
            } else if (decimalPoints.length() == zeroCount) {
                result = temp[0];
            } else if (decimalPoints.length() > zeroCount) {
                result = temp[0] + "." + temp[1].substring(0, temp[1].length() - zeroCount);
            } else {
                //Exception situation.Normally it's never go to this.
            }
        }
        return result;
    }

}
