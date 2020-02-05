package com.wav.handler;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.wav.util.LogUtils;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/1/4<br>
 * Time: 9:54<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class AudioRecordHandler extends Thread {

    private final String TAG = AudioRecordHandler.class.getSimpleName();

    private AudioRecord mAudioRecord;

    private int mAudioSource;
    private int mSampleRate;
    private int mChannels;
    private int mAudioEncoding;
    private int mMinBufferSize;

    private int mMaxVolume = 50;

    /**
     * Handle frequency.
     * The unit is milliseconds.
     */
    private int mHandleFrequency = 200;
    private boolean mIsCheckVolume;

    public AudioRecordHandler() {
        this(MediaRecorder.AudioSource.MIC, 16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
    }

    public AudioRecordHandler(int audioSource, int sampleRate, int channels, int audioEncoding) {
        mAudioSource = audioSource;
        mSampleRate = sampleRate;
        mChannels = channels;
        mAudioEncoding = audioEncoding;
        LogUtils.logI(TAG,
                "AudioRecordHandler -> init constructor " +
                        "audioSource : " + mAudioSource +
                        ", mSampleRate : " + mSampleRate +
                        ", mChannels : " + mChannels + ", " +
                        "mAudioEncoding : " + mAudioEncoding
        );
    }


    @Override
    public void run() {
        try {
            if (mOnRecordingListener != null) {
                mOnRecordingListener.onStart();
            }
            LogUtils.logI(TAG, "run -> thread started, initial AudioRecord and start recording。");
            // Get the minimum buffer size required for the successful creation of
            // an AudioRecord object.
            mMinBufferSize = AudioRecord.getMinBufferSize(mSampleRate, mChannels, mAudioEncoding);
            // Initialize Audio Recorder.
            mAudioRecord = new AudioRecord(mAudioSource, mSampleRate, mChannels, mAudioEncoding, mMinBufferSize);
            mAudioRecord.startRecording();
            byte[] buf = new byte[mMinBufferSize];
            while (mIsRecording) {
                int bufferReadResult = mAudioRecord.read(buf, 0, mMinBufferSize);
                if (bufferReadResult <= 0) {
                    LogUtils.logE(TAG, "run -> The bufferReadResult is " + bufferReadResult + ", so continue for next read.");
                    continue;
                }

                if (mOnRecordingListener != null) {
                    mOnRecordingListener.onDataRead(buf, bufferReadResult);
                }

                if (mIsCheckVolume) {
                    long sumLevel = 0;
                    for (int i = 0; i < bufferReadResult; i++) {
                        sumLevel += Math.abs(buf[i]);
                    }
                    int averageVolume = (int) (sumLevel * 1.0f / bufferReadResult + 0.5);
                    mMaxVolume = Math.max(averageVolume, mMaxVolume);
                    //resultVolume : [0-100]
                    int resultVolume = (int) (averageVolume * 1.0f / mMaxVolume * 100 + 0.5);
                    LogUtils.logT(TAG,
                            "run -> on recording; " +
                                    "averageVolume : " + averageVolume
                                    + ", bufferReadResult : " + bufferReadResult
                                    + "， sumLevel : " + sumLevel
                                    + ", resultVolume : " + resultVolume
                                    + ", mMaxVolume : " + mMaxVolume
                    );
                    if (mOnRecordingListener != null) {
                        mOnRecordingListener.onVolumeChanged(resultVolume);
                    }
                }
                if (mHandleFrequency > 0) {
                    //Recording audio data slowly.
                    Thread.sleep(mHandleFrequency);
                }
            }
        } catch (Throwable t) {
            if (mOnRecordingListener != null) {
                mOnRecordingListener.onException(t);
            }
        } finally {
            LogUtils.logI(TAG, "run -> release audio record.");
            if (mAudioRecord != null) {
                mAudioRecord.release();
                mAudioRecord = null;
            }
        }
        if (mOnRecordingListener != null) {
            mOnRecordingListener.onEnd();
        }
        LogUtils.logI(TAG, "run -> thread stopping。");
    }

    private boolean mIsRecording = false;

    public synchronized void startRecording() {
        if (!mIsRecording) {
            LogUtils.logI(TAG, "startRecording -> start thread, mIsRecording : " + mIsRecording);
            super.start();
            mIsRecording = true;
        }
    }

    public synchronized void stopRecording() {
        if (mIsRecording) {
            LogUtils.logI(TAG, "stopRecording -> mIsRecording : " + mIsRecording);
            mIsRecording = false;
        }
    }

    /**
     * @param millis millis <= 0 handle recording data for real time.
     *               millis > 0 handle recording data per millis;
     */
    public void setHandleFrequency(int millis) {
        mHandleFrequency = millis;
    }

    public void setIsCheckVolume(boolean checkVolume) {
        mIsCheckVolume = checkVolume;
    }

    private OnRecordingListener mOnRecordingListener;

    public void setOnRecordingListener(OnRecordingListener l) {
        mOnRecordingListener = l;
    }

    public interface OnRecordingListener {

        /**
         * @param volume //resultVolume : [0-100]
         */
        void onVolumeChanged(int volume);

        void onStart();

        void onDataRead(byte[] buf, int bufferReadResult);

        void onException(Throwable t);

        void onEnd();

    }

}
