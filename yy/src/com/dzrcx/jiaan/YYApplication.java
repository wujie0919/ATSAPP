package com.dzrcx.jiaan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.CannedAccessControlList;
import com.alibaba.sdk.android.oss.model.CreateBucketRequest;
import com.alibaba.sdk.android.oss.model.CreateBucketResult;
import com.dzrcx.jiaan.Constans.YYOptions;
import com.dzrcx.jiaan.tools.DevMountInfo;
import com.dzrcx.jiaan.tools.LG;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class YYApplication extends Application {

    private static YYApplication application;

    /**
     * 本地文件缓存的根目录
     */
    public static String sdCardRootPath = "";
    private List<SoftReference<Activity>> mListDel = new ArrayList<SoftReference<Activity>>();
    public static LocationClient mLocationClient;
    public static double Longitude = 115.48715;
    public static double Latitude = 38.86837;
    public static String LocaAdrrName = "保定火车站";
    public static String TAGorder = "";
    /**
     * 标记是否是为了请求其他信息而定位；区别于baseactivity每次resume时候的纯定位目的，
     * 需要返回定位数据界面直接在本onresum中设置为true即可；
     */
    private static OnGetLocationlistener onGetLocationlistener = null;

    public static String versionName;
    public static int versionCode;

    public static boolean isBeijing = true;


    private static OSS oss;
    // 运行sample前需要配置以下字段为有效的值
    public static final String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    public static final String bucketName = "dzrzloss";
    public static final String OSSBaseUrl = "http://" + bucketName + ".oss-cn-shanghai.aliyuncs.com/";


    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        application = this;
        initSys();
    }

    public static YYApplication getApplication() {
        return application;
    }


    BDLocationListener baseListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
//            Longitude = 115.48715;
//            Latitude = 38.86837;
//            115.48715,38.86837
            if (location != null) {
//                if(isBeijing) {
                    Longitude = location.getLongitude();
                    Latitude = location.getLatitude();
                    LocaAdrrName = location.getAddrStr();
//                }else{
//                    Longitude = 115.48715;
//                    Latitude = 38.86837;
//                }
            }
            if (onGetLocationlistener != null) {
                onGetLocationlistener.getBdlocation(location);
            }
        }
    };

    public interface OnGetLocationlistener {
        void getBdlocation(BDLocation location);
    }

    /**
     * 注册地图回调接口
     *
     * @param listener
     */
    public static void setLocationIFC(OnGetLocationlistener listener) {
        onGetLocationlistener = listener;
    }

    public void initBaseLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;// 设置为5分钟定位一次
        option.setScanSpan(span);//
        // 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        // option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        // option.setLocationNotify(true);//
        // 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        // option.setIsNeedLocationPoiList(true);//
        // 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public static OSS getOss() {
        return oss;
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
        System.gc();
    }

    /**
     * 添加到栈
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        mListDel.add(new SoftReference<Activity>(activity));
    }

    /**
     * 结束当前activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            mListDel.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void exit() {
        try {
            for (SoftReference<Activity> activitySoftRef : mListDel) {
                try {
                    if (activitySoftRef != null) {
                        Activity activityInstance = activitySoftRef.get();
                        if (activityInstance != null)
                            activityInstance.finish();
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public void exitPre() {
        try {
            for (int i = 0; i < mListDel.size() - 1; i++) {
                SoftReference<Activity> activitySoftRef = mListDel.get(i);
                if (activitySoftRef != null) {
                    Activity activityInstance = activitySoftRef.get();
                    if (activityInstance != null)
                        activityInstance.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.gc();
        }
    }

    /**
     * 对app的一些参数设置
     */
    private void initSys() {
        LG.isDebug = true;//关闭log打印
        //YYCrashHandler.getInstance().init(getApplicationContext());
        readConfig();
        initOSS();
        /**
         * 语音播报d
         */
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5941de91");
        SDKInitializer.initialize(getApplicationContext());// 初始化百度地图
        DevMountInfo.getInstance().init(getApplicationContext(), true);
        sdCardRootPath = DevMountInfo.getInstance().getSDCardPath();
        initImageLoad(sdCardRootPath);
        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        mLocationClient.registerLocationListener(baseListener); // 注册监听函数
        initBaseLocation();
    }


    private void initImageLoad(String rootPath) {

        if (!TextUtils.isEmpty(rootPath)) {

            File externalStorageDirectory = new File(rootPath + File.separator
                    + "yiyi" + File.separator + "image" + File.separator
                    + "imageCach" + File.separator);
            if (!externalStorageDirectory.exists()) {
                if (!externalStorageDirectory.mkdirs()) {
                    externalStorageDirectory = null;
                }
            }
            Builder config = new Builder(getApplicationContext())
                    .threadPriority(Thread.NORM_PRIORITY - 1)
                    .memoryCache(new LruMemoryCache(10 * 1024 * 1024))
                    .memoryCacheSize(10 * 1024 * 1024)
                    .denyCacheImageMultipleSizesInMemory()
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .tasksProcessingOrder(QueueProcessingType.LIFO);
            if (externalStorageDirectory != null) {
                config.diskCache(new UnlimitedDiscCache(
                        externalStorageDirectory));
            }
            config.defaultDisplayImageOptions(YYOptions.OPTION_DEF);
            ImageLoaderConfiguration build = config.build();
            ImageLoader.getInstance().init(build);
        }
    }


    private void readConfig() {
        ApplicationInfo appInfo = null;
        try {
            PackageManager pm = getPackageManager();//context为当前Activity上下文
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            versionName = pi.versionName;
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    private void initOSS() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAImm2E6ormnadm", "tyXhFM42jz4f4VsXxm6TNyddNHaLaY");
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(5); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider, conf);
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        createBucketRequest.setBucketACL(CannedAccessControlList.PublicRead);
        createBucketRequest.setLocationConstraint("oss-cn-shanghai");
        OSSAsyncTask createTask = oss.asyncCreateBucket(createBucketRequest, new OSSCompletedCallback<CreateBucketRequest, CreateBucketResult>() {
            @Override
            public void onSuccess(CreateBucketRequest request, CreateBucketResult result) {
                Log.d("locationConstraint", request.getLocationConstraint());
            }

            @Override
            public void onFailure(CreateBucketRequest request, ClientException clientException, ServiceException serviceException) {
                // 请求异常
                if (clientException != null) {
                    // 本地异常如网络异常等
                    clientException.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }


}
