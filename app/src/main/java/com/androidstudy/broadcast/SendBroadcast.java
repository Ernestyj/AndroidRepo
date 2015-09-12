package com.androidstudy.broadcast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.androidstudy.R;

/**SendBroadcast extends Activity
 * 自定义发送广播示例，由MyBroadcastReceiver.class来接收
 * action:com.androidstudy.broadcast.send
 * @author Eugene
 * @data 2014-12-22
 */
public class SendBroadcast extends Activity{
//	private static final String TAG = "SendBroadcast";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btn1 = (Button) findViewById(R.id.btn1);
		btn1.setVisibility(View.VISIBLE);
		btn1.setText("Send Broadcast");
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				//自定义广播动作
				intent.setAction("com.androidstudy.broadcast.send");
				//发送无序广播，不可拦截、不可终止
				sendBroadcast(intent);
				//发送有序广播，可拦截、可终止、可修改数据
//				sendOrderedBroadcast(intent, null);
//				sendOrderedBroadcast(intent, receiverPermission, resultReceiver, scheduler, initialCode, initialData, initialExtras);
			}
		});
	}
	
}
