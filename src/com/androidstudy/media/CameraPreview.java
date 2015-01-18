package com.androidstudy.media;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**CameraPreview extends SurfaceView implements SurfaceHolder.Callback
 * 照相机预览；
 * 
 * 在低版本模拟器上运行记得加上下面的参数（不自己维护双缓冲区，而是等待多媒体播放框架主动的推送数据）：
 * 		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
 * 
 * @author Eugene
 * @data 2015-1-18
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
	private static final String TAG = "CameraPreview";
	
	private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        // Install a SurfaceHolder.Callback so we get notified when the underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
//        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        if (mHolder.getSurface() == null){ // preview surface does not exist
          return;
        }
        try {
            mCamera.stopPreview(); // stop preview before making changes
        } catch (Exception e){
        	// ignore: tried to stop a non-existent preview
        }
        //TODO: set preview size and make any resize, rotate or reformatting changes here

        try { // start preview with new settings
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. Take care of releasing the Camera preview in your activity.
	}

}
