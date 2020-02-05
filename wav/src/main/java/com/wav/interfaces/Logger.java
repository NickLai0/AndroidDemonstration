package com.wav.interfaces;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/2/4<br>
 * Time: 14:46<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public interface Logger {

    /**
     * Temporary log.
     */
    void logT(String tag, String msg);

    /**
     * Information log
     */
    void logI(String tag, String msg);

    /**
     * Error log.
     */
    void logE(String tag, String msg);

}
