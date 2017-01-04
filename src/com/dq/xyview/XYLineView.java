package com.dq.xyview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
/**
 * 2016.12.1
 * 
 * XY 折线图
 * 
 * {@link #refreshXY(List)} 刷新UI
 * 
 * @author DQ
 *
 */
public class XYLineView extends View{
	
	private String Xdatas[];
	private String Ydatas[];
	private List<XYPoint> points; // 坐标点，数量==Xdatas.length，X，Y数据从0开始
	Paint XYdatas_paint;
	Paint XY_Hint_paint;
	Paint XY_Points_paint;//文字
	Paint XY_Lines;// 折线
	Paint XY_Paths;//背景
	Paint circle_bj;
	private float X_distance;//X轴 相邻两数据间隔  单位<=1天 为小时 >1天为 天
	private float maxYdata;
	private float mradius;
	public XYLineView(Context context) {
		super(context);
		init();
	}
	
	public XYLineView(Context context, AttributeSet attrs) {  
		super(context, attrs);
		init();
	}
	
	private void init() {
		Xdatas=new String[]{"11.1","11.2","11.3","11.4","11.5","11.6","11.7"};
		Ydatas=new String[]{"13.9","7.0","4.4"};
		maxYdata=35;
		XYdatas_paint=new Paint();
		XYdatas_paint.setColor(Color.BLACK);
		XYdatas_paint.setTextSize(20);
		XYdatas_paint.setStrokeWidth(3);
		XY_Hint_paint=new Paint();
		XY_Hint_paint.setColor(Color.parseColor("#2f000000"));
		XY_Hint_paint.setStrokeWidth(1);
		XY_Points_paint=new Paint();
		XY_Lines=new Paint();
		XY_Lines.setColor(Color.BLUE);
		XY_Lines.setStrokeWidth(2);
		X_distance=24;
		points=new ArrayList<XYPoint>();
		mPaths=new Path();
		XY_Paths=new Paint();
		XY_Paths.setColor(Color.parseColor("#2f298472"));
		circle_bj=new Paint();
		circle_bj.setColor(Color.WHITE);
		mradius=10;
		/**
		 * 测试数据，解除注释可以在布局中查看效果
		 * 
		 */
		points.add(new XYPoint(24*0, 4.4f));
		points.add(new XYPoint(24*1.5f, 7.0f));
		points.add(new XYPoint(24*2, 13.9f));
		points.add(new XYPoint(24*3.2f, 4.1f));
		points.add(new XYPoint(24*4.6f, 35));
		points.add(new XYPoint(24*5, 4.8f));
		points.add(new XYPoint(24*6, 6));
		
	}
	
