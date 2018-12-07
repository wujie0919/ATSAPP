//package com.dzrcx.jiaan.distance;
//
//import java.util.Collections;
//import java.util.Map;
//import java.util.WeakHashMap;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Looper;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.TextView;
//
//import com.dzrcx.jiaan.YYApplication;
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.search.route.DrivingRouteResult;
//import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
//import com.baidu.mapapi.search.route.PlanNode;
//import com.baidu.mapapi.search.route.RoutePlanSearch;
//import com.baidu.mapapi.search.route.TransitRouteResult;
//import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
//import com.baidu.mapapi.search.route.WalkingRouteResult;
//
//public class DistanceLoader {
//
//	private static DistanceLoader instance;
//
//	MemoryCache memoryCache = new MemoryCache();
//	// FileCache fileCache;
//	private Map<TextView, DistancePointVo> distanceVos = Collections
//			.synchronizedMap(new WeakHashMap<TextView, DistancePointVo>());
//	private Map<View, DistancePointVo> views = Collections
//			.synchronizedMap(new WeakHashMap<View, DistancePointVo>());
//	// ExecutorService executorService;
//	private ThreadPoolExecutor poolExecutor;
//	private BlockingQueue<Runnable> queue;
//	private Context mContext;
//
//	/**
//	 * 一般图片
//	 */
//	public static int IMAGE_TYPE_COMMON = 1;
//	/**
//	 * 圆形图片
//	 */
//	public static int IMAGE_TYPE_ROUND = 2;
//	/**
//	 * 模糊图片
//	 */
//	public static int IMAGE_TYPE_FUZZY = 3;
//	/**
//	 * 晶格化图片
//	 */
//	public static int IMAGE_TYPE_CRYSTALLIZE = 4;
//
//	public static int IMAGE_TYPE_SCALE = 5;
//
//	/**
//	 * 最大线程池容量数，默认20
//	 */
//	private int maxTask = 20;
//
//	private DistanceLoader(Context context) {
//		// fileCache = new FileCache(context);
//		this.mContext = context;
//		// executorService = Executors.newFixedThreadPool(5);
//		queue = new LinkedBlockingQueue<Runnable>();
//		poolExecutor = new ThreadPoolExecutor(5, 20, 15, TimeUnit.SECONDS,
//				queue);
//		instance = this;
//	}
//
//	public static DistanceLoader getInstance() {
//		if (instance == null) {
//			new DistanceLoader(YYApplication.getApplication()
//					.getApplicationContext());
//		}
//		return instance;
//	}
//
//	public int getMaxTask() {
//		return maxTask;
//	}
//
//	/**
//	 *
//	 * @param maxTask可容纳线程数
//	 *            最少是5
//	 */
//	public void setMaxTask(int maxTask) {
//		if (maxTask < 5) {
//			return;
//		}
//		this.maxTask = maxTask;
//	}
//
//	// 默认显示背景图片
//	final String stub_id = "距离测量中...";
//	final String stub_id_f = "距离测失败";
//
//	/**
//	 * 显示图片
//	 *
//	 * @param url
//	 * @param imageView
//	 */
//	@SuppressWarnings({ "deprecation", "deprecation" })
//	public void displayDistance(TextView textView, DistancePointVo pointVo) {
//		distanceVos.put(textView, pointVo);
//		// 从缓存中获取图片
//
//		String dis = memoryCache.get(pointVo.toString());
//		if (dis != null) {
//			textView.setText(dis);
//		} else {
//			// 下载或从本地获取图片
//			textView.setText(stub_id);
//			queuePhoto(textView, pointVo);
//			// imageView.setImageResource(stub_id);
//			// imageView.setBackgroundResource(stub_id);
//		}
//	}
//
//	private void queuePhoto(TextView textView, DistancePointVo pointVo) {
//		PhotoToLoad p = new PhotoToLoad(textView, pointVo);
//		// executorService.submit(new PhotosLoader(p));
//		if (queue.size() > maxTask) {
//			try {
//				queue.take();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		poolExecutor.submit(new PhotosLoader(p));
//
//	}
//
//	private String getBitmap(DistancePointVo url) {
//
//		String sdis = null;
//		// 从网络请求中获取图片
//		Looper.prepare();
//		RequestDis requestDis = new RequestDis(url);
//		requestDis.run();
//		// Looper.loop();
//		while (!requestDis.getresult) {
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				sdis = requestDis.dis;
//				requestDis.canFinish = true;
//				e.printStackTrace();
//			}
//		}
//		sdis = requestDis.dis;
//		requestDis.canFinish = true;
//
//		return sdis;
//
//	}
//
//	static double DEF_PI = 3.14159265359; // PI
//	static double DEF_2PI = 6.28318530712; // 2*PI
//	static double DEF_PI180 = 0.01745329252; // PI/180.0
//	static double DEF_R = 6370693.5; // radius of earth
//
//	private String getShortDistance(DistancePointVo pointVo) {
//
//		if (pointVo == null) {
//			return "火星上";
//		}
//
//		double ew1, ns1, ew2, ns2;
//		double distance;
//		// 角度转换为弧度
//		ew1 = pointVo.getLocationLng() * DEF_PI180;
//		ns1 = pointVo.getLocationLat() * DEF_PI180;
//		ew2 = pointVo.getTargetLng() * DEF_PI180;
//		ns2 = pointVo.getTargetLat() * DEF_PI180;
//		// 求大圆劣弧与球心所夹的角(弧度)
//		distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1)
//				* Math.cos(ns2) * Math.cos(ew1 - ew2);
//		// 调整到[-1..1]范围内，避免溢出
//		if (distance > 1.0)
//			distance = 1.0;
//		else if (distance < -1.0)
//			distance = -1.0;
//		// 求大圆劣弧长度
//		distance = DEF_R * Math.acos(distance);
//		// return distance;
//
//		return ((int) distance) + "米";
//	}
//
//	class RequestDis extends Thread implements OnGetRoutePlanResultListener {
//
//		private DistancePointVo distancePointVo;
//
//		private String dis = null;
//
//		public boolean getresult = false;
//		public boolean canFinish = false;
//
//		private int count = 0;
//
//		public RequestDis(DistancePointVo distancePointVo) {
//			super();
//			this.distancePointVo = distancePointVo;
//		}
//
//		@Override
//		public void onGetDrivingRouteResult(DrivingRouteResult arg0) {
//			// TODO Auto-generated method stub
//			getresult = true;
//		}
//
//		@Override
//		public void onGetTransitRouteResult(TransitRouteResult arg0) {
//			// TODO Auto-generated method stub
//			getresult = true;
//		}
//
//		@Override
//		public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
//			// TODO Auto-generated method stub
//
//			if (arg0 != null && arg0.getRouteLines() != null) {
//				dis = arg0.getRouteLines().get(0).getDuration() + "";
//			}
//
//			getresult = true;
//		}
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			// Looper.prepare();
//
//			while (!getresult || count < 101) {
//				try {
//					if (count == 0) {
//						RoutePlanSearch mSearch = RoutePlanSearch.newInstance();
//
//						PlanNode stNode = PlanNode.withLocation(new LatLng(
//								distancePointVo.getLocationLat(),
//								distancePointVo.getLocationLng()));
//						PlanNode enNode = PlanNode.withLocation(new LatLng(
//								distancePointVo.getTargetLat(), distancePointVo
//										.getLocationLng()));
//						mSearch.walkingSearch((new WalkingRoutePlanOption())
//								.from(stNode).to(enNode));
//						Looper.loop();
//					}
//
//					count++;
//					if (count == 100) {
//						getresult = true;
//						Looper.myLooper().quit();
//					}
//
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			// Looper.loop();
//			Looper.myLooper().quit();
//
//		}
//
//	}
//
//	private class PhotoToLoad {
//		public TextView textView;
//		public DistancePointVo pointVo;
//		public View view;// 要显示背景图片的view
//		public int type;
//
//		public boolean isFixed;// true 等比缩放，false 平铺效果
//
//		public PhotoToLoad(TextView t, DistancePointVo d) {
//			textView = t;
//			pointVo = d;
//		}
//
//	}
//
//	class PhotosLoader implements Runnable {
//		PhotoToLoad photoToLoad;
//
//		PhotosLoader(PhotoToLoad photoToLoad) {
//			this.photoToLoad = photoToLoad;
//		}
//
//		@Override
//		public void run() {
//			if (imageViewReused(photoToLoad))
//				return;
//			// String dis = getBitmap(photoToLoad.pointVo);
//			String dis = getShortDistance(photoToLoad.pointVo);
//			if (!TextUtils.isEmpty(dis)) {
//				memoryCache.put(photoToLoad.pointVo.toString(), dis);
//			}
//			if (imageViewReused(photoToLoad)) {
//				return;
//			}
//
//			distanceDisplayer bd = new distanceDisplayer(dis, photoToLoad);
//			Activity a = (Activity) photoToLoad.textView.getContext();
//			a.runOnUiThread(bd);
//		}
//	}
//
//	/**
//	 * 锟斤拷止图片锟斤拷位
//	 */
//	boolean imageViewReused(PhotoToLoad photoToLoad) {
//
//		if (photoToLoad.textView == null) {
//			DistancePointVo tag = views.get(photoToLoad.textView);
//			if (tag == null
//					|| !tag.toString().equals(photoToLoad.pointVo.toString()))
//				return true;
//		} else {
//			DistancePointVo tag = distanceVos.get(photoToLoad.textView);
//			if (tag == null
//					|| !tag.toString().equals(photoToLoad.pointVo.toString()))
//				return true;
//		}
//
//		return false;
//	}
//
//	// 设置背景图片的线程
//	class distanceDisplayer implements Runnable {
//		String dis;
//		PhotoToLoad photoToLoad;
//
//		public distanceDisplayer(String b, PhotoToLoad p) {
//			dis = b;
//			photoToLoad = p;
//		}
//
//		@SuppressWarnings("deprecation")
//		public void run() {
//			if (imageViewReused(photoToLoad))
//				return;
//
//			if (dis != null) {
//				photoToLoad.textView.setText(dis);
//			} else {
//				photoToLoad.textView.setText(stub_id_f);
//			}
//		}
//
//		public void clearCache() {
//			// memoryCache.clear();
//			// fileCache.clear();
//			distanceVos.clear();
//			queue.clear();
//		}
//
//		public String getBitmapView(DistancePointVo pointVo) {
//
//			return null;
//
//		}
//
//		public String getBitmapFromCache(String url) {
//
//			String dis = null;
//
//			dis = memoryCache.get(url);
//
//			return dis;
//		}
//	}
//
//}
