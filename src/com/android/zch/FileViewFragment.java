package com.android.zch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.zch.R;
import com.android.zch.CategoryActivity.MyTouchListener;
import com.android.zch.adapter.DragAdapter;
import com.android.zch.base.BaseFragment;
import com.android.zch.entities.ChannelItem;
import com.android.zch.entities.FileInfoEntity;
import com.android.zch.file.FileCategoryHelper.FileCategory;
import com.android.zch.util.Utils;

/**
 * test...
 * 
 * @Title: FileViewFragment.java
 * @Package com.android.touchjet
 * @Description: TODO
 * @author zch:qw8shop@gmail.com
 * @date 2015-2-3 上午10:58:24
 * @version V1.0
 */
@SuppressLint("ResourceAsColor")
public class FileViewFragment extends BaseFragment implements MyTouchListener,
		OnItemLongClickListener {
	private View rootView;
	private CategoryActivity activity;
	private DragAdapter adapter;
	private GridView gridView;
	private LinearLayout emptyView;
	private boolean drag = false;
	private ImageView drag_img;
	private int lastX, lastY, left, right, top, bottom;
	private int position = 0;
	private int viewW = 0;
	private int viewH = 0;
	private Bitmap bitmap;
	private List<FileInfoEntity> fileInfoEntities = new ArrayList<FileInfoEntity>();

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		rootView = View.inflate(getActivity(), R.layout.file_view_fragment,
				null);
		Log.e("ss", "*********");
		activity = (CategoryActivity) getActivity();
		((CategoryActivity) getActivity()).registerTouchEvent(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return initViews();
	}

	private FileCategory getFileCategory() {
		return activity.getFileCategory();
	}

	@Override
	protected View initViews() {
		// TODO Auto-generated method stub
		emptyView = (LinearLayout) rootView.findViewById(R.id.empty_view);
		gridView = (GridView) rootView.findViewById(R.id.gridview);
		drag_img = (ImageView) rootView.findViewById(R.id.touch_img);
		fileInfoEntities = activity.getData();
		adapter = new DragAdapter(getActivity(), fileInfoEntities, gridView,
				getFileCategory());
		if (FileCategory.Apk.equals(activity.getFileCategory())) {
			gridView.setNumColumns(4);
		}
		gridView.setAdapter(adapter);
		left = drag_img.getLeft();
		top = drag_img.getTop();
		right = drag_img.getRight();
		bottom = drag_img.getBottom();
		initEvents();
		return rootView;
	}

	public void notifyAdapterData(List<FileInfoEntity> fileInfoEntities) {
		adapter.setListDate(fileInfoEntities);
	}

	public void notityNoData() {
		Log.e("ssss", "no files------	");
		emptyView.setVisibility(View.VISIBLE);
		gridView.setVisibility(View.GONE);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		gridView.setOnItemLongClickListener(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		((CategoryActivity) getActivity()).unregisterTouchEvent(this);
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
	}

	@Override
	public void onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (drag)
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// lastX = (int) event.getRawX();
				// lastY = (int) event.getRawY();

				left = (int) event.getX() - (drag_img.getWidth() / 2);
				top = (int) event.getY() - (drag_img.getHeight());
				right = left + drag_img.getWidth();
				bottom = top + drag_img.getHeight();
				drag_img.layout(left, top, right, bottom);
				drag_img.setImageBitmap(bitmap);
				drag_img.setVisibility(0);
				break;
			/**
			 * layout(l,t,r,b) l Left position, relative to parent t Top
			 * position, relative to parent r Right position, relative to parent
			 * b Bottom position, relative to parent
			 * */
			case MotionEvent.ACTION_MOVE:
				left = (int) event.getX() - (drag_img.getWidth() / 2);
				top = (int) event.getY() - (drag_img.getHeight());
				right = left + drag_img.getWidth();
				bottom = top + drag_img.getHeight();
				drag_img.layout(left, top, right, bottom);
				drag_img.setVisibility(View.VISIBLE);
				drag_img.setImageBitmap(bitmap);
				if (left < -30) {
				}
				break;
			case MotionEvent.ACTION_UP:
				drag = false;
				gridView.setEnabled(true);
				drag_img.setVisibility(4);
				break;
			default:
				break;
			}
	}
	private void createImage(){
//		ImageView img=new ImageView(getActivity());
//		img.setImageBitmap(bitmap);
//		rootView.add
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view,
			int position, long arg3) {
		// TODO Auto-generated method stub
		this.position = position;
		drag = true;
		viewW = view.getWidth();
		viewH = view.getHeight();
		bitmap = Utils.getBitmapFromFile(new File(fileInfoEntities
				.get(position).getFileData()), viewW, viewH);
		gridView.setEnabled(false);
		return false;
	}
}
