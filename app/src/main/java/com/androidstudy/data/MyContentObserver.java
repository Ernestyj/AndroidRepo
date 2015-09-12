package com.androidstudy.data;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.androidstudy.MyApplication;
import com.androidstudy.phone.SMS;

/**MyContentObserver extends ContentObserver
 * @author Eugene
 * @date 2014-12-8
 */
public class MyContentObserver extends ContentObserver{
	private static final String TAG = "MyContentObserver";

	public MyContentObserver(Handler handler) {
		super(handler);
	}
	
	/**当被监听的内容改变时回调
	 * 下面的函数可以触发onChange(): 
	 * 		getContentResolver().notifyChange(Uri uri, ContentObserver observer);
	 * 		//其中uri与registerContentObserver()时的uri保持一致；
	 * 		//若register时已指定observer，上面函数的observer可以传入null
	 */
	@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			Log.i(TAG, "SMS has been changed.");
			Uri uri = Uri.parse(SMS.URI_CONTENT_SMS_OUT);
			ContentResolver resolver = MyApplication.GetInstance().getContentResolver();
			//查询发件箱的内容
			Cursor cursor = resolver.query(uri, new String[]{"address", "date", "body"}, null, null, null);
			if(cursor != null && cursor.getCount() > 0) {
				String address;
				long date;
				String body;
				while(cursor.moveToNext()) {
					address = cursor.getString(0);
					date = cursor.getLong(1);
					body = cursor.getString(2);
					Log.i(TAG, "Number: " + address + ", Date: " + date + ", Content: " + body);
				}
				cursor.close();
			}
		}

}
