package com.dzrcx.jiaan.User;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.ComplainVo;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;

import java.util.ArrayList;

/**
 * Created by zhangyu on 16-10-18.
 */
public class ChooseSeciveTypeAdp extends BaseAdapter {


    private YYBaseFragment baseFragment;
    private LayoutInflater inflater;

    private ArrayList<ComplainVo> messageList;

    private ItemClick itemClick;

    public ChooseSeciveTypeAdp(YYBaseFragment baseFragment) {
        this.baseFragment = baseFragment;
        inflater = LayoutInflater.from(baseFragment.mContext);
        messageList = new ArrayList<ComplainVo>();
        itemClick = new ItemClick();
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
            convertView = inflater.inflate(R.layout.item_choosesecive, null);
            viewHold = new ViewHold();
            viewHold.text = (TextView) convertView.findViewById(R.id.chooseservice_name);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        ComplainVo messageVO = messageList.get(position);
        viewHold.text.setText(messageVO.getComplainName());
        viewHold.text.setTag(messageVO);
        viewHold.text.setOnClickListener(itemClick);

        return convertView;
    }


    public ArrayList<ComplainVo> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<ComplainVo> messageList) {
        this.messageList = messageList;
    }

    private class ViewHold {

        private TextView text;

    }


    private class ItemClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            ComplainVo complainVo = (ComplainVo) v.getTag();
            if (complainVo != null) {
                Intent intent = new Intent();
                intent.putExtra("ComplainVo", complainVo);
                baseFragment.getActivity().setResult(Activity.RESULT_OK, intent);
                baseFragment.getActivity().finish();
            }
        }
    }

}
