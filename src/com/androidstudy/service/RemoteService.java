package com.androidstudy.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import com.androidstudy.R;

/**RemoteService extends Service
 * （模拟）远程服务示例；
 * *********************************************************************************************
 * 远程服务：调用者和服务在不同的工程代码里面；应用场景示例：系统提供的getSystemService()；
 * 本地服务：调用者和服务在同一个工程代码里面；
 * 
 * 每一个应用程序都是运行在自己独立的进程里面的；
 * 进程操作系统分配内存空间的一个单位；进程的数据都是独立的；独立的内存空间；
 * IPC： inter-process communication 进程间通讯
 * 
 * AIDL：android interface definition language 安卓接口定义语言；
 * AIDL文件都是公有的，没有访问权限修饰符；
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
 * VS. 调用（本地）服务中方法的步骤：
 * 		1. 在Service内部定义一个IBinder（中间人）用来调用服务中的方法（即使服务被销毁，此中间人仍然存在，仍可调用服务中的方法）；
 * 		#	建议此中间人实现一个暴露公共方法的接口（即需额外定义一个interface）；
 * 			示例：Intermediator extends Binder implements IIntermediator
 * 		2. 在Service的onBind()中返回一个IBinder对象（中间人）；
 * 		3. 在Activity中，采用绑定的方式开启服务；
 * 		#4. 在Activity中，当服务被连接的时候（服务被成功绑定）获取IBinder对象（中间人）；
 * 			示例：intermediator = (IIntermediator) service;
 * 		5. 在Activity中，通过IBinder中间人调用服务里面的方法。
 * @author Eugene
 * @data 2015-1-1
 */
public class RemoteService extends Service{
//	private static final String TAG = "RemoteService";

	@Override
	public IBinder onBind(Intent intent) {
		return new RemoteIntermediator();
	}
	
	/**服务中的一个测试方法
	 */
	private void methodInRemoteService(){
		Toast.makeText(getApplicationContext(), "methodInRemoteService()", Toast.LENGTH_LONG).show();
	}
	
	/**RemoteIntermediator extends IRemoteIntermediator.Stub
	 * 定义IBinder中间人；Stub实现了IBinder接口；
	 * @author Eugene
	 * @data 2015-1-1
	 */
	private class RemoteIntermediator extends IRemoteIntermediator.Stub{
		@Override
		public void callMethodInRemoteService() {
			methodInRemoteService();
		}
	}
	
	
}
