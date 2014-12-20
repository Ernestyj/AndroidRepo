package com.androidstudy;

import com.androidstudy.data.DataPath;
import com.androidstudy.data.MyContentResolver;
import com.androidstudy.data.SQLite;
import com.androidstudy.data.XMLProcess;
import com.androidstudy.demo.sinanews.SinaNews;
import com.androidstudy.net.CloudViewer;
import com.androidstudy.net.Download;
import com.androidstudy.net.Login;
import com.androidstudy.phone.Contact;
import com.androidstudy.phone.Dial;
import com.androidstudy.phone.SMS;
import com.androidstudy.ui.MyListView;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**MainListActivity extends ListActivity
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
					startActivity(new Intent(getApplicationContext(), SMS.class));
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
				case 5:
					startActivity(new Intent(getApplicationContext(), MyListView.class));
					break;
				case 6:
					startActivity(new Intent(getApplicationContext(), MyContentResolver.class));
					break;
				case 7:
					startActivity(new Intent(getApplicationContext(), Contact.class));
					break;
				case 8:
					startActivity(new Intent(getApplicationContext(), CloudViewer.class));
					break;
				case 9:
					startActivity(new Intent(getApplicationContext(), Login.class));
					break;
				case 10:
					startActivity(new Intent(getApplicationContext(), SinaNews.class));
					break;
				case 11:
					startActivity(new Intent(getApplicationContext(), Download.class));
					break;
				case 12:
					Intent intent = new Intent();
					intent.setAction("com.androidstudy.find");
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.parse("Asian:Chinese"), "application/person");
					startActivity(intent);
					break;
					
					
				default:
					break;
				}
			}
		});
	}

}
