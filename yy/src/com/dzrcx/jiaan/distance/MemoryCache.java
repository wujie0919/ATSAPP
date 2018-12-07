package com.dzrcx.jiaan.distance;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;

public class MemoryCache {

	private static final String TAG = "MemoryCache";
	// ���뻺��ʱ�Ǹ�ͬ������
	private static Map<String, SoftReference<String>> cache = Collections
			.synchronizedMap(new LinkedHashMap<String, SoftReference<String>>(
					10, 1.5f, true));
	// ������ͼƬ��ռ�õ��ֽڣ���ʼ0����ͨ��˱��ϸ���ƻ�����ռ�õĶ��ڴ�
	private long size = 0;// current allocated size
	// ����ֻ��ռ�õ������ڴ�
	private long limit = 10000000;// max memory in bytes

	public MemoryCache() {
		setLimit(Runtime.getRuntime().maxMemory() / 4);
	}

	public void setLimit(long new_limit) {
		limit = new_limit;
		Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
	}

	public String get(String id) {
		try {
			if (!cache.containsKey(id)) {
				return null;
			} else {
				String str = cache.get(id).get();
				if (str == null) {
					cache.remove(id);
				}
				return str;
			}
		} catch (NullPointerException ex) {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public void put(String id, String str) {
		try {
			if (cache.containsKey(id))
				size -= getSizeInBytes(cache.get(id).get());
			cache.put(id, new SoftReference(str));
			size += getSizeInBytes(str);
			checkSize();
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

	/**
	 * �ϸ���ƶ��ڴ棬���������滻�������ʹ�õ��Ǹ�ͼƬ����
	 */
	private void checkSize() {
		Log.i(TAG, "cache size=" + size + " length=" + cache.size());
		if (size > limit) {
			// �ȱ����������ʹ�õ�Ԫ��
			Iterator<Entry<String, SoftReference<String>>> iter = cache
					.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, SoftReference<String>> entry = iter.next();
				size -= getSizeInBytes(entry.getValue().get());
				iter.remove();
				if (size <= limit / 2)
					break;
			}
			if (!iter.hasNext()) {
				size = 0;
			}
			Log.i(TAG, "Clean cache. New size " + cache.size());
		}
	}

	public void clear() {
		cache.clear();
	}

	/**
	 * ͼƬռ�õ��ڴ�
	 * 
	 * @param bitmap
	 * @return
	 */
	long getSizeInBytes(String str) {
		if (str == null)
			return 0;
		return str.length();
	}
}