package com.dq.xyview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
/**
 * 2016.11.30
 * 
 * XY 血糖柱形图
 * 
 * <com.dq.dqdemo.XYHistogramView
        android:padding="10dp" 
        android:layout_width="match_parent"
        android:layout_margin="10dp"
        android:layout_height="200dp" />
 * 
 * {@link #refreshXY(List)} 刷新UI
 * 
 * @author DQ
 *
 */
public class XYHistogramView extends View{
	
	private String Xdatas[];
	private String Ydatas[];
	private List<XYPoint> points; // 坐标点，数量==Xdatas.length
	Paint XYdatas_paint;
	Paint XY_Hint_paint;
	Paint XY_Points_paint;
	private float distance;
	private float maxYdata;
	public XYHistogramView(Context context) {
		super(context);
		init();
	}
	
	public XYHistogramView(Context context, AttributeSet attrs) {  
		super(context, attrs);
		init();
	}
	
	private void init() {
		Xdatas=new String[]{"早餐前|早餐后","午餐前|午餐后","晚餐前|晚餐后","睡觉"};
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
		XYdatas_paint.setTextSize(15);
		distance=3;
		points=new ArrayList<XYPoint>();
		/**
		 * 测试数据，解除注释可以在布局中查看效果
		 */
		points.add(new XYPoint(0, 6.5f,15));
		points.add(new XYPoint(1, 7.2f,8.2f));
		points.add(new XYPoint(2, 3.4f,6.8f));
		points.add(new XYPoint(3, 6.8f,-1));
	}
	
	float X_zero,Y_zero;
	float X_length,Y_length;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		 X_zero=getPaddingLeft()+(XYdatas_paint.getTextSize()-XYdatas_paint.getStrokeWidth())*3;
		 Y_zero=getPaddingTop()+getHeight()/(Ydatas.length+2)*(1+Ydatas.length);
		 X_length=getWidth()-getPaddingRight()-X_zero;
		 Y_length=Y_zero-getPaddingTop();
		for (int i = 0; i < Ydatas.length; i++) {
			canvas.drawText(Ydatas[i], getPaddingLeft(), getPaddingTop()+getHeight()/(Ydatas.length+2)*(1+i), XYdatas_paint);
			canvas.drawLine(X_zero, getPaddingTop()+getHeight()/(Ydatas.length+2)*(1+i), X_zero+X_length, getPaddingTop()+getHeight()/(Ydatas.length+2)*(1+i), XY_Hint_paint);
		}
		canvas.drawLine(X_zero, getPaddingTop(), X_zero, Y_zero, XY_Hint_paint);
		canvas.drawLine(X_zero,  Y_zero, getWidth()-getPaddingRight(), Y_zero, XY_Hint_paint);
		for (int i = 0; i < Xdatas.length; i++) {
			canvas.drawText(Xdatas[i], X_zero+X_length/7*2*i, Y_zero+(XYdatas_paint.getTextSize()-XYdatas_paint.getStrokeWidth())*2, XYdatas_paint);
			canvas.drawLine(X_zero+X_length/7*2*(i), getPaddingTop(), X_zero+X_length/7*2*(i), Y_zero, XY_Hint_paint);
			if (i!= Xdatas.length-1) {
				canvas.drawLine(X_zero+X_length/7*2*i+X_length/7, getPaddingTop(), X_zero+X_length/7*2*i+X_length/7, Y_zero, XY_Hint_paint);
			}
			
			canvas.drawLine(X_zero+X_length/7*2*i+X_length/14, getPaddingTop(), X_zero+X_length/7*2*i+X_length/14, Y_zero, XY_Hint_paint);
			
		}
		canvas.save();
		canvas.translate(X_zero, Y_zero-Y_length);
		for (int i = 0; i < points.size(); i++) {
			XY_Points_paint.setColor(getColorBefore(points.get(i).getY_before()));
			drawRect(canvas, X_length/7*2*(i),X_length/14,getYHight(points.get(i).Y_before),points.get(i).Y_before+"");
			if (points.get(i).Y_after>0) {
				XY_Points_paint.setColor(getColorAfpter(points.get(i).getY_after()));
				drawRect(canvas, X_length/7*2*(i)+X_length/14,X_length/14,getYHight(points.get(i).Y_after),points.get(i).Y_after+"");
			}
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
		this.points = points;
		invalidate();
	}
	
	private float getYHight(float y){
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
	
	/**
	 * 
	 * @param canvas
	 * @param startX 
	 * @param startY 矩形底部距离顶部
	 * @param width
	 */
	private void drawRect(Canvas canvas,float startX, float width,float startY,String Ydata){
		canvas.drawRect(startX+distance, Y_length-startY, startX+width-distance,Y_length, XY_Points_paint);
		canvas.drawText(Ydata, startX+distance, Y_length-startY-2*distance, XY_Points_paint);
	}
	
	private int getColorBefore(float y){
		if (y>Float.parseFloat(Ydatas[0])) {
			return Color.RED;
		}else if (y>Float.parseFloat(Ydatas[1])) {
			return Color.YELLOW;
		}else if (y>Float.parseFloat(Ydatas[2])) {
			return Color.BLUE;
		}else {
			return Color.RED;
		}
	}
	
	private int getColorAfpter(float y){
		if (y>Float.parseFloat(Ydatas[0])) {
			return Color.RED;
		}else if (y>Float.parseFloat(Ydatas[1])) {
			return Color.YELLOW;
		}else if (y>Float.parseFloat(Ydatas[2])) {
			return Color.BLUE;
		}else {
			return Color.RED;
		}
	}
	
	/**
	 * X 0 早上   1中午  2 晚上  3睡觉
	 * Y > 0 绘制在坐标轴上 <0 不绘制 
	 * @author DQ
	 *
	 */
	public class XYPoint{
		float X;
		float Y_before;
		float Y_after;
		public float getY_before() {
			return Y_before;
		}
		public void setY_before(float y_before) {
			Y_before = y_before;
		}
		public float getX() {
			return X;
		}
		public void setX(float x) {
			X = x;
		}
		public float getY_after() {
			return Y_after;
		}
		public void setY_after(float y_after) {
			Y_after = y_after;
		}
		public XYPoint(float x,float y_before, float y_after) {
			X=x;
			Y_before = y_before;
			Y_after = y_after;
		}
	}
}
