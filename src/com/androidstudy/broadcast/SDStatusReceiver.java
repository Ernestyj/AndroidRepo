package com.androidstudy.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.androidstudy.R;

/**SDStatusReceiver extends BroadcastReceiver
 * 监听SD卡移除/卸载状况
 * 注意配置广播接收者：
 * 		<receiver android:name="com.androidstudy.broadcast.SDStatusReceiver">
            <intent-filter>
                <action android:name="android.intent.action.MEDIA_UNMOUNTED"/>
                <action android:name="android.intent.action.MEDIA_REMOVED"/>
               	<!-- 注意设置data scheme为file -->
               	<data android:scheme="file"></data>
            </intent-filter>
        </receiver>
 * 主要添加权限：
 * 		<!-- 安装/卸载SD卡的权限 -->
    	<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
 * @author Eugene
 * @data 2014-12-22
 */
public class SDStatusReceiver extends BroadcastReceiver{
//	private static final String TAG = "SDStatusReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "SD is removed or unmounted.", Toast.LENGTH_LONG).show();
	}

}
