package com.android.zch.wifi;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.android.zch.entities.Entity;
import com.android.zch.entities.MessageEntity;
import com.android.zch.util.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * @Title: UDPSocketThread.java
 * @Package com.android.touchjet.wifi
 * @Description: TODO
 * @author spring:qw8shop@gmail.com
 * @date 2015-2-3 下午11:07:30
 * @version V1.0
 */

public class UDPSocketThread implements Runnable {
	private static final String tag = "UDPSocketThread";
	private static UDPSocketThread instance;
	private static Context context;
	private boolean isThreadRunning;
	private Thread receiveUDPThread; // 接收UDP数据线程
	private DatagramSocket UDPSocket;
	private DatagramPacket sendDatagramPacket;
	private DatagramPacket receiveDatagramPacket;
	private static final int BUFFERLENGTH = 1024; // 缓冲大小
	private byte[] receiveBuffer = new byte[BUFFERLENGTH];
	private byte[] sendBuffer = new byte[BUFFERLENGTH];
	private static final String IMEI = "IMEI";

	private UDPSocketThread() {

	}

	public static UDPSocketThread getInstance(Context mContext) {
		if (instance == null) {
			context = mContext;
			instance = new UDPSocketThread();
		}
		return instance;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isThreadRunning) {

			try {
				UDPSocket.receive(receiveDatagramPacket);
			} catch (IOException e) {
				isThreadRunning = false;
				receiveDatagramPacket = null;
				if (UDPSocket != null) {
					UDPSocket.close();
					UDPSocket = null;
				}
				receiveUDPThread = null;
				Log.e(tag, "UDP数据包接收失败！线程停止");
				e.printStackTrace();
				break;
			}

			if (receiveDatagramPacket.getLength() == 0) {
				Log.i(tag, "无法接收UDP数据或者接收到的UDP数据为空");
				continue;
			}

			String UDPListenResStr = ""; // 清空以前的监听数据
			try {
				UDPListenResStr = new String(receiveBuffer, 0,
						receiveDatagramPacket.getLength(), "gbk");
			} catch (UnsupportedEncodingException e) {
				Log.e(tag, "系统不支持GBK编码");
			}
			Log.i(tag, "接收到的UDP数据内容为:" + UDPListenResStr);

			IPMSGProtocol ipmsgRes = new IPMSGProtocol(UDPListenResStr);
			int commandNo = ipmsgRes.getCommandNo(); // 获取命令字
			String senderIMEI = ipmsgRes.getSenderIMEI(); // 获取对方IMEI
			// 此处暂未做处理
			if (true) { // 过滤自己发送的广播
				switch (commandNo) {

				// 收到上线数据包，添加用户，并回送IPMSG_ANSENTRY应答。
				case IPMSGConst.IPMSG_BR_ENTRY: {
					Log.i(tag, "收到上线通知");
					// addUser(ipmsgRes); // 增加用户至在线列表
					// //
					// BaseActivity.sendEmptyMessage(IpMessageConst.IPMSG_BR_ENTRY);
					//
					// sendUDPdata(IPMSGConst.IPMSG_ANSENTRY,
					// receiveDatagramPacket.getAddress(), mNearByPeople);
					// Log.i(TAG, "成功发送上线应答");
				}
					break;

				// 收到上线应答，更新在线用户列表
				case IPMSGConst.IPMSG_ANSENTRY: {
					Log.i(tag, "收到上线应答");
					// addUser(ipmsgRes); // 增加用户至在线列表
				}
					break;

				// 收到下线广播
				case IPMSGConst.IPMSG_BR_EXIT: {
					// mApplication.removeOnlineUser(senderIMEI, 1); // 移除用户
					Log.i(tag, "根据下线报文成功删除imei为" + senderIMEI + "的用户");
				}
					break;

				// 拒绝接受文件
				case IPMSGConst.IPMSG_RELEASEFILES: {
					// BaseActivity.sendEmptyMessage(IpMessageConst.IPMSG_RELEASEFILES);
				}
					break;

				// 收到消息
				case IPMSGConst.IPMSG_SENDMSG: {
					Log.i(tag, "收到MSG消息");
					String senderIp = receiveDatagramPacket.getAddress()
							.getHostAddress();
					MessageEntity msg = (MessageEntity) ipmsgRes.getAddObject();
					// TcpService tcpService;
					Log.d(tag, msg.getContentType().toString());

					switch (msg.getContentType()) {
					case IMAGE:
						Log.d(tag, "收到图片发送请求");
						// tcpService = TcpService.getInstance(mContext);
						// tcpService.setSavePath(BaseApplication.IMAG_PATH);
						// tcpService.startReceive();
						// sendUDPdata(IPMSGConst.IPMSG_RECIEVE_IMAGE_DATA,
						// senderIp);
						//
						// msg.setMsgContent(BaseApplication.IMAG_PATH
						// + File.separator + msg.getSenderIMEI()
						// + File.separator + msg.getMsgContent());
						Log.d(tag, "接收路径:" + msg.getMsgContent());
						break;

					case VOICE:
						// Log.d(TAG, "收到录音发送请求");
						// tcpService = TcpService.getInstance(mContext);
						// tcpService.setSavePath(BaseApplication.VOICE_PATH);
						// tcpService.startReceive();
						// sendUDPdata(IPMSGConst.IPMSG_RECIEVE_VOICE_DATA,
						// senderIp);
						//
						// msg.setMsgContent(BaseApplication.VOICE_PATH
						// + File.separator + msg.getSenderIMEI()
						// + File.separator + msg.getMsgContent());
						Log.d(tag, "接收路径:" + msg.getMsgContent());
						break;

					case TEXT:
						Log.i(tag, "接收text:" + msg.getMsgContent());
						sendUDPdata(IPMSGConst.IPMSG_RECVMSG, senderIp,
								ipmsgRes.getPacketNo());
						break;

					case FILE:
						break;
					}
					// mDBOperate.addChattingInfo(senderIMEI, mIMEI,
					// msg.getSendTime(), msg.getMsgContent(),
					// msg.getContentType()); // 将聊天记录加入数据库
					// Log.d(TAG, msg.getMsgContent());
					//
					// if (!isExistActiveActivity(msg)) { //
					// 若没有对应的ChatActivity打开
					// mApplication.addUnReadPeople(mApplication.getOnlineUser(senderIMEI));
					// // 添加到未读用户列表
					//
					// }
					// mApplication.addLastMsgCache(senderIMEI, msg); // 添加到消息缓存
					// BaseActivity.sendEmptyMessage(IPMSGConst.IPMSG_SENDMSG);

				}
					break;

				case IPMSGConst.IPMSG_RECIEVE_IMAGE_DATA: {
					Log.d(tag, "收到图片发送请求确认");
					// BaseActivity.sendEmptyMessage(IPMSGConst.IPMSG_RECIEVE_IMAGE_DATA);
				}
					break;
				case IPMSGConst.IPMSG_RECIEVE_VOICE_DATA: {
					Log.d(tag, "收到语音发送请求确认");
					// BaseActivity.sendEmptyMessage(IPMSGConst.IPMSG_RECIEVE_VOICE_DATA);
				}
					break;
				case IPMSGConst.IPMSG_GET_IMAGE_SUCCESS: {

				}
					break;
				} // End of switch

				// 每次接收完UDP数据后，重置长度。否则可能会导致下次收到数据包被截断。
				if (receiveDatagramPacket != null) {
					receiveDatagramPacket.setLength(BUFFERLENGTH);
				}
			}

		}// end while

