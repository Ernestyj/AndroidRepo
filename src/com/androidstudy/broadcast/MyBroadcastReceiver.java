package com.androidstudy.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.androidstudy.R;

/**MyBroadcastReceiver extends BroadcastReceiver
 * 自定义广播接收示例，广播发送自SendBroadcast.class；
 * 注意配置好action:com.androidstudy.broadcast.send；
 * 配置示例：注意Warning:Exported receiver does not require permission，即不跨程序时不需声明intent-filter
 * 		<receiver android:name="com.androidstudy.broadcast.MyBroadcastReceiver">
  			<intent-filter>
                <action android:name="com.androidstudy.broadcast.send" />
            </intent-filter>
        </receiver>
 * @author Eugene
 * @data 2014-12-22
 */
public class MyBroadcastReceiver extends BroadcastReceiver{
//	private static final String TAG = "MyBroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "Message from SendBroadcast.class received.", Toast.LENGTH_LONG).show();
	}

}
