package com.android.zch.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.android.zch.R;
import com.android.zch.R.id;
import com.android.zch.R.layout;
import com.android.zch.entities.ChannelItem;
import com.android.zch.entities.FileInfoEntity;
import com.android.zch.file.UtilsFile;
import com.android.zch.file.FileCategoryHelper.FileCategory;
import com.android.zch.util.MyImageView;
import com.android.zch.util.NativeImageLoader;
import com.android.zch.util.MyImageView.OnMeasureListener;
import com.android.zch.util.NativeImageLoader.NativeImageCallBack;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DragAdapter extends BaseAdapter {
	/** TAG */
	private final static String tag = "DragAdapter";
	private final static String TAG = "DragAdapter";
	/** �Ƿ���ʾ�ײ���ITEM */
	private boolean isItemShow = false;
	private Context context;
	/** ���Ƶ�postion */
	private int holdPosition;
	/** �Ƿ�ı� */
	private boolean isChanged = false;
	/** �Ƿ�ɼ� */
	boolean isVisible = true;
	/** �����϶����б����û�ѡ���Ƶ���б� */
	public List<FileInfoEntity> channelList;
	/** TextView Ƶ������ */
	private TextView item_text;
	/** Ҫɾ����position */
	public int remove_position = -1;
	private Point mPoint = new Point(0, 0);
	private HashMap<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();
	private GridView gridView;
	private FileCategory fc;

	public DragAdapter(Context context, List<FileInfoEntity> channelList,
			GridView gridView, FileCategory fc) {
		this.context = context;
		this.channelList = channelList;
		this.gridView = gridView;
		this.fc = fc;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		if (channelList != null && channelList.size() != 0) {
			return channelList.get(position).getDate();
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Log.e("sss", "position====" + position);
		final ViewHolder viewHolder;
		String path = channelList.get(position).getFileData();
		Log.e(tag,
				"channelList.get(position).getDate()="
						+ channelList.get(position).getDate());
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.grid_child_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (MyImageView) convertView
					.findViewById(R.id.child_image);
			viewHolder.mCheckBox = (CheckBox) convertView
					.findViewById(R.id.child_checkbox);
			viewHolder.txtView = (TextView) convertView
					.findViewById(R.id.txt_name);
			// 用来监听ImageView的宽和高
			viewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {

				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mImageView
					.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		
		viewHolder.mImageView.setTag(path);
//		viewHolder.mCheckBox
//				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//					@Override
//					public void onCheckedChanged(CompoundButton buttonView,
//							boolean isChecked) {
//						// 如果是未选中的CheckBox,则添加动画
//						if (!mSelectMap.containsKey(position)
//								|| !mSelectMap.get(position)) {
//							addAnimation(viewHolder.mCheckBox);
//						}
//						mSelectMap.put(position, isChecked);
//					}
//				});
//
//		viewHolder.mCheckBox
//				.setChecked(mSelectMap.containsKey(position) ? mSelectMap
//						.get(position) : false);
		// 利用NativeImageLoader类加载本地图片
		Log.e(tag, "path="+path);
		Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path,
				mPoint, new NativeImageCallBack() {

					@Override
					public void onImageLoader(Bitmap bitmap, String path) {
						ImageView mImageView = (ImageView) gridView
								.findViewWithTag(path);
						if (bitmap != null && mImageView != null) {
							mImageView.setImageBitmap(bitmap);
						}
					}
				});

		if (bitmap != null) {
			viewHolder.mImageView.setImageBitmap(bitmap);
		} else {
			if (!fc.equals(FileCategory.Picture)){
				if(fc.equals(FileCategory.Apk)){
					viewHolder.mImageView
					.setImageDrawable(UtilsFile.getApkIcon(context, path));
				}else{
					viewHolder.mImageView
					.setImageResource(R.drawable.friends_sends_pictures_no);
				}
			}else{
			viewHolder.mImageView
					.setImageResource(R.drawable.friends_sends_pictures_no);
			}
		}

		return convertView;

		// View view =
		// LayoutInflater.from(context).inflate(R.layout.subscribe_category_item,
		// null);
		// item_text = (TextView) view.findViewById(R.id.text_item);
		// String channel = getItem(position);
		// item_text.setText(channel);

		// if (isChanged && (position == holdPosition) && !isItemShow) {
		// item_text.setText("");
		// item_text.setSelected(true);
		// item_text.setEnabled(true);
		// isChanged = false;
		// }
		// if (!isVisible && (position == -1 + channelList.size())) {
		// item_text.setText("");
		// item_text.setSelected(true);
		// item_text.setEnabled(true);
		// }
		// if(remove_position == position){
		// item_text.setText("");
		// }
		// return view;
	}

	/**
	 * 给CheckBox加点击动画，利用开源库nineoldandroids设置动画
	 * 
	 * @param view
	 */
	private void addAnimation(View view) {
		float[] vaules = new float[] { 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f,
				1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f };
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
				ObjectAnimator.ofFloat(view, "scaleY", vaules));
		set.setDuration(150);
		set.start();
	}

	/**
	 * 获取选中的Item的position
	 * 
	 * @return
	 */
	public List<Integer> getSelectItems() {
		List<Integer> list = new ArrayList<Integer>();
		for (Iterator<Map.Entry<Integer, Boolean>> it = mSelectMap.entrySet()
				.iterator(); it.hasNext();) {
			Map.Entry<Integer, Boolean> entry = it.next();
			if (entry.getValue()) {
				list.add(entry.getKey());
			}
		}

		return list;
	}

	public static class ViewHolder {
		public MyImageView mImageView;
		public CheckBox mCheckBox;
		public TextView txtView;
	}

	// /** ���Ƶ���б� */
	// public void addItem(ChannelItem channel) {
	// channelList.add(channel);
	// notifyDataSetChanged();
	// }

	// /** �϶����Ƶ������ */
	// public void exchange(int dragPostion, int dropPostion) {
	// holdPosition = dropPostion;
	// ChannelItem dragItem = getItem(dragPostion);
	// Log.d(TAG, "startPostion=" + dragPostion + ";endPosition=" +
	// dropPostion);
	// if (dragPostion < dropPostion) {
	// channelList.add(dropPostion + 1, dragItem);
	// channelList.remove(dragPostion);
	// } else {
	// channelList.add(dropPostion, dragItem);
	// channelList.remove(dragPostion + 1);
	// }
	// isChanged = true;
	// notifyDataSetChanged();
	// }

	// /** ��ȡƵ���б� */
	// public List<ChannelItem> getChannnelLst() {
	// return channelList;
	// }

	// /** ����ɾ����position */
	// public void setRemove(int position) {
	// remove_position = position;
	// notifyDataSetChanged();
	// }
	//
	// /** ɾ��Ƶ���б� */
	// public void remove() {
	// channelList.remove(remove_position);
	// remove_position = -1;
	// notifyDataSetChanged();
	// }

	/** ����Ƶ���б� */
	public void setListDate(List<FileInfoEntity> list) {
		channelList = list;
		notifyDataSetChanged();
	}

	/** ��ȡ�Ƿ�ɼ� */
	public boolean isVisible() {
		return isVisible;
	}

	/** �����Ƿ�ɼ� */
	public void setVisible(boolean visible) {
		isVisible = visible;
	}

	/** ��ʾ���µ�ITEM */
	public void setShowDropItem(boolean show) {
		isItemShow = show;
	}
}