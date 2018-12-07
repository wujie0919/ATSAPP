package com.dzrcx.jiaan.Bean;

import java.util.ArrayList;

/**
 * Created by zhangyu on 16-10-18.
 */
public class MessageListBean extends YYBaseResBean {

    private ArrayList<MessageVO> returnContent;

    public ArrayList<MessageVO> getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(ArrayList<MessageVO> returnContent) {
        this.returnContent = returnContent;
    }
}
