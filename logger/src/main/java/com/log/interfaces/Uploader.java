package com.log.interfaces;

import com.log.listener.OnUploadListener;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/6/18<br>
 * Time: 21:15<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public interface Uploader extends StartStop {

    void check(String entrance);

    void notifyUpload(String entrance);

    void upload(String entrance);

    void addOnUploadListener(OnUploadListener listener);

    void removeOnUploadListener(OnUploadListener listener);

}
