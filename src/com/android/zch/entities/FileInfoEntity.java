package com.android.zch.entities;

import java.io.Serializable;

/**   
* @Title: FileInfoEntity.java 
* @Package com.android.touchjet.entities 
* @Description: TODO 
* @author spring:qw8shop@gmail.com  
* @date 2015-2-1 下午4:12:05 
* @version V1.0   
*/ 

public class FileInfoEntity implements Serializable{
	private int ID;
	private String FileData;
	private int size;
	private String date;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getFileData() {
		return FileData;
	}

	public void setFileData(String fileData) {
		FileData = fileData;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
