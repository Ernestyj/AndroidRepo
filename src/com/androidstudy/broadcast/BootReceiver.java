package com.androidstudy.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**BootReceiver extends BroadcastReceiver
 * 开机自启动服务
 * @author Eugene
 * @data 2014-12-30
 */
public class BootReceiver extends BroadcastReceiver{
//	private static final String TAG = "BootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		
		//开启来去电窃听服务
//		context.startService(new Intent(context, PhoneWiretap.class));
		
		
	}

}
