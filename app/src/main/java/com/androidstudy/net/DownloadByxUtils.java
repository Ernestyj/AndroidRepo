package com.androidstudy.net;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**DownloadByxUtils extends Activity
 * 包示例：xUtils-2.6.14.jar: HttpUtils, HttpHandler, ResponseInfo, RequestCallBack, HttpException
 * xUtils-2.6.14.jar包封装了主次线程的操作，可安全更新UI
 * 注意：使用xUtils-2.6.14.jar包应添加权限：
 * 		<uses-permission android:name="android.permission.INTERNET" />
 * 		<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * @author Eugene
 * @data 2014-12-20
 */
public class DownloadByxUtils extends Activity{
//	private static final String TAG = "DownloadByxUtils";
	
	private EditText et_path;
	private TextView tv_info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_downloadbyxutils);
		
		et_path = (EditText) findViewById(R.id.et_path);
		tv_info = (TextView) findViewById(R.id.tv_info);
	}
	
	/**多线程断点下载
	 * xUtils-2.6.14.jar包封装了主次线程的操作，可安全更新UI
	 * Button: android:onClick="download"
	 * 注意：指定android:onClick时应添加参数(View v)并声明public，否则无法识别；
	 * 注意：使用xUtils-2.6.14.jar包应添加权限：
 	 * 		<uses-permission android:name="android.permission.INTERNET" />
 	 *		<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
	 * @param view
	 */
	@SuppressLint("SdCardPath")
	@SuppressWarnings({ "rawtypes", "unused" })
	public void download(View view){
		String path = et_path.getText().toString().trim();
		if(TextUtils.isEmpty(path)){
			Toast.makeText(this, "Please input download URL.", Toast.LENGTH_SHORT).show();
			return;
		}else{
			HttpUtils http = new HttpUtils();
			HttpHandler handler = http.download(path, //下载地址
					// 保存文件的路径，例如"/sdcard/xxx.zip"或Environment.getExternalStorageDirectory().getPath()
					"/sdcard/example.dat",
				    true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载
				    true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名
				    new RequestCallBack<File>() {
				        @Override
				        public void onStart() {
				        	tv_info.setText("Connecting ...");
				        }
				        @Override
				        public void onLoading(long total, long current, boolean isUploading) {
				        	tv_info.setText(current + "/" + total);
				        }
				        @Override
				        public void onSuccess(ResponseInfo<File> responseInfo) {
				        	tv_info.setText("Downloaded: " + responseInfo.result.getPath());
				        }
				        @Override
				        public void onFailure(HttpException error, String msg) {
				        	tv_info.setText(msg);
				        }
				});
		}
	}
	
}
