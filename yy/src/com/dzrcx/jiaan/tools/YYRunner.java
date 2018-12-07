package com.dzrcx.jiaan.tools;

import android.text.TextUtils;
import android.util.Log;

import com.dzrcx.jiaan.Bean.YYBaseResBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.clicklistener.RequestInterface;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

public class YYRunner {
    public static String Method_GET = "get";
    public static String Method_POST = "post";

    public static void getData(final int tag, String method, final String url,
                               final Map<String, String> params, final RequestInterface listener) {
        Log.d("getData","url:"+url);
        params.put("version", YYApplication.versionName);
        Set<String> set = params.keySet();
        if (Method_GET.equals(method)) {
            String lastUrl = url;
            if (set.size() > 0)
                for (String s : set) {
                    if (!lastUrl.contains("?")) {
                        lastUrl = url + "?" + s + "=" + params.get(s);

                    } else {
                        lastUrl = lastUrl + "&" + s + "=" + params.get(s);
                    }
                }
            else {
                lastUrl = url;
            }
            LG.d("gtgt", lastUrl + "lastUrl");
            FinalHttp finalHttp = new FinalHttp();
            finalHttp.configTimeout(60000);
            finalHttp.get(lastUrl, new AjaxCallBack<Object>() {
                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    listener.onError(tag, strMsg);
                    LG.d("gtgt", "errorNo---->" + errorNo + "     strMsg----->"
                            + strMsg);
                }

                @Override
                public void onSuccess(Object t) {
                    super.onSuccess(t);
                    listener.onComplete(tag, t == null ? "null" : t.toString());
                    LG.d("gtgt", "----onComplete--->" + t.toString());
                }

                @Override
                public void onLoading(long count, long current) {
                    super.onLoading(count, current);
                    LG.d("gtgt", "-------onLoading----->" + current + "/"
                            + count);
                }

            });
        } else if (Method_POST.equals(method)) {
            FinalHttp finalHttp = new FinalHttp();
            finalHttp.configTimeout(60000);
            AjaxParams params_s = new AjaxParams();
            boolean hasSigned = false;
            for (String string : set) {

                if (!hasSigned && !TextUtils.isEmpty(params.get(string))) {
                    params_s.put("sign", sign(string, params.get(string)));
                    hasSigned = true;
                }
                params_s.put(string, params.get(string));
                if (string.equals("skey")) {
                    params_s.put("userId", YYConstans.getUserInfoBean().getReturnContent().getUser().getUserId() + "");
                }
            }

            finalHttp.post(url, params_s, new AjaxCallBack<Object>() {

                @Override
                public AjaxCallBack<Object> progress(boolean progress, int rate) {
                    // TODO Auto-generated method stub
                    return super.progress(progress, rate);
                }

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    listener.onError(tag, strMsg);
                    LG.d("gtgt", "errorNo---->" + errorNo + "     strMsg----->"
                            + strMsg);
                }

                @Override
                public void onSuccess(Object t) {
                    super.onSuccess(t);
                    YYBaseResBean baseResBean = GsonTransformUtil.fromJson(
                            t.toString(), YYBaseResBean.class);
                    if (baseResBean != null && baseResBean.getErrno() == 9999) {
                        YYConstans.setUserInfoBean(null);
                    }

                    listener.onComplete(tag, t == null ? "null" : t.toString());
                    LG.d("gtgt", "----onComplete--->" + t.toString());
                }

                @Override
                public void onLoading(long count, long current) {
                    super.onLoading(count, current);
                    LG.d("gtgt", "-------onLoading----->" + current + "/"
                            + count);
                }

            });
        }

    }

    /**
     * 上传图片
     *
     * @param tag
     * @param url
     * @param params
     * @param listener
     */
    public static void uploadImg(final int tag, final String url,
                                 final AjaxParams params, final RequestInterface listener) {
        FinalHttp finalHttp = new FinalHttp();
        finalHttp.configTimeout(120000);
        finalHttp.post(url, params, new AjaxCallBack<Object>() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                listener.onError(tag, strMsg);
                LG.d("gtgt", "errorNo---->" + errorNo + "     strMsg----->"
                        + strMsg);
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                listener.onComplete(tag, t == null ? "null" : t.toString());
                LG.d("gtgt", "------->" + t.toString());
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
                listener.onLoading(count, current);
                LG.d("gtgt", "-------onLoading----->" + current + "/" + count);
            }

            @Override
            public AjaxCallBack<Object> progress(boolean progress, int rate) {
                // TODO Auto-generated method stub
                return super.progress(progress, rate);
            }

        });
    }

    private static AES mAes;

    /**
     * 字段加密
     */
    private static String sign(String key, String values) {
        if (mAes == null) {
            mAes = new AES();
        }
        byte[] mBytes = null;
        String endStr = null;
        try {
            mBytes = (key + "=" + values).getBytes("UTF8");
            String enString = mAes.encrypt(mBytes);
            endStr = URLEncoder.encode(enString, "UTF8");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return endStr;
    }


}
