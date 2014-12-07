package com.androidstudy.data;

import java.util.ArrayList;
import java.util.List;

import com.androidstudy.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**MyContentResolver extends Activity
 * 注意：ContentResolver通常于另一工程中创建，测出仅用于联合MyContentProvider的测试
 * 使用ContentProvider前要声明其定义的权限：Ex.
 *  <uses-permission android:name="aa.bb.cc.read"/>
 *  <uses-permission android:name="aa.bb.cc.write"/>
 * @author Eugene
 * @date 2014-12-6
 */
public class MyContentResolver extends Activity{
	private static final String TAG = "MyContentResolver";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final TextView textView = (TextView) findViewById(R.id.txView);
		
		Button btnInsert = (Button) findViewById(R.id.btn1);
		btnInsert.setVisibility(View.VISIBLE);
		btnInsert.setText("Insert");
		btnInsert.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				testInsert();
				Toast.makeText(getApplicationContext(), "插入一条数据", Toast.LENGTH_SHORT).show();
				showAllData(textView);
			}
		});
		Button btnUpdate = (Button) findViewById(R.id.btn2);
		btnUpdate.setVisibility(View.VISIBLE);
		btnUpdate.setText("Update");
		btnUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				testUpdate();
				Toast.makeText(getApplicationContext(), "更新一条数据", Toast.LENGTH_SHORT).show();
				showAllData(textView);
			}
		});
		Button btnDelete = (Button) findViewById(R.id.btn3);
		btnDelete.setVisibility(View.VISIBLE);
		btnDelete.setText("Delete");
		btnDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				testDelete();
				Toast.makeText(getApplicationContext(), "删除一条数据", Toast.LENGTH_SHORT).show();
				showAllData(textView);
			}
		});
		Button btnQueryAll = (Button) findViewById(R.id.btn4);
		btnQueryAll.setVisibility(View.VISIBLE);
		btnQueryAll.setText("QueryAll");
		btnQueryAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showAllData(textView);
			}
		});
		Button btnQuery = (Button) findViewById(R.id.btn5);
		btnQuery.setVisibility(View.VISIBLE);
		btnQuery.setText("Query");
		btnQuery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Person p = testQuerySingleItem();
				textView.setText(p.toString());
			}
		});
	}
	
	private void showAllData(final TextView textView) {
		List<Person> data = testQueryAll();
		StringBuilder s = new StringBuilder();
		for (Person person : data) {
			s.append(person.toString() + "\n");
		}
		textView.setText(s);
	}
	
	/**Test insert
	 * @return 插入的数据ID
	 */
	public long testInsert() {
		Uri uri = Uri.parse("content://com.androidstudy.data.MyContentProvider/person/insert");
		//内容访问对象
		ContentResolver resolver = getApplicationContext().getContentResolver();
		//测试数据
		ContentValues values = new ContentValues();
		values.put("name", "testName");
		values.put("age", 20);
		//插入数据
		uri = resolver.insert(uri, values);
		Log.i(TAG, "Uri: " + uri);
		//获取插入数据的ID
		//Ex. ContentProvider.insert()中执行过ContentUris.withAppendedId(uri, id);
		//得到content://com.androidstudy.data.MyContentProvider/person/insert/id
		long id = ContentUris.parseId(uri);
		Log.i(TAG, "Insert ID: " + id);
		return id;
	}
	
	/**Test update
	 * @return 成功更新的行数
	 */
	public int testUpdate() {
		Uri uri = Uri.parse("content://com.androidstudy.data.MyContentProvider/person/update");
		ContentResolver resolver = getApplicationContext().getContentResolver();
		ContentValues values = new ContentValues();
		values.put("name", "lisi");
		int count = resolver.update(uri, values, "_id = ?", new String[]{"100"});
		Log.i(TAG, "Update count: " + count);
		return count;
	}
	
	/**Test delete
	 * @return 成功删除的行数
	 */
	public int testDelete() {
		Uri uri = Uri.parse("content://com.androidstudy.data.MyContentProvider/person/delete");
		ContentResolver resolver = getApplicationContext().getContentResolver();
		String where = "_id = ?";
		String[] selectionArgs = {"21"};
		int count = resolver.delete(uri, where, selectionArgs);
		Log.i(TAG, "Delete count: " + count);
		return count;
	}
	
	/**Test queryAll
	 * @return 返回数据列表
	 */
	public List<Person> testQueryAll() {
		Uri uri = Uri.parse("content://com.androidstudy.data.MyContentProvider/person/queryAll");
		ContentResolver resolver = getApplicationContext().getContentResolver();
		Cursor cursor = resolver.query(uri, new String[]{"_id", "name", "age"}, null, null, "_id desc");
		List<Person> data = null;
		if(cursor != null && cursor.getCount() > 0) {
			data = new ArrayList<Person>();
			int _id;
			String name;
			int age;
			while(cursor.moveToNext()) {
				_id = cursor.getInt(0);
				name = cursor.getString(1);
				age = cursor.getInt(2);
				data.add(new Person(_id, name, age));
//				Log.i(TAG, "id: " + _id + ", name: " + name + ", age: " + age);
			}
			cursor.close();
			return data;
		}
		return null;
	}
	
	/**Test querySingleItem
	 * @return
	 */
	public Person testQuerySingleItem() {
		Uri uri = Uri.parse("content://com.androidstudy.data.MyContentProvider/person/query/#");
		//在uri的末尾添加一个id
		//Ex. content://com.androidstudy.data.MyContentProvider/person/query/20
		uri = ContentUris.withAppendedId(uri, 20);
		
		ContentResolver resolver = getApplicationContext().getContentResolver();
		Cursor cursor = resolver.query(uri, new String[]{"_id", "name", "age"}, null, null, null);
		if(cursor != null && cursor.moveToFirst()) {
			int _id = cursor.getInt(0);
			String name = cursor.getString(1);
			int age = cursor.getInt(2);
			cursor.close();
			Log.i(TAG, "id: " + _id + ", name: " + name + ", age: " + age);
			return new Person(_id, name, age);
		}
		return null;
	}
}
