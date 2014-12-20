package com.androidstudy.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidstudy.MyApplication;
import com.androidstudy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**Login extends Activity
 * Test: GET, POST, HttpURLConnection, HttpClient
 * 包示例：android-async-http-1.4.4.jar: AsyncHttpClient, AsyncHttpResponseHandler, RequestParams
 * @author Eugene
 * @date 2014-12-9
 */
public class Login extends Activity{
	private static final String TAG = "Login";
	
	//Genymotion访问宿主机的地址是192.168.200.1
	private static final String URL_LOCAL = 
			"http://192.168.200.1:8080/LoginTest/servlet/LoginServlet";

	private EditText etUserName, etPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_login);
		
		etUserName = (EditText) findViewById(R.id.et_username);
		etPassword = (EditText) findViewById(R.id.et_password);
		//android:onClick="doGet"
		//android:onClick="doPost"
	}
	
	/**开启GET线程
	 * 注意：指定android:onClick="doGet"时应添加参数(View v)并声明public，否则无法识别；
	 * @param v
	 */
	public void doGet(View v) {
		final String userName = etUserName.getText().toString();
		final String password = etPassword.getText().toString();
		new Thread(
				new Runnable() {
					@Override
					public void run() {
//						final String state = LoginByGet(userName, password);
						final String state = LoginByGetV2(userName, password);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(getApplication(), "Return: " + state, Toast.LENGTH_SHORT).show();
							}
						});
					}
				}).start();
	}
	
	/**使用GET的方式登录
	 * Lib: android-async-http-1.4.4.jar
	 * android:onClick="doGetV2"
	 * 注意：指定android:onClick="doGet"时应添加参数(View v)并声明public，否则无法识别；
	 * @param v
	 */
	@SuppressWarnings("deprecation")
	public void doGetV2(View v) {
		final String userName = etUserName.getText().toString();
		final String password = etPassword.getText().toString();
	
        AsyncHttpClient client = new AsyncHttpClient();

        String data = "username=" + URLEncoder.encode(userName) + "&password=" + URLEncoder.encode(password);
        
        client.get(URL_LOCAL + "?" + data, new MyResponseHandler());
	}
	
	/**开启POST线程
	 * 注意：指定android:onClick="doPost"时应添加参数(View v)并声明public，否则无法识别；
	 * @param v
	 */
	public void doPost(View v) {
		final String userName = etUserName.getText().toString();
		final String password = etPassword.getText().toString();
		new Thread(new Runnable() {
			@Override
			public void run() {
//				final String state = LoginByPost(userName, password);
				final String state = LoginByPostV2(userName, password);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getApplication(), "Return: " + state, Toast.LENGTH_SHORT).show();
					}
				});
			}
		}).start();
	}
	
	/**使用Post的方式登录
	 * Lib: android-async-http-1.4.4.jar
	 * android:onClick="doPostV2"
	 * 注意：指定android:onClick="doPost"时应添加参数(View v)并声明public，否则无法识别；
	 * @param v
	 */
	public void doPostV2(View v) {
		final String userName = etUserName.getText().toString();
		final String password = etPassword.getText().toString();
		
		AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("username", userName);
        params.put("password", password);
        
        client.post(URL_LOCAL, params, new MyResponseHandler());
	}
	
	/**使用GET的方式登录(HttpURLConnection)
	 * 注意：GET请求的参数应对敏感数据进行编码，URLEncoder.encode();
	 * @param userName
	 * @param password
	 * @return 登录返回的结果字符
	 */
	@SuppressWarnings("deprecation")
	public static String LoginByGet(String userName, String password) {
		HttpURLConnection conn = null;
		try {
			// GET请求的参数，URLEncoder.encode()
			String data = "username=" + URLEncoder.encode(userName) + "&"
					+ "password=" + URLEncoder.encode(password);
			// 注意地址
			URL url = new URL(URL_LOCAL + "?" + data);
			conn = (HttpURLConnection) url.openConnection();
			// get或者post必须得全大写
			conn.setRequestMethod("GET");		
			conn.setConnectTimeout(10000); 
			conn.setReadTimeout(5000);
//			conn.connect();//省略则系统自动连接
			int responseCode = conn.getResponseCode();
			if(responseCode == 200) {
				InputStream is = conn.getInputStream();
				String state = CloudViewer.GetStringFromInputStream(is);
				return state;
			} else {
				Log.i(TAG, "Request fail: " + responseCode);
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
	
	/**使用GET的方式登录(DefaultHttpClient)
	 * 注意：GET请求的参数应对敏感数据进行编码，URLEncoder.encode();
	 * @param userName
	 * @param password
	 * @return 登录返回的结果字符
	 */
	@SuppressWarnings("deprecation")
	public static String LoginByGetV2(String userName, String password) {
		HttpClient client = null;
		try {
			// 定义一个客户端
			client = new DefaultHttpClient();
			// 定义一个get请求方法
			String data = "username=" + URLEncoder.encode(userName) + "&"
					+ "password=" + URLEncoder.encode(password);
			HttpGet get = new HttpGet(URL_LOCAL + "?" + data);
			// response 服务器相应对象, 其中包含了状态信息和服务器返回的数据
			// 开始执行get方法, 请求网络
			HttpResponse response = client.execute(get);	
			// 获得响应码
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200) {
				InputStream is = response.getEntity().getContent();
				String text = CloudViewer.GetStringFromInputStream(is);
				return text;
			} else {
				Log.i(TAG, "Request fail: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(client != null) {
				client.getConnectionManager().shutdown();	// 关闭连接, 和释放资源
			}
		}
		return null;
	}
	
	/**使用POST的方式登录(HttpURLConnection)
	 * 注意：HttpURLConnection POST方式必须设置conn.setDoOutput(true),允许输出（默认情况下, 系统不允许向服务器输出内容）
	 * @param userName
	 * @param password
	 * @return 登录返回的结果字符
	 */
	public static String LoginByPost(String userName, String password) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(URL_LOCAL);
			conn = (HttpURLConnection) url.openConnection();
			// get或者post必须得全大写
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(5000);
			// 必须设置此方法, 允许输出
			conn.setDoOutput(true);	
//			conn.connect();//省略则系统自动连接
			// POST请求的参数
			String data = "username=" + userName + "&password=" + password;
			// 用于向服务器写数据, 默认情况下, 系统不允许向服务器输出内容
			OutputStream out = conn.getOutputStream();	
			out.write(data.getBytes());
			out.flush();
			out.close();
			
			int responseCode = conn.getResponseCode();
			if(responseCode == 200) {
				InputStream is = conn.getInputStream();
				String state = CloudViewer.GetStringFromInputStream(is);
				return state;
			} else {
				Log.i(TAG, "Request fail: " + responseCode);
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
	
	/**使用POST的方式登录(DefaultHttpClient)
	 * @param userName
	 * @param password
	 * @return 登录返回的结果字符
	 */
	public static String LoginByPostV2(String userName, String password) {
		HttpClient client = null;
		try {
			// 定义一个客户端
			client = new DefaultHttpClient();
			// 定义post方法
			HttpPost post = new HttpPost(URL_LOCAL);
			// 定义post请求的参数
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("username", userName));
			parameters.add(new BasicNameValuePair("password", password));
			// 把post请求的参数再包装一层
			// 不写编码名称服务器收数据时乱码. 需要指定字符集为utf-8
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
			// 设置参数.
			post.setEntity(entity);
			// 设置请求头消息
//			post.addHeader("Content-Length", "20");
			// 使用客户端执行post方法
			// 开始执行post请求, 会返回给我们一个HttpResponse对象
			HttpResponse response = client.execute(post);	
			// 使用响应对象, 获得状态码, 处理内容
			int statusCode = response.getStatusLine().getStatusCode();	// 获得状态码
			if(statusCode == 200) {
				// 使用响应对象获得实体, 获得输入流
				InputStream is = response.getEntity().getContent();
				String text = CloudViewer.GetStringFromInputStream(is);
				return text;
			} else {
				Log.i(TAG, "Request fail: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(client != null) {
				client.getConnectionManager().shutdown();	// 关闭连接和释放资源
			}
		}
		return null;
	}
	
}



/**MyResponseHandler extends AsyncHttpResponseHandler
 * Lib: android-async-http-1.4.4.jar
 * Test for: doGetV2, doPostV2
 * @author Eugene
 * @date 2014-12-9
 */
class MyResponseHandler extends AsyncHttpResponseHandler {
	@Override
	public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
		Toast.makeText(MyApplication.GetInstance().getApplicationContext(), 
				"Connected! statusCode: " + statusCode + ", \n responseBody: " + new String(responseBody), 
				Toast.LENGTH_LONG).show();
	}
	@Override
	public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
		Toast.makeText(MyApplication.GetInstance().getApplicationContext(), 
				"Disconnect! statusCode: " + statusCode, Toast.LENGTH_LONG).show();
	}
}
