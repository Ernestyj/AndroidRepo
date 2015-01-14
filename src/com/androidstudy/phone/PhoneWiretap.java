package com.androidstudy.phone;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import com.androidstudy.R;

/**PhoneWiretap extends Service
 * 来去电窃听（录音）
 * 录音使用MediaRecorder类，电话状态监听采用PhoneStateListener
 * Extra test: 流氓服务示例
 * @author Eugene
 * @data 2014-12-30
 */
public class PhoneWiretap extends Service{
//	private static final String TAG = "PhoneWiretap";
	
	// 电话管理器
	private TelephonyManager tm;
	// 监听器对象
	private MyListener listener;
	// 声明录音机
	private MediaRecorder mediaRecorder;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	// 服务创建的时候调用的方法
	@Override
	public void onCreate() {
		super.onCreate();
		
		Toast.makeText(getApplicationContext(), "来去电窃听服务开启", Toast.LENGTH_SHORT).show();
		// 后台监听电话的呼叫状态
		tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);// 得到电话管理器
		listener = new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	// 服务销毁的时候调用的方法
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		//流氓代码，在销毁服务前再次开启此服务（服务别名）
//		Intent i = new Intent(this, SystemService2.class);
//		startService(i);
		
		Toast.makeText(getApplicationContext(), "来去电窃听服务销毁", Toast.LENGTH_SHORT).show();
		// 取消电话的监听
		// PhoneStateListener.LISTEN_NONE: Stop listening for updates
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
	}

	/**MyListener extends PhoneStateListener
	 * PhoneStateListener电话监听器
	 * @author Eugene
	 * @data 2014-12-30
	 */
	private class MyListener extends PhoneStateListener {
		@Override// 当电话的呼叫状态发生变化的时候调用的方法
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			try {
				switch (state) {
				case TelephonyManager.CALL_STATE_RINGING://铃响状态
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK://通话状态
					Toast.makeText(getApplicationContext(), "开始录音", Toast.LENGTH_LONG).show();
					//开始录音
					//1.实例化一个录音机
					mediaRecorder = new MediaRecorder();
					//2.指定录音机的声音源
					mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					//3.设置录制的文件输出的格式
					mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
					//4.指定录音文件的名称
					File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+".mp3");
					mediaRecorder.setOutputFile(file.getAbsolutePath());
					//5.设置音频的编码
					mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
					//6.准备开始录音
					mediaRecorder.prepare();
					//7.开始录音
					mediaRecorder.start();
					break;
				case TelephonyManager.CALL_STATE_IDLE://空闲状态
					if(mediaRecorder != null){
						//8.停止捕获
						mediaRecorder.stop();
						//9.释放资源
						mediaRecorder.release();
						mediaRecorder = null;
						
						Toast.makeText(getApplicationContext(), "录制完毕", Toast.LENGTH_LONG).show();
					}
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
