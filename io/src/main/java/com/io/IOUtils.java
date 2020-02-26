package com.io;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {

    public static void close(Closeable... closeableArr) {
        if (closeableArr == null) {
            return;
        }
        for (Closeable c : closeableArr) {
            if (c == null) {
                continue;
            }
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
