package com.android.zch;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.zch.R;
import com.android.zch.adapter.PicGroupAdapter;
import com.android.zch.base.BaseActivity;
import com.android.zch.entities.FileInfoEntity;
import com.android.zch.entities.ImageEntity;
import com.android.zch.file.FileCategoryHelper.FileCategory;

/**   
* @Title: PicGroupActivity.java 
* @Package com.android.touchjet 
* @Description: TODO
* @author zch:qw8shop@gmail.com  
* @date 2015-2-3 上午10:58:35 
* @version V1.0   
*/
public class PicGroupActivity extends BaseActivity {
	private HashMap<String, List<String>> mGruopMap = new HashMap<String, List<String>>();
	private HashMap<String, List<FileInfoEntity>> entities = new HashMap<String, List<FileInfoEntity>>();
	private List<ImageEntity> list = new ArrayList<ImageEntity>();
	private final static int SCAN_OK = 1;
	private ProgressDialog mProgressDialog;
	private PicGroupAdapter adapter;
	private GridView mGroupGridView;
	List<FileInfoEntity> fileInfoEntities = new ArrayList<FileInfoEntity>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_picgroup_main);
		initViews();
		initEvents();

	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		mGroupGridView = (GridView) findViewById(R.id.main_grid);

		getImages();

	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		mGroupGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(PicGroupActivity.this,
						CategoryActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("fc", FileCategory.Picture);
				bundle.putSerializable("data", (Serializable) entities.get(list.get(position).getFolderName()));
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});
	}

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub

	}

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
	 */
	private void getImages() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}

		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

		new Thread(new Runnable() {

			@Override
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = PicGroupActivity.this
						.getContentResolver();

				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);
				while (mCursor.moveToNext()) {
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					// 获取该图片的父路径名
					String parentName = new File(path).getParentFile()
							.getName();
					Log.e("sss", "parentName="+new File(path).getParentFile()
							.getName());
					// 根据父路径名将图片放入到mGruopMap中
					if (!mGruopMap.containsKey(parentName)) {
						List<String> chilFeList = new ArrayList<String>();
						chilFeList.add(path);
						List<FileInfoEntity> chileList2=new ArrayList<FileInfoEntity>();
						FileInfoEntity fileInfoEntity=new FileInfoEntity();
						fileInfoEntity.setFileData(path);
						chileList2.add(fileInfoEntity);
						mGruopMap.put(parentName, chilFeList);
						entities.put(parentName, chileList2);
					} else {
						mGruopMap.get(parentName).add(path);
						FileInfoEntity fileInfoEntity=new FileInfoEntity();
						fileInfoEntity.setFileData(path);
						entities.get(parentName).add(fileInfoEntity);
					}
				}

				mCursor.close();

				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(SCAN_OK);

			}
		}).start();

	}

	/**
	 * 组装分组界面GridView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中 所以需要遍历HashMap将数据组装成List
	 * 
	 * @param mGruopMap
	 * @return
	 */
	private List<ImageEntity> subGroupOfImage(
			HashMap<String, List<String>> mGruopMap) {
		if (mGruopMap.size() == 0) {
			return null;
		}
		List<ImageEntity> list = new ArrayList<ImageEntity>();

		Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<String>> entry = it.next();
			ImageEntity mImageBean = new ImageEntity();
			String key = entry.getKey();
			List<String> value = entry.getValue();

			mImageBean.setFolderName(key);
			mImageBean.setImageCounts(value.size());
			mImageBean.setTopImagePath(value.get(0));// 获取该组的第一张图片
			list.add(mImageBean);
			
			FileInfoEntity fileInfoEntity=new FileInfoEntity();
			fileInfoEntity.setFileData(key);
			fileInfoEntities.add(fileInfoEntity);
			
			
		}

		return list;

	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SCAN_OK:
				// 关闭进度条
				mProgressDialog.dismiss();
				adapter = new PicGroupAdapter(PicGroupActivity.this,
						list = subGroupOfImage(mGruopMap), mGroupGridView);
				Log.e("222", "&&&&&&="+fileInfoEntities.get(0).getFileData());
				mGroupGridView.setAdapter(adapter);
				break;
			}
		}

	};
}
