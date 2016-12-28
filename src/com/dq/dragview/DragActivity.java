package com.dq.dragview;

import java.util.ArrayList;
import java.util.List;

import com.zed3.sipua.dqtest.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DragActivity extends Activity {

	private ListView content_list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		content_list=(ListView) findViewById(R.id.content_list);
		List<String>  lists=new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			lists.add("this is "+ i +" item");
		}
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, lists);
		content_list.setAdapter(adapter);
		Toast.makeText(this, content_list.getCount()+"i", 1000).show();
		
		ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		int memClass = activityManager.getMemoryClass();
		int memClass2 = activityManager.getLargeMemoryClass();
		Log.d("vv", "memClass"+memClass+", memClass2 "+memClass2 );
//		ClassLoader.
	}

	
}
