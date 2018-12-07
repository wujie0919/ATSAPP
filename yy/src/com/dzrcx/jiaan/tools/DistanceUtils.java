package com.dzrcx.jiaan.tools;

import com.dzrcx.jiaan.distance.DistancePointVo;

public class DistanceUtils {

	static double DEF_PI = 3.14159265359; // PI
	static double DEF_2PI = 6.28318530712; // 2*PI
	static double DEF_PI180 = 0.01745329252; // PI/180.0
	static double DEF_R = 6370693.5; // radius of earth

	public static int getShortDistance(DistancePointVo pointVo) {

		if (pointVo == null) {
			return -99;
		}

		double ew1, ns1, ew2, ns2;
		double distance;
		// 角度转换为弧度
		ew1 = pointVo.getLocationLng() * DEF_PI180;
		ns1 = pointVo.getLocationLat() * DEF_PI180;
		ew2 = pointVo.getTargetLng() * DEF_PI180;
		ns2 = pointVo.getTargetLat() * DEF_PI180;
		// 求大圆劣弧与球心所夹的角(弧度)
		distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1)
				* Math.cos(ns2) * Math.cos(ew1 - ew2);
		// 调整到[-1..1]范围内，避免溢出
		if (distance > 1.0)
			distance = 1.0;
		else if (distance < -1.0)
			distance = -1.0;
		// 求大圆劣弧长度
		distance = DEF_R * Math.acos(distance);
		// return distance;

		return ((int) distance);
	}

}