	float X_zero,Y_zero;
	float X_length,Y_length;
	private Path mPaths;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		 X_zero=getPaddingLeft()+(XYdatas_paint.getTextSize()-XYdatas_paint.getStrokeWidth())*3;
		 Y_zero=getPaddingTop()+getHeight()/(Ydatas.length+2)*(1+Ydatas.length);
		 X_length=getWidth()-getPaddingRight()-X_zero-XYdatas_paint.measureText(Xdatas[Xdatas.length-1], 0, Xdatas[Xdatas.length-1].length()-1)/2;
		 Y_length=Y_zero-getPaddingTop();
		for (int i = 0; i < Ydatas.length; i++) {
			canvas.drawText(Ydatas[i], getPaddingLeft(), getPaddingTop()+getHeight()/(Ydatas.length+2)*(1+i), XYdatas_paint);
			canvas.drawLine(X_zero, getPaddingTop()+getHeight()/(Ydatas.length+2)*(1+i), X_zero+X_length, getPaddingTop()+getHeight()/(Ydatas.length+2)*(1+i), XY_Hint_paint);
		}
		canvas.drawLine(X_zero, getPaddingTop(), X_zero, Y_zero, XY_Hint_paint);
		canvas.drawLine(X_zero,  Y_zero, X_zero+X_length, Y_zero, XY_Hint_paint);
		for (int i = 0; i < Xdatas.length; i++) {
			canvas.drawLine(X_zero+X_length/(Xdatas.length-1)*(i), getPaddingTop(),X_zero+X_length/(Xdatas.length-1)*(i), Y_zero, XY_Hint_paint);
			float text_half=XYdatas_paint.measureText(Xdatas[i], 0, Xdatas[i].length()-1)/2;
			canvas.drawText(Xdatas[i], X_zero+X_length/(Xdatas.length-1)*(i)-text_half, Y_zero+(XYdatas_paint.getTextSize()-XYdatas_paint.getStrokeWidth())*2, XYdatas_paint);
			
		}
		canvas.save();
		canvas.translate(X_zero, Y_zero-Y_length);
		
		
		for (int i = 0; i < points.size(); i++) {
			if (i==0) {
				mPaths.moveTo(0, Y_length-0);
				drawPath(canvas, points.get(i).X, points.get(i).y);
			}
			if (i>0) {
				drawPath(canvas,points.get(i).X,points.get(i).y);
				drawLine(canvas, getX(points.get(i-1).X), getY(points.get(i-1).y), getX(points.get(i).X), getY(points.get(i).y));
			}
			if (i== points.size()-1) {
				drawPath(canvas,points.get(i).X,0);
				mPaths.moveTo(0, Y_length-getY(0));
				canvas.drawPath(mPaths, XY_Paths);
			}
			XY_Points_paint.setColor(getColor(points.get(i).y));
			canvas.drawText(points.get(i).y+"", getX(points.get(i).X)-XYdatas_paint.measureText(Xdatas[i], 0, Xdatas[i].length()-1)/2, Y_length-getY(points.get(i).y)-XY_Points_paint.getTextSize()/2-mradius, XY_Points_paint);
		}
		for (int i = 0; i < points.size(); i++) {
			drawCircle(canvas, getX(points.get(i).X),Y_length-getY(points.get(i).y));
		}
		canvas.restore();
	}
	public List<XYPoint> getXYPoints() {
		return points;
	}
	
	
	
	/**
	 * 刷新UI
	 * 参考测试数据
	 * points=new ArrayList<XYPoint>();
	 * points.add(new XYPoint(0, 6.5f,12));
	   points.add(new XYPoint(1, 7.2f,8.2f));
	   points.add(new XYPoint(2, 3.4f,6.8f));
	   points.add(new XYPoint(3, 6.8f,-1)); -1不在坐标轴显示
	 * @param points
	 */
	public void refreshXY(List<XYPoint> points) {
		mPaths=new Path();
		this.points = points;
		invalidate();
	}
	
	private float getX(float x){
		float disX=X_length/(Xdatas.length-1);
		return disX/X_distance*x;
	}
	
	private float getY(float y){
		float disY=Y_length/(Ydatas.length+1);
		if (y>Float.parseFloat(Ydatas[0])) {
			float dis_y=maxYdata-Float.parseFloat(Ydatas[0]);
			float dis_datay=y-Float.parseFloat(Ydatas[0]);
			
			return disY*3+disY/dis_y*dis_datay;
		}else if (y>Float.parseFloat(Ydatas[1])) {
			float dis_y=Float.parseFloat(Ydatas[0])-Float.parseFloat(Ydatas[1]);
			float dis_datay=y-Float.parseFloat(Ydatas[1]);
			
			return disY*2+disY/dis_y*dis_datay;
		}else if (y>Float.parseFloat(Ydatas[2])) {
			float dis_y=Float.parseFloat(Ydatas[1])-Float.parseFloat(Ydatas[2]);
			float dis_datay=y-Float.parseFloat(Ydatas[2]);
			
			return disY*1+disY/dis_y*dis_datay;
		}else {
			float dis_y=Float.parseFloat(Ydatas[2])-0;
			float dis_datay=y-0;
			
			return disY/dis_y*dis_datay;
		}
	}
	private void drawPath(Canvas canvas,float X, float Y){
		mPaths.lineTo(getX(X), Y_length-getY(Y));
	}
	
	private void drawCircle(Canvas canvas,float X, float Y){
		canvas.drawCircle(X, Y, mradius, XY_Lines);
		canvas.drawCircle(X, Y, mradius-XY_Lines.getStrokeWidth(), circle_bj);
		canvas.drawCircle(X, Y, XY_Lines.getStrokeWidth()*2, XY_Lines);
	}
	/**
	 * 
	 * @param canvas
	 * @param startX 
	 * @param startY 
	 * @param width
	 */
	private void drawLine(Canvas canvas,float startX, float startY,float stopX, float stopY){
		canvas.drawLine(startX,Y_length-startY, stopX,Y_length-stopY, XY_Lines);
	}
	
	private int getColor(float y){
		if (y>Float.parseFloat(Ydatas[0])) {
			return Color.RED;
		}else if (y>Float.parseFloat(Ydatas[1])) {
			return Color.MAGENTA;
		}else if (y>Float.parseFloat(Ydatas[2])) {
			return Color.BLUE;
		}else {
			return Color.RED;
		}
	}
	
	/**
	 * X 数据x值 
	 * Y 数据y值
	 * @author DQ
	 *
	 */
	public class XYPoint{
		float X;
		float y;
		public XYPoint(float x, float y) {
			super();
			X = x;
			this.y = y;
		}
		public float getX() {
			return X;
		}
		public void setX(float x) {
			X = x;
		}
		public float getY() {
			return y;
		}
		public void setY(float y) {
			this.y = y;
		}
	}
}
