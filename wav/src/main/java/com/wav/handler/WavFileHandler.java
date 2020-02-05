package com.wav.handler;

import android.text.TextUtils;

import com.wav.util.WavUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/2/4<br>
 * Time: 16:27<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class WavFileHandler {

    private boolean isInitialized;

    private String mDir;
    private String mFileName;

    private RandomAccessFile mRaf;

    private int mHeaderLength;

    public WavFileHandler(String dir, String fileName) {
        if (TextUtils.isEmpty(dir)) {
            throw new IllegalArgumentException("the dir can not be empty.");
        }
        if (TextUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("the fileName can not be empty.");
        }
        mDir = dir;
        mFileName = fileName;
    }

    public synchronized void init() throws IOException {
        if (isInitialized) {
            return;
        }
        File dir = new File(mDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(mDir, mFileName);
        if (file.exists()) {
            file.delete();
        } else {
            file.createNewFile();
        }
        mRaf = new RandomAccessFile(file, "rw");
        isInitialized = true;
    }

    public synchronized void uninit() {
        if (mRaf != null) {
            try {
                mRaf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRaf = null;
        }
        isInitialized = false;
    }

    public void writeWavHeader(short bitsPerSample, short channels, int sampleRate) throws IOException {
        byte[] wavHeader = WavUtils.makeSimpleWavHeader(bitsPerSample, channels, sampleRate);
        mRaf.write(wavHeader);
        mHeaderLength = wavHeader.length;
    }

    public void write(byte[] data, int offset, int length) throws IOException {
        mRaf.write(data, offset, length);
    }

    public void finalizeWavFile() throws IOException {
        int fileLength = (int) mRaf.length();
        int dataLength = fileLength - mHeaderLength;

        //[4-7]	File size, but the default value is empty.
        //Size of the overall file - 8 bytes, in bytes (32-bit integer). Typically, you'd fill this in after creation.
        byte[] bytes = new byte[4];
        bytes[0] = (byte) fileLength;
        bytes[1] = (byte) (fileLength >> 8);
        bytes[2] = (byte) (fileLength >> 16);
        bytes[3] = (byte) (fileLength >> 24);
        mRaf.seek(4);
        mRaf.write(bytes);

        //40-43	File size (data). Size of the data section.
        //dataSize = fileSize - headerLength;
        bytes[0] = (byte) dataLength;
        bytes[1] = (byte) (dataLength >> 8);
        bytes[2] = (byte) (dataLength >> 16);
        bytes[3] = (byte) (dataLength >> 24);
        mRaf.seek(40);
        mRaf.write(bytes);
    }

}
