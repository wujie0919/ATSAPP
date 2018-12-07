package com.dzrcx.jiaan.xgpush;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

public class XGPushMessageReceiver extends XGPushBaseReceiver {
    private Intent intent = new Intent("com.qq.xgdemo.activity.UPDATE_LISTVIEW");
    public static final String LogTag = "TPushReceiver";
    private Context mContext;

    private void show(Context context, String text) {
        // Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }



    // 通知展示 接受推送过来的通知消息 就是 非自定消息 通过title区分
    @Override
    public void onNotifactionShowedResult(Context context,
                                          final XGPushShowedResult notifiShowedRlt) {
        if (context == null || notifiShowedRlt == null) {
            return;
        }
        if (notifiShowedRlt.getTitle().contains("押金")) {
            Intent intent = new Intent("authorize_yinlian");
            intent.putExtra("title", notifiShowedRlt.getTitle());
            intent.putExtra("content", notifiShowedRlt.getContent());
            context.sendBroadcast(intent);
        } else {
            Log.e("还车提醒","走了还车提醒");
            if (notifiShowedRlt.getTitle().contains("还车提醒")) {
                //requestOrderList(context);
//                Intent intent = new Intent(context, PushDialogActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
                Intent i = new Intent();
                i.setAction("pushBroadcastReceiver");
                context.sendBroadcast(i);
            }

            XGNotification notific = new XGNotification();
            notific.setMsg_id(notifiShowedRlt.getMsgId());
            notific.setTitle(notifiShowedRlt.getTitle());
            notific.setContent(notifiShowedRlt.getContent());
            // notificationActionType==1为Activity，2为url，3为intent
            notific.setNotificationActionType(notifiShowedRlt
                    .getNotificationActionType());
            // Activity,url,intent都可以通过getActivity()获得
            notific.setActivity(notifiShowedRlt.getActivity());
            notific.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(Calendar.getInstance().getTime()));
            // NotificationService.getInstance(context).save(notific);
            context.sendBroadcast(intent);
            // show(context, "您有1条新消息, " + "通知被展示 ， " + notifiShowedRlt.toString());
        }
    }


