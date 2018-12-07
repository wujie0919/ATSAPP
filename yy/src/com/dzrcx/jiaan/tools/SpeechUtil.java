package com.dzrcx.jiaan.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.speech.SpeakListener;
import com.dzrcx.jiaan.speech.SpeechService;

/**
 * Created by zhangyu on 16-3-25.
 */
public class SpeechUtil {


    private static SpeechUtil instance;

    public boolean isPeaking = false;

    public static SpeechUtil getInstance() {

        if (instance == null) {
            instance = new SpeechUtil();
        }
        return instance;
    }

    /**
     * 目前只有一个界面有语音播报功能，就不采用map遍历
     *
     * @param mContext
     * @param message
     */
    // private HashMap<String, SpeakListener> speakListeners = new HashMap(String name, SpeakListener speak);

    private SpeakListener speakListener;
    private SpeackBroad speackBroad;

    public void peechMessage(Context mContext, String message) {
        initBroad();
        Intent intent = new Intent(mContext, SpeechService.class);
        intent.putExtra("type", 1);
        intent.putExtra("message", message);
        mContext.startService(intent);

    }

    public void stop(Context mContext){
        initBroad();
        Intent intent = new Intent(mContext, SpeechService.class);
        intent.putExtra("type", 2);
        mContext.startService(intent);
    }

    public void closed(Context mContext) {
        isPeaking = false;
        Intent intent = new Intent(mContext, SpeechService.class);
        mContext.stopService(intent);
        desBroad();
    }


    private void initBroad() {
        if (speackBroad == null) {
            speackBroad = new SpeackBroad();
            IntentFilter filter = new IntentFilter();
            filter.addAction("yyzc_speak_begin");
            filter.addAction("yyzc_speak_paused");
            filter.addAction("yyzc_speak_resumed");
            filter.addAction("yyzc_speak_completed");
            YYApplication.getApplication().registerReceiver(speackBroad, filter);
        }

    }


    private void desBroad() {
        if (speackBroad != null) {
            YYApplication.getApplication().unregisterReceiver(speackBroad);
            speackBroad = null;
        }

    }

    public void setSpeakListener(SpeakListener speakListener) {
        this.speakListener = speakListener;
    }

    private class SpeackBroad extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("yyzc_speak_begin".equals(action) && speakListener != null) {
                isPeaking = true;
                speakListener.onSpeakBegin();
            } else if ("yyzc_speak_paused".equals(action) && speakListener != null) {
                isPeaking = false;
                speakListener.onSpeakPaused();
            } else if ("yyzc_speak_resumed".equals(action) && speakListener != null) {
                isPeaking = true;
                speakListener.onSpeakResumed();
            } else if ("yyzc_speak_completed".equals(action) && speakListener != null) {
                isPeaking = false;
                speakListener.onCompleted();
            }
        }
    }
}
