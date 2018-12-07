package com.dzrcx.jiaan.User;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.tools.MyUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zhangyu on 16-8-19.
 */
public class TalkComplainAdp extends BaseAdapter {


    private YYBaseFragment baseFragment;
    private ArrayList<String> imgs;

    private LayoutInflater inflater;


    private RemovedClick removedClick;
    private AddImageClick addImageClick;

    private int wid, pad;


    public TalkComplainAdp(YYBaseFragment baseFragment) {
        this.baseFragment = baseFragment;
        this.inflater = LayoutInflater.from(baseFragment.mContext);
        removedClick = new RemovedClick();
        addImageClick = new AddImageClick();
        wid = MyUtils.getScreenWidth(baseFragment.mContext) / 3;
        pad = MyUtils.dip2px(baseFragment.mContext, 15);
    }

    public YYBaseFragment getBaseFragment() {
        return baseFragment;
    }

    public void setBaseFragment(YYBaseFragment baseFragment) {
        this.baseFragment = baseFragment;
    }

    public ArrayList<String> getImgs() {
        if (imgs == null) {
            imgs = new ArrayList<String>();
        }
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    @Override
    public int getCount() {
        return (imgs == null || imgs.size() == 0) ? 1 : imgs.size() >= 6 ? 6 : imgs.size() + 1;
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
            convertView = inflater.inflate(R.layout.item_talk, null);
            viewHold = new ViewHold();
            viewHold.layout = (RelativeLayout) convertView.findViewById(R.id.item_talk_layout);
            viewHold.addImage = (ImageView) convertView.findViewById(R.id.item_talk_add);
            viewHold.remoImage = (ImageView) convertView.findViewById(R.id.item_talk_remove);
            viewHold.remoImage.setOnClickListener(removedClick);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(wid, wid - pad);
            viewHold.layout.setPadding(pad, pad, pad, 0);
            viewHold.layout.setLayoutParams(layoutParams);

            convertView.setTag(viewHold);

        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        if (parent.getChildCount() == position) {


            viewHold.addImage.setImageResource(R.drawable.icon_add);
            if (position == 0) {
                if (imgs == null) {
                    imgs = new ArrayList<String>();
                }
            }

            viewHold.remoImage.setTag(position);

            if (position <= imgs.size() - 1) {
                String imageurl = imgs.get(position);
                ImageLoader.getInstance().displayImage(imgs.get(position), viewHold.addImage);
                viewHold.remoImage.setVisibility(View.VISIBLE);
                viewHold.addImage.setOnClickListener(null);
            } else {
                viewHold.addImage.setImageResource(R.drawable.icon_add);
                viewHold.remoImage.setVisibility(View.GONE);
                viewHold.addImage.setOnClickListener(addImageClick);
            }

        }
        return convertView;
    }


    private class ViewHold {

        private RelativeLayout layout;
        private ImageView addImage, remoImage;


    }


    class RemovedClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Integer index = (Integer) v.getTag();

            if (index != null) {
                imgs.remove((int) index);
                TalkComplainAdp.this.notifyDataSetChanged();
            }

        }
    }


    class AddImageClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (baseFragment != null && baseFragment instanceof TalkComplainFrg) {
                ((TalkComplainFrg) baseFragment).addImage();
            }
        }
    }


    public String getImages() {
        StringBuffer sb = new StringBuffer();
        if (imgs != null) {
            for (int x = 0; x < imgs.size(); x++) {
                sb.append(imgs.get(x));
                if (x != imgs.size() - 1) {
                    sb.append(";");
                }
            }
        }
        return sb.toString();
    }

}
