package com.test.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 解决Double运算精度问题
 *
 * @title DoubleUtils.java
 * @date 2018年2月1日
 */
public class DoubleUtils {

    /**
     * @title 解决double加法精度问题
     */
    public static double add(Double... doubles) {
        BigDecimal result = new BigDecimal(0);
        for (Double a : doubles) {
            result = result.add(new BigDecimal(String.valueOf(a)));
        }
        return result.doubleValue();
    }

    /**
     * @param minuend    被减数
     * @param subtractor 减数
     * @title 解决double减法精度问题
     */
    public static double subtract(Double minuend, Double subtractor) {
        return new BigDecimal(String.valueOf(minuend))
                .subtract(new BigDecimal(String.valueOf(subtractor)))
                .doubleValue();
    }

    /**
     * @title 解决double乘法精度问题
     */
    public static double multiply(Double... doubles) {
        BigDecimal result = new BigDecimal(1);
        for (Double a : doubles) {
            result = result.multiply(new BigDecimal(String.valueOf(a)));
        }
        return result.doubleValue();
    }

    /**
     * @param dividend     被除数
     * @param divisor      除数
     * @param scale        保留小数位数
     * @param roundingMode 小数保留模式
     * @title 解决double除法精度问题
     */
    public static double divide(Double dividend, Double divisor, int scale, RoundingMode roundingMode) {
        BigDecimal result = null;
        result = new BigDecimal(String.valueOf(dividend)).divide(new BigDecimal(String.valueOf(divisor)), scale, roundingMode);
        return result.doubleValue();
    }

}
