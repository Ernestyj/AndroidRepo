package com.androidstudy.media;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.androidstudy.R;

/**DefaultCamera extends Activity
 * 拍照（系统提供照相机）；
 * @author Eugene
 * @data 2015-1-16
 */
public class DefaultCamera extends Activity{
//	private static final String TAG = "CameraTest";
	
	static final int REQUEST_CODE = 100;
	
	private ImageView iv;
	private File file ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_main);
		
		Button btn = (Button) findViewById(R.id.btn1);
		btn.setVisibility(View.VISIBLE);
		btn.setText("Take a photo");
		
		iv = (ImageView) findViewById(R.id.iv1);
		iv.setVisibility(View.VISIBLE);
	}
	
	/**单击响应处理
	 * @param view
	 */
	public void onClickProcess(View view) {
		if(view.getId() == R.id.btn1){
			Intent intent = new Intent();
			//指定拍照的意图MediaStore.ACTION_IMAGE_CAPTURE
			intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			file = new File(Environment.getExternalStorageDirectory(), "defaultcamera.jpg");
			//指定保存文件的路径
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file)); 
			startActivityForResult(intent, REQUEST_CODE);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CODE){
			iv.setImageURI(Uri.fromFile(file));
		}
	
		super.onActivityResult(requestCode, resultCode, data);
	}

}
