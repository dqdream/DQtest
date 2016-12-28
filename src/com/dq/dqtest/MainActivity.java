package com.dq.dqtest;

import java.util.ArrayList;
import java.util.List;

import com.dq.dqtest.RefreshLayoutView.OnLoadListener;
import com.dq.dragview.DragActivity;
import com.dq.drawview.DrawActivity;
import com.dq.html.HtmlActivity;
import com.dq.okhttp.OkHttpTestUtil;
import com.dq.retrofit.RetrofitTestUtil;
import com.zed3.sipua.dqtest.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity{
	private ListView content_list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		content_list=(ListView) findViewById(R.id.listview);
		final List<String>  lists=new ArrayList<String>();
		lists.add("bottom_dragview底部拖拽");
		lists.add("okhttptest");
		lists.add("retrofittest");
		lists.add("handview自动手绘");
		lists.add("htmlactivity");
		final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, lists);
		content_list.setAdapter(adapter);
		content_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					startActivity(new Intent(MainActivity.this, DragActivity.class));
					break;
				case 1:
					OkHttpTestUtil.request();
					break;
				case 2:
					RetrofitTestUtil.request();
					break;
				case 3:
					startActivity(new Intent(MainActivity.this, DrawActivity.class));
					break;
				case 4:
					startActivity(new Intent(MainActivity.this, HtmlActivity.class));
					break;
				}
				
			}
		});
		
	}

}
