package com.android.zch.util;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;


public class JsonUtils {
	private static final String tag = "JsonUtils";

	/**
	 * 将javaBean转换成json对象
	 * 
	 * @param paramObject
	 *            需要解析的对象
	 */
	public static String createJsonString(Object paramObject) {
		Log.e(tag, "str1111");
		Gson gson = new Gson();
		String str = null;
		try {
			// str = JSON.toJSONString(paramObject);
			str = gson.toJson(paramObject);
			Log.e(tag, "str22222");
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(tag, "str3333");
		}
		return str;
	}

	/**
	 * 对单个javaBean进行解析
	 * 
	 * @param <T>
	 * @param paramJson
	 *            需要解析的json字符串
	 * @param paramCls
	 *            需要转换成的类
	 * @return
	 */
	public static <T> T getObject(String paramJson, Class<T> paramCls) {
		T t = null;
		try {
			t = JSON.parseObject(paramJson, paramCls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

}