    //获取订单列表 这里只取未完成订单
//    private void requestOrderList(final Context context) {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
//        params.put("status", "0");//0获取进行中订单
//        params.put("pageNo", "1");
//        params.put("pageSize", "10");
//        YYRunner.getData(YYConstans.TAG_ORDERLISTATY2_1, YYRunner.Method_POST,
//                YYUrl.GETORDERLIST, params, new RequestInterface() {
//                    @Override
//                    public void onError(int tag, String error) {
//                        Log.e("error错误", error);
//                        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onComplete(int tag, String json) {
//                        Log.e("success成功", json);
//                        OrderListBean listBean = (OrderListBean) GsonTransformUtil.fromJson(
//                                json, OrderListBean.class);
//                        if (listBean == null || listBean.getErrno() != 0) {
//                            return;
//                        } else {
//                            if (listBean.getReturnContent().getOrderList().size() > 0) {
//                                OrderListItemBean listItemBean = listBean.getReturnContent().getOrderList().get(0);//获取第一条未完成订单
//                                if(listItemBean.getTimeMode() ==1 && listItemBean.getDailyState() ==2 && listItemBean.getReletRemainingSeconds()>0){
//                                    Intent intent = new Intent(context, PushDialogActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    context.startActivity(intent);
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onLoading(long count, long current) {
//
//                    }
//                }
//
//        );
//    }

    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "反注册成功";
        } else {
            text = "反注册失败" + errorCode;
        }
        Log.d(LogTag, text);
        // show(context, text);

    }

    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"设置成功";
        } else {
            text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
        }
        Log.d(LogTag, text);
        // show(context, text);

    }

    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"删除成功";
        } else {
            text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);

    }

    // 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
    @Override
    public void onNotifactionClickedResult(Context context,
                                           XGPushClickedResult message) {
        Log.e("还车提醒","还车提醒onNotifactionClickedResult");
        if (context == null || message == null) {
            return;
        }
        String text = "";
        if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
            // 通知在通知栏被点击啦。。。。。
            // APP自己处理点击的相关动作
            // 这个动作可以在activity的onResume也能监听，请看第3点相关内容
            text = "通知被打开 :" + message;
        } else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
            // 通知被清除啦。。。。
            // APP自己处理通知被清除后的相关动作
            text = "通知被清除 :" + message;
        }
        // Toast.makeText(context, "广播接收到通知被点击:" + message.toString(),
        // Toast.LENGTH_SHORT).show();
        // 获取自定义key-value
        String customContent = message.getCustomContent();
        if (customContent != null && customContent.length() != 0) {
            try {
                JSONObject obj = new JSONObject(customContent);
                // key1为前台配置的key
                if (!obj.isNull("key")) {
                    String value = obj.getString("key");
                    Log.d(LogTag, "get custom value:" + value);
                }
                // ...
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // APP自主处理的过程。。。
        Log.d(LogTag, text);
        show(context, text);
    }

    @Override
    public void onRegisterResult(Context context, int errorCode,
                                 XGPushRegisterResult message) {
        // TODO Auto-generated method stub
        if (context == null || message == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = message + "注册成功";
            // 在这里拿token
            String token = message.getToken();
        } else {
            text = message + "注册失败，错误码：" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);
    }

    // 消息透传 接收推送过来的消息XGPushTextMessage message
    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        // TODO Auto-generated method stub
        Log.e("gtgt", message + "  onTextMessage");
        Map<String, String> map = new HashMap<String, String>();
        // 获取自定义key-value
        String customContent = message.getCustomContent();
        if (customContent == null || customContent.length() == 0) {
            customContent = message.getContent();
        }
        if (customContent != null && customContent.length() != 0) {
            try {
                JSONObject obj = new JSONObject(customContent);
                if (obj.has("serviceType")) {
                    // PushCount.getInstance(context).addOne(
                    // Integer.parseInt(obj.getString("serviceType")));
                    map.put("serviceType", obj.getString("serviceType"));
                }
                if (obj.has("title")) {
                    map.put("title", obj.getString("title"));
                } else {
                    map.put("title", message.getTitle());
                }
                if (obj.has("topicId"))
                    map.put("topicId", obj.getString("topicId"));
                if (obj.has("order400"))
                    map.put("order400", obj.getString("order400"));
                if (obj.has("activity"))
                    map.put("activity", obj.getString("activity"));
                // if (obj.has("objectId"))
                // map.put("objectId", obj.getString("objectId"));

                if (obj.has("activityId"))
                    map.put("activityId", obj.getString("activityId"));

                // ...
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            // XGBasicPushNotificationBuilder builder = new
            // XGBasicPushNotificationBuilder();
            // builder.setIcon(R.drawable.push);
            // builder.setTitle("推送测试");
            // builder.setTickerText("我是内容");
            // XGPushManager.setPushNotificationBuilder(context, 0, builder);

            // XGPushNotificationBuilder builder2 = XGPushManager
            // .getNotificationBuilder(context, 0);
        } else {
            return;
        }
        // context.sendBroadcast(new Intent(Constants.ACTION_MYMESSAGE_FLAG));//
        // 来消息就发送广播通知我的消息右上角的数字加1
        switch (map.get("serviceType")) {
            case "0":// 系统
            case "1":// 约赛
            case "3":// 约人
            case "4":// 400订单
            case "5":// 优惠券
            case "7":// 畅打几天前通知
            case "8":// 畅打话题评论
                showNotification(context, map);
                break;
            case "2":// 网球圈子
                // if (DMGApplication.isBackGround())// 在后台
                // showNotification(context, map);
                // else {
                // Utils.showToast(context, map.get("title"));
                // context.sendBroadcast(new Intent(
                // Constants.ACTION_CIRCLE_PUSH_FLAG));
                // }// 在前台
                break;

            default:
                break;
        }
        // APP自主处理消息的过程...
        // Log.d(LogTag, text);
        // XGLocalMessage msg = new XGLocalMessage();
        // msg.setType(2);
        // msg.setTitle("我是标题");
        // msg.setContent("我是内容");
        // msg.setDate("20140909");
        // msg.setHour("11");
        // msg.setMin("50");
        // msg.setBuilderId(0);
        // XGPushManager.addLocalNotification(context, msg);
        // show(context, text);
    }

    /**
     * 展示通知
     */
    private void showNotification(Context context, Map<String, String> map) {
        // NotificationManager nm = (NotificationManager) context
        // .getSystemService(Context.NOTIFICATION_SERVICE);// 获取服务
        // // 第二步：定义Notification
        // Intent intent = new Intent();
        // ComponentName cn;
        //
        // switch (map.get("serviceType")) {
        // case "0":// 系统
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // cn = new ComponentName("com.grandslam.dmg",
        // "com.grandslam.dmg.Home");
        // intent.setComponent(cn);
        // break;
        // case "1":// 约赛
        // break;
        // case "2":// 网球圈
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // intent.putExtra("invitationServerId", map.get("topicId"));
        // intent.putExtra("viewUserId", "0");
        // cn = new ComponentName("com.grandslam.dmg",
        // "com.grandslam.dmg.activity.invitation.InvitationDetailsActivity");
        // intent.setComponent(cn);
        // break;
        // case "3":// 约人活动
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // intent.putExtra("id", map.get("activity"));
        // intent.putExtra("from", "from");
        // cn = new ComponentName("com.grandslam.dmg",
        // "com.grandslam.dmg.activity.AboutPeopleDetail");
        // intent.setComponent(cn);
        //
        // break;
        // case "4":// 400
        // context.sendBroadcast(new Intent(Constants.ACTION_LEFT_PUSH_FLAG));
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // intent.putExtra("order_id", Long.parseLong(map.get("order400")));
        // LocaOrderinfo info = new LocaOrderinfo();
        // info.setHasChange(false);
        // info.setOrderID(map.get("order400"));
        // info.setStatus("0");
        // intent.putExtra("LocaOrderinfo", info);
        // intent.putExtra("order_from", "my_order");
        // intent.putExtra("invalid_time", "");
        //
        // cn = new ComponentName("com.grandslam.dmg",
        // "com.grandslam.dmg.activity.menu.MyOrder.MyOrderToPay");
        // intent.setComponent(cn);
        // break;
        // case "5":// 优惠券
        // context.sendBroadcast(new Intent(Constants.ACTION_LEFT_PUSH_FLAG));
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // cn = new ComponentName("com.grandslam.dmg",
        // "com.grandslam.dmg.activity.MyCouponActivity");
        // intent.setComponent(cn);
        // break;
        // case "7":// 畅打几天后活动开始
        // context.sendBroadcast(new Intent(Constants.ACTION_LEFT_PUSH_FLAG));
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // intent.putExtra("ID", Long.valueOf(map.get("activityId")));
        // cn = new ComponentName("com.grandslam.dmg",
        // "com.grandslam.dmg.activity.FreePlayInfoAct");
        // intent.setComponent(cn);
        // break;
        // case "8":// 畅打评论回复
        // context.sendBroadcast(new Intent(Constants.ACTION_LEFT_PUSH_FLAG));
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // intent.putExtra("invitationServerId", map.get("topicId"));
        // intent.putExtra("viewUserId", "0");
        // cn = new ComponentName("com.grandslam.dmg",
        // "com.grandslam.dmg.activity.invitation.HuaTiDetailAty");
        // intent.setComponent(cn);
        // break;
        // default:
        // cn = null;
        // intent = null;
        // break;
        // }
        // // PendingIntent是待执行的Intent
        // PendingIntent pi = PendingIntent.getActivity(context,
        // Integer.parseInt(map.get("serviceType")), intent,
        // PendingIntent.FLAG_UPDATE_CURRENT);// 更新之前的消息
        //
        // // new notification样式
        // Notification notification = new Notification(R.drawable.push,
        // "您有新消息",
        // System.currentTimeMillis());
        // // notification.setLatestEventInfo(context, "大满贯网球",
        // // "title/r\nadfaffadsfasdfasdf",
        // // pi);//系统的通知样式
        // RemoteViews contentView = new RemoteViews(context.getPackageName(),
        // R.layout.notification_view);
        // contentView.setImageViewResource(R.id.img_notification_logo,
        // R.drawable.push);
        // contentView.setTextViewText(R.id.tv_title, "大满贯网球");
        // contentView.setTextViewText(R.id.tv_content, map.get("title"));
        // notification.contentView = contentView;
        // notification.contentIntent = pi;
        // notification.defaults = Notification.DEFAULT_SOUND;
        // notification.flags = Notification.FLAG_AUTO_CANCEL;
        // // 第三步：启动通知栏，第一个参数是一个通知的唯一标识
        // nm.notify(Integer.parseInt(map.get("serviceType")), notification);
    }
}
