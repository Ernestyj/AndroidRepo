package com.androidstudy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**TemplateAty extends Activity
 * 用于快速复制模板代码；
 * 注意：此Activity未配置到xml
 * @author Eugene
 * @data 2015-1-6
 */
public class TemplateAty extends Activity{
	static final String TAG = "TemplateAty";
	
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
		
	}

}
