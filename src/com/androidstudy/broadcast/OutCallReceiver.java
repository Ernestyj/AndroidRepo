package com.androidstudy.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.androidstudy.R;

/**OutCallReceiver extends BroadcastReceiver
 * IP拨号助手；
 * 注意配置广播接收者：
 * 		<!-- 配置广播接收者 -->
        <receiver android:name="com.androidstudy.broadcast.OutCallReceiver">
            <intent-filter>
                <!-- 配置其动作为外拨电话 -->
                <action android:name="android.intent.action.NEW_OUTGOING_CALL"/>
            </intent-filter>
        </receiver>
 * 注意添加相应权限：
 * 		<!-- 处理拨出电话的权限 -->
    	<uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"/>
 * @author Eugene
 * @data 2014-12-22
 */
public class OutCallReceiver extends BroadcastReceiver{
//	private static final String TAG = "OutCallReceiver";

	private static final String NUM_PREFIX = "0797";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		//注意getResultData()是有序广播专用函数，无序广播不支持
		String number = getResultData();
		System.out.println("Calling: " + number);
		//禁止向5556拨打电话
		if("5556".equals(number)) setResultData(null);
		//在拨打号码前添加数字
		else setResultData(NUM_PREFIX + number);
		
		
	}
	

}
