package com.androidstudy.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.androidstudy.R;

/**MyListView extends Activity(View: ex.ListView)
 * Test: 1. BaseAdapter 2. ArrayAdapter 3. SimpleAdapter
 * @author Eugene
 * @date 2014-12-5
 */
public class MyListView extends Activity{
//	private static final String TAG = "MyListView";
	
	private ListView mListView = null;
	private static List<Person> data = null;
	private ListAdapter mAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_listview);
		
		//TODO 获取测试数据
		data = new ArrayList<Person>();
		data.add(new Person(1, "eugene", 18));
		data.add(new Person(2, "yang", 19));
		data.add(new Person(3, "ernest", 20));
		data.add(new Person(4, "mie", 21));
		data.add(new Person(5, "leo", 22));
		
		//1. BaseAdapter
//		mAdapter = new MyAdapter();
		//2. ArrayAdapter
//		String[] textArray = {"F1","F2","F3","F4","F5"};
//		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, textArray);
		//3. SimpleAdapter
		// TODO Test data
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "N1");
		map.put("icon", R.drawable.ic_launcher);
		data.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "N2");
		map.put("icon", R.drawable.ic_launcher);
		data.add(map);
		//SimpleAdapter args
		int resource = R.layout.listview_item;//listview的子条目的布局的id
		String[] from = new String[]{"name", "icon"};//数据中的map集合里的key
		int[] to = new int[]{R.id.tv_listview_item_name, R.id.iv_icon};//resource中的id
		mAdapter = new SimpleAdapter(this, data, resource, from, to);
		
		mListView = (ListView)findViewById(R.id.listview);
		//把View层对象ListView和控制器Adapter关联起来
        mListView.setAdapter(mAdapter);
	}
	
	/**获取数据列表（测试）
	 * @return
	 */
	public static List<Person> GetData(){
		return data;
	}
	
}

/**MyAdapter extends BaseAdapter(Controller：Adpter)
 * TODO 继承BaseAdapter，可以使得ListView中显示按钮（因为SimpleAdapter不能映射按钮控件）
 * 此部分Controller耦合了Model与View，即把Model与View绑定起来
 * 注意：若将BaseAdapter作为Activity内部类，将更方便获取待绑定数据(Model：ex. Person)及视图(View：ex. ListView)
 * @author Eugene
 * @date 2014-12-5
 */
class MyAdapter extends BaseAdapter{
//	private static final String TAG = "BaseAdapter";
	
	/**定义ListView的数据的长度
	 */
	@Override
	public int getCount() {
		return MyListView.GetData().size();
	}
	@Override//TODO
	public Object getItem(int position) {
		return null;
	}
	@Override//TODO
	public long getItemId(int position) {
		return 0;
	}

	/**返回ListView的列表中某一行的View对象
	 * position 当前返回的view的索引位置
	 * convertView 缓存对象
	 * parent 即ListView对象
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if(convertView != null) {		
			//复用
			view = convertView;
		//第一次显示
		} else {	
			//LayoutInflater可以把一个xml布局文件, 转换成一个view对象
			//Activity.getLayoutInflater()，由于无法直接获取Activity，注意此处强制转换
			LayoutInflater inflater = ((Activity) parent.getContext()).getLayoutInflater();
			view = inflater.inflate(R.layout.listview_item, null);
		}
		//方式一：获得指定位置的数据绑定到相关的View
		//TODO 获取数据
		Person person = MyListView.GetData().get(position);
		//给view中的姓名和年龄赋值
		((TextView) view.findViewById(R.id.tv_listview_item_name))
			.setText("Name: " + person.getName());
		((TextView) view.findViewById(R.id.tv_listview_item_age))
			.setText("Age: " + person.getAge());
		//方式二：直接返回View对象，在外部设置View对象的数据
		return view;
	}
	
}

/**Person(For test)
 * @author Eugene
 * @date 2014-12-5
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
