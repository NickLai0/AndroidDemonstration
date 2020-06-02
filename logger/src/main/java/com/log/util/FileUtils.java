package com.log.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/6/20<br>
 * Time: 15:43<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class FileUtils {

    public static void checkDirs(File... dirs) {
        if (dirs == null) {
            return;
        }
        for (File dir : dirs) {
            checkDir(dir);
        }
    }

    public static boolean checkDir(File dir) {
        if (dir == null) {
            return false;
        }
        boolean isDirExists =  dir.exists();
        if(!isDirExists) {
            isDirExists = dir.mkdirs();
        }
        return isDirExists;
    }

    /**
     * 帮组关闭任何Closeable接口子对象
     *
     * @param closeables
     */

    public static void close(Closeable... closeables) {
        if (closeables == null || closeables.length == 0) {
            return;
        }
        for (Closeable c : closeables) {
            try {
                if (c == null) {
                    continue;
                }
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean deleteFile(File file) {
        if (file == null) {
            return false;
        }
        return file.delete();
    }

    public static boolean createNewFile(File file) {
        if (file == null) {
            return false;
        }
        checkDirs(file.getParentFile());
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
