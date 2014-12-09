package com.androidstudy.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.Header;

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
 * Yest: GET, POST
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
	 * 指定android:onClick="doGet"时应添加参数(View v)并声明public，否则无法识别
	 * @param v
	 */
	public void doGet(View v) {
		final String userName = etUserName.getText().toString();
		final String password = etPassword.getText().toString();
		new Thread(
				new Runnable() {
					@Override
					public void run() {
						final String state = LoginByGet(userName, password);
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
	 * 指定android:onClick="doPost"时应添加参数(View v)并声明public，否则无法识别
	 * @param v
	 */
	public void doPost(View v) {
		final String userName = etUserName.getText().toString();
		final String password = etPassword.getText().toString();
		new Thread(new Runnable() {
			@Override
			public void run() {
				final String state = LoginByPost(userName, password);
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
	
	/**使用GET的方式登录
	 * 注意：GET请求的参数应对敏感数据进行编码，URLEncoder.encode();
	 * @param userName
	 * @param password
	 * @return 登录的状态
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
				Log.i(TAG, "Disconnect: " + responseCode);
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
	
	/**使用POST的方式登录
	 * 注意：POST方式必须设置conn.setDoOutput(true),允许输出（默认情况下, 系统不允许向服务器输出内容）
	 * @param userName
	 * @param password
	 * @return 登录的状态
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
				Log.i(TAG, "Disconnect: " + responseCode);
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
