package com.dq.drawview;

import com.dq.decode.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * 手绘
 * @author dq
 *
 */
public class DrawActivity extends Activity implements OnClickListener {
	private HandDrawView drawOutlineView;
	private Bitmap sobelBm;
	private Button image_pickbtn, image_startbtn;
	private CheckBox checkBox;
	private ImageView image_demo;
	boolean first = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hand_draw_view);
		image_pickbtn = (Button) findViewById(R.id.image_pickbtn);
		image_demo = (ImageView) findViewById(R.id.image_demo);
		image_startbtn=(Button) findViewById(R.id.image_startbtn);
		drawOutlineView = (HandDrawView) findViewById(R.id.outline);
		checkBox=(CheckBox) findViewById(R.id.checkBox1);
		image_pickbtn.setOnClickListener(this);
		image_startbtn.setOnClickListener(this);
	}

	private void initZoomBitmp(final String path) {
		new Thread(new Runnable() {
			public void run() {
				// 将Bitmap压缩处理，防止OOM
//				Bitmap bm = ImageZoomUtil.getResourceZoomBitmap(this, R.drawable.test, -1, 309 * 300);
				final Bitmap bm = ImageZoomUtil.getFileZoomBitmap(path, drawOutlineView.getWidth()/2, drawOutlineView.getWidth() * drawOutlineView.getHeight());
				// 返回的是处理过的Bitmap
				if (checkBox.isChecked()) {
					sobelBm = SobelColorMoreUtil.Sobel(bm, 0.02, 0.02, 0.02);
				}else {
					sobelBm = SobelUtils.Sobel(bm, 0.2, 0.06, 0.03);
				}
				Bitmap paintBm = ImageZoomUtil.getResourceZoomBitmap(DrawActivity.this, R.drawable.paint, -1, 64 * 64);
				drawOutlineView.setPaintBm(paintBm);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						image_demo.setImageBitmap(bm);
					}
				});
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_pickbtn:
			Intent intent = new Intent(Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, 1);
			break;
		case R.id.image_startbtn:
			if (sobelBm!=null) {
				if (first) {
					first = false;
					drawOutlineView.beginDraw(getArray1(sobelBm));
				} else
					drawOutlineView.reDraw(getArray1(sobelBm));
			}else {
				Toast.makeText(this, "请选择图片", 1000).show();
			}
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 获取图片路径
		if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
			Uri selectedImage = data.getData();
			String[] filePathColumns = { MediaStore.Images.Media.DATA };
			Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
			c.moveToFirst();
			int columnIndex = c.getColumnIndex(filePathColumns[0]);
			String imagePath = c.getString(columnIndex);
			c.close();
			initZoomBitmp(imagePath);
		}
	}

	// 根据Bitmap信息，获取每个位置的像素点是否需要绘制
	// 使用boolean数组而不是int[][]主要是考虑到内存的消耗
	private boolean[][] getArray(Bitmap bitmap) {
		boolean[][] b = new boolean[bitmap.getWidth()][bitmap.getHeight()];

		for (int i = 0; i < bitmap.getWidth(); i++) {
			for (int j = 0; j < bitmap.getHeight(); j++) {
				if (bitmap.getPixel(i, j) != Color.WHITE)
					b[i][j] = true;
				else
					b[i][j] = false;
			}
		}
		return b;
	}

	private int[][] getArray1(Bitmap bitmap) {
		int[][] b = new int[bitmap.getWidth()][bitmap.getHeight()];

		for (int i = 0; i < bitmap.getWidth(); i++) {
			for (int j = 0; j < bitmap.getHeight(); j++) {
				b[i][j] = bitmap.getPixel(i, j);
			}
		}
		return b;
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// if (first) {
	// first = false;
	// drawOutlineView.beginDraw(getArray1(sobelBm));
	// } else
	// drawOutlineView.reDraw(getArray1(sobelBm));
	// return true;
	// }

}
