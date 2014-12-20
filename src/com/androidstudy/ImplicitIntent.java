package com.androidstudy;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**ImplicitIntent extends Activity
 * 打开浏览器（隐式方式）测试；
 * 隐式意图使用示例，配合AndroidManifest.xml: 
 * 		<!-- 隐式意图配置  -->
        <activity android:name="com.androidstudy.ImplicitIntent">
            <intent-filter>
                <!-- 自定义并声明动作action为find -->
	            <action android:name="com.androidstudy.find" />
	            <!-- 声明类别category -->
	            <category android:name="android.intent.category.DEFAULT" />
	            <!-- 自定义并声明数据类型mimeType为application/person -->
	            <data android:mimeType="application/person" />
	            <!-- 自定义并声明数据约束规范scheme为Asian -->
	            <data android:scheme="Asian" />
            </intent-filter>
        </activity>
 * @author Eugene
 * @data 2014-12-20
 */
public class ImplicitIntent extends Activity{
//	private static final String TAG = "ImplicitIntent";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView textView = (TextView) findViewById(R.id.txView);
		textView.setText("实际运行代码请查看MainListActivity.java中的case 12");
		startImplicitIntent();
		
		Button btn1 = (Button) findViewById(R.id.btn1);
		btn1.setVisibility(View.VISIBLE);
		btn1.setText("Open Browser");
		btn1.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
//				浏览器配置信息：
//		        <action android:name="android.intent.action.VIEW" />
//		        <category android:name="android.intent.category.DEFAULT" />
//		        <category android:name="android.intent.category.BROWSABLE" />
//		        <data android:scheme="http" />
//		        <data android:scheme="https" />
//		        <data android:scheme="about" />
//		        <data android:scheme="javascript" />
				Intent intent = new Intent();
				//显式Intent，缺点：若没有设定默认浏览器将报错
//				intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
				//隐式Intent，优点：将应用程序解耦
				intent.setAction("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addCategory("android.intent.category.BROWSABLE");
				intent.setData(Uri.parse("http://www.baidu.com"));
				startActivity(intent);
			}
		});
		
	}
	
	/**隐式Intent设置与调用示例
	 * 实际运行代码请查看MainListActivity.java中的case 12；
	 */
	private void startImplicitIntent(){
//		Intent intent = new Intent();
		//action动作，例如find
//		intent.setAction("com.androidstudy.find");
		//category类别，例如DEFAULT, EMBED, TV等
//		intent.addCategory(Intent.CATEGORY_DEFAULT);
		//data数据（动作的对象），注意setType与setData二者要共存需调用setDataAndType
//		intent.setDataAndType(Uri.parse("Asian:Chinese"), "application/person");
		
		//MIMEType，例如person（即find person）
		//注意：automatically clears any data that was previously set (for example by setData). 
//		intent.setType("application/person");
		//scheme数据约束规范，例如Asian:Chinese
		//注意：setData() automatically clears any type that was previously set by setType or setTypeAndNormalize. 
//		intent.setData(Uri.parse("Asian:Chinese"));
		
//		startActivity(intent);
	}

}
