package com.androidstudy.phone;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidstudy.R;

/**Contact extends Activity
 * @author Eugene
 * @date 2014-12-7
 */
public class Contact extends Activity{
//	private static final String TAG = "Contact";
	
	private static final String URI_CONTENT_CONTACTS_RAW = "content://com.android.contacts/raw_contacts";
	private static final String URI_CONTENT_CONTACTS_DATA = "content://com.android.contacts/data";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final TextView textView = (TextView) findViewById(R.id.txView);
		
		Button btnGetAllContactsRaw = (Button) findViewById(R.id.btn1);
		btnGetAllContactsRaw.setVisibility(View.VISIBLE);
		btnGetAllContactsRaw.setText("Get all contacts raw");
		btnGetAllContactsRaw.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				textView.setText(GetAllContactsRaw(getApplicationContext()));
			}
		});
		
		Button btnGetAllContactsData = (Button) findViewById(R.id.btn2);
		btnGetAllContactsData.setVisibility(View.VISIBLE);
		btnGetAllContactsData.setText("Get all contacts data");
		btnGetAllContactsData.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				textView.setText(GetAllContactsData(getApplicationContext()));
			}
		});
		
		Button btnQueryContacts = (Button) findViewById(R.id.btn3);
		btnQueryContacts.setVisibility(View.VISIBLE);
		btnQueryContacts.setText("Query Contacts");
		btnQueryContacts.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				textView.setText(QueryContacts(getApplicationContext()));
			}
		});
		
		Button btnAddContact = (Button) findViewById(R.id.btn4);
		btnAddContact.setVisibility(View.VISIBLE);
		btnAddContact.setText("Add Contact");
		btnAddContact.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				AddContact(getApplicationContext());
				textView.setText("Add a new contact success.");
			}
		});
	}
	
	/**获取联系人表中所有字段content://com.android.contacts/data
	 * @param context
	 * @return 返回结果字符
	 */
	public static String GetAllContactsRaw(Context context) {
		return GetAllContacts(context, URI_CONTENT_CONTACTS_RAW);
	}
	
	/**获取联系人表中所有字段content://com.android.contacts/data
	 * @param context
	 * @return 返回结果字符
	 */
	public static String GetAllContactsData(Context context) {
		return GetAllContacts(context, URI_CONTENT_CONTACTS_DATA);
	}
	
	/**获取联系人表中所有“指定”字段
	 * 注意添加权限：
	 * 		<!-- 读取Contacts的权限 -->
     * 		<uses-permission android:name="android.permission.READ_CONTACTS"/>
	 * @param context
	 * @return 返回结果字符
	 */
	private static String GetAllContacts(Context context, String type) {
		StringBuilder sBuilder = new StringBuilder();
		Uri uri = Uri.parse(type);
		Cursor cursor = context.getContentResolver()
				.query(uri, null, null, null, null);
		if(cursor != null && cursor.getCount() > 0) {
			while(cursor.moveToNext()) {
				int columnCount = cursor.getColumnCount();//列的总数
				for (int i = 0; i < columnCount; i++) {
					String columnName = cursor.getColumnName(i);//取对应i位置的列的名称
					String columnValue = cursor.getString(i);//取出对应i位置的列的值
					sBuilder.append("Row" + cursor.getPosition() + ": " + columnName + " = " + columnValue + "\n");
				}
			}
			cursor.close();
			return sBuilder.toString();
		}
		return null;
	}
	
	/**查询所有联系人
	 * @param context
	 * @return 返回结果字符
	 */
	public static String QueryContacts(Context context){
		StringBuilder sBuilder = new StringBuilder();
		//1.在raw_contacts表中取所有联系人的_id
		Uri uri = Uri.parse(URI_CONTENT_CONTACTS_RAW);
		Uri dataUri = Uri.parse(URI_CONTENT_CONTACTS_DATA);
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[]{"_id"}, null, null, null);
		if(cursor != null && cursor.getCount() > 0) {
			while(cursor.moveToNext()) {
				int _id = cursor.getInt(0);
				//2.在data表中根据上面取到的_id查询对应id的数据.
				String selection = "raw_contact_id = ?";
				String[] selectionArgs = {String.valueOf(_id)};
				Cursor c = resolver.query(dataUri, new String[]{"data1", "mimetype"}, 
						selection, selectionArgs, null);
				if(c != null && c.getCount() > 0) {
					while(c.moveToNext()) {
						String data1 = c.getString(0);//当前取的是data1数据
						String mimetype = c.getString(1);//当前取的是mimetype的值
						if("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
							sBuilder.append("Number: " + data1 + "\n");
						} else if("vnd.android.cursor.item/name".equals(mimetype)) {
							sBuilder.append("Name: " + data1 + "\n");
						} else if("vnd.android.cursor.item/email_v2".equals(mimetype)) {
							sBuilder.append("Email: " + data1 + "\n");
						}
					}
					c.close();
				}
			}
			cursor.close();
			return sBuilder.toString();
		}
		return null;
	}
	
	/**添加联系人
	 * @param context
	 */
	public static void AddContact(Context context){
		Uri uri = Uri.parse(URI_CONTENT_CONTACTS_RAW);
		Uri dataUri = Uri.parse(URI_CONTENT_CONTACTS_DATA);
		ContentResolver resolver = context.getContentResolver();
		// 1. 在raw_contacts表中添加一个记录
		// 取raw_contacts表中contact_id的值
		String sortOrder = "contact_id desc limit 1";
		Cursor cursor = resolver.query(uri, new String[]{"contact_id"}, null, null, sortOrder);
		if(cursor != null && cursor.moveToFirst()) {
			int contact_id = cursor.getInt(0);
			contact_id ++;
			cursor.close();
			
			ContentValues values = new ContentValues();
			values.put("contact_id", contact_id);
			resolver.insert(uri, values);
			// 2. 根据上面添加记录的id, 在data表中添加三条数据
			// 存号码
			values = new ContentValues();
			values.put("mimetype", "vnd.android.cursor.item/phone_v2");
			values.put("data1", "10086");
			values.put("raw_contact_id", contact_id);
			resolver.insert(dataUri, values);
			// 存姓名
			values = new ContentValues();
			values.put("mimetype", "vnd.android.cursor.item/name");
			values.put("data1", "中国移动");
			values.put("raw_contact_id", contact_id);
			resolver.insert(dataUri, values);
			// 存姓名
			values = new ContentValues();
			values.put("mimetype", "vnd.android.cursor.item/email_v2");
			values.put("data1", "10086@kengni.com");
			values.put("raw_contact_id", contact_id);
			resolver.insert(dataUri, values);
		}
	}
	
}
