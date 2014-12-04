package com.androidstudy.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Eugene
 * @date 2014-12-4
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper{
//	private static final String TAG = "MySQLiteOpenHelper";
	private static final String CREATE_TABLE = "create table person( " +
			"_id integer primary key autoincrement, name varchar(20), age integer);";

	/**构造函数
	 * @param context
	 * @param name 数据库名
	 * @param factory 游标工厂，通常设置为null
	 * @param version 从1开始
	 */
	public MySQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
		//调用构造函数并未创建数据库文件，在第一次连接打开数据库时创建.db文件，并调用onCreate()
		super(context, name, factory, version);
		
	}

	/**初始化数据库：创建表
	 * 创建person表示例：
	 * create table person(
	 * 		_id integer primary key autoincrement,
	 * 		name varchar(20),
	 * 		age integer
	 * );
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		//TODO 操作数据库，创建表
		db.execSQL(CREATE_TABLE);
	}

	/**
	 * 数据库版本更新时回调此方法
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
