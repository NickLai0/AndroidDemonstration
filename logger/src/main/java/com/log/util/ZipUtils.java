package com.log.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/9/1<br>
 * Time: 17:21<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class ZipUtils {

    /**
     * 压缩文件和文件夹
     *
     * @param srcFileString 要压缩的文件或文件夹
     * @param zipFileString 解压完成的Zip路径
     * @throws Exception
     */
    public static void zipFolder(String srcFileString, String zipFileString) throws Exception {
        //创建ZIP
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
        //创建文件
        File file = new File(srcFileString);
        //压缩
        try {
            zipFiles(file.getParent() + File.separator, file.getName(), outZip);
            //完成
            outZip.finish();
        } catch (Exception e) {
            throw e;
        } finally {
            FileUtils.close(outZip);
        }
    }

    /**
     * 压缩文件
     *
     * @param dir
     * @param relatedPath
     * @param zipOutputSteam
     * @throws Exception
     */
    private static void zipFiles(String dir, String relatedPath, ZipOutputStream zipOutputSteam) throws Exception {
        if (zipOutputSteam == null) {
            return;
        }
        File file = new File(dir + relatedPath);
        if (file.isFile()) {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
                zipOutputSteam.putNextEntry(new ZipEntry(relatedPath));
                int len;
                byte[] buffer = new byte[4096];
                while ((len = inputStream.read(buffer)) != -1) {
                    zipOutputSteam.write(buffer, 0, len);
                }
                zipOutputSteam.closeEntry();
            } catch (Exception e) {
                throw e;
            } finally {
                FileUtils.close(inputStream);
            }
        } else {
            //文件夹
            String fileList[] = file.list();
            //没有子文件可压缩
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(relatedPath + File.separator);
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }
            //子文件和递归
            for (int i = 0; i < fileList.length; i++) {
                zipFiles(dir, relatedPath + File.separator + fileList[i], zipOutputSteam);
            }
        }
    }

    /**
     * 压缩一个文件到目标目标zip包下， 目标zip包
     * 文件名称会自动+.zip后缀
     *
     * @param src  需要被压缩的文件
     * @param dest 压缩到此绝对路径下的文件， 会自动添加.zip后缀名
     * @throws Exception 压缩失败抛出的任何可能的异常
     */
    public static void zipFile(File src, File dest) throws Exception {
        if (src == null || !src.exists()) {
            return;
        }
        BufferedInputStream bis = null;
        ZipOutputStream zos = null;
        try {
            bis = new BufferedInputStream(
                    new FileInputStream(src));
            zos = new ZipOutputStream(new FileOutputStream(dest.getAbsolutePath() + ".zip"));
            //创建Zip实体，并添加进压缩包
            ZipEntry entry = new ZipEntry(dest.getName());
            zos.putNextEntry(entry);
            //读取待压缩的文件并写进压缩包里
            final int BUFFER = 1024 * 8;
            byte data[] = new byte[BUFFER];
            int count;
            while ((count = bis.read(data, 0, BUFFER)) > 0) {
                zos.write(data, 0, count);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            FileUtils.close(bis, zos);
        }
    }


}
