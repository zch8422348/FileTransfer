package com.android.zch.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**   
* @Title: BaseFragment.java 
* @Package com.android.touchjet.base 
* @Description: TODO
* @author zch:qw8shop@gmail.com  
* @date 2015-1-30 下午4:42:00 
* @version V1.0   
*/
public abstract class BaseFragment extends Fragment{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	

	protected abstract View initViews();

	protected abstract void initEvents();
	
//	protected abstract void onClick(OnClickListener listener);
}
