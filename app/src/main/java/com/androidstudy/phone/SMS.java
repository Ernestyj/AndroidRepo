package com.androidstudy.phone;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.R;
import com.androidstudy.data.MyContentObserver;

/**SMS extends Activity
 * @author Eugene
 * @date 2014-12-3
 */
public class SMS extends Activity{
//	private static final String TAG = "SMS";
	
	public static final String URI_CONTENT_SMS = "content://sms/";
	public static final String URI_CONTENT_SMS_OUT = "content://sms/outbox";
	
	private static final String TAG_SMSES = "smses";
	private static final String TAG_SMS = "sms";
	private static final String ATTR_ID = "id";
	private static final String TAG_ADDRESS = "address";
	private static final String TAG_DATE = "date";
	private static final String TAG_TYPE = "type";
	private static final String TAG_BODY = "body";

	private static final int INTERVAL = 10000;
	
	private static final String TEXT = "Hello";
	private static final String NUM = "13808024296";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final TextView textView = (TextView) findViewById(R.id.txView);
		
		Button btnSendSMS = (Button) findViewById(R.id.btn1);
		btnSendSMS.setVisibility(View.VISIBLE);
		btnSendSMS.setText("SendSMS");
		btnSendSMS.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				SendSMS();
				textView.setText("SendSMS" + ": \"" + TEXT + "\" to " + NUM + " per " + INTERVAL + "ms" 
						+ "Please read logs.");
			}
		});
		
		Button btnBackupSMS = (Button) findViewById(R.id.btn2);
		btnBackupSMS.setVisibility(View.VISIBLE);
		btnBackupSMS.setText("BackupSMS");
		btnBackupSMS.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				BackupSMS(getApplicationContext());
				textView.setText("Backup successfully.");
			}
		});
		
		Button btnAddSMS = (Button) findViewById(R.id.btn3);
		btnAddSMS.setVisibility(View.VISIBLE);
		btnAddSMS.setText("AddSMS");
		btnAddSMS.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				AddSMS(getApplicationContext());
				textView.setText("Add a SMS to local successfully.");
			}
		});
		
		Button btnListenSMS = (Button) findViewById(R.id.btn4);
		btnListenSMS.setVisibility(View.VISIBLE);
		btnListenSMS.setText("ListenSMS");
		btnListenSMS.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				ListenSMS(getApplicationContext());
				textView.setText("Listening SMS.");
			}
		});
	}
	
	/**发送SMS
	 * Additional Test: SystemClock.sleep(INTERVAL);//相当于Thread.sleep()，但无需处理异常
	 * 注意添加权限：
	 * 		<!-- 发送SMS的权限 -->
	 * 		<uses-permission android:name="android.permission.SEND_SMS"/>
	 */
	public static void SendSMS() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				//Send SMS repeatedly
				while(true){
					//相当于Thread.sleep()，但无需处理异常
					SystemClock.sleep(INTERVAL);
					//短信管理器
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(NUM, //收件人号码
							null, //短信中心号码，可忽略
							TEXT, //信息内容
							null, //若发送成功，回调此广播通知我们
							null);//若对方接受成功，回调此广播通知我们
				}
			}
		}).start();
	}
	
	/**10秒钟后向系统短信数据库中写一条短信
	 * 注意添加权限：
	 * 		<!-- 写入SMS的权限 -->
     * 		<uses-permission android:name="android.permission.WRITE_SMS"/>
	 * @param context
	 */
	public static void AddSMS(final Context context){
		new Thread(new Runnable() {
			@Override
			public void run() {
				SystemClock.sleep(10 * 1000);
				Uri uri = Uri.parse(URI_CONTENT_SMS);
				ContentValues values = new ContentValues();
				//date列由触发器自动生成
				values.put("address", "95555");
				values.put("type", "1");
				values.put("body", "您的尾号为8890的账户, 收到100, 000, 000, 000.00元的转账. 活期余额为: 899, 777, 000, 111, 000.00元");
				context.getContentResolver().insert(uri, values);
			}
		}).start();
	}
	
	/**备份SMS到XML本地文件
	 * 注意添加权限：
	 * 		<!-- 读取SMS的权限 -->
     * 		<uses-permission android:name="android.permission.READ_SMS"/>
	 * @param context
	 */
	public static void BackupSMS(Context context){
		//1.查出所有短信
		Uri uri = Uri.parse(URI_CONTENT_SMS);
		ContentResolver resolver = context.getContentResolver();
		String[] projection = {"_id", "address", "date", "type", "body"};
		Cursor cursor = resolver.query(uri, projection , null, null, null);
		List<SMSInfo> data = new ArrayList<SMSInfo>();
		if (cursor != null && cursor.getCount() > 0) {
			SMSInfo sms = null;
			while (cursor.moveToNext()) {
				sms = new SMSInfo();
				sms.setId(cursor.getInt(0));//短信id
				sms.setAddress(cursor.getString(1));//短信的号码
				sms.setDate(cursor.getLong(2));//短信的日期
				sms.setType(cursor.getInt(3));//短信的类型, 接收1还是发送2
				sms.setBody(cursor.getString(4));//短信内容
				data.add(sms);
			}
			cursor.close();
		}
		//2.序列化到本地
		WriteXML2Local(context, data, "sms.xml");
	}
	
	/**监听系统SMS，使用ContentObserver
	 * @param context
	 */
	public static void ListenSMS(Context context){
		ContentResolver resolver = context.getContentResolver();
		ContentObserver observer = new MyContentObserver(new Handler());
		//注册一个内容观察者观察短信数据库
		resolver.registerContentObserver(Uri.parse(URI_CONTENT_SMS), true, observer);
		
	}
	
	/**将SMS写成XML保存到本地/data/data/包名/files/
	 * @param context
	 * @param data
	 * @param fileName
	 */
	private static void WriteXML2Local(Context context, List<SMSInfo> data, String fileName){
		try {
			XmlSerializer serializer = Xml.newSerializer();
			File path = new File(context.getFilesDir(), fileName);
			FileOutputStream fos = new FileOutputStream(path);
			serializer.setOutput(fos, "UTF-8");
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, TAG_SMSES);
			for (SMSInfo sms : data) {
				serializer.startTag(null, TAG_SMS);
				serializer.attribute(null, ATTR_ID, String.valueOf(sms.getId()));
				//addreee号码
				serializer.startTag(null, TAG_ADDRESS);
				serializer.text(sms.getAddress());
				serializer.endTag(null, TAG_ADDRESS);
				//date
				serializer.startTag(null, TAG_DATE);
				serializer.text(String.valueOf(sms.getDate()));
				serializer.endTag(null, TAG_DATE);
				//type
				serializer.startTag(null, TAG_TYPE);
				serializer.text(String.valueOf(sms.getType()));
				serializer.endTag(null, TAG_TYPE);
				//body内容
				serializer.startTag(null, TAG_BODY);
				serializer.text(sms.getBody());
				serializer.endTag(null, TAG_BODY);
				
				serializer.endTag(null, TAG_SMS);
			}
			serializer.endTag(null, TAG_SMSES);
			serializer.endDocument();
			fos.flush();
			fos.close();
			Toast.makeText(context, "备份成功", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "备份失败", Toast.LENGTH_SHORT).show();
		}
	}

}

/**SMSInfo entity
 * @author Eugene
 * @date 2014-12-7
 */
class SMSInfo{
	private int id;
	private String address;
	private long date;
	private int type;
	private String body;
	public SMSInfo(int id, String address, long date, int type, String body) {
		this.id = id;
		this.address = address;
		this.date = date;
		this.type = type;
		this.body = body;
	}
	public SMSInfo(){}
	@Override
	public String toString() {
		return "SmsInfo [id=" + id + ", address=" + address + ", date=" + date
				+ ", type=" + type + ", body=" + body + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
}
