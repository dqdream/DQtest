package com.dq.xyview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 12.05
 * 
 * 圈形进度View
 * 
 * {@link #refresh(float, float, float)}
 * 
 * @author DQ
 *
 */
public class CircleView extends TextView {
	Paint circle_hint_paint;
	String circle_text;
	int length;
	Paint circle0_paint, circle1_paint, circle2_paint;
	/**
	 * 0 外圈 1 中圈 2 内圈
	 */
	float b0, b1, b2;

	public CircleView(Context context) {
		super(context);
		init();
	}

	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		setGravity(Gravity.CENTER);
		circle_hint_paint = new Paint();
		circle0_paint = new Paint();
		circle1_paint = new Paint();
		circle2_paint = new Paint();
		path = new Path();
		initPaint(circle_hint_paint, Color.GRAY);
		initPaint(circle0_paint, Color.CYAN);
		initPaint(circle1_paint, Color.BLUE);
		initPaint(circle2_paint, Color.RED);
		circle_text = "达标率\n&%";
		length = circle_text.length();
		setTextSize(30);
		refresh(29.5f, 45.5f, 25);
	}

	float mradius0, mradius1, mradius2;
	float cx, cy;
	float text_width;
	Path path;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		text_width = getLayout().getPaint().measureText(circle_text, 0, length) + getPaddingLeft() + getPaddingRight();
		mradius0 = (getWidth() - getPaddingLeft() - getPaddingRight()) / 2;
		mradius2 = text_width / 2;
		mradius1 = (mradius0 + mradius2) / 2;
		cx = getWidth() / 2;
		cy = getHeight() / 2;
		canvas.drawCircle(cx, cy, mradius0, circle_hint_paint);
		canvas.drawCircle(cx, cy, mradius1, circle_hint_paint);
		canvas.drawCircle(cx, cy, mradius2, circle_hint_paint);
		drawAngle(canvas, mradius0, b0, circle0_paint);
		drawAngle(canvas, mradius1, b1, circle1_paint);
		drawAngle(canvas, mradius2, b2, circle2_paint);
	}

	/**
	 * 0 外圈 1 中圈 2 内圈
	 */
	public void refresh(float b0, float b1, float b2) {
		path = new Path();
		this.b0 = getX(b0);
		this.b1 = getX(b1);
		this.b2 = getX(b2);
		circle_text = "达标率\n"+b1+"%";
		setText(circle_text);
		invalidate();
	}

	private void initPaint(Paint paint, int color) {
		paint.setStyle(Style.STROKE);
		paint.setColor(color);
		paint.setStrokeWidth(20);
	}

	private int drawAngle(Canvas canvas, float radius, float bx, Paint paint) {
		float margin = (getWidth() - radius * 2) / 2;
		RectF oval = new RectF(margin, margin, margin + radius * 2, margin + radius * 2);
		int startAngle = 270;
		canvas.drawArc(oval, startAngle - bx / 100 * 360, bx / 100 * 360, false, paint);
		return length;

	}

	/**
	 * 格式化只保留一位小数
	 * 
	 * @param x
	 * @return
	 */
	private float getX(float x) {
		return (float) ((int) (x * 10) / 10.0);
	}
}
