package com.android.zch.entities;

import android.util.Log;

/**
 * @Title: Message.java
 * @Package com.android.touchjet.entities
 * @Description: TODO
 * @author spring:qw8shop@gmail.com
 * @date 2015-2-3 下午11:44:10
 * @version V1.0
 */

public class MessageEntity extends Entity {

	private String senderIMEI;
	private String sendTime;
	private String MsgContent;
	private CONTENT_TYPE contentType;
	private static final String tag = "MessageEntity";

	public MessageEntity() {
	}

	public MessageEntity(String paramSenderIMEI, String paramSendTime,
			String paramMsgContent, CONTENT_TYPE paramContentType) {
		this.senderIMEI = paramSenderIMEI;
		this.sendTime = paramSendTime;
		this.MsgContent = paramMsgContent;
		this.contentType = paramContentType;

		Log.e(tag, "this.senderIMEI=" + this.senderIMEI + "|this.sendtime="
				+ this.sendTime + "|this.MsgContent " + this.MsgContent
				+ "|this.contentType" + this.contentType);
	}

	/** 消息内容类型 **/
	public enum CONTENT_TYPE {
		TEXT, IMAGE, FILE, VOICE;
	}

	/**
	 * 获取消息发送方IMEI
	 * 
	 * @return
	 */

	public String getSenderIMEI() {
		return senderIMEI;
	}

	/**
	 * 设置消息发送方IMEI
	 * 
	 * @param paramSenderIMEI
	 * 
	 */
	public void setSenderIMEI(String paramSenderIMEI) {
		this.senderIMEI = paramSenderIMEI;
	}

	/**
	 * 获取消息内容类型
	 * 
	 * @return
	 * @see CONTENT_TYPE
	 */
	public CONTENT_TYPE getContentType() {
		return contentType;
	}

	/**
	 * 设置消息内容类型
	 * 
	 * @param paramContentType
	 * @see CONTENT_TYPE
	 */
	public void setContentType(CONTENT_TYPE paramContentType) {
		this.contentType = paramContentType;
	}

	/**
	 * 获取消息发送时间
	 * 
	 * @return
	 */
	public String getSendTime() {
		return sendTime;
	}

	/**
	 * 设置消息发送时间
	 * 
	 * @param paramSendTime
	 *            发送时间,格式 xx年xx月xx日 xx:xx:xx
	 */
	public void setSendTime(String paramSendTime) {
		this.sendTime = paramSendTime;
	}

	/**
	 * 获取消息内容
	 * 
	 * @return
	 */
	public String getMsgContent() {
		return MsgContent;
	}

	/**
	 * 设置消息内容
	 * 
	 * @param paramMsgContent
	 */
	public void setMsgContent(String paramMsgContent) {
		this.MsgContent = paramMsgContent;
	}

	/**
	 * 克隆对象
	 * 
	 * @param
	 */

	public MessageEntity clone() {
		return new MessageEntity(senderIMEI, sendTime, MsgContent, contentType);
	}

}
