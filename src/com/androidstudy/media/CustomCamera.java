package com.androidstudy.media;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.androidstudy.R;

/**CustomCamera extends Activity
 * 自定义照相机（带预览）；
 * 注意：此次测试失败（无法拍照保存）
 * 
 * 注意声明权限：
 * 		<!-- 开启照相机权限 -->
    	<uses-permission android:name="android.permission.CAMERA" />
    	<!-- 声明使用照相机特征 -->
		<uses-feature android:name="android.hardware.camera" />
 * 
 * @author Eugene
 * @data 2015-1-18
 */
public class CustomCamera extends Activity{
	private static final String TAG = "CustomCamera";
	
	private Camera mCamera;
	private CameraPreview mPreview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_customcamera);
		
        mCamera = GetCameraInstance();// Create an instance of Camera
        mPreview = new CameraPreview(this, mCamera); // Create our Preview view and set it as the content of our activity.
        
		FrameLayout previewFrame = (FrameLayout) findViewById(R.id.fl_previewcamera);
        previewFrame.addView(mPreview);
	}
	
	/**单击事件处理
	 * @param view
	 */
	public void click(View view){
		Log.i(TAG, "click()");
		mCamera.autoFocus(new AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				mCamera.takePicture(null, null, new PictureCallback() {
					@Override
					public void onPictureTaken(byte[] data, Camera camera) {
						try {
							Toast.makeText(getApplicationContext(), "Taking...", Toast.LENGTH_SHORT).show();
							File file = new File(Environment.getExternalStorageDirectory(), "customimg.jpg");
							Log.i(TAG, file.getPath());
							FileOutputStream fos = new FileOutputStream(file);
							fos.write(data);
							fos.close();
							Toast.makeText(getApplicationContext(), "Image saved.", Toast.LENGTH_SHORT).show();
							mCamera.startPreview();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		
        
	}
	
	/**获取一个照相机实例
	 * @return
	 */
	public static Camera GetCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	
	@Override
	protected void onDestroy() {
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
		super.onDestroy();
	}

}
