package com.log.adapter;

import com.log.listener.OnUploadListener;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/6/25<br>
 * Time: 16:02<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class UploadAdapter implements OnUploadListener {
    @Override
    public void onBeforeFileUpload() {

    }

    @Override
    public void onHasNoLogFile() {

    }

    @Override
    public void onFileUploadSuccess(String urlSuffix, String dir, String fileName, int failedCount, int successCount, int total) {

    }

    @Override
    public void onFileUploadFailed(String dir, String fileName, int failedCount, int successCount, int total) {

    }
}
