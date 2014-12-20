package com.androidstudy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

/**MainActivity extends Activity
 * 生命周期示例：
 * 		完整生命周期  oncreate -> onstart -> onresume -> onpause -> onstop -> ondestory
 * 		可视生命周期  onstart -> onresume -> onpause -> onstop
 * 		前台生命周期  onresume -> onpause  界面用户仍然可见，但是失去焦点
 * 使用场景：
 * 		1.应用程序退出自动保存数据   ondestory oncreate
 * 		2.应用程序最小化暂停的操作  onstop onstart 视频播放器
 * 		3.游戏的暂停和开始前台生命周期
 * 忽略横竖屏切换|键盘切换|屏幕大小对生命周期的影响（尤其是4.0以上版本模拟器，一定加上screenSize）：
 * 		android:configChanges="orientation|keyboardHidden|screenSize"
 * 将屏幕朝向固定为横屏：
 * 		android:screenOrientation="landscape"
 * *********************************************************************************************
 * Activity的启动模式android:launchMode=""：
 * 1. standard 默认标准的启动模式， 每次startActivity都是创建一个新的activity的实例。
              应用场景：适用于绝大大数情况。
 * 2. singleTop 单一顶部，如果要开启的activity在任务栈的顶部已经存在，就不会创建新的实例，而是调用 onNewIntent()方法。
              应用场景： 浏览器书签。 避免栈顶的activity被重复的创建，解决用户体验问题。
 * 3. singleTask 单一任务栈 ， activity只会在任务栈里面存在一个实例。如果要激活的activity在任务栈里面已经存在，
              就不会创建新的activity，而是复用这个已经存在的activity，调用 onNewIntent()方法，并且清空当前activity任务栈上面所有的activity。
              应用场景：浏览器activity， 整个任务栈只有一个实例，节约内存和cpu的目的。
              注意： activity还是运行在当前应用程序的任务栈里面的。不会创建新的任务栈。
 * 4. singleInstance 单态单例模式
              单一实例，整个手机操作系统里面只有一个实例存在。不同的应用去打开这个activity，共享公用的同一个activity。
              他会运行在自己单独、独立的任务栈里面，并且任务栈里面只有他一个实例存在。
              应用场景：呼叫来电界面 InCallScreen。
 * @author Eugene
 * @data 2014-12-20
 */
public class MainActivity extends Activity {
	//被创建的时候调用的方法 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		System.out.println("oncreate");
		startActivity(new Intent(this, MainListActivity.class));
		finish();
	}
	//被销毁的时候调用的方法
	@Override
	protected void onDestroy() {
		System.out.println("ondestory");
		super.onDestroy();
	}
	//当activity界面用户可见的时候调用的方法
	@Override
	protected void onStart() {
		System.out.println("onstart");
		super.onStart();
	}
	@Override
	protected void onRestart() {
		System.out.println("onrestart");
		super.onRestart();
	}
	//当activity界面用户不可见的时候调用的方法
	@Override
	protected void onStop() {
		System.out.println("onstop");
		super.onStop();
	}
	//界面开始获取到焦点对应的方法。 (界面按钮可以被点击，文本框可以输入内容）
	@Override
	protected void onResume() {
		System.out.println("onresume");
		super.onResume();
	}
	//界面失去焦点对应的方法（暂停）（按钮不可被点击，文本框不可输入内容，但是界面用户仍然能看见）
	@Override
	protected void onPause() {
		System.out.println("onpause");
		super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
