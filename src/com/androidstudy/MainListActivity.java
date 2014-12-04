package com.androidstudy;

import com.androidstudy.data.DataPath;
import com.androidstudy.data.SQLite;
import com.androidstudy.data.XMLProcess;
import com.androidstudy.phone.Dial;
import com.androidstudy.phone.SendSMS;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author Eugene
 * @date 2014-12-3
 */
public class MainListActivity extends ListActivity{
	
	private ListView mListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 
				getResources().getStringArray(R.array.aty_list)));
		mListView = getListView();
		mListView.setTextFilterEnabled(true);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					startActivity(new Intent(getApplicationContext(), Dial.class));
					break;
				case 1:
					startActivity(new Intent(getApplicationContext(), SendSMS.class));
					break;
				case 2:
					startActivity(new Intent(getApplicationContext(), DataPath.class));
					break;
				case 3:
					startActivity(new Intent(getApplicationContext(), XMLProcess.class));
					break;
				case 4:
					startActivity(new Intent(getApplicationContext(), SQLite.class));
					break;
					
					
				default:
					break;
				}
			}
		});
	}

}
