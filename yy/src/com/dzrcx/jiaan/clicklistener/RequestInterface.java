package com.dzrcx.jiaan.clicklistener;

public interface RequestInterface {
    void onError(int tag, String error);

    void onComplete(int tag, String json);

    void onLoading(long count, long current);
}
