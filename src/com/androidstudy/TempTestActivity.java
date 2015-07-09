package com.androidstudy;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TempTestActivity extends Activity{
	static final String TAG = "TempTestActivity";
	
	static String s = "N/A";
	static int code = -100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_main);
		
		Button btn1 = (Button) findViewById(R.id.btn1);
		btn1.setVisibility(View.VISIBLE);
		btn1.setText("Click");
	}
	
	/**Button单击事件处理函数
	 * @param view
	 */
	public void onClickProcess(View v){
		if(v.getId() == R.id.btn1){
			RequestForJson(getApplicationContext(), "http://202.120.40.98:60000/CloudCheck/upload.do", 
					Environment.getRootDirectory() + "/CSCVersion.txt");
			Toast.makeText(getApplicationContext(), "content: " + s + " code: " + code, Toast.LENGTH_LONG).show();
		}
	}
	
	public static void RequestForJson(Context context, String url, String fileString){
    	FileBody fileBody = new FileBody(new File(Environment.getRootDirectory() + "/CSCVersion.txt"));
//    	StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);
    	HttpEntity reqEntity = MultipartEntityBuilder.create()
    	                    .addPart("file", fileBody)
//    	                    .addPart("comment", comment)
    	                    .build();

//    	RequestParams params = new RequestParams();
//    	File myFile = new File(fileString);
//    	try {
//    	    params.put("file", myFile);
//    	} catch(FileNotFoundException e) {}
//    	params.put("apikey", "8040393cafd08de0d64464e1c224cc5a7f12271386ffbcf43ad6b83a875f6926");
    	
    	AsyncHttpClient client = new AsyncHttpClient();//创建客户端对象
        client.post(context, url, reqEntity, "multipart/form-data", new JsonHttpResponseHandler() {  
        	@Override
        	public void onSuccess(int statusCode, Header[] arg1, String content) {
                if (statusCode == 200) {
                	//TODO
                	Log.i(TAG, "content" + content);
                }  
        	}
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Log.i(TAG, "onFailure code = " + arg0);
			}
        });  
    }

}
