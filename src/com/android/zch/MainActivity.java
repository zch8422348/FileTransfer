package com.android.zch;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.zch.R;
import com.android.zch.base.BaseActivity;
import com.android.zch.entities.MessageEntity;
import com.android.zch.entities.MessageEntity.CONTENT_TYPE;
import com.android.zch.file.FileCategoryHelper;
import com.android.zch.file.UtilsFile;
import com.android.zch.file.FileCategoryHelper.CategoryInfo;
import com.android.zch.file.FileCategoryHelper.FileCategory;
import com.android.zch.file.UtilsFile.SDCardInfo;
import com.android.zch.util.DateUtils;
import com.android.zch.wifi.CreateAPProcess;
import com.android.zch.wifi.IPMSGConst;
import com.android.zch.wifi.UDPSocketThread;
import com.android.zch.wifi.WifiApConst;
import com.android.zch.wifi.WifiHelper;
import com.android.zch.wifi.WifiSearchProcess;
import com.android.zch.wifi.WifiapBroadcast;
import com.android.zch.wifi.WifiapBroadcast.EventHandler;

/**
 * @Title: MainActivity.java
 * @Package com.android.touchjet
 * @Description: TODO
 * @author zch:qw8shop@gmail.com
 * @date 2015-1-29 上午10:49:31
 * @version V1.0
 */

public class MainActivity extends BaseActivity implements EventHandler {
	private boolean client = false;// is client?
	private Context context;
	private CreateAPProcess createAPProcess;
	private WifiSearchProcess wifiSearchProcess;
	private String tag = "MainActivity";
	private WifiapBroadcast wifiapBroadcast;
	private FileFragment fileFragment;
	private List<ScanResult> scanResults;
	public UDPSocketThread udpSocketThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// register wifi brocast
		Log.e(tag, "scanResults==========" + (scanResults == null));
		initBroadcast();
		initViews();
		initEvents();
		OpenWifiAP();
		startUDPSocketThread();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		unregisterReceiver(wifiapBroadcast); // 撤销广播
		wifiapBroadcast.removeehList(this);
		if (wifiSearchProcess != null)
			wifiSearchProcess.stop();

