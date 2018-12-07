package com.dzrcx.jiaan.speech;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

/**
 * Created by zhangyu on 16-3-25.
 */
public class SpeechService extends Service {

    private SpeechSynthesizer mTts;
    private Context mContext;

    private String lastMessage = "";


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getBaseContext();
        initSpeech();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (intent==null){
            return super.onStartCommand(intent, flags, startId);
        }

        int type = intent.getIntExtra("type", 0);
        switch (type) {
            case 1:
                String message = intent.getStringExtra("message");
                speak(message);
                break;
            case 2:
                push();
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private boolean initSpeech() {
        if (mTts == null) {
            mTts = SpeechSynthesizer.createSynthesizer(mContext, null);
            mTts.setParameter(SpeechConstant.VOICE_NAME, "vixy");//设置发音人
            mTts.setParameter(SpeechConstant.SPEED, "70");//设置语速
            mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
            //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
            //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
            //如果不需要保存合成音频，注释该行代码
            mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        }

        if (mTts == null) {
            return false;
        } else {
            return true;
        }
    }


    private SynthesizerListener mSynListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {

            sendBroadcast(new Intent("yyzc_speak_begin"));

        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        @Override
        public void onSpeakPaused() {
            sendBroadcast(new Intent("yyzc_speak_paused"));
        }

        @Override
        public void onSpeakResumed() {
            sendBroadcast(new Intent("yyzc_speak_resumed"));
        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {
            sendBroadcast(new Intent("yyzc_speak_completed"));
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };


    private void speak(String message) {
        if (!TextUtils.isEmpty(message) && initSpeech()) {

            if (message.equals(lastMessage) && mTts.isSpeaking()) {
                mTts.stopSpeaking();
                sendBroadcast(new Intent("yyzc_speak_completed"));
            } else {
                lastMessage = message;
                mTts.startSpeaking(message, mSynListener);
            }

        }
    }

    private void push() {
        if (initSpeech()) {
            sendBroadcast(new Intent("yyzc_speak_completed"));
            mTts.stopSpeaking();
        }
    }
}
