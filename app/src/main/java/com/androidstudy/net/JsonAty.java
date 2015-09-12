package com.androidstudy.net;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidstudy.R;

/**JsonAty extends Activity
 * json操作（创建、解析）测试；com.loopj.android.http外部包网络测试（POST）；
 * @author Eugene
 * @data 2015-1-25
 */
public class JsonAty extends Activity{
	static final String TAG = "JsonAty";
	
	static final String URL = "http://128.199.226.246/location/getLatestLocation";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_main);
		
		Button btn1 = (Button) findViewById(R.id.btn1);
		btn1.setVisibility(View.VISIBLE);
		btn1.setText("Get Json String(POST)");
	}
	
	/**Button单击事件处理函数
	 * @param view
	 */
	public void onClickProcess(View v){
		if (v.getId() == R.id.btn1) {
			new Thread(){
				@Override
				public void run() {
					try {
						JsonProcess.RequestForString(getApplicationContext());
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}
			}.run();
		}
	}
	
	
}
