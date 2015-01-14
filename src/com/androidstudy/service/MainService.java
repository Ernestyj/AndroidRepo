package com.androidstudy.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import com.androidstudy.R;

/**MainService extends Service
 * 服务生命周期示例；本地服务测试；
 * *********************************************************************************************
 * 一、采用start方式开启服务：
 * 生命周期示例：（onStart()过时了）
 * 		开启服务： onCreate() --> onStartCommand() ---> onDestory();
 * 如果服务已经开启，不会重复的执行onCreate()， 而是会调用onStart()或 onStartCommand();
 * 服务停止的时候 onDestory()；服务只会被停止一次；
 * 
 * 二、采用bind方式开启服务：
 * 生命周期示例：
 * 		onCreate() ---> onBind() ---> onUnbind() --> onDestory();
 * 绑定服务不会调用onstart或onstartcommand方法；
 * 
 * 三、混合调用的服务的生命周期：
 * 服务长期后台运行，又想调用服务的方法：
 * 		1. start方式开启服务（保证服务长期后台运行）；
		2. bind方式绑定服务（保证可以调用服务的方法）；
		3. unbind解除绑定服务；
		4. stopService停止服务。
		
 * 四、两种开启服务方法的区别：
 * 1. start方式开启服务， 一旦服务开启就跟调用者（开启者）没有任何关系了；开启者退出或销毁了，服务还在后台长期的运行；
 * 开启者无法调用服务里面的方法。
 * 2. bind的方式开启服务，当调用者退出或销毁了，服务也会跟着退出或销毁。
 * 开启者可以调用服务里面的方法。
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
public class MainService extends Service{
//	private static final String TAG = "MainService";
	
	//服务成功绑定 ，返回一个IBinder接口（中间人）
	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("onBind");
		Toast.makeText(getApplicationContext(), "MainService.onBind", Toast.LENGTH_SHORT).show();
		//调用服务中方法的步骤：2. 在Service的onBind()中返回一个IBinder对象（中间人）；
		return new Intermediator();
	}
	@Override
	public boolean onUnbind(Intent intent) {
		System.out.println("onUnbind");
		Toast.makeText(getApplicationContext(), "MainService.onUnbind", Toast.LENGTH_SHORT).show();
		return super.onUnbind(intent);
	}
	
	/**服务中的一个测试方法
	 */
	private void methodInService(){
		Toast.makeText(getApplicationContext(), "methodInService()", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onCreate() {
		System.out.println("oncreate");
		Toast.makeText(getApplicationContext(), "MainService.onCreate", Toast.LENGTH_SHORT).show();
		super.onCreate();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("onstartcommand");
		Toast.makeText(getApplicationContext(), "MainService.onStartCommand", Toast.LENGTH_SHORT).show();
		//以上方法应置于super.onStartCommand前
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		System.out.println("ondestory");
		Toast.makeText(getApplicationContext(), "MainService.onDestroy", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}
	
	/**Intermediator extends Binder implements IIntermediator
	 * 定义IBinder中间人；
	 * 为了暴露公共方法，使用了接口实现方式。
	 * 调用服务中方法的步骤：1. 定义一个中间人（IBinder）用来调用服务中的方法（即使服务被销毁，此中间人仍然存在，仍可调用服务中的方法）；
	 * @author Eugene
	 * @data 2015-1-1
	 */
	private class Intermediator extends Binder implements IIntermediator{
		//实现IIntermediator接口的函数，在此调用服务中的方法
		public void callMethodInService(){
			Toast.makeText(getApplicationContext(), "called methodInService()", Toast.LENGTH_SHORT).show();
			methodInService();
		}
	}
	
}
