package com.log.interfaces;

import com.log.listener.OnLogRefreshListener;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/6/18<br>
 * Time: 21:15<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public interface Logger extends StartStop {

    /**
     * Temporary log,It wouldn't record
     * the logs into local file when
     * isDebug flag is false.
     */
    void logT(String tag, String msg);

    /**
     * Information log,It would record
     * the logs into local file when
     */
    void logI(String tag, String msg);

    /**
     * log .
     */
    void logE(String tag, String msg);

    void refreshLogRequest(OnLogRefreshListener l);

}
