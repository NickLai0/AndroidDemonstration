package com.log.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/6/25<br>
 * Time: 20:37<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class ExceptionUtils {

    public static String getStackTrace(Throwable t) {
        if (t == null) {
            return "Throwable is null";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            t.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }

}
