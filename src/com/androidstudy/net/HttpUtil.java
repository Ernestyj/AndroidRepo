package com.androidstudy.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**HttpUtil
 * @author Eugene
 * @data 2015-1-25
 */
public class HttpUtil {
	private static final String TAG = "HttpUtil";
	
    /**   
     * @param url 网页地址   
     * @param params 参数   
     * @return 返回网页内容   
     * @throws Exception
     * Ex.
     * String data;
     * List<NameValuePair> params = new ArrayList<NameValuePair>();   
     * params.add(new BasicNameValuePair("username", username));   
     * params.add(new BasicNameValuePair("password", password));   
     * data = HttpUtils.Post("http://10.0.2.2:8080/Auction/LoginAction.action", params);
     */   
    public static String PostByHttpClient(String url, List<NameValuePair> params) throws Exception {   
    	return PostByHttpClient(url, new UrlEncodedFormEntity(params, HTTP.UTF_8));
    }
    
    /**
     * @param url
     * @param jsonString
     * @return
     * @throws Exception
     */
    public static String PostByHttpClient(String url, String jsonString) throws Exception {   
    	return PostByHttpClient(url, new StringEntity(URLEncoder.encode(jsonString, HTTP.UTF_8)));
//    	return Post(url, new StringEntity(jsonString));
    }
    
    /**
     * @param url
     * @param entity
     * @return
     * @throws Exception
     */
    public static String PostByHttpClient(String url, HttpEntity entity) throws Exception {   
        String response = null;   
        HttpClient httpClient;   
        HttpPost httpPost;   
        try {
        	httpClient = new DefaultHttpClient();
        	httpPost = new HttpPost(url);
//            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            Log.i(TAG, "Data posted.");
            int statusCode = httpResponse.getStatusLine().getStatusCode();   
            if (statusCode == HttpStatus.SC_OK) {	//200请求成功
                response = EntityUtils.toString(httpResponse.getEntity());
                Log.i(TAG, "Get server response success.");
            } else {
                response = "Request error: " + statusCode;
                Log.i(TAG, "Post failed. " + response);
            }   
        } catch (Exception e) {   
            e.getMessage(); e.printStackTrace();
        }
        return response;   
    }
    
    /**   
     * @param url地址   
     * @return 返回网页内容   
     * @throws Exception
     * Ex.
     * String data;   
     * data = HttpUtils.Get("http://10.0.2.2:8080/Auction/LoginAction.action?username="+   
     *		username + "&password=" + password + "");
     */   
    public static String Get(String url) throws Exception {  
    	String response = null;
        HttpClient client = new DefaultHttpClient();   
        HttpGet get = new HttpGet(url);   
        HttpResponse httpResponse = client.execute(get);   
        Log.i(TAG, "Get request sended.");
        int statusCode = httpResponse.getStatusLine().getStatusCode();   
        if (statusCode == HttpStatus.SC_OK) {	//200请求成功
            response = EntityUtils.toString(httpResponse.getEntity());
            Log.i(TAG, "Get server response success.");
        } else {
            response = "Request error: " + statusCode;
            Log.i(TAG, "Get request failed. " + response);
        }
        return response;   
    }
	
    /**
     * @param urlString
     * @param data
     * @return
     */
    public static String PostByHttpURLConnection(String urlString, String data) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
			// get或者post必须得全大写
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			// 必须设置此方法, 允许输出
			conn.setDoOutput(true);	
			Log.i(TAG, "stub1");
//			conn.connect();//省略则系统自动连接
			// 用于向服务器写数据, 默认情况下, 系统不允许向服务器输出内容
			OutputStream out = conn.getOutputStream();
			Log.i(TAG, "stub!!!");
			out.write(data.getBytes());
			Log.i(TAG, "stub2");
			out.flush();
			out.close();
			Log.i(TAG, "stub3");
			int responseCode = conn.getResponseCode();
			Log.i(TAG, "stub4");
			if(responseCode == 200) {
				InputStream is = conn.getInputStream();
				String state = GetStringFromInputStream(is);
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
    
    /**根据流返回字符串
	 * @param is
	 * @return 字符数据
	 * @throws IOException
	 */
	public static String GetStringFromInputStream(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while((len = is.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
		}
		is.close();
		// 把流中的数据转换成字符串, 采用的编码是UTF-8
		String s = baos.toString();	
		// 非UTF-8，则转码为GBK
		if(s.contains("gbk") || s.contains("gb2312")
				|| s.contains("GBK") || s.contains("GB2312")) {
			s = new String(baos.toByteArray(), "GBK");
		}
		baos.close();
		return s;
	}

}
