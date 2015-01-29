package com.androidstudy.media;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.androidstudy.R;

/**ImageProcess extends Activity
 * 图像处理测试：创建可编辑图片副本；
 * 缩放、旋转、平移、倒影效果；
 * 图片擦除效果（并播放音频）；
 * 图片颜色滤镜；
 * @author Eugene
 * @data 2015-1-7
 */
public class ImageProcess extends Activity{
//	private static final String TAG = "ImageProcess";
	
	Bitmap copyBmp, srcBmp, srcBmp2, eraseBmp;
	ImageView iv1, iv2, iv3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_main);
		
		Button btn1 = (Button) findViewById(R.id.btn1);
		btn1.setVisibility(View.VISIBLE);
		btn1.setText("Copy Image");
		iv1 = (ImageView) findViewById(R.id.iv1);
		iv1.setVisibility(View.VISIBLE);
		iv2 = (ImageView) findViewById(R.id.iv2);
		iv2.setVisibility(View.VISIBLE);
		iv3 = (ImageView) findViewById(R.id.iv3);
		iv3.setVisibility(View.VISIBLE);
		
		//创建原图
		srcBmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		iv1.setImageBitmap(srcBmp);
		
		//擦除图片
		srcBmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.pic);
		iv3.setImageBitmap(srcBmp2);
		eraseBitmap(srcBmp2);
	}
	
	/**Button单击事件处理函数
	 * @param view
	 */
	public void onClickProcess(View v){
		if (v.getId() == R.id.btn1) {
			copyBmp = CopyImage(srcBmp);
			iv2.setImageBitmap(copyBmp);
		}
	}
	
	/**图片擦除并播放音频
	 * 播放内部资源音频：MediaPlayer.create(getApplicationContext(), R.raw.higirl).start();
	 * @param srcBmp
	 */
	@SuppressLint("ClickableViewAccessibility")
	private void eraseBitmap(Bitmap srcBmp){
		eraseBmp = Bitmap.createBitmap(srcBmp.getWidth(), srcBmp.getHeight(), srcBmp.getConfig());
		
		Canvas canvas = new Canvas(eraseBmp);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		canvas.drawBitmap(srcBmp, new Matrix(), paint);
		
		iv3.setImageBitmap(eraseBmp);
		iv3.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 手指按下屏幕
					break;
				case MotionEvent.ACTION_UP:// 手指离开屏幕
					//播放音频
					MediaPlayer.create(getApplicationContext(), R.raw.higirl).start();
					break;
				case MotionEvent.ACTION_CANCEL:// 锁屏时触摸
					break;
				case MotionEvent.ACTION_MOVE:// 手指在屏幕上移动
					//event.getRawX();//获取相对屏幕的坐标
					int x = (int) event.getX();//获取相对于控件的坐标
					int y = (int) event.getY();
					//设置为透明颜色（让擦除块大一些，便于观察）
					for(int i = -4; i < 5; i++){
						for(int j = -4; j < 5; j++){
							//注意x + i, y + j的范围，这里为简化而使用try直接忽略此问题
							try {
								eraseBmp.setPixel(x + i, y + j, Color.TRANSPARENT);
							} catch (Exception e) {
							}
						}
					}
					iv3.setImageBitmap(eraseBmp);
					break;
				}
				return true;//可以重复循环的处理事件
			}
		});
	}

	/**创建可编辑图片副本；
	 * 图形变换示例；
	 * 颜色滤镜示例；
	 * 步骤：
	 * 		0.创建原图副本（可修改）（空白）
	 * 		1.准备一个画板（在上面放上准备好的空白位图）
	 * 		2.准备一个画笔
	 * 		3.画画
	 * @param srcBmp
	 * @return 可编辑图片副本
	 */
	public static Bitmap CopyImage(Bitmap srcBmp) {
		//0.创建原图副本（可修改）（空白）
		Bitmap copyBmp = Bitmap.createBitmap(srcBmp.getWidth(), srcBmp.getHeight(), srcBmp.getConfig());
		//1.准备一个画板（在上面放上准备好的空白位图）
		Canvas canvas = new Canvas(copyBmp);
		//2.准备一个画笔
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		//3.画画（及各种效果设置）
		Matrix matrix = new Matrix();//使用默认Matrix将按原比例绘图
		//缩放
//		matrix.setScale(1.0f, 1.0f);//注意绘图框的大小调整
		//旋转
//		matrix.setRotate(180, srcBmp.getWidth() / 2, srcBmp.getHeight() / 2);
		//平移
//		matrix.postTranslate(srcBmp.getWidth() / 2, 0);//注意绘图框的大小调整
		//倒影效果（x轴翻转）
//		matrix.setScale(1.0f, -1.0f);
//		matrix.postTranslate(0, srcBmp.getHeight());
		//颜色滤镜
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.set(new float[] {
			0.5f, 0, 0, 0, 0,	//R
			0, 0.8f, 0, 0, 0,	//G
			0, 0, 0.6f, 0, 0,	//B
			0, 0, 0, 1, 0		//Alpha
		});
		paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
		
		canvas.drawBitmap(srcBmp, matrix, paint);//第一个参数为绘图样本（原图）
		
		return copyBmp;
	}
	

}