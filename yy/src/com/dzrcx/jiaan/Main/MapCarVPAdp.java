package com.dzrcx.jiaan.Main;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.MapcarfrgCarVo;
import com.dzrcx.jiaan.Constans.YYOptions;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.widget.CustomRoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 地图的maocarfragment  底部layout的viewpager适配器
 */
public class MapCarVPAdp extends PagerAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<MapcarfrgCarVo> mapcarfrgCarVos;
    private Dialog messageDlg;
    private View.OnClickListener clickListener;

    public MapCarVPAdp(Activity activity, ArrayList<MapcarfrgCarVo> mapcarfrgCarVos, View.OnClickListener clickListener) {
        this.mapcarfrgCarVos = mapcarfrgCarVos;
        this.activity = activity;
        inflater = activity.getLayoutInflater();
        this.clickListener = clickListener;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (mapcarfrgCarVos == null) {
            return 0;
        } else {
            return mapcarfrgCarVos.size();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View childView = inflater.inflate(R.layout.item_mapcarvpadp, null);
        CustomRoundImageView cv_carphoto = (CustomRoundImageView) childView.findViewById(R.id.cv_carphoto);
        TextView tv_car_license = (TextView) childView.findViewById(R.id.tv_car_license);
        TextView tv_elctric = (TextView) childView.findViewById(R.id.tv_elctric);
        TextView tv_price = (TextView) childView.findViewById(R.id.tv_price);
        TextView tv_acount = (TextView) childView.findViewById(R.id.tv_acount);
        TextView tv_startpricetxt = (TextView) childView.findViewById(R.id.tv_startpricetxt);
        ImageView iv_carphoto_float = (ImageView) childView.findViewById(R.id.iv_carphoto_float);

        TextView tv_price_2 = (TextView) childView.findViewById(R.id.tv_price_2);
        TextView tv_acount_2 = (TextView) childView.findViewById(R.id.tv_acount_2);
        TextView tv_startpricetxt_2 = (TextView) childView.findViewById(R.id.tv_startpricetxt_2);
//        final RelativeLayout rl_pricetxt=(RelativeLayout) childView.findViewById(R.id.rl_pricetxt);
//        final RelativeLayout rl_pricetxt_2=(RelativeLayout) childView.findViewById(R.id.rl_pricetxt_2);
//        rl_pricetxt_2.setVisibility(View.GONE);
//        rl_pricetxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(rl_pricetxt_2.getVisibility() == View.GONE){
//                    rl_pricetxt_2.setVisibility(View.VISIBLE);
//                }else{
//                    rl_pricetxt_2.setVisibility(View.GONE);
//                }
//            }
//        });

        iv_carphoto_float.setOnClickListener(clickListener);
//        /**
//         * 填充数据
//         */
        MapcarfrgCarVo mapcarfrgCarVo = mapcarfrgCarVos.get(position);
        ImageLoader.getInstance().displayImage(mapcarfrgCarVo.getCarMainPhoto(),
                cv_carphoto, YYOptions.Option_CARITEM);
        tv_car_license.setText(mapcarfrgCarVo.getBrand() + " " + mapcarfrgCarVo.getSeries() + " · " + mapcarfrgCarVo.getLicense());
        tv_elctric.setText((int) mapcarfrgCarVo.getMileage() + "公里");
        //分时用车
//        String price = "<font color='#04b575'>" +
//                MyUtils.formatPriceShort(mapcarfrgCarVo.getWorkDayPrice()).trim() +
//                "</font>" + "<font color='#3d3f42'>元/时 +</font>" + "<font color='#04b575'>" + MyUtils.formatPriceShort(mapcarfrgCarVo.getMileagePrice()).trim() + "</font>" + "<font color='#3d3f42'>元/公里</font>";
//        tv_price.setText(Html.fromHtml(price));
//        String startPricetxt = "<font color='#a6a6a6'>起步价</font>" + "<font color='#04b575'>" + MyUtils.formatPriceShort(mapcarfrgCarVo.getStartPrice()).trim() + "</font>" + "<font color='#a6a6a6'>元，按时间+里程计费，限时" + MyUtils.formatPriceShort(mapcarfrgCarVo.getDiscount()).trim() + "折</font>";
//        tv_startpricetxt.setText(Html.fromHtml(startPricetxt));
        String price = "分时用车最低一小时起租";
        tv_price.setText(price);
        String startPricetxt = "<font color='#04b575'>" +
                MyUtils.formatPriceShort(mapcarfrgCarVo.getWorkDayPrice()).trim()
                + "</font>" + "<font color='#04b575'>元/时</font>" + "<font color='#04b575'>"
                + "("
                + MyUtils.formatPriceShort(mapcarfrgCarVo.getDiscount()).trim() + "折后"
                + ")"
                + "+"
                + MyUtils.formatPriceShort(mapcarfrgCarVo.getMileagePrice()).trim()
                + "</font>" + "<font color='#04b575'>元/公里</font>";
        tv_startpricetxt.setText(Html.fromHtml(startPricetxt));
        //按日用车
//        String price2 = "<font color='#04b575'>" +
//                MyUtils.formatPriceShort(mapcarfrgCarVo.getWorkDayPrice()).trim() +
//                "</font>" + "<font color='#3d3f42'>元/时 +</font>" + "<font color='#04b575'>" + MyUtils.formatPriceShort(mapcarfrgCarVo.getMileagePrice()).trim() + "</font>" + "<font color='#3d3f42'>元/公里</font>";
//        tv_price_2.setText(price2);
//        String startPricetxt2 = "<font color='#a6a6a6'>起步价</font>" + "<font color='#04b575'>" + MyUtils.formatPriceShort(mapcarfrgCarVo.getStartPrice()).trim() + "</font>" + "<font color='#a6a6a6'>元，按时间+里程计费，限时" + MyUtils.formatPriceShort(mapcarfrgCarVo.getDiscount()).trim() + "折</font>";
//        tv_startpricetxt_2.setText(Html.fromHtml(startPricetxt2));
        String price2 = "按日(24h)用车价格更合算";
        tv_price_2.setText(price2);
        String startPricetxt2 = "<font color='#04b575'>"
                + MyUtils.formatPriceShort(mapcarfrgCarVo.getDailyRentalPrice()).trim()
                + "</font>"
                + "<font color='#04b575'>元/天不计里程费</font>";
        tv_startpricetxt_2.setText(Html.fromHtml(startPricetxt2));
        view.addView(childView);
        return childView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return POSITION_NONE;
    }

    public void replace(ArrayList<MapcarfrgCarVo> mapcarfrgCarVos) {
        this.mapcarfrgCarVos = mapcarfrgCarVos;
    }

    public ArrayList<MapcarfrgCarVo> getCarsData() {
        return mapcarfrgCarVos;
    }

    private void showPopuWindow(View v) {
        View view = inflater.inflate(R.layout.showinfo_layout, null);
        final PopupWindow popupWindow = new PopupWindow(view,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView textView = (TextView) view.findViewById(R.id.showinfo_text);
        textView.setText((String) v.getTag());
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = view.getMeasuredWidth();
        int popupHeight = view.getMeasuredHeight();
        int[] location = new int[2];
        // 允许点击外部消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // 获得位置
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
                (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1]
                        - popupHeight + 20);
    }


    /**
     * 打开拨打客服电话弹窗
     */
    public void showMessageDialog(String messge, String buttonstr) {
        if (messageDlg == null) {
            messageDlg = new Dialog(activity, R.style.MyDialog);
            View mDlgCallView = LayoutInflater.from(activity).inflate(
                    R.layout.dlg_message, null);
            TextView tv_cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_do_txt);
            tv_cancel_txt.setText(buttonstr);
            tv_cancel_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    messageDlg.dismiss();
                    if ("Identification".equals(activity)) {
                        activity.finish();
                    }
                }
            });
            final TextView tv_number = (TextView) mDlgCallView
                    .findViewById(R.id.tv_message);
            tv_number.setText(messge);
            messageDlg.setCanceledOnTouchOutside(false);
            messageDlg.setContentView(mDlgCallView);
        }
        messageDlg.show();

        Window dlgWindow = messageDlg.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(activity) / 10 * 7;
        dlgWindow.setAttributes(lp);

    }

}
