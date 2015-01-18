package com.androidstudy.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**MyContentProvider extends ContentProvider
 * 注意：使用ContentProvider需要声明并定义其读写权限：
 * 		<!-- authorities：用于唯一指定，通常可用包名类名  -->
 *       <provider android:name="com.androidstudy.data.MyContentProvider"
 *           android:authorities="com.com.androidstudy.data.MyContentProvider"
 *           android:readPermission="aa.bb.cc.read"
 *           android:writePermission="aa.bb.cc.write" >
 *       </provider>
 *      <!-- 定义ContentProvider读写权限  -->
 *   	<permission android:name="aa.bb.cc.read" ></permission>
 *   	<permission android:name="aa.bb.cc.write" ></permission>
 * @author Eugene
 * @date 2014-12-6
 */
public class MyContentProvider extends ContentProvider{
//	private static final String TAG = "MyContentProvider";
	private static final String AUTHORITY = "com.androidstudy.data.MyContentProvider";
	//操作person表insert操作的uri匹配码
	private static final int CODE_PERSON_INSERT = 0;
	private static final int CODE_PERSON_DELETE = 1;
	private static final int CODE_PERSON_UPDATE = 2;
	private static final int CODE_PERSON_QUERY_ALL = 3;
	private static final int CODE_PERSON_QUERY_ITEM = 4;
	
	private static final String DB_NAME = "person.db";
	
	private MySQLiteOpenHelper mOpenHelper = null;
	private SQLiteDatabase db = null;
	
	private static UriMatcher uriMatcher;
	//注册需要匹配的Uri分支路径
	static{
		//常量UriMatcher.NO_MATCH表示不匹配任何路径的返回码 
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		//authority：域名
		//path：authority下的路径
		//code：匹配成功返回的匹配码
		//Ex. content://com.androidstudy.data.MyContentProvider/person/insert
		uriMatcher.addURI(AUTHORITY, "person/insert", CODE_PERSON_INSERT);
		//Ex. content://com.androidstudy.data.MyContentProvider/person/delete
		uriMatcher.addURI(AUTHORITY, "person/delete", CODE_PERSON_DELETE);
		//Ex. content://com.androidstudy.data.MyContentProvider/person/update
		uriMatcher.addURI(AUTHORITY, "person/update", CODE_PERSON_UPDATE);
		//Ex. content://com.androidstudy.data.MyContentProvider/person/query
		uriMatcher.addURI(AUTHORITY, "person/queryAll", CODE_PERSON_QUERY_ALL);
		//Ex. com.androidstudy.data.MyContentProvider/person/query/#
		uriMatcher.addURI(AUTHORITY, "person/query/#", CODE_PERSON_QUERY_ITEM);
	}
	
	@Override
	public boolean onCreate() {
		mOpenHelper = new MySQLiteOpenHelper(getContext(), DB_NAME, null, 1);
		return true;
	}
	
	//此函数不常用，通常不实现
	@Override
	public String getType(Uri uri) {
//		switch (uriMatcher.match(uri)) {
//		//返回多条的MIME-type，前缀vnd.android.cursor.dir
//		case CODE_PERSON_QUERY_ALL: 
//			return "vnd.android.cursor.dir" + "/person";
//		//返回单条的MIME-type，前缀vnd.android.cursor.item
//		case CODE_PERSON_QUERY_ITEM: 
//			return "vnd.android.cursor.item" + "/person";
//		default:
//			break;
//		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (uriMatcher.match(uri)) {
		case CODE_PERSON_DELETE:
			db = mOpenHelper.getWritableDatabase();
			if(db.isOpen()) {
				int count = db.delete(Person.TABLE, selection, selectionArgs);
				db.close();
				return count;
			}
			break;
		default:
			throw new IllegalArgumentException("Fail match: " + uri);
		}
		return 0;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (uriMatcher.match(uri)) {
		case CODE_PERSON_INSERT:
			db = mOpenHelper.getWritableDatabase();
			if(db.isOpen()) {
				long id = db.insert(Person.TABLE, null, values);
				db.close();
				//Ex. content://com.androidstudy.data.MyContentProvider/person/insert/id
				return ContentUris.withAppendedId(uri, id);
			}
			break;

		default:
			throw new IllegalArgumentException("Fail match: " + uri);
		}
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		switch (uriMatcher.match(uri)) {
		case CODE_PERSON_UPDATE:
			SQLiteDatabase db = mOpenHelper.getWritableDatabase();
			if(db.isOpen()) {
				int count = db.update(Person.TABLE, values, selection, selectionArgs);
				db.close();
				return count;
			}
			break;
		default:
			throw new IllegalArgumentException("Fail match: " + uri);
		}
		return 0;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		 db = mOpenHelper.getReadableDatabase();
			switch (uriMatcher.match(uri)) {
			case CODE_PERSON_QUERY_ALL:
				if(db.isOpen()) {
					Cursor cursor = db.query(Person.TABLE, projection, selection, 
							selectionArgs, null, null, sortOrder);
					return cursor;
					//db.close(); 返回cursor结果集时，不可以关闭数据库
				}
				break;
			case CODE_PERSON_QUERY_ITEM:
				if(db.isOpen()) {
					//获取ID
					//Ex. com.androidstudy.data.MyContentProvider/person/query/#
					long id = ContentUris.parseId(uri);
					Cursor cursor = db.query("person", projection, "_id = ?", 
							new String[]{String.valueOf(id)}, null, null, sortOrder);
					return cursor;
					//db.close(); 返回cursor结果集时，不可以关闭数据库
				}
				break;
			default:
				throw new IllegalArgumentException("Fail match: " + uri);
			}
			return null;
	}

}
