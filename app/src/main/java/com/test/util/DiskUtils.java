package com.test.util;

import android.os.Environment;
import android.os.StatFs;

import java.text.DecimalFormat;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/5/28<br>
 * Time: 20:44<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class DiskUtils {

    /*************************************************************************************************
     Returns size in bytes.

     If you need calculate external memory, change this:
     StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
     to this:
     StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
     **************************************************************************************************/
    public static long internalTotalBytes() {
        return getTotalBytes(Environment.getRootDirectory().getAbsolutePath());
    }

    public static long internalFreeBytes() {
        return getFreeBytes(Environment.getRootDirectory().getAbsolutePath());
    }

    public static long internalBusyBytes() {
        return getBusyBytes(Environment.getRootDirectory().getAbsolutePath());
    }

    public static long externalTotalBytes() {
        return getTotalBytes(Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    public static long externalFreeBytes() {
        return getFreeBytes(Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    public static long externalBusyBytes() {
        return getBusyBytes(Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    public static long getTotalBytes(String path) {
        StatFs statFs = new StatFs(path);
        long total = (statFs.getBlockCount() * 1L * statFs.getBlockSize());
        return total;
    }

    public static long getFreeBytes(String path) {
        StatFs statFs = new StatFs(path);
        long free = (statFs.getAvailableBlocks() * 1L * statFs.getBlockSize());
        return free;
    }

    public static long getBusyBytes(String path) {
        StatFs statFs = new StatFs(path);
        long total = (statFs.getBlockCount() * 1L * statFs.getBlockSize());
        long free = (statFs.getAvailableBlocks() * 1L * statFs.getBlockSize());
        long busy = total - free;
        return busy;
    }

    public static String floatForm(double d) {
        return new DecimalFormat("#.##").format(d);
    }

    public static String bytesToHuman(long size) {
        long Kb = 1 * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size < Kb) return floatForm(size) + " byte";
        if (size >= Kb && size < Mb) return floatForm((double) size / Kb) + " Kb";
        if (size >= Mb && size < Gb) return floatForm((double) size / Mb) + " Mb";
        if (size >= Gb && size < Tb) return floatForm((double) size / Gb) + " Gb";
        if (size >= Tb && size < Pb) return floatForm((double) size / Tb) + " Tb";
        if (size >= Pb && size < Eb) return floatForm((double) size / Pb) + " Pb";
        if (size >= Eb) return floatForm((double) size / Eb) + " Eb";

        return "???";
    }

}
