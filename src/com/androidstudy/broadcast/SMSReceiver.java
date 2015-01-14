package com.androidstudy.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import com.androidstudy.R;

/**SMSReceiver extends BroadcastReceiver
 * 短信窃听（注意4.4以后版本可能失效）
 * 注意：短信到来的广播接收者与 4.4版本取消
 * 注意配置广播接收者：
 * 
 * 注意添加权限：
 * 		<!-- 接收SMS的权限 -->
    	<uses-permission android:name="android.permission.RECEIVE_SMS"/>
 * @author Eugene
 * @data 2014-12-22
 */
public class SMSReceiver extends BroadcastReceiver{
//	private static final String TAG = "SMSReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("SMS received.");
		//pdus是一种短信存储格式
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		for (Object obj : objs) {
			// 得到短信对象
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
			String body = smsMessage.getMessageBody();
			String sender = smsMessage.getOriginatingAddress();
			System.out.println("Body: " + body);
			System.out.println("Sender: " + sender);
			//禁止向5556发送SMS
			if ("5556".equals(sender)) {
				// 终止掉当前的广播
				abortBroadcast();
			}
		}
	}

}
