package com.dzrcx.jiaan.SearchCar;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.UndoOrderBean;
import com.dzrcx.jiaan.Main.MainActivity2_1;
import com.dzrcx.jiaan.Order.OrderAty;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.tools.LG;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;

/**
 * webview 页面 带参数 url，tv_title
 *
 * @author Administrator
 */
public class WebFrg extends YYBaseFragment {
    private WebView webView;
    private ImageView iv_left_raw;
    private TextView tv_title;
    private String url;
    private boolean isComming, isSuccess;

    private boolean isStartPate;
    private UndoOrderBean undoOrderBean;
    private View webFrgView;
    private Intent intent;
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 999:
                    finish();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.intent = getActivity().getIntent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (webFrgView == null) {
            webFrgView = inflater.inflate(R.layout.aty_webview, null);
            initView();
            String title = intent.getStringExtra("title");
            url = intent.getStringExtra("url");
            isStartPate = intent.getBooleanExtra("isStartPate", false);
            undoOrderBean = (UndoOrderBean) intent
                    .getSerializableExtra("UndoOrderBean");

            tv_title.setText(title);
            if (NetHelper.checkNetwork(mContext)) {
                showNoNetDlg();
                MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
                return webFrgView;
            }
            dialogShow();
            webView.loadUrl(url);
        }
        return webFrgView;
    }

    public void finish() {
        // TODO Auto-generated method stub
        if (isComming && isSuccess) {
            mContext.sendBroadcast(new Intent("authorizeaty"));
            mContext.sendBroadcast(new Intent("cardetailaty"));
            YYApplication.TAGorder = "notcomplete";
        }
        getActivity().finish();
    }

    private void initView() {
        tv_title = (TextView) webFrgView.findViewById(R.id.tv_title);
        iv_left_raw = (ImageView) webFrgView.findViewById(R.id.iv_left_raw);
        iv_left_raw.setVisibility(View.VISIBLE);
        iv_left_raw.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isStartPate) {
                    if (undoOrderBean == null) {
                        Intent intent = new Intent(mContext,
                                MainActivity2_1.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.activity_up,
                                R.anim.activity_push_no_anim);
                        mHandler.sendEmptyMessageDelayed(999, 1200);
                    } else {
                        Intent intent = new Intent(mContext,
                                OrderAty.class);
                        intent.putExtra("UndoOrderBean", undoOrderBean);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.activity_up,
                                R.anim.activity_push_no_anim);
                        mHandler.sendEmptyMessageDelayed(999, 1200);
                    }
                } else {
                    finish();
                }
            }

        });
        webView = (WebView) webFrgView.findViewById(R.id.webView);
        WebViewClient client = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("mobile/views/phone/index.html")) {
                    isComming = true;
                }
                if (url.contains("mobile/callback.action")) {
                    isSuccess = true;
                }
                if (isSuccess && isComming) {
                    finish();
                }
                view.loadUrl(url); // 在当前的webview中跳转到新的url
                LG.e("url-----" + url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                progresssDialog.dismiss();
                super.onPageFinished(view, url);
            }

            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                // handler.cancel(); // Android默认的处理方式
                handler.proceed(); // 接受所有网站的证书
                // handleMessage(Message msg); // 进行其他处理
            }
        };
        webView.setWebViewClient(client);
        WebSettings settings = webView.getSettings();
        settings.setRenderPriority(RenderPriority.HIGH); // 加速webview加载速度
        settings.setBlockNetworkImage(false);// 同上，图片放在最后加载
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (isStartPate) {
                if (undoOrderBean == null) {
                    Intent intent = new Intent(mContext,
                            MainActivity2_1.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_up,
                            R.anim.activity_push_no_anim);
                    mHandler.sendEmptyMessageDelayed(999, 1200);

                } else {
                    Intent intent = new Intent(mContext,
                            OrderAty.class);
                    intent.putExtra("UndoOrderBean", undoOrderBean);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_up,
                            R.anim.activity_push_no_anim);
                    mHandler.sendEmptyMessageDelayed(999, 1200);
                }
            } else {
                finish();
            }
            return true;
        }
        return true;
    }
}
