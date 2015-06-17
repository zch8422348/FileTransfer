package com.android.zch;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.android.zch.R;
import com.android.zch.adapter.DragAdapter;
import com.android.zch.base.BaseFragment;
import com.android.zch.entities.ChannelItem;
import com.android.zch.entities.FileInfoEntity;
import com.android.zch.file.FileCategoryHelper.FileCategory;

/**   
* @Title: FileViewFragment.java 
* @Package com.android.touchjet 
* @Description: TODO
* @author zch:qw8shop@gmail.com  
* @date 2015-2-3 上午10:58:24 
* @version V1.0   
*/
public class FileViewFragment extends BaseFragment {
	private View rootView;
	private CategoryActivity activity;
	private DragAdapter adapter;
	private GridView gridView;
	private LinearLayout emptyView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		rootView = View.inflate(getActivity(), R.layout.file_view_fragment,
				null);
		Log.e("ss", "*********");
		activity = (CategoryActivity) getActivity();
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
		adapter = new DragAdapter(getActivity(), activity.getData(), gridView,
				getFileCategory());
		if(FileCategory.Apk.equals(activity.getFileCategory())){
			gridView.setNumColumns(4);
		}
		gridView.setAdapter(adapter);
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

	// public void getCategoryAdapter(FileCategory fc) {
	// switch (fc) {
	// case Doc:
	// case Zip:
	// case Apk:
	// break;
	// case Music:
	// adapter = new DragAdapter(getActivity(), activity.getData(),
	// gridView, fc);
	// break;
	// case Video:
	// adapter = new DragAdapter(getActivity(), activity.getData(),
	// gridView);
	// break;
	// case Picture:
	// adapter = new DragAdapter(getActivity(), activity.getData(),
	// gridView);
	// break;
	// default:
	// break;
	// }
	// }

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

	}

}
