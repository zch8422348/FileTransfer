package com.android.zch.wifi;

import com.android.zch.MainActivity;

import android.os.Message;

/**
 * @Title: CreateAPProcess.java
 * @Package com.android.touchjet.wifi
 * @Description: TODO thread of create wifi-ap
 * @author zch:qw8shop@gmail.com
 * @date 2015-1-29 上午11:26:25
 * @version V1.0
 */
public class CreateAPProcess implements Runnable {

	public MainActivity context;

	public boolean running = false;
	private long startTime = 0L;
	private Thread thread = null;
	private WifiHelper wifiHelper;

	public CreateAPProcess(MainActivity context,WifiHelper wifiHelper) {
		super();
		this.context = context;
		this.wifiHelper=wifiHelper;
	}

	public void run() {
		while (true) {
			if (!running)
				return;
			if ((wifiHelper.getWifiApState() == 3)
					|| (wifiHelper.getWifiApState() == 13)
					|| (System.currentTimeMillis() - this.startTime >= 30000L)) {
				Message msg = context.handler
						.obtainMessage(WifiApConst.ApCreateAPResult);
				context.handler.sendMessage(msg);
			}
			try {
				Thread.sleep(5L);
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
			e.printStackTrace();
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
