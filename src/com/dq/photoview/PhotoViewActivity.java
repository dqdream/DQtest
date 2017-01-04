package com.dq.photoview;

import com.dq.decode.R;

import android.app.Activity;
import android.os.Bundle;

public class PhotoViewActivity extends Activity {
	PhotoView photo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_activity);
		photo=(PhotoView) findViewById(R.id.photo);
		photo.setImageResource(R.drawable.hashiq);
	}

}
