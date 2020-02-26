package com.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtils {

    public static void copy(String source, String dest) throws IOException {
        copy(new File(source), new File(dest));
    }

    public static void copy(File src, File dest) throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dest);
            copy(in, out);
        } finally {
            IOUtils.close(in, out);
        }
    }

    public static void copy(FileInputStream in, FileOutputStream out) throws IOException {
        copyStyle1(in, out);
    }

    private static void copyStyle1(FileInputStream in, FileOutputStream out) throws IOException {
        byte[] buf = new byte[8 * 1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.flush();
    }

    private static void copyStyle2(FileInputStream in, FileOutputStream out) throws IOException {
        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        //outChannel.transferFrom(inChannel, 0, inChannel.size());
    }

}
