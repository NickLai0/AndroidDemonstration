package com.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    public static void zip(String src, String dest) throws IOException {
        File file = new File(src);
        zip(file.getParent(), file.getName(), new File(dest));
    }

    public static void zip(String rootDir, String fileName, File dest) throws IOException {
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(dest));
            zip(rootDir, fileName, out);
        } finally {
            IOUtils.close(out);
        }
    }

    public static void zip(String rootDir, String fileName, ZipOutputStream out) throws IOException {
        File file = new File(rootDir, fileName);
        if (file.isFile()) {
            FileInputStream in = new FileInputStream(file);
            byte[] buf = new byte[8 * 1024];
            int len;
            out.putNextEntry(new ZipEntry(fileName));
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            IOUtils.close(in);
        } else {
            String[] fileNameArr = file.list();
            if (fileNameArr == null || fileNameArr.length == 0) {
                out.putNextEntry(new ZipEntry(fileName));
                out.closeEntry();
            } else {
                for (int i = 0; i < fileNameArr.length; i++) {
                    zip(rootDir, fileName + File.separator + fileNameArr[i], out);
                }
            }
        }
    }

    private static void unzip(String zipPath, String destDir) throws IOException {
        ZipInputStream in = new ZipInputStream(new FileInputStream(zipPath));
        ZipEntry zipEntry = null;
        try {
            byte[] buf = new byte[8 * 1024];
            int len;
            while ((zipEntry = in.getNextEntry()) != null) {
                File file = new File(destDir, zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    file.mkdirs();
                } else {
                    FileOutputStream out = null;
                    try {
                        //Make sure the parent folder exist.
                        file.getParentFile().mkdirs();
                        out = new FileOutputStream(file);
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                    } finally {
                        IOUtils.close(out);
                    }
                }
                in.closeEntry();
            }
        } finally {
            IOUtils.close(in);
        }
    }

}