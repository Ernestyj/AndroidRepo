package com.androidstudy.demo.sinanews;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.R;
import com.loopj.android.image.SmartImageView;

/**新浪新闻客户端
 * @author Eugene
 * @date 2014-12-10
 */
public class SinaNews extends Activity{
	private static final String TAG = "SinaNews";
	
	private static final String URL = "http://192.168.200.1:8080/SinaNewsServer/new.xml";
	private static final int SUCCESS = 0;
    private static final int FAILED = 1;
    
	private ListView lvNews;
	private List<NewsInfo> newsInfoList;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				// 给Listview列表绑定数据
				newsInfoList = (List<NewsInfo>) msg.obj;
				MyAdapter adapter = new MyAdapter();
				lvNews.setAdapter(adapter);
				break;
			case FAILED:
				Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_aty_sinanews);
		
		Toast.makeText(getApplicationContext(), "等待连接服务器...", Toast.LENGTH_LONG).show();
		init();
	}
	
	private void init() {
		lvNews = (ListView) findViewById(R.id.lv_news);
		// 抓取新闻数据
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 获得新闻集合
				List<NewsInfo> newInfoList = getNewsFromInternet();
				Message msg = new Message();
				if(newInfoList != null) {
					msg.what = SUCCESS;
					msg.obj = newInfoList;
				} else {
					msg.what = FAILED;
				}
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	/**获取新闻数据
	 * @return 新闻数据
	 */
	private List<NewsInfo> getNewsFromInternet() {
		HttpClient client = null;
		try {
			client = new DefaultHttpClient();
			HttpGet get = new HttpGet(URL);
			HttpResponse response = client.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			
			if(statusCode == 200) {
				InputStream is = response.getEntity().getContent();
				List<NewsInfo> newInfoList = parseNewsFromInputStream(is);
				return newInfoList;
			} else {
				Log.i(TAG, "Request fail: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(client != null) {
				client.getConnectionManager().shutdown();		// 关闭和释放资源
			}
		}
		return null;
	}
	
	/**从流中解析新闻集合
	 * @param is
	 * @return 新闻数据
	 * @throws Exception
	 */
	private List<NewsInfo> parseNewsFromInputStream(InputStream is) throws Exception {
		XmlPullParser parser = Xml.newPullParser();	// 创建一个pull解析器
		parser.setInput(is, "utf-8");	// 指定解析流, 和编码
		int eventType = parser.getEventType();
		
		List<NewsInfo> newInfoList = null;
		NewsInfo newInfo = null;
		while(eventType != XmlPullParser.END_DOCUMENT) {	// 如果没有到结尾处, 继续循环
			String tagName = parser.getName();	// 节点名称
			switch (eventType) {
			case XmlPullParser.START_TAG: // <news>
				if("news".equals(tagName)) {
					newInfoList = new ArrayList<NewsInfo>();
				} else if("new".equals(tagName)) {
					newInfo = new NewsInfo();
				} else if("title".equals(tagName)) {
					newInfo.setTitle(parser.nextText());
				} else if("detail".equals(tagName)) {
					newInfo.setDetail(parser.nextText());
				} else if("comment".equals(tagName)) {
					newInfo.setComment(Integer.valueOf(parser.nextText()));
				} else if("image".equals(tagName)) {
					newInfo.setImageUrl(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:	// </news>
				if("new".equals(tagName)) {
					newInfoList.add(newInfo);
				}
				break;
			default:
				break;
			}
			eventType = parser.next();		// 取下一个事件类型
		}
		return newInfoList;
	}
	
	class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return newsInfoList.size();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if(convertView == null) {
				LayoutInflater inflater = getLayoutInflater();
				view = inflater.inflate(R.layout.demo_listview_item_sinanews, null);
			} else {
				view = convertView;
			}
			// 重新赋值, 不会产生缓存对象中原有数据保留的现象
			SmartImageView sivIcon = (SmartImageView) view.findViewById(R.id.siv_listview_item_icon);
			TextView tvTitle = (TextView) view.findViewById(R.id.tv_listview_item_title);
			TextView tvDetail = (TextView) view.findViewById(R.id.tv_listview_item_detail);
			TextView tvComment = (TextView) view.findViewById(R.id.tv_listview_item_comment);
			
			NewsInfo newInfo = newsInfoList.get(position);
			
			sivIcon.setImageUrl(newInfo.getImageUrl());		// 设置图片
			tvTitle.setText(newInfo.getTitle());
			tvDetail.setText(newInfo.getDetail());
			tvComment.setText(newInfo.getComment() + "跟帖");
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
}
