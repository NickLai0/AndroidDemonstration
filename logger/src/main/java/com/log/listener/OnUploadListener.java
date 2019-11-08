package com.log.listener;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/6/20<br>
 * Time: 19:56<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public interface OnUploadListener {

    /**
     * Before the file upload event performed.
     * This would be call back.
     * Notice that this would called in
     * the work thread.
     */
    void onBeforeFileUpload();

    /**
     * Has no log file in the waiting
     * upload directory.
     */
    void onHasNoLogFile();

    /**
     * Every time the file uploaded success
     * that this would be call back.
     */
    void onFileUploadSuccess(String dir, String fileName, int failedCount, int successCount, int total);

    /**
     * Every time the file uploaded failed
     * that this would be call back.
     */
    void onFileUploadFailed(String dir, String fileName, int failedCount, int successCount, int total);

}
