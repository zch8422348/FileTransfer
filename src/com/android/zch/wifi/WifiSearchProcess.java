package com.android.zch.wifi;

import com.android.zch.MainActivity;

import android.os.Message;
import android.util.Log;

/**
 * @Title: WFSearchProcess.java
 * @Package com.android.touchjet.wifi
 * @Description: TODO
 * @author zch:qw8shop@gmail.com
 * @date 2015-1-29 下午2:36:30
 * @version V1.0
 */
public class WifiSearchProcess implements Runnable {

	public MainActivity context;
	private static final String tag="WifiSearchProcess";
	public WifiSearchProcess(MainActivity context) {
		Log.e(tag, "wifi search thread");
		this.context = context;
	}

	public boolean running = false;
	private long startTime = 0L;
	private Thread thread = null;

	public void run() {
		while (true) {
			if (!running)
				return;
			if (System.currentTimeMillis() - startTime >= 30000L) {
				Message msg = context.handler
						.obtainMessage(WifiApConst.ApSearchTimeOut);
				context.handler.sendMessage(msg);
			}
			Log.e(tag, "wifi search-----------------------");
			try {
				Thread.sleep(10L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void start() {
		try {
			thread = new Thread(this);
			running = true;
			startTime = System.currentTimeMillis();
			thread.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void stop() {
		try {
			running = false;
			thread = null;
			startTime = 0L;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
