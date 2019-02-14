package com.test.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 创建者     Nick
 * 创建时间   2018/7/2 16:21
 * 描述	      获取异常的方法栈跟踪String（格式化后的）
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述
 */

public class ExceptionUtil {

    public static String getStackTrace() {
        return getStackTrace(new RuntimeException("方法栈"));
    }

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
