package com.androidstudy.phone;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidstudy.R;

/**
 * @author Eugene
 * @date 2014-12-3
 */
public class SendSMS extends Activity{
	private static final int INTERVAL = 10000;
	private static final String TAG = "SendSMS";
	private static final String TOAST_STRING = "每10s发送一次短信到：";
	private static final String TEXT = "Hello";
	private static final String NUM = "13808024296";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_common_btn);
		
		Button btn = (Button) findViewById(R.id.btn);
		btn.setText(TAG + ": " + TEXT + " to " + NUM + " per " + INTERVAL + "ms");
		btn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						//Send SMS repeatedly
						while(true){
							//相当于Thread.sleep()，但无需处理异常
							SystemClock.sleep(INTERVAL);
							//短信管理器
							SmsManager smsManager = SmsManager.getDefault();
							smsManager.sendTextMessage(NUM, //收件人号码
									null, //短信中心号码，可忽略
									TEXT, //信息内容
									null, //若发送成功，回调此广播通知我们
									null);//若对方接受成功，回调此广播通知我们
							
							Toast.makeText(getApplicationContext(), TOAST_STRING + NUM, Toast.LENGTH_SHORT).show();
						}
					}
				}).start();
			}
		});
		
		
		
	}

}
