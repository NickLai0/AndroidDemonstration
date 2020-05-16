package com.test.mvp.presenter;

import com.test.mvp.model.MessageModel;
import com.test.mvp.view.ITestView;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/4/21<br>
 * Time: 11:45<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestPresenter extends BasePresenter {

    private ITestView mTestView;

    private MessageModel mMessageModel;

    public TestPresenter(ITestView iTestView) {
        mTestView = iTestView;
        mMessageModel = new MessageModel();
    }

    public void sendMessage(String msg) {
        boolean isSendSuccess = mMessageModel.sendMessage(msg);
        if (isSendSuccess) {
            mTestView.clearMessage();
        } else {
            mTestView.notifyMessageFailed();
        }
    }

}
