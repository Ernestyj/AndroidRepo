package com.androidstudy.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidstudy.R;

/**BeginServiceAty extends Activity
 * 用于服务测试；
 * *********************************************************************************************
 * 调用服务中方法的步骤：
 * 		1. 在Service内部定义一个IBinder（中间人）用来调用服务中的方法（即使服务被销毁，此中间人仍然存在，仍可调用服务中的方法）；
 * 			建议此中间人实现一个暴露公共方法的接口（即需额外定义一个interface）；
 * 			示例：Intermediator extends Binder implements IIntermediator
 * 		2. 在Service的onBind()中返回一个IBinder对象（中间人）；
 * 		3. 在Activity中，采用绑定的方式开启服务；
 * 		4. 在Activity中，当服务被连接的时候（服务被成功绑定）获取IBinder对象（中间人）；
 * 			示例：intermediator = (IIntermediator) service;
 * 		5. 在Activity中，通过IBinder中间人调用服务里面的方法。
 * 
 * @author Eugene
 * @data 2015-1-1
 */
public class BeginServiceAty extends Activity{
//	private static final String TAG = "BeginServiceActivity";
	
	private MyServiceConnection conn;
	private IIntermediator intermediator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btn1 = (Button) findViewById(R.id.btn1);
		btn1.setVisibility(View.VISIBLE);
		btn1.setText("ServiceLifeCycle(onStart)");
		btn1.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				startService(new Intent(getApplicationContext(), MainService.class));
			}
		});
		
		final Button btn2 = (Button) findViewById(R.id.btn2);
		final Button btn3 = (Button) findViewById(R.id.btn3);
		final Button btn4 = (Button) findViewById(R.id.btn4);
		btn2.setVisibility(View.VISIBLE);
		btn2.setText("ServiceLifeCycle(bind)");
		btn2.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				bind();
				btn3.setEnabled(true);
				btn4.setEnabled(true);
			}
		});
		btn3.setVisibility(View.VISIBLE);
		btn3.setText("ServiceLifeCycle(callMethodInService)");
		btn3.setEnabled(false);
		btn3.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				call();
			}
		});
		btn4.setVisibility(View.VISIBLE);
		btn4.setText("ServiceLifeCycle(unbind)");
		btn4.setEnabled(false);
		btn4.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				unbind();
				btn3.setEnabled(false);
				btn4.setEnabled(false);
			}
		});
	}

    /**绑定服务
     */
	private void bind(){
    	//在Activity中采用绑定的方式开启服务
    	Intent intent = new Intent(getApplicationContext(), MainService.class);
    	conn = new MyServiceConnection();
    	//调用服务中方法的步骤：3. 在Activity中，采用绑定的方式开启服务；
    	bindService(intent, conn, BIND_AUTO_CREATE);
    }
    
    /**解除绑定服务
     */
    private void unbind(){
    	unbindService(conn);
    	//收回中间人对象
    	intermediator = null;
    	Toast.makeText(getApplicationContext(), "unbindService", Toast.LENGTH_SHORT).show();
    }
    
    /**通过中间人调用服务里面的方法
     */
    private void call(){
    	//调用服务中方法的步骤：5. 在Activity中，通过IBinder中间人调用服务里面的方法。
    	intermediator.callMethodInService();
    }
    
    /**MyServiceConnection implements ServiceConnection
     * ServiceConnection: Interface for monitoring the state of an application service.
     * @author Eugene
     * @data 2015-1-1
     */
    private class MyServiceConnection implements ServiceConnection{
    	
    	//调用服务中方法的步骤：4. 在Activity中，当服务被连接的时候（服务被成功绑定）获取IBinder对象（中间人）；
    	//当服务被连接的时候（服务被成功绑定）调用
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Toast.makeText(getApplicationContext(), "onServiceConnected", Toast.LENGTH_SHORT).show();
			intermediator = (IIntermediator) service;
		}
		
		//当服务失去连接的时候（一般进程挂了、服务被异常杀死）调用
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Toast.makeText(getApplicationContext(), "onServiceDisconnected", Toast.LENGTH_SHORT).show();
		}
    }
	
}
