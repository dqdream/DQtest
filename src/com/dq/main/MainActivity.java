package com.dq.main;

import java.util.ArrayList;
import java.util.List;

import com.dq.decode.R;
import com.dq.dragview.DragActivity;
import com.dq.drawview.DrawActivity;
import com.dq.html.HtmlActivity;
import com.dq.okhttp.OkHttpTestUtil;
import com.dq.photoview.PhotoViewActivity;
import com.dq.retrofit.RetrofitTestUtil;
import com.dq.xyview.XYActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
		lists.add("handview手绘");
		lists.add("htmlactivity");
		lists.add("photoView");
		lists.add("坐标系View");
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
				case 5:
					startActivity(new Intent(MainActivity.this, PhotoViewActivity.class));
					break;
				case 6:
					startActivity(new Intent(MainActivity.this, XYActivity.class));
					break;
				}
				
			}
		});
		
	}

}
