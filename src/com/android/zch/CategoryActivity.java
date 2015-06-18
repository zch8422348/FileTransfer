package com.android.zch;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.android.zch.R;
import com.android.zch.base.BaseActivity;
import com.android.zch.entities.FileInfoEntity;
import com.android.zch.file.SearchThread;
import com.android.zch.file.FileCategoryHelper.CategoryInfo;
import com.android.zch.file.FileCategoryHelper.FileCategory;

/**
 * @Title: CategoryActivity.java
 * @Package com.android.touchjet
 * @Description: TODO
 * @author spring:qw8shop@gmail.com
 * @date 2015-2-1 下午5:06:13
 * @version V1.0
 */

public class CategoryActivity extends BaseActivity {
	private List<String> data = new ArrayList<String>();
	private List<FileInfoEntity> fileInfoEntities = new ArrayList<FileInfoEntity>();
	private FileViewFragment fragment;
	private static final int MSG_SCAN_OK = 0;
	private FileCategory fc;
	private static final String tag = "CategoryActivity";
	private List<MyTouchListener> myTouchListeners = new ArrayList<CategoryActivity.MyTouchListener>();

	public interface MyTouchListener {
		public void onTouchEvent(MotionEvent event);
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categoryinfo);
		initViews();
	}

	private void getFragment(Fragment fragment) {
		FragmentManager fragmentManager = CategoryActivity.this
				.getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.catagoryinfo, fragment);
		transaction.commit();
	}

	private void FileSearch(FileCategory fc) {
		SearchThread searchThread = new SearchThread(CategoryActivity.this, fc);
		new Thread(searchThread).start();
	}

	public void getIntents() {
		fc = (FileCategory) getIntent().getSerializableExtra("fc");
		Log.e(tag, "fc=" + fc.toString());
		if (fc.equals(FileCategory.Picture)) {
			fileInfoEntities = (List<FileInfoEntity>) getIntent()
					.getSerializableExtra("data");

		} else {
			FileSearch(fc);
		}
	}

	public List<FileInfoEntity> getFileInfo() {
		return fileInfoEntities;
	}

	public void setFileInfo(List<FileInfoEntity> fileInfoEntities) {
		this.fileInfoEntities = fileInfoEntities;
	}

	public List<FileInfoEntity> getData() {
		return fileInfoEntities;
	}

	public FileCategory getFileCategory() {
		return fc;
	}

	public void updateUI(List<FileInfoEntity> fileInfoEntities) {
		setFileInfo(fileInfoEntities);
		handler.sendEmptyMessage(MSG_SCAN_OK);
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		getIntents();
		fragment = new FileViewFragment();
		getFragment(fragment);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		finish();
		// return;
	}

	public void registerTouchEvent(MyTouchListener listener) {
		myTouchListeners.add(listener);
	}
	public void unregisterTouchEvent(MyTouchListener listener){
		myTouchListeners.remove(listener);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		for (MyTouchListener myTouchListener : myTouchListeners) {
			myTouchListener.onTouchEvent(ev);
		}
		return super.dispatchTouchEvent(ev);
	}

	public Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			// after scan data for update adapter
			case MSG_SCAN_OK:
				if (fileInfoEntities == null || fileInfoEntities.size() == 0) {
					fragment.notityNoData();
				} else {
					fragment.notifyAdapterData(fileInfoEntities);
				}
				break;

			default:
				break;
			}
		}
	};

}
