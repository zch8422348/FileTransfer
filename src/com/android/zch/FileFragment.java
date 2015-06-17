package com.android.zch;

import java.util.HashMap;

import com.android.zch.R;
import com.android.zch.MainActivity;
import com.android.zch.base.BaseActivity;
import com.android.zch.base.BaseFragment;
import com.android.zch.file.FileCategoryHelper;
import com.android.zch.file.UtilsFile;
import com.android.zch.file.FileCategoryHelper.CategoryInfo;
import com.android.zch.file.FileCategoryHelper.FileCategory;
import com.android.zch.file.UtilsFile.SDCardInfo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @Title: FileFragment.java
 * @Package com.android.touchjet
 * @Description: TODO
 * @author zch:qw8shop@gmail.com
 * @date 2015-2-3 上午10:58:17
 * @version V1.0
 */
public class FileFragment extends BaseFragment implements OnClickListener {
	private LinearLayout lay_music, lay_video, lay_pic, lay_doc, lay_zip,
			lay_apk;
	private static String tag = "FileFragment";
	private HashMap<FileCategory, Integer> categoryIndex = new HashMap<FileCategory, Integer>();
	private FileCategoryHelper fileCategoryHelper;
	private View rootView;
	private CategoryBar categoryBar;
	private Intent intent;
	private HashMap<FileCategory, CategoryInfo> categoryInfo = new HashMap<FileCategory, CategoryInfo>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		rootView = View.inflate(getActivity(), R.layout.file_info_fargment,
				null);
		setupCategoryInfo();
		getCategoryInfo();
		// categoryInfo=fileCategoryHelper.getCategoryInfos();
		// categoryInfo.get("").
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return initViews();
	}

	@Override
	protected View initViews() {
		lay_music = (LinearLayout) rootView.findViewById(R.id.category_music);
		lay_video = (LinearLayout) rootView.findViewById(R.id.category_video);
		lay_pic = (LinearLayout) rootView.findViewById(R.id.category_picture);
		lay_doc = (LinearLayout) rootView.findViewById(R.id.category_document);
		lay_zip = (LinearLayout) rootView.findViewById(R.id.category_zip);
		lay_apk = (LinearLayout) rootView.findViewById(R.id.category_apk);

		// txt_music = (TextView)
		// rootView.findViewById(R.id.category_music_count);
		// txt_video = (TextView)
		// rootView.findViewById(R.id.category_video_count);
		// txt_pic = (TextView)
		// rootView.findViewById(R.id.category_picture_count);
		// txt_doc = (TextView)
		// rootView.findViewById(R.id.category_document_count);
		// txt_zip = (TextView) rootView.findViewById(R.id.category_zip_count);
		// txt_apk = (TextView) rootView.findViewById(R.id.category_apk_count);

		initEvents();
		return rootView;

	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		lay_music.setOnClickListener(this);
		lay_video.setOnClickListener(this);
		lay_pic.setOnClickListener(this);
		lay_doc.setOnClickListener(this);
		lay_zip.setOnClickListener(this);
		lay_apk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		MainActivity activity = (MainActivity) getActivity();
		activity.send();
		Bundle bundle = new Bundle();
		switch (v.getId()) {
		case R.id.category_music:
			intent = new Intent(getActivity(), CategoryActivity.class);
			bundle.putSerializable("fc", FileCategory.Music);
			intent.putExtras(bundle);
			getActivity().startActivity(intent);
			break;
		case R.id.category_video:
			intent = new Intent(getActivity(), CategoryActivity.class);
			bundle.putSerializable("fc", FileCategory.Video);
			intent.putExtras(bundle);
			getActivity().startActivity(intent);
			break;
		case R.id.category_picture:
			intent = new Intent(getActivity(), PicGroupActivity.class);
			bundle.putSerializable("fc", FileCategory.Picture);
			intent.putExtras(bundle);
			getActivity().startActivity(intent);
			break;
		case R.id.category_apk:
			intent = new Intent(getActivity(), CategoryActivity.class);
			bundle.putSerializable("fc", FileCategory.Apk);
			intent.putExtras(bundle);
			getActivity().startActivity(intent);
			break;
		case R.id.category_document:
			intent = new Intent(getActivity(), CategoryActivity.class);
			bundle.putSerializable("fc", FileCategory.Doc);
			intent.putExtras(bundle);
			getActivity().startActivity(intent);
			break;
		case R.id.category_zip:
			intent = new Intent(getActivity(), CategoryActivity.class);
			bundle.putSerializable("fc", FileCategory.Zip);
			intent.putExtras(bundle);
			getActivity().startActivity(intent);
			break;
		case R.id.category_favorite:
			// intent = new Intent(getActivity(), PicGroupActivity.class);
			// bundle.putSerializable("fc", FileCategory.Zip);
			// intent.putExtras(bundle);
			// getActivity().startActivity(intent);
			break;
		default:
			break;
		}

	}

	private void setCategoryCount(FileCategory fc, long count) {
		int id = getCategoryCountId(fc);
		if (id == 0)
			return;

		setTextView(id, "(" + count + ")");
	}

	private void setTextView(int id, String t) {
		TextView text = (TextView) rootView.findViewById(id);
		text.setText(t);
	}

	private static int getCategoryCountId(FileCategory fc) {
		switch (fc) {
		case Music:
			return R.id.category_music_count;
		case Video:
			return R.id.category_video_count;
		case Picture:
			return R.id.category_picture_count;
			// case Theme:
			// return R.id.category_picture_count;
		case Doc:
			return R.id.category_document_count;
		case Zip:
			return R.id.category_zip_count;
		case Apk:
			return R.id.category_apk_count;
		case Favorite:
			return R.id.category_favorite_count;
		}

		return 0;
	}

	// set view of categoryinfo
	private void setupCategoryInfo() {
		fileCategoryHelper = new FileCategoryHelper(getActivity());
		categoryBar = (CategoryBar) rootView.findViewById(R.id.category_bar);
		int[] imgs = new int[] { R.drawable.category_bar_music,
				R.drawable.category_bar_video, R.drawable.category_bar_picture,
				R.drawable.category_bar_theme,
				R.drawable.category_bar_document, R.drawable.category_bar_zip,
				R.drawable.category_bar_apk, R.drawable.category_bar_other };
		for (int i = 0; i < imgs.length; i++) {
			categoryBar.addCategory(imgs[i]);
		}
		Log.e(tag, "FileCategoryHelper.sCategories.length="
				+ FileCategoryHelper.sCategories.length);
		for (int i = 0; i < FileCategoryHelper.sCategories.length; i++) {
			categoryIndex.put(FileCategoryHelper.sCategories[i], i);
		}
	}

	public void notifyFileChanged() {

	}

	// get caterotyinfo
	public void getCategoryInfo() {
		refreshCategoryInfo();
	}

	// refreshcategoryInfo
	private void refreshCategoryInfo() {
		SDCardInfo sdCardInfo = UtilsFile.getSDCardInfo();
		if (sdCardInfo != null) {
			categoryBar.setFullValue(sdCardInfo.total);
			setTextView(
					R.id.sd_card_capacity,
					getString(R.string.sd_card_size,
							UtilsFile.convertStorage(sdCardInfo.total)));
			setTextView(
					R.id.sd_card_available,
					getString(R.string.sd_card_available,
							UtilsFile.convertStorage(sdCardInfo.free)));
		}
		fileCategoryHelper.refreshCategoryInfo();
		Log.e(tag, "categoryInfo.count=" + FileCategoryHelper.sCategories);
		long size = 0;
		for (FileCategory fc : FileCategoryHelper.sCategories) {
			CategoryInfo categoryInfo = fileCategoryHelper.getCategoryInfos()
					.get(fc);
			Log.e(tag, "categoryInfo.count=" + categoryInfo.count);
			setCategoryCount(fc, categoryInfo.count);

			// other category size should be set separately with calibration
			if (fc == FileCategory.Other)
				continue;

			setCategorySize(fc, categoryInfo.size);
			setCategoryBarValue(fc, categoryInfo.size);
			size += categoryInfo.size;
		}
		Log.e(tag, "123123");

		// other info
		if (sdCardInfo != null) {
			long otherSize = sdCardInfo.total - sdCardInfo.free - size;
			setCategorySize(FileCategory.Other, otherSize);
			setCategoryBarValue(FileCategory.Other, otherSize);
		}

		// setCategoryCount(FileCategory.Favorite, mFavoriteList.getCount());

		if (categoryBar.getVisibility() == View.VISIBLE) {
			categoryBar.startAnimation();
		}
	}

	private void setCategorySize(FileCategory fc, long size) {
		int txtId = 0;
		int resId = 0;
		switch (fc) {
		case Music:
			txtId = R.id.category_legend_music;
			resId = R.string.category_music;
			break;
		case Video:
			txtId = R.id.category_legend_video;
			resId = R.string.category_video;
			break;
		case Picture:
			txtId = R.id.category_legend_picture;
			resId = R.string.category_picture;
			break;
		// case Theme:
		// txtId = R.id.category_legend_theme;
		// resId = R.string.category_theme;
		// break;
		case Doc:
			txtId = R.id.category_legend_document;
			resId = R.string.category_document;
			break;
		case Zip:
			txtId = R.id.category_legend_zip;
			resId = R.string.category_zip;
			break;
		case Apk:
			txtId = R.id.category_legend_apk;
			resId = R.string.category_apk;
			break;
		// case Other:
		// txtId = R.id.category_legend_other;
		// resId = R.string.category_other;
		// break;
		}

		if (txtId == 0 || resId == 0)
			return;

		setTextView(txtId,
				getString(resId) + ":" + UtilsFile.convertStorage(size));
	}

	private void setCategoryBarValue(FileCategory f, long size) {
		if (categoryBar == null) {
			categoryBar = (CategoryBar) rootView
					.findViewById(R.id.category_bar);
		}
		categoryBar.setCategoryValue(categoryIndex.get(f), size);
	}

}
