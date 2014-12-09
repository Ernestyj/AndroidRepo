package com.androidstudy.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.R;

/**CloudViewer extends Activity
 * Additional example: MyStaticHandler<T extends Activity> extends Handler
 * 注意：涉及网络操作应开启多线程，避免阻塞主线程造成ANR异常：Android not Responding(应用程序无响应)。
 * 注意：只有原始的线程(主线程/UI线程)才能修改view对象，否则造成CalledFromWrongThreadException.
 * @author Eugene
 * @date 2014-12-8
 */
public class CloudViewer extends Activity{
	private static final String TAG = "CloudViewer";
	
	protected static final int SUCCESS_IMG = 0;
	protected static final int SUCCESS_HTML = 1;
	protected static final int ERROR = -1;
	private EditText etUrl;
	private ImageView ivImage;
	private TextView tvHtml;
	
	/**此处为了简便忽略Handler Leak warning
	 * 消除此警告，可以采用2种方法：
	 * 1. 采用MyStaticHandler方式
	 * 2. 把Handler定义成static，然后用post方法把Runnable对象传送到主线程
	 */
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		//接收消息
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.i(TAG, "what = " + msg.what);
			if(msg.what == SUCCESS_IMG) {
				//注意：只有原始的线程(主线程/UI线程)才能修改view对象，否则造成CalledFromWrongThreadException
				tvHtml.setVisibility(View.GONE);
				ivImage.setVisibility(View.VISIBLE);
				ivImage.setImageBitmap((Bitmap) msg.obj);
			} else if(msg.what == SUCCESS_HTML) {
				ivImage.setVisibility(View.GONE);
				tvHtml.setVisibility(View.VISIBLE);
				tvHtml.setText((String) msg.obj);
			}else if(msg.what == ERROR) {
				Toast.makeText(getApplicationContext(), "获取失败", Toast.LENGTH_LONG).show();
			}
		}
	};
	/**MyStaticHandler<T extends Activity> extends Handler
	 * 1. 采用MyStaticHandler方式：
	 * 用弱引用的方式消除Handler Leak warning，
	 * 先定义一个static的inner Class MyStaticHandler然后让它持有Activity的弱引用
	 * 注意：采用泛型方式不方便在handleMessage操作，此处采用泛型便于代码理解
	 * @author Eugene
	 * @date 2014-12-8
	 * @param <T>
	 */
//	static class MyStaticHandler<T extends Activity> extends Handler {
//        WeakReference<T> mActivity;
//        MyStaticHandler(T activity) {
//        	mActivity = new WeakReference<T>(activity);
//        }
//        @Override
//        public void handleMessage(Message msg) {
//            T theActivity = mActivity.get();
//            switch (msg.what) {
//            case 0:
//            	theActivity.getClass();
//                break;
//            }
//        }
//	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_cloudviewer);
		
		etUrl = (EditText) findViewById(R.id.et_url);
		ivImage = (ImageView) findViewById(R.id.iv_icon);
		tvHtml = (TextView) findViewById(R.id.tv_html);
		
		findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String url = etUrl.getText().toString();
				//判断是否是图片
				if(url.contains(".jpg") || url.contains(".jpeg") || url.contains(".bmp") 
						|| url.contains(".gif") || url.contains(".png")){
					getImage(url);
				}else {
					getHTML(url);
				}
				
			}
		});
	}
	
	/**开启新线程获取Image并发送消息
	 * @param url
	 */
	private void getImage(final String url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				//注意：涉及网络操作应开启多线程，避免阻塞主线程造成ANR异常：Android not Responding(应用程序无响应)。
				Bitmap bitmap = GetImageFromNet(url);
				if(bitmap != null) {
					Message msg = new Message();
					msg.what = SUCCESS_IMG;
					msg.obj = bitmap;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = ERROR;
					handler.sendMessage(msg);
				}
			}}).start();
	}
	
	/**根据url连接网络获取图片
	 * 注意添加权限：
	 * 		<!-- 访问网络的权限 -->
     * 		<uses-permission android:name="android.permission.INTERNET"/>
	 * @param url
	 * @return url对应的Bitmap图片
	 */
	public static Bitmap GetImageFromNet(String url) {
		HttpURLConnection conn = null;
		try {
			URL _url = new URL(url);
			conn = (HttpURLConnection) _url.openConnection();
			
			conn.setRequestMethod("GET");
			// 设置连接服务器的超时时间, 如果超过10秒钟, 没有连接成功, 会抛异常
			conn.setConnectTimeout(10000);
			// 设置读取数据时超时时间, 如果超过5秒, 抛异常
			conn.setReadTimeout(5000);		
			// 开始链接
			conn.connect();		
			// 得到服务器的响应码
			int responseCode = conn.getResponseCode(); 
			// 访问成功
			if(responseCode == 200) {
				// 获得服务器返回的流数据
				InputStream is = conn.getInputStream();	
				// 根据 流数据 创建一个bitmap位图对象
				Bitmap bitmap = BitmapFactory.decodeStream(is); 
				return bitmap;
			} else {
				Log.i(TAG, "Failed: responseCode = " + responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				conn.disconnect();		
			}
		}
		return null;
	}
	
	/**开启新线程获取HTML并发送消息
	 * @param url
	 */
	private void getHTML(final String url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String html = GetHtmlFromNet(url);
				if(!TextUtils.isEmpty(html)) {
					Message msg = new Message();
					msg.what = SUCCESS_HTML;
					msg.obj = html;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = ERROR;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}
	
	/**根据url连接网络获取html代码
	 * 注意添加权限：
	 * 		<!-- 访问网络的权限 -->
     * 		<uses-permission android:name="android.permission.INTERNET"/>
	 * @param url
	 * @return  url对应的html代码字符
	 */
	public static String GetHtmlFromNet(String url) {
		try {
			URL mURL = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) mURL.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(5000);
			conn.connect();
			int responseCode = conn.getResponseCode();
			if(responseCode == 200) {
				InputStream is = conn.getInputStream();
				String html = getStringFromInputStream(is);
				return html;
			} else {
				Log.i(TAG, "Failed: responseCode = " + responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**根据流返回字符串
	 * @param is
	 * @return 字符数据
	 * @throws IOException
	 */
	private static String getStringFromInputStream(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while((len = is.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
		}
		is.close();
		// 把流中的数据转换成字符串, 采用的编码是UTF-8
		String html = baos.toString();	
		if(html.contains("gbk") || html.contains("gb2312")
				|| html.contains("GBK") || html.contains("GB2312")) {
			html = new String(baos.toByteArray(), "GBK");
		}
		baos.close();
		return html;
	}

}