		if (createAPProcess != null)
			createAPProcess.stop();
		if (scanResults != null)
			scanResults.clear();
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_main);
		this.context = this;
		fileFragment = new FileFragment();
		getFragment(fileFragment);
		// updateUI();

	}

	private void getFragment(Fragment fragment) {
		FragmentManager fragmentManager = MainActivity.this
				.getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.details, fragment);
		transaction.commit();
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		wifiapBroadcast.addehList(this);

	}

	public void test() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (client) {
					Socket socket = null;
					OutputStream outputStream = null;
					try {
						socket = new Socket("192.168.43.1", 19905);
						outputStream = socket.getOutputStream();
						outputStream.write(0x01);
						outputStream.flush();
						socket.close();
					} catch (IOException e) {

					}
				} else {
					// TODO Auto-generated method stub
					ServerSocket serverSocket = null;
					Socket socket = null;
					OutputStream outputStream = null;
					try {
						serverSocket = new ServerSocket(19905);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					while (true) {
						if (serverSocket != null && !serverSocket.isClosed()) {
							Log.e(tag, "开启接收socket");
							try {
								socket = serverSocket.accept();
								InputStream inputStream = socket
										.getInputStream();
								byte[] in = new byte[1];
								inputStream.read(in);
								if (in[0] == 0x01) {
									Log.e(tag, "server get cmd-----------");
								}
								inputStream.close();
								socket.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}).start();
	}

	public void initBroadcast() {
		wifiapBroadcast = new WifiapBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(wifiapBroadcast, filter);
	}

	private void startUDPSocketThread() {
		// TODO Auto-generated method stub
		udpSocketThread = UDPSocketThread.getInstance(this);
		udpSocketThread.connectUDPSocket();
	}

	public void send() {
		if (udpSocketThread != null) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					String nowtime = DateUtils.getNowtime();
					MessageEntity msg = new MessageEntity("IMEI", nowtime,
							"111111", CONTENT_TYPE.TEXT);
					Log.e(tag, "msg==" + msg.getMsgContent());
					// TODO Auto-generated method stub
					udpSocketThread.sendUDPdata(IPMSGConst.IPMSG_SENDMSG,
							"192.168.43.1", msg);
				}
			}).start();

		}
	}

	// open wifi-ap
	private void OpenWifiAP() {
		wifiHelper = WifiHelper.getInstance(context);
		if (client) {
			if (!wifiHelper.mWifiManager.isWifiEnabled())
				wifiHelper.OpenWifi();
			if (wifiSearchProcess == null)
				wifiSearchProcess = new WifiSearchProcess(MainActivity.this);
			wifiHelper.startScan();
			wifiSearchProcess.start();
		} else {
			wifiHelper.closeWifi();
			wifiHelper.createWifiAP(wifiHelper.createWifiInfo(
					WifiApConst.WIFI_AP_NAME, WifiApConst.WIFI_AP_PASSWORD, 3,
					"ap"), true);
			if (createAPProcess == null) {
				createAPProcess = new CreateAPProcess(MainActivity.this,
						wifiHelper);
				createAPProcess.start();
			}
		}
	}

	private void updateUI() {
		boolean sdCardReady = UtilsFile.isSDCardReady();
		if (sdCardReady) {
			Log.e(tag, "fileFragment==" + (fileFragment == null));
			fileFragment.getCategoryInfo();
			// refresh file list
		} else {
		}
	}

	/** 含有Bundle通过Class跳转界面 **/
	public void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	private static final int MSG_FILE_CHANGED_TIMER = 100;

	private Timer timer;

	// process file changed notification, using a timer to avoid frequent
	// refreshing due to batch changing on file system
	synchronized public void notifyFileChanged() {
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {

			public void run() {
				timer = null;
				Message message = new Message();
				message.what = MSG_FILE_CHANGED_TIMER;
				handler.sendMessage(message);
			}

		}, 1000);
	}

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WifiApConst.ApConnectting:
				wifiSearchProcess.stop();
				break;
			case WifiApConst.ApConnected:

				break;
			// wifi connect ok
			case WifiApConst.ApConnectResult:
				wifiSearchProcess.stop();
				Toast.makeText(context, "ip=" + wifiHelper.getIPAddress(),
						Toast.LENGTH_LONG).show();
				// Log.e(tag,
				// "wifi ok-----------------"+wifiHelper.getIPAddress());
				break;
			// scan result of wifi
			case WifiApConst.ApScanResult:
				scanResults = new ArrayList<ScanResult>();
				scanResults = wifiHelper.mWifiManager.getScanResults();
				int size = scanResults.size();
				Log.e(tag, "scan result..." + size);
				if (size > 0) {
					for (int i = 0; i < size; i++) {
						String apSSID = scanResults.get(i).SSID;
						Log.e(tag,
								"ssid=="
										+ apSSID
										+ "|name="
										+ WifiApConst.WIFI_AP_NAME
										+ "|"
										+ apSSID.equals(WifiApConst.WIFI_AP_NAME));
						if (apSSID.startsWith(WifiApConst.WIFI_AP_NAME)) {
							Toast.makeText(context, "apSSID=" + apSSID,
									Toast.LENGTH_SHORT).show();
							// wifiSearchProcess.stop();
							WifiConfiguration localWifiConfiguration = wifiHelper
									.createWifiInfo(apSSID,
											WifiApConst.WIFI_AP_PASSWORD, 3,
											"wt");
							wifiHelper.addNetwork(localWifiConfiguration);
							// handler.sendEmptyMessageDelayed(WifiApConst.ApConnected,
							// 3500L);
						}
					}
				}
				break;
			case WifiApConst.ApUserResult:
				break;
			case WifiApConst.ApSearchTimeOut:
				Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT).show();
				wifiSearchProcess.stop();
				break;
			case WifiApConst.ApCreateAPResult:
				createAPProcess.stop();
				break;
			case WifiApConst.APDisconnect:
				Toast.makeText(context, "wifi is closed", Toast.LENGTH_SHORT)
						.show();
				break;
			case MSG_FILE_CHANGED_TIMER:
				// notify fragment to update ui
				fileFragment.notifyFileChanged();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub

	}

	// net change
	@Override
	public void handleConnectChange() {
		Log.e(tag, "connect change");
		// TODO Auto-generated method stub
		Message msg = handler.obtainMessage(WifiApConst.ApConnectResult);
		handler.sendMessage(msg);
	}

	// scan result of wifi
	@Override
	public void scanResultsAvailable() {
		// TODO Auto-generated method stub
		Log.e(tag, "scan result" + (scanResults == null));
		if (scanResults == null) {
			Message msg = handler.obtainMessage(WifiApConst.ApScanResult);
			handler.sendMessage(msg);
		}
	}

	// wifi state
	@Override
	public void wifiStatusNotification() {
		// TODO Auto-generated method stub

	}

	// wifi disconnect
	@Override
	public void wifiDisconnect() {
		// TODO Auto-generated method stub
		Message msg = handler.obtainMessage(WifiApConst.APDisconnect);
		handler.sendMessage(msg);
	}

}
