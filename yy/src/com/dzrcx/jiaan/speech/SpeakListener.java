package com.dzrcx.jiaan.speech;

/**
 * Created by zhangyu on 16-4-7.
 */
public interface SpeakListener {

    public void onSpeakBegin();

    public void onSpeakPaused();

    public void onSpeakResumed();

    public void onCompleted();
}
