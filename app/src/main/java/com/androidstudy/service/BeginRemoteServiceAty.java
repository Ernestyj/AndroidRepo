package com.androidstudy.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidstudy.R;

/**BeginRemoteServiceAty extends Activity
 * 用于远程服务测试；
 * *********************************************************************************************
 * 调用（远程）服务中方法的步骤：
 * 		1. 在远程Service内部定义一个IBinder（中间人）用来调用服务中的方法（注意：Stub实现了IBinder接口）；
 * 		#1.1  把暴露公共方法的接口文件的扩展名改为.aidl文件，并去掉访问修饰符 public
 * 			示例：RemoteIntermediator extends IRemoteIntermediator.Stub
 * 		2. 在远程Service的onBind()中返回一个IBinder对象（中间人）；
 * 		3. 在Activity中，采用绑定的方式开启服务；
 * 		#4. 在Activity中，当服务被连接的时候（服务被成功绑定）获取IBinder对象（中间人）；
 * 			示例：intermediator = IRemoteIntermediator.Stub.asInterface(service);
 * 		5. 在Activity中，通过IBinder中间人调用服务里面的方法。
 * 
 * @author Eugene
 * @data 2015-1-1
 */
public class BeginRemoteServiceAty extends Activity{
//	private static final String TAG = "BeginRemoteServiceAty";
	
	private MyRemoteServiceConnection conn;
	private IRemoteIntermediator intermediator;
	
	private Button btn1, btn2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_main);
		
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		
		btn1.setVisibility(View.VISIBLE);
		btn1.setText("RemoteService(bind)");
		
		btn2.setVisibility(View.VISIBLE);
		btn2.setEnabled(false);
		btn2.setText("RemoteService(callMethodInRemoteService)");
	}
	
	@Override
	protected void onDestroy() {
		unbindService(conn);
		super.onDestroy();
	}
	
	/**Button单击事件处理函数
	 * @param view
	 */
	public void onClickProcess(View v){
		if (v.getId() == R.id.btn1) {
			bind();
			btn2.setEnabled(true);
		} else if (v.getId() == R.id.btn2) {
			call();
		}
	}
	
	/**绑定远程服务
     */
	private void bind(){
    	Intent intent = new Intent();
    	intent.setAction("com.androidstudy.service.remoteService");
    	
    	conn = new MyRemoteServiceConnection();
    	bindService(intent, conn, BIND_AUTO_CREATE);
    }
    
    /**通过中间人调用服务里面的方法
     */
    private void call(){
    	try {
			intermediator.callMethodInRemoteService();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    }
    
    /**MyRemoteServiceConnection implements ServiceConnection
     * ServiceConnection: Interface for monitoring the state of an application service.
     * @author Eugene
     * @data 2015-1-1
     */
    private class MyRemoteServiceConnection implements ServiceConnection{
    	
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Toast.makeText(getApplicationContext(), "onServiceConnected", Toast.LENGTH_SHORT).show();
			
			//下面获取IBinder的方式适用于本地
//			intermediator = (IRemoteIntermediator) service;
			
			//调用远程服务时，用Stub方式获取IBinder对象
			//Stub: Local-side IPC implementation stub class. 
			intermediator = IRemoteIntermediator.Stub.asInterface(service);
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
    }

}