		receiveDatagramPacket = null;
		if (UDPSocket != null) {
			UDPSocket.close();
			UDPSocket = null;
		}
		receiveUDPThread = null;
	}

	/** 建立Socket连接 **/
	public void connectUDPSocket() {
		try {
			// 绑定端口
			if (UDPSocket == null)
				UDPSocket = new DatagramSocket(IPMSGConst.PORT);
			Log.i(tag, "connectUDPSocket() 绑定端口成功");

			// 创建数据接受包
			if (receiveDatagramPacket == null)
				receiveDatagramPacket = new DatagramPacket(receiveBuffer,
						BUFFERLENGTH);
			Log.i(tag, "connectUDPSocket() 创建数据接收包成功");

			startUDPSocketThread();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	/** 开始监听线程 **/
	public void startUDPSocketThread() {
		if (receiveUDPThread == null) {
			receiveUDPThread = new Thread(this);
			receiveUDPThread.start();
		}
		isThreadRunning = true;
		Log.i(tag, "startUDPSocketThread() 线程启动成功");
	}

	/** 暂停监听线程 **/
	public void stopUDPSocketThread() {
		if (receiveUDPThread != null)
			receiveUDPThread.interrupt();
		isThreadRunning = false;
		Log.i(tag, "stopUDPSocketThread() 线程停止成功");
	}

	/**
	 * 发送UDP数据包
	 * 
	 * @param commandNo
	 *            消息命令
	 * @param targetIP
	 *            目标地址
	 * @param addData
	 *            附加数据
	 * @see IPMSGConst
	 */
	public synchronized void sendUDPdata(int commandNo, String targetIP) {
		sendUDPdata(commandNo, targetIP, null);
	}

	public synchronized void sendUDPdata(int commandNo, InetAddress targetIP) {
		sendUDPdata(commandNo, targetIP, null);
	}

	public synchronized void sendUDPdata(int commandNo, InetAddress targetIP,
			Object addData) {
		Log.e(tag, "obj i");
		sendUDPdata(commandNo, targetIP.getHostAddress(), addData);
	}

	public synchronized void sendUDPdata(int commandNo, String targetIP,
			Object addData) {
		Log.e(tag, "obj s");
		// 构造发送协议数据
		IPMSGProtocol ipmsgProtocol = null;
		if (addData == null) {
			Log.e(tag, "obj s1111");
			ipmsgProtocol = new IPMSGProtocol(IMEI, commandNo);
		} else if (addData instanceof Entity) {
			Log.e(tag, "obj s2222");
			ipmsgProtocol = new IPMSGProtocol(IMEI, commandNo, (Entity) addData);
		} else if (addData instanceof String) {
			Log.e(tag, "obj s3333");
			ipmsgProtocol = new IPMSGProtocol(IMEI, commandNo, (String) addData);
		}
		Log.e(tag, "obj s444444");
		sendUDPdata(ipmsgProtocol, targetIP);
	}

	public synchronized void sendUDPdata(IPMSGProtocol ipmsgProtocol,
			String targetIP) {
		// 构造发送报文
		InetAddress targetAddr;
		try {
			Log.e(tag,
					"targetIP==" + targetIP + "local=="
							+ Utils.getLocalIpAdress(context));
			targetAddr = InetAddress.getByName("192.168.43.1"); // 目的地址
			Log.e(tag, "targetIP2222==");
			sendBuffer = ipmsgProtocol.getProtocolJSON().getBytes("gbk");
			Log.e(tag,
					"targetIP3333==" + targetIP + "local=="
							+ Utils.getLocalIpAdress(context));
			sendDatagramPacket = new DatagramPacket(sendBuffer,
					sendBuffer.length, targetAddr, IPMSGConst.PORT);
//			byte[] buffer = { 0x01 };
//			sendDatagramPacket = new DatagramPacket(buffer, buffer.length,
//					targetAddr, IPMSGConst.PORT);

			Log.i(tag, "sendDatagramPacket 创建成功");
			UDPSocket.send(sendDatagramPacket);
			Log.i(tag, "sendUDPdata() 数据发送成功");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(tag, "sendUDPdata() 发送UDP数据包失败2222");
		} finally {
			sendDatagramPacket = null;
		}
	}

}
