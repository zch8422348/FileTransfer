package com.android.zch.util;

import java.io.File;
import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

	public static void showActionBarMenu(Context context) {
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

	public static Bitmap getBitmapFromFile(File dst, int width, int height) {
		if (null != dst && dst.exists()) {
			BitmapFactory.Options opts = null;
			if (width > 0 && height > 0) {
				opts = new BitmapFactory.Options(); // 设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(dst.getPath(), opts);
				// 计算图片缩放比例
				final int minSideLength = Math.min(width, height);
				opts.inSampleSize = computeSampleSize(opts, minSideLength,
						width * height); // 这里一定要将其设置回false，因为之前我们将其设置成了true
				opts.inJustDecodeBounds = false;
				opts.inInputShareable = true;
				opts.inPurgeable = true;
			}
			try {
				return BitmapFactory.decodeFile(dst.getPath(), opts);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}
