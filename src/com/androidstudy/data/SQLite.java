package com.androidstudy.data;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.R;

/**
 * @author Eugene
 * @date 2014-12-4
 */
public class SQLite extends Activity{
//	private static final String TAG = "SQLite";
	private static final String DB_NAME = "person.db";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final PersonDAO dao = new PersonDAO(this, DB_NAME);
		
		final TextView textView = (TextView) findViewById(R.id.txView);
		
		Button btnInsert = (Button) findViewById(R.id.btn1);
		btnInsert.setVisibility(View.VISIBLE);
		btnInsert.setText("Insert");
		btnInsert.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dao.insert(new Person(0, "eugene", 22));
				dao.insert(new Person(0, "yang", 21));
				dao.insert(new Person(0, "miemie", 20));
				dao.insert(new Person(0, "skele", 19));
				Toast.makeText(getApplicationContext(), "插入四条数据", Toast.LENGTH_SHORT).show();
				showAllData(dao, textView);
			}
		});
		Button btnUpdate = (Button) findViewById(R.id.btn2);
		btnUpdate.setVisibility(View.VISIBLE);
		btnUpdate.setText("Update");
		btnUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dao.update("ernest", 1);
				Toast.makeText(getApplicationContext(), "更新一条数据", Toast.LENGTH_SHORT).show();
				showAllData(dao, textView);
			}
		});
		Button btnDelete = (Button) findViewById(R.id.btn3);
		btnDelete.setVisibility(View.VISIBLE);
		btnDelete.setText("Delete");
		btnDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dao.delete(3);
				Toast.makeText(getApplicationContext(), "删除一条数据", Toast.LENGTH_SHORT).show();
				showAllData(dao, textView);
			}
		});
		Button btnQueryAll = (Button) findViewById(R.id.btn4);
		btnQueryAll.setVisibility(View.VISIBLE);
		btnQueryAll.setText("QueryAll");
		btnQueryAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showAllData(dao, textView);
			}
		});
		Button btnQuery = (Button) findViewById(R.id.btn5);
		btnQuery.setVisibility(View.VISIBLE);
		btnQuery.setText("Query");
		btnQuery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Person p = dao.query(4);
				textView.setText(p.toString());
			}
		});
	}
	
	private void showAllData(final PersonDAO dao, final TextView textView) {
		List<Person> data = dao.queryAll();
		StringBuilder s = new StringBuilder();
		for (Person person : data) {
			s.append(person.toString() + "\n");
		}
		textView.setText(s);
	}
}


class PersonDAO{
	private static final String INSERT = "insert into person(name, age) values(?, ?);";
	private static final String DELETE = "delete from person where _id = ?;";
	private static final String UPDATE = "update person set name = ? where _id = ?;";
	private static final String SELECT = "select _id, name, age from person where _id = ?;";
	private static final String SELECT_ALL = "select _id, name, age from person;";
	
	private MySQLiteOpenHelper mSQLiteOpenHelper;
	private SQLiteDatabase db = null;
	
	public PersonDAO(Context context, String dbName){
		mSQLiteOpenHelper = new MySQLiteOpenHelper(context, dbName, null, 1);
	}
	
	/**插入数据
	 * 示例：insert into person(name, age) values(?, ?);
	 * @param person
	 */
	public void insert(Person person){
		//获取可写数据库
		db = mSQLiteOpenHelper.getWritableDatabase();
		//判断数据库是否打开
		if(db.isOpen()){
			//执行插入语句
			db.execSQL(INSERT, new Object[]{person.getName(), person.getAge()});
			//关闭数据库
			db.close();
		}
	}
	
	/**删除数据
	 * 示例：delete from person where _id = ?;
	 * @param id
	 */
	public void delete(int id){
		db = mSQLiteOpenHelper.getWritableDatabase();
		if(db.isOpen()){
			db.execSQL(DELETE, new Integer[]{id});
			db.close();
		}
	}
	
	/**更新数据
	 * 示例：update person set name = ? where _id = ?;
	 * @param name
	 * @param id
	 */
	public void update(String name, int id){
		db = mSQLiteOpenHelper.getWritableDatabase();
		if(db.isOpen()){
			db.execSQL(UPDATE, new Object[]{name, id});
			db.close();
		}
	}
	
	/**查询所有数据
	 * 示例：select * from person;
	 * 推荐示例：select _id, name, age from person;
	 * @return 返回数据列表
	 */
	public List<Person> queryAll(){
		//获取只读数据库
		db = mSQLiteOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			List<Person> data = null;
			//Cursor起始位置为-1
			Cursor cursor = db.rawQuery(SELECT_ALL, null);
			if (cursor != null && cursor.getCount() > 0) {
				data = new ArrayList<Person>();
				int _id = 0, age = 0;
				String name = null;
				//游标移动到返回结果的下一条
				while (cursor.moveToNext()) {
					//获取第0列数据
					_id = cursor.getInt(0);
					//获取第2列数据
					name = cursor.getString(1);
					//获取第3列数据
					age = cursor.getInt(2);
					data.add(new Person(_id, name, age));
				}
			}
			db.close();
			return data;
		}
		return null;
	}
	
	/**查询指定的一条数据
	 * 示例：select * from person where _id = ?;
	 * 推荐示例：select _id, name, age from person where _id = ?;
	 * @param id
	 * @return 返回第一条数据
	 */
	public Person query(int id){
		db = mSQLiteOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(SELECT, new String[]{String.valueOf(id)});
			int _id = 0, age = 0;
			String name = null;
			//游标移动到返回结果的第一条
			if (cursor != null && cursor.moveToFirst()) {
				_id = cursor.getInt(0);
				name = cursor.getString(1);
				age = cursor.getInt(2);
			}
			db.close();
			return new Person(_id, name, age);
		}
		return null;
	}
	
}