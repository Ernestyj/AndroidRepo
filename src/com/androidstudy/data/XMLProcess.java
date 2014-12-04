package com.androidstudy.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.R;

/**
 * @author Eugene
 * @date 2014-12-4
 */
public class XMLProcess extends Activity{
	private static final String TAG = "XMLProcess";
	private static String TAG_ROOT = "persons", 
			TAG_ELE = "person", 
			ATTR_ID = "id", 
			TAG_NAME = "name", 
			TAG_AGE = "age";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnWrite = (Button) findViewById(R.id.btn1);
		btnWrite.setVisibility(View.VISIBLE);
		btnWrite.setText("写入XML文件");
		btnWrite.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				WriteXML2Local(getApplication(), getDataList(), "person.xml");
			}
		});
		
		
		Button btnRead = (Button) findViewById(R.id.btn2);
		btnRead.setVisibility(View.VISIBLE);
		btnRead.setText("读取XML文件");
		btnRead.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				List<Person> data = ParseXMLFromLocal(getApplication(), "person.xml");
				Log.i(TAG, "ParseXMLFromLocal()");
				StringBuilder s = new StringBuilder();
				for (Person person : data) {
					s.append(person.toString() + "\n");
				}
				TextView textView = (TextView) findViewById(R.id.txView);
				textView.setText(s.toString());
			}
		});
	}
	
	/**写出XML到/data/data/包名/files/
	 * @param context
	 * @param data 列表数据
	 * @param fileName
	 */
	public static <T> void WriteXML2Local(Context context, List<T> data, String fileName){
		try {
			//获取XML序列化对象
			XmlSerializer serializer = Xml.newSerializer();
			//获取输出流
			File path = new File(context.getFilesDir(), fileName);
			FileOutputStream fos = new FileOutputStream(path);
			//指定XML序列化对象输出流
			serializer.setOutput(fos, "UTF-8");
			//开始XML，即<?xml version="1.0" encoding="utf-8" standalone="yes"?>
			//This method can only be called just after setOutput.
			serializer.startDocument("UTF-8", true);
			
			//TODO XML正文
			// persons
			serializer.startTag(null, TAG_ROOT);// <persons>
			for (T t : data) {
				Person p = (Person) t;
				// person
				serializer.startTag(null, TAG_ELE);// <person>
				serializer.attribute(null, ATTR_ID, String.valueOf(p.getId()));// <person id="">
				// name
				serializer.startTag(null, TAG_NAME);// <name>
				serializer.text(p.getName());
				serializer.endTag(null, TAG_NAME);// </name>
				// age
				serializer.startTag(null, TAG_AGE);// <age>
				serializer.text(String.valueOf(p.getAge()));
				serializer.endTag(null, TAG_AGE);// </age>
				
				serializer.endTag(null, TAG_ELE);// </person>
			}
			serializer.endTag(null, TAG_ROOT);// </persons>
			
			//结束XML
			serializer.endDocument();
			fos.flush();
			fos.close();
			Toast.makeText(context, "写入成功", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "写入失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**从本地XML文件解析得到数据
	 * @param context
	 * @param fileName
	 * @return 数据列表
	 */
	public static <T> List<T> ParseXMLFromLocal(Context context, String fileName){
		try {
			//获取XMLPull解析器
			XmlPullParser parser = Xml.newPullParser();
			//获取输入流
			File path = new File(context.getFilesDir(), fileName);
			FileInputStream fis;
			fis = new FileInputStream(path);
			//指定XML输入流
			parser.setInput(fis, "UTF-8");
			//获取事件类型
			int eventType = parser.getEventType();
			//TODO XML正文
			List<T> data = null;
			Person p = null;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagName = parser.getName();
				
				switch (eventType) {
				case XmlPullParser.START_TAG:
					// <persons>
					if (tagName.equals(TAG_ROOT)) {
						data = new ArrayList<T>();
					// <person>
					}else if (tagName.equals(TAG_ELE)) {
						p = new Person();
						// <person id="">
						p.setId(Integer.parseInt(parser.getAttributeValue(null, ATTR_ID)));
					// <name>
					}else if (tagName.equals(TAG_NAME)) {
						p.setName(parser.nextText());
					// <age>
					}else if (tagName.equals(TAG_AGE)) {
						p.setAge(Integer.parseInt(parser.nextText()));
					}
					break;
				case XmlPullParser.END_TAG:
					if (tagName.equals(TAG_ELE)) {
						data.add((T) p);
					}
					break;
				default:
					break;
				}
				//获取下一个事件类型
				eventType = parser.next();
			}//while
			fis.close();
			Toast.makeText(context, "读取成功", Toast.LENGTH_SHORT).show();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "读取失败", Toast.LENGTH_SHORT).show();
			return null;
		}
	}
	
	//Test
	private List<Person> getDataList(){
		List<Person> data = new ArrayList<Person>();
		for(int i = 0; i < 30; i++){
			data.add(new Person(i, "P" + i, 1 + i));
		}
		return data;
	}
}

/**Person类用于XML解析测试
 * @author Eugene
 * @date 2014-12-4
 */
class Person{
	private int id;
	private String name;
	private int age;
	public Person(int id, String name, int age) {
		this.id = id;
		this.name = name;
		this.age = age;
	}
	public Person() {
	}
	@Override
	public String toString() {
		return "id: " + id + "; name: " + name + "; age: " + age;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}