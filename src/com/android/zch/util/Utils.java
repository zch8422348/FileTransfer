package com.android.zch.util;

import java.lang.reflect.Field;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.ViewConfiguration;

/**   
* @Title: Utils.java 
* @Package com.android.touchjet.util 
* @Description: TODO 
* @author spring:qw8shop@gmail.com  
* @date 2015-2-4 上午12:01:14 
* @version V1.0   
*/ 

public class Utils {

	public static String getLocalIpAdress(Context context) {
		WifiManager wifimanage = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);// 获取WifiManager
		return (wifimanage.getConnectionInfo() == null) ? ""
				: intToIp(wifimanage.getConnectionInfo().getIpAddress());
	}
	// 将获取的int转为真正的ip地址
	public static String intToIp(int paramIntip) {
		return (paramIntip & 0xFF) + "." + ((paramIntip >> 8) & 0xFF) + "."
				+ ((paramIntip >> 16) & 0xFF) + "."
				+ ((paramIntip >> 24) & 0xFF);
	}
	public static void showActionBarMenu(Context context){
		try {
			ViewConfiguration mconfig = ViewConfiguration.get(context);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(mconfig, false);
			}
		} catch (Exception ex) {
		}
	}
}
