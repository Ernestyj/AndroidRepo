package com.androidstudy.phone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.androidstudy.R;

/**Dial extends Activity
 * Additional Test: requestWindowFeature(Window.FEATURE_NO_TITLE);
 * @author Eugene
 * @date 2014-12-3
 */
public class Dial extends Activity{
	private static final String TAG = "Dial";
	private static final String NUM = "10086";
	private static final String TEL = "tel:";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//去除标题, 必须在setContentView方法前调用，注意此种方式在初始化界面时仍会闪现一下标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_main);
		
		Button btn = (Button) findViewById(R.id.btn1);
		btn.setVisibility(View.VISIBLE);
		btn.setText(TAG + " to " + NUM);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			    //添加拨打电话的权限 <uses-permission android:name="android.permission.CALL_PHONE"/>
				intent.setAction(Intent.ACTION_DIAL);
				intent.setData(Uri.parse(TEL + NUM));
				startActivity(intent);
			}
		});
	}

}
