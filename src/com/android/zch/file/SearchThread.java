package com.android.zch.file;

import java.util.ArrayList;
import java.util.List;

import com.android.zch.CategoryActivity;
import com.android.zch.entities.FileInfoEntity;
import com.android.zch.file.FileCategoryHelper.FileCategory;
import com.android.zch.file.FileCategoryHelper.SortMethod;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Files.FileColumns;
import android.util.Log;

/**
 * @Title: SearchThread.java
 * @Package com.android.touchjet.file
 * @Description: TODO
 * @author spring:qw8shop@gmail.com
 * @date 2015-2-1 下午3:16:18
 * @version V1.0
 */

public class SearchThread implements Runnable {
	private FileCategory fc;
	private CategoryActivity context;
	private FileCategoryHelper categoryHelper;
	private final static String tag="SearchThread";
	public SearchThread(CategoryActivity context, FileCategory fc) {
		this.fc = fc;
		this.context = context;
		categoryHelper = new FileCategoryHelper(context);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		query();
	}
	private void query() {
		Uri uri = categoryHelper.getContentUriByCategory(fc);
		ContentResolver contentResolver = context.getContentResolver();
		String[] columns = new String[] { FileColumns._ID, FileColumns.DATA,
				FileColumns.SIZE, FileColumns.DATE_MODIFIED };
		Cursor cursor = contentResolver.query(uri, columns,
				categoryHelper.buildSelectionByCategory(fc), null,
				categoryHelper.buildSortOrder(SortMethod.date));
		if(cursor==null){
			Log.e(tag, "cursor null");
			return;
		}
		List<FileInfoEntity> fileInfoEntities =new ArrayList<FileInfoEntity>();
		while(cursor.moveToNext()){
			FileInfoEntity fileInfo=new FileInfoEntity();
			fileInfo.setID(cursor.getInt(cursor.getColumnIndex(FileColumns._ID)));
			fileInfo.setFileData(cursor.getString(cursor.getColumnIndex(FileColumns.DATA)));
			fileInfo.setDate(cursor.getString(cursor.getColumnIndex(FileColumns.DATE_MODIFIED)));
			fileInfo.setSize(cursor.getInt(cursor.getColumnIndex(FileColumns.SIZE)));
			fileInfoEntities.add(fileInfo);
		}
		cursor.close();
		context.updateUI(fileInfoEntities);
	}
}
