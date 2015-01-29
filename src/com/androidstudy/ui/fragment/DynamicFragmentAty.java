package com.androidstudy.ui.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.androidstudy.R;

/**DynamicFragmentAty extends Activity
 * Fragment测试（动态）；
 * @author Eugene
 * @data 2015-1-23
 */
public class DynamicFragmentAty extends Activity implements View.OnClickListener{

	private LinearLayout mTabBtnWeixin, mTabBtnFriend, mTabBtnContacts, mTabBtnSettings;
    private ContentFragment mContentWeixin, mContentFriend, mContentContacts, mContentSettings;

    private FragmentManager fragmentManager;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_dynamicfragment);
		
        mTabBtnWeixin = (LinearLayout) findViewById(R.id.tab_bottom_weixin);  
        mTabBtnFriend = (LinearLayout) findViewById(R.id.tab_bottom_friend);
        mTabBtnContacts = (LinearLayout) findViewById(R.id.tab_bottom_contact);
		mTabBtnSettings = (LinearLayout) findViewById(R.id.tab_bottom_setting);
        mTabBtnWeixin.setOnClickListener(this);  
        mTabBtnFriend.setOnClickListener(this);  
        mTabBtnContacts.setOnClickListener(this);  
        mTabBtnSettings.setOnClickListener(this);
        
        fragmentManager = getFragmentManager();
        
        setTab(0);//设置默认Tab为Weixin
    }
	
	@Override  
    public void onClick(View v) {  
		switch (v.getId()) {
		case R.id.tab_bottom_weixin:
			setTab(0);
			break;
		case R.id.tab_bottom_friend:
			setTab(1);
			break;
		case R.id.tab_bottom_contact:
			setTab(2);
			break;
		case R.id.tab_bottom_setting:
			setTab(3);
			break;
		default:
			break;
		}
    }
	
	/**切换Tab
	 * @param index
	 */
	private void setTab(int index){
		// 重置按钮显示效果
		resetTabViewState();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment以防止有多个Fragment显示在界面上的情况
		hideAllContentFragment(transaction);
		switch (index) {
		case 0://Weixin
			//改变按钮显示效果
			((ImageButton) mTabBtnWeixin.findViewById(R.id.btn_tab_bottom_weixin)).setImageResource(R.drawable.tab_weixin_pressed);
			if (mContentWeixin == null) {
				// 如果fragment为空，则创建一个并添加到界面上
				mContentWeixin = new ContentFragment();
				transaction.add(R.id.id_content, mContentWeixin);
			} else // 如果fragment不为空，则直接将它显示出来
				transaction.show(mContentWeixin);
			break;
		case 1://Friend
			((ImageButton) mTabBtnFriend.findViewById(R.id.btn_tab_bottom_friend)).setImageResource(R.drawable.tab_find_frd_pressed);
			if (mContentFriend == null) {
				mContentFriend = new ContentFragment();
				transaction.add(R.id.id_content, mContentFriend);
			} else transaction.show(mContentFriend);
			break;
		case 2://Contacts
			((ImageButton) mTabBtnContacts.findViewById(R.id.btn_tab_bottom_contact)).setImageResource(R.drawable.tab_address_pressed);
			if (mContentContacts == null) {
				mContentContacts = new ContentFragment();
				transaction.add(R.id.id_content, mContentContacts);
			} else transaction.show(mContentContacts);
			break;
		case 3://Settings
			((ImageButton) mTabBtnSettings.findViewById(R.id.btn_tab_bottom_setting)).setImageResource(R.drawable.tab_settings_pressed);
			if (mContentSettings == null) {
				mContentSettings = new ContentFragment();
				transaction.add(R.id.id_content, mContentSettings);
			} else transaction.show(mContentSettings);
			break;
		default:
			break;
		}
		transaction.commit();
	}
	
	/**重置Tab按钮显示效果
	 */
	private void resetTabViewState() {
		((ImageButton) mTabBtnWeixin.findViewById(R.id.btn_tab_bottom_weixin)).setImageResource(R.drawable.tab_weixin_normal);
		((ImageButton) mTabBtnFriend.findViewById(R.id.btn_tab_bottom_friend)).setImageResource(R.drawable.tab_find_frd_normal);
		((ImageButton) mTabBtnContacts.findViewById(R.id.btn_tab_bottom_contact)).setImageResource(R.drawable.tab_address_normal);
		((ImageButton) mTabBtnSettings.findViewById(R.id.btn_tab_bottom_setting)).setImageResource(R.drawable.tab_settings_normal);
	}
	
	/**隐藏所有内容体fragment
	 * @param transaction
	 */
	private void hideAllContentFragment(FragmentTransaction transaction) {
		if (mContentWeixin != null) transaction.hide(mContentWeixin);
		if (mContentFriend != null) transaction.hide(mContentFriend);
		if (mContentContacts != null) transaction.hide(mContentContacts);
		if (mContentSettings != null) transaction.hide(mContentSettings);
	}
  
}
