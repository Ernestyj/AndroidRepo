package com.androidstudy.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

public class SharedDataUtil {
	private final static String TAG = "SharedDataUtil";
	
	private final static String SHAREDPREFERENCES_FILE = "shareddata";
	
	private final static String IMEI = "userid";
	
	private static SharedPreferences mSharedPreferences;
	private static SharedPreferences.Editor mEditor;
	
	public static boolean WriteIMEI(Context context){
		initSharedData(context);
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		mEditor.putString(IMEI, telephonyManager.getDeviceId());
		return mEditor.commit();
	}
	
	public static String GetIMEI(Context context){
		initSharedData(context);
		return mSharedPreferences.getString(IMEI, null);
	}
	
	
	private static void initSharedData(Context context){
		mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCES_FILE, Context.MODE_WORLD_WRITEABLE);
		mEditor = mSharedPreferences.edit();
	}

}
