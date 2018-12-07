package com.dzrcx.jiaan.User;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.MessageVO;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.tools.TimeDateUtil;

import java.util.ArrayList;

/**
 * Created by zhangyu on 16-10-13.
 */
public class MessageCentreAdp extends BaseAdapter {


    private YYBaseFragment baseFragment;
    private LayoutInflater inflater;

    private ArrayList<MessageVO> messageList;

    public MessageCentreAdp(YYBaseFragment baseFragment) {
        this.baseFragment = baseFragment;
        inflater = LayoutInflater.from(baseFragment.mContext);
        messageList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return messageList == null ? 0 : messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_messagecentre, null);
            viewHold = new ViewHold();
            viewHold.time = (TextView) convertView.findViewById(R.id.item_message_time);
            viewHold.image = (ImageView) convertView.findViewById(R.id.item_message_iamge);
            viewHold.text = (TextView) convertView.findViewById(R.id.item_message_text);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        MessageVO messageVO = messageList.get(position);
        viewHold.time.setText(TimeDateUtil.formatTime(messageVO.getCreateTime(), "yyyy年MM月dd日 HH:mm"));
        viewHold.text.setText(messageVO.getContent());

        return convertView;
    }


    private class ViewHold {

        private TextView time;
        private ImageView image;
        private TextView text;

    }


    public ArrayList<MessageVO> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<MessageVO> messageList) {
        this.messageList = messageList;
    }
}
