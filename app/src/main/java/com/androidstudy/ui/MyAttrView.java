package com.androidstudy.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.androidstudy.R;

/**MyAttrView extends View
 * 自定义视图与自定义属性（有/无命名空间）测试，自定义属性声明于attr.xml文件；
 * 
 * 知识点：
 * 1. AttributeSet：在MyAttrView(Context context, AttributeSet attrs)函数中，
 * 	对layout xml文件解析后的结果封装为 AttributeSet对象，其存储的都是原始数据，仅对数据进行简单加工；
 * 2. TypedArray：是对AttributeSet中的原始数据按照attr.xml中声明的类型
 * 	（这里是R.styleable.MyAttrView中声明的类型）创建出的具体对象；
 * 
 * 自定义属性format常用类型：reference引用、color颜色、boolean布尔值、dimension尺寸值、
 * 		float浮点值、integer整型值、string字符串、enum布尔值；
 * 
 * 使用自定义属性示例：
 * 		在layout文件中声明命名空间格式（http://schemas.android.com/apk/res/ + 应用程序包名）：
 * 			xmlns:androidstudy="http://schemas.android.com/apk/res/com.androidstudy"
 * 		注意testAttrs="@drawable/ic_launcher"为没有命名空间的属性（其值可以设定为任何类型，但其Value输出为字符串）；
 * 		声明自定义控件示例：
 * 			<com.androidstudy.ui.MyAttrView
		        android:layout_width="wrap_content"
		        android:layout_height="wrap_content"
		        android:background="@drawable/ic_launcher"
		        androidstudy:test_id="123"
		        androidstudy:test_msg="@string/app_name"
		        androidstudy:test_bitmap="@drawable/ic_launcher"
		        testAttrs="@drawable/ic_launcher" />
 *
 * @author Eugene
 * @data 2015-1-25
 */
public class MyAttrView extends View{
	private static final String TAG = "MyAttrView";
	
	public MyAttrView(Context context) {
		super(context);
	}

	public MyAttrView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//输出AttributeSet元素
		int count = attrs.getAttributeCount();
		for (int i = 0; i < count; i++) {
			String name = attrs.getAttributeName(i);
			String value = attrs.getAttributeValue(i);
			Log.i(TAG, "name: " + name + ", value: " + value);
		}
		//无命名空间属性测试（无论设置时指定类型为何，其值输出均为字符串类型）
		String testAttrs = attrs.getAttributeValue(null, "testAttrs");
		Log.i(TAG, "testAttrs: " + testAttrs);
		//输出TypedArray元素
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyAttrView);
		int taCount = typedArray.getIndexCount();//获得对象的个数
		for (int i = 0; i < taCount; i++) {
			int itemId = typedArray.getIndex(i);
			Log.i(TAG, "typedArray.getIndex: " + itemId);
			switch (itemId) {
			case R.styleable.MyAttrView_test_id:
				int idInt = typedArray.getInt(itemId, -1);
				Log.i(TAG, "MyAttrView_test_id getInt: " + idInt);
				break;
			case R.styleable.MyAttrView_test_msg:
				String msg = typedArray.getString(itemId);
				Log.i(TAG, "MyAttrView_test_msg getString: " + msg);
				break;
			case R.styleable.MyAttrView_test_bitmap:
				//typedArray.getDrawable(itemId);
				int bitmapId = typedArray.getResourceId(itemId, -1);
				Log.i(TAG, "MyAttrView_test_bitmap getResourceId: " + bitmapId);
				break;
			}
		}
		typedArray.recycle();
	}
	
	@Override//测量尺寸的回调方法 
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(300, 300);
	}
	
	@Override//确定位置的时候调用此方法（自定义view的时候，作用不大，可省略）
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}
	
	@Override//绘制当前view的内容
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	
}
