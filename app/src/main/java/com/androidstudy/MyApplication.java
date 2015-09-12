package com.androidstudy;

import android.app.Application;

/**MyApplication extends Application
 * @author Eugene
 * @date 2014-12-8
 */
public class MyApplication extends Application{
//	private final static String TAG = "MyApplication";
	
	private static MyApplication mApplicationInstance;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mApplicationInstance = this;
	}
	
	public static MyApplication GetInstance() {
		return mApplicationInstance;
	}

}
