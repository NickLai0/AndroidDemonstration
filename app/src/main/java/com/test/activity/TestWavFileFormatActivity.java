package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.test.R;
import com.test.app.LogMgr;
import com.test.app.MyApp;
import com.test.util.ExceptionUtil;
import com.wav.handler.AudioRecordHandler;
import com.wav.handler.WavFileHandler;

import java.io.IOException;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/2/4<br>
 * Time: 10:34<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestWavFileFormatActivity extends BaseActivity {

    private final int[] mMicVolumeResIds = new int[]{
            R.drawable.ic_volume_01, R.drawable.ic_volume_02,
            R.drawable.ic_volume_03, R.drawable.ic_volume_04,
            R.drawable.ic_volume_05,
    };

    private final String WAV_DIR = MyApp.i().getFilesDir() + "/wav";
    private final String WAV_NAME = "test.wav";

    private AudioRecordHandler mAudioRecordHandler;

    private Button mBtnRecordingStart;
    private Button mBtnRecordingStop;
    private Button mBtnPlayingStart;
    private Button mBtnPlayingStop;
    private ImageView mIvVolume;

    private final int MSG_VOLUME_CHANGED = 1;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_VOLUME_CHANGED:
                    int index = msg.arg1 / (100 / mMicVolumeResIds.length);
                    if (index == mMicVolumeResIds.length) {
                        index--;
                    }
                    mIvVolume.setImageResource(mMicVolumeResIds[index]);
                    break;
            }
        }
    };

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestWavFileFormatActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_format_file_wav_test;
    }

    @Override
    protected void initView() {
        mBtnRecordingStart = findViewById(R.id.affwt_btn_recording_start);
        mBtnRecordingStop = findViewById(R.id.affwt_btn_recording_stop);
        mBtnPlayingStart = findViewById(R.id.affwt_btn_playing_start);
        mBtnPlayingStop = findViewById(R.id.affwt_btn_playing_stop);
        mIvVolume = findViewById(R.id.affwt_iv_volume);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
        mBtnRecordingStart.setOnClickListener(this);
        mBtnRecordingStop.setOnClickListener(this);
        mBtnPlayingStart.setOnClickListener(this);
        mBtnPlayingStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.affwt_btn_recording_start:
                recordingStart();
                mIvVolume.setVisibility(View.VISIBLE);
                break;

            case R.id.affwt_btn_recording_stop:
                recordingStop();
                mIvVolume.setVisibility(View.GONE);
                break;

            case R.id.affwt_btn_playing_start:
                playingStart();
                break;

            case R.id.affwt_btn_playing_stop:
                playingStop();
                break;
        }
    }

    private short bitsPerSample = 16;
    private short channels = 2;
    private int sampleRate = 48000;

    private void recordingStart() {
        LogMgr.i().logI(TAG, "recordingStart -> start microphone recording handling.");
        recordingStop();

        mAudioRecordHandler = new AudioRecordHandler(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        //real time frequency.
        mAudioRecordHandler.setHandleFrequency(-1);
        mAudioRecordHandler.setIsCheckVolume(true);
        mAudioRecordHandler.setOnRecordingListener(mOnRecordingListener);
        mAudioRecordHandler.startRecording();
    }

    private void recordingStop() {
        LogMgr.i().logI(TAG, "recordingStop -> stop microphone recording handling.");
        if (mAudioRecordHandler != null) {
            //Couldn't set to null immediately.
            //mAudioRecordHandler.setOnRecordingListener(null);
            mAudioRecordHandler.stopRecording();
            mAudioRecordHandler = null;
        }
    }


    private MediaPlayer mMediaPlayer;

    private void playingStart() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, Uri.parse(WAV_DIR + '/' + WAV_NAME));
        }
        mMediaPlayer.start();
    }

    private void playingStop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private AudioRecordHandler.OnRecordingListener mOnRecordingListener = new AudioRecordHandler.OnRecordingListener() {

        private WavFileHandler mWavFileHandler = new WavFileHandler(WAV_DIR, WAV_NAME);

        @Override
        public void onVolumeChanged(int volume) {
            Message.obtain(mHandler, MSG_VOLUME_CHANGED, volume, 0).sendToTarget();
        }

        @Override
        public void onStart() {
            try {
                LogMgr.i().logT(TAG, "onStart -> onRecording");
                mWavFileHandler.init();
                mWavFileHandler.writeWavHeader(bitsPerSample, channels, sampleRate);
            } catch (IOException e) {
                LogMgr.i().logE(TAG, "onStart -> " + ExceptionUtil.getStackTrace(e));
                recordingStop();
            }
        }

        @Override
        public void onDataRead(byte[] buf, int bufferReadResult) {
            try {
                LogMgr.i().logT(TAG, "onDataRead -> bufferReadResult=" + bufferReadResult);
                mWavFileHandler.write(buf, 0, bufferReadResult);
            } catch (IOException e) {
                LogMgr.i().logE(TAG, "onDataRead -> " + ExceptionUtil.getStackTrace(e));
                recordingStop();
            }
        }

        @Override
        public void onException(Throwable t) {
            LogMgr.i().logE(TAG, "onException -> " + ExceptionUtil.getStackTrace(t));
            recordingStop();
        }

        @Override
        public void onEnd() {
            LogMgr.i().logT(TAG, "onEnd -> on recording ended.");
            try {
                mWavFileHandler.finalizeWavFile();
            } catch (IOException e) {
                LogMgr.i().logE(TAG, "onEnd -> " + ExceptionUtil.getStackTrace(e));
                recordingStop();
            }
            mWavFileHandler.uninit();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recordingStop();
        playingStop();
        mHandler.removeCallbacksAndMessages(null);
    }

}
