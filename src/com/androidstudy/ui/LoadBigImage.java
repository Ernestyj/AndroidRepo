package com.androidstudy.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidstudy.R;

/**LoadBigImage extends Activity
 * 加载大图片到内存；
 * 步骤：
 * 		1.得到屏幕的宽高信息；
 * 		2.得到图片的宽高；
 * 		3.计算缩放比例；
 * 		4.缩放加载图片到内存。
 * 
 * @author Eugene
 * @data 2015-1-6
 */
public class LoadBigImage extends Activity{
//	private static final String TAG = "LoadBigImage";
	
	ImageView iv;
	Button btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_btnandimage);
		
		iv = (ImageView) findViewById(R.id.iv);
		btn = (Button) findViewById(R.id.btn);
		btn.setText("Load Big Image");
	}
	
	/**Button单击事件处理函数
	 * @param view
	 */
	public void onClickProcess(View v){
		if (v.getId() == R.id.btn) {
			loadBigIamge();
		}
	}
	
	/**加载大图
	 * 步骤：
	 * 		1.得到屏幕的宽高信息；
	 * 		2.得到图片的宽高；
	 * 		3.计算缩放比例；
	 * 		4.缩放加载图片到内存。
	 */
	@SuppressWarnings("deprecation")
	public void loadBigIamge(){
		//下述方法相当消耗内存资源（根据图片的分辨率而定）
//		Bitmap bitmap = BitmapFactory.decodeFile("/mnt/sdcard/photo.jpg");
//		iv.setImageBitmap(bitmap);
		
		//1.得到屏幕的宽高信息；
		WindowManager wm = getWindowManager();
		int screenWidth = wm.getDefaultDisplay().getWidth();//getSize()方法要求API-13
		int screenHeight = wm.getDefaultDisplay().getHeight();
		Toast.makeText(getApplicationContext(), 
				"Screen size: " + screenWidth + " * " + screenHeight, Toast.LENGTH_SHORT).show();
		//2.得到图片的宽高；
		BitmapFactory.Options imageOptions = new Options();//解析位图的附加条件
		//inJustDecodeBounds: allowing the caller to query the bitmap without having to allocate the memory for its pixels
		imageOptions.inJustDecodeBounds = true;//不去解析真实的位图，只是获取这个位图的头文件信息
		//当inJustDecodeBounds为true时返回null，但size信息被解析保存到imageOptions
		BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher, imageOptions);
		int bitmapWidth = imageOptions.outWidth;
		int bitmapHeight = imageOptions.outHeight;
		Toast.makeText(getApplicationContext(), 
				"Image size: " + bitmapWidth + " * " + bitmapHeight, Toast.LENGTH_SHORT).show();
		//3.计算缩放比例；
		int xScale = bitmapWidth / screenWidth;
		int yScale = bitmapHeight / screenHeight;
		int scale = 1;
		if(xScale > yScale && yScale > 1){//按照水平方法缩放
			scale = xScale;
		}else if(yScale > xScale && xScale > 1){//按照垂直方法缩放
			scale = yScale;
		}
		//4.缩放加载图片到内存。
		imageOptions.inSampleSize = scale;
		imageOptions.inJustDecodeBounds = false;//真正的去解析这个位图
		//当inJustDecodeBounds为false时返回真实的位图
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher, imageOptions);
		iv.setImageBitmap(bitmap);
	}

}