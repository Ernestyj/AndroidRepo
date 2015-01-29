package com.androidstudy.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidstudy.R;

/**ContentFragment extends Fragment
 * 内容体fragment；
 * fragment内部控件操控应于onActivityCreated内完成；外部操作将造成空指针异常。
 * @author Eugene
 * @data 2015-1-23
 */
public class ContentFragment extends Fragment{
//	private static final String TAG = "ContentFragment";
	
	private View view = null;
//	private TextView tv = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_content, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		tv = (TextView) getView().findViewById(R.id.tv_fragcontent);
	}
	
	/**注意此方法于fragment动态加载中调用将引发空指针异常；应于onActivityCreated中完成相关操作。
	 * @param s
	 */
//	public void setContentText(String s){
//		tv.setText(s);
//	}

}
