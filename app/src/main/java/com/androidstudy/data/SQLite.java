package com.androidstudy.data;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.R;

/**SQLite extends Activity
 * @author Eugene
 * @date 2014-12-4
 */
public class SQLite extends Activity{
//	private static final String TAG = "SQLite";
	private static final String DB_NAME = "person.db";
	private static final int DB_VERSION = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final PersonDAO dao = new PersonDAO(this, DB_NAME, DB_VERSION);
		
		final TextView textView = (TextView) findViewById(R.id.txView);
		
		Button btnInsert = (Button) findViewById(R.id.btn1);
		btnInsert.setVisibility(View.VISIBLE);
		btnInsert.setText("Insert");
		btnInsert.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dao.insert(new Person(0, "eugene", 22));
				dao.insert(new Person(0, "yang", 21));
				dao.insertV2(new Person(0, "miemie", 20));
				dao.insertV2(new Person(0, "skele", 19));
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
				dao.update("ernest", 1);//dao.updateV2("ernest", 1);
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
				dao.delete(3);//dao.deleteV2(3);
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
				Person p = dao.query(4);//Person p = dao.queryV2(4);
				textView.setText(p.toString());
			}
		});
	}
	
	private void showAllData(final PersonDAO dao, final TextView textView) {
		List<Person> data = dao.queryAll();//List<Person> data = dao.queryAllV2();
		StringBuilder s = new StringBuilder();
		for (Person person : data) {
			s.append(person.toString() + "\n");
		}
		textView.setText(s);
	}
}


/**PersonDAO(For test)
 * @author Eugene
 * @date 2014-12-5
 */
class PersonDAO{
	private static final String INSERT = "insert into person(name, age) values(?, ?);";
	private static final String DELETE = "delete from person where _id = ?;";
	private static final String UPDATE = "update person set name = ? where _id = ?;";
	private static final String SELECT = "select _id, name, age from person where _id = ?;";
	private static final String SELECT_ALL = "select _id, name, age from person;";
	
	private MySQLiteOpenHelper mSQLiteOpenHelper;
	private SQLiteDatabase db = null;
	
	public PersonDAO(Context context, String dbName, int version){
		mSQLiteOpenHelper = new MySQLiteOpenHelper(context, dbName, null,version);
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
	/**插入数据V2版
	 * @param person
	 * @return 新插入的行ID
	 */
	public long insertV2(Person person){
		db = mSQLiteOpenHelper.getWritableDatabase();
		if(db.isOpen()){
			//封装数据
			ContentValues values = new ContentValues();
			values.put(Person.COLUMN_NAME, person.getName());
			values.put(Person.COLUMN_AGE, person.getAge());
			//插入数据，第二个参数为values为null时指定的缺省值
			long rowId = db.insert(Person.TABLE, null, values);
			
			db.close();
			return rowId;
		}
		return -1;
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
	/**删除数据V2版
	 * @param id
	 * @return 返回修改成功的行数
	 */
	public int deleteV2(int id){
		db = mSQLiteOpenHelper.getWritableDatabase();
		if(db.isOpen()){
			//定义查询字符串
			String whereClause = "_id = ?";
			String[] whereArgs = {String.valueOf(id)};
			//删除数据
			int count = db.delete(Person.TABLE, whereClause, whereArgs);
			
			db.close();
			return count;
		}
		return -1;
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
	/**更新数据V2版
	 * @param name
	 * @param id
	 * @return 返回修改成功的行数
	 */
	public int updateV2(String name, int id){
		db = mSQLiteOpenHelper.getWritableDatabase();
		if(db.isOpen()){
			//封装数据
			ContentValues values = new ContentValues();
			values.put(Person.COLUMN_NAME, name);
			//定义查询字符串
			String whereClause = "_id = ?";
			String[] whereArgs = {String.valueOf(id)};
			int count = db.update(Person.TABLE, values, whereClause, whereArgs);
			
			db.close();
			return count;
		}
		return -1;
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
			//取出数据
			if (cursor != null && cursor.getCount() > 0) {
				data = new ArrayList<Person>();
				//游标移动到返回结果的下一条
				while (cursor.moveToNext()) {
					//获取第0列数据
					int _id = cursor.getInt(0);
					//获取第2列数据
					String name = cursor.getString(1);
					//获取第3列数据
					int age = cursor.getInt(2);
					
					data.add(new Person(_id, name, age));
				}
			}
			//关闭cursor防止内存溢出
			cursor.close();
			db.close();
			return data;
		}
		return null;
	}
	/**查询所有数据V2版
	 * @return 返回数据列表
	 */
	public List<Person> queryAllV2(){
		//获取只读数据库
		db = mSQLiteOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			List<Person> data = null;
			Cursor cursor = null;
			//列
			String[] columns = {Person.COLUMN_ID, Person.COLUMN_NAME, Person.COLUMN_AGE};
			//选择条件为null则查询所有
			String selection = null;
			//选择条件对应的参数
			String[] selectionArgs = null;
			//分组语句 Ex. group by name，即传入name即可
			String groupBy = null;
			//过滤语句
			String having = null;
			//排序语句
			String orderBy = null;
			cursor = db.query(Person.TABLE, columns, selection, selectionArgs, groupBy, having, orderBy);
			//取出数据
			if (cursor != null && cursor.getCount() > 0) {
				data = new ArrayList<Person>();
				while (cursor.moveToNext()) {
					int _id = cursor.getInt(0);
					String name = cursor.getString(1);
					int age = cursor.getInt(2);
					
					data.add(new Person(_id, name, age));
				}
			}
			cursor.close();
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
			cursor.close();
			db.close();
			return new Person(_id, name, age);
		}
		return null;
	}
	/**查询指定的一条数据V2版
	 * @param id
	 * @return 返回第一条数据
	 */
	public Person queryV2(int id){
		db = mSQLiteOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = null;
			
			String[] columns = {Person.COLUMN_ID, Person.COLUMN_NAME, Person.COLUMN_AGE};
			String selection = "_id = ?";
			String[] selectionArgs = {String.valueOf(id)};
			
			cursor = db.query(Person.TABLE, columns, selection, selectionArgs, null, null, null);
			
			int _id = 0, age = 0;
			String name = null;
			if (cursor != null && cursor.moveToFirst()) {
				_id = cursor.getInt(0);
				name = cursor.getString(1);
				age = cursor.getInt(2);
			}
			cursor.close();
			db.close();
			return new Person(_id, name, age);
		}
		return null;
	}
	
	/**事务中进行操作（示例）
	 * 1. 开启事务 db.beginTransaction();
	 * 2. 标记事务成功 db.setTransactionSuccessful();相当于设置回滚点，可设置多个回滚点
	 * 3. 停止事务 db.endTransaction();
	 */
	public void doInTransaction(){
		db = mSQLiteOpenHelper.getWritableDatabase();
		if(db.isOpen()){
			try {
				//开启事务
				db.beginTransaction();
				//TODO 此处示例要求数据库添加一个balance列  alter table person add balance integer;
				//1. 从张三账户中扣1000块钱
				db.execSQL("update person set balance = balance - 1000 where name = 'zhangsan';");
				//ATM机, 挂掉了.
				@SuppressWarnings("unused")
				int result = 10 / 0;
				//2. 向李四账户中加1000块钱
				db.execSQL("update person set balance = balance + 1000 where name = 'lisi';");
				//标记事务成功
				db.setTransactionSuccessful();
			} finally {
				// 停止事务
				db.endTransaction();
			}
			db.close();
		}
	}
	
	
}