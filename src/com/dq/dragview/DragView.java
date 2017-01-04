package com.dq.dragview;

import com.dq.decode.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 使用布局参照
 * 
 * <com.dq.dqtest.DragView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true" >

        <TextView
            android:id="@+id/drag_title_text"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:background="@android:color/black"
            android:gravity="center"
            android:text="这里向上拖动" />

        <RelativeLayout
            android:id="@+id/content_rel"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_below="@+id/drag_title_text"
            android:background="#33CCCC"
            android:visibility="gone" >
	
            //你自己的布局
        </RelativeLayout>
    </com.dq.dqtest.DragView>
 * 
 * 3.0
 * 
 * setMakeAnimation 设置�?启动�?
 * 
 * setDragListener  设置监听回调
 * 
 * @author dq
 *
 */
public class DragView extends RelativeLayout implements OnTouchListener, OnClickListener {
	private static final String TAG="dqvv";
	/**
	 * 底部
	 */
	private static final int BOTTOM = 0;
	/**
	 * 中间
	 */
	private static final int HALF = 1;
	/**
	 * 顶部
	 */
	private static final int TOP = 2;
	
	private int mCurrentType = HALF;// 当前状�??, 默认值起始状�?

	private TextView drag_title_text;
	private RelativeLayout content_rel;
	private OnDragListener mDragListener;
	private int mTopHeightTop = 0;
	private int mHalfHeightTop = 0;
	private int mBottomHeightTop = 0;
	private boolean isMakeAnimation = false;
	public DragView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mCurrentType = 1;
		setOnTouchListener(this);
		setOnClickListener(this);
		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
				initChildViews();
			}
		});
	}

	private void initChildViews() {
		drag_title_text = (TextView) this.findViewById(R.id.drag_title_text);
		content_rel = (RelativeLayout) this.findViewById(R.id.content_rel);
		mBottomHeightTop = getTop();
		content_rel.setVisibility(View.VISIBLE);
		content_rel.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				content_rel.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				getHeight();
				mTopHeightTop = getTop();
				mHalfHeightTop = mBottomHeightTop / 2;
				switch (mCurrentType) {
				case BOTTOM:
					drag_title_text.setText("向上拖动");
					onBottomLayout();
					break;
				case HALF:
					drag_title_text.setText("向上或向下拖动");
					onHalfLayout();
					break;
				case TOP:
					drag_title_text.setText("向下拖动");
					onTopLayout();
					break;
				}
			}
		});
	}

	private float startY = 0;
	private float difY = 0;
	private boolean isMove = false;
	private boolean isPerformMove = false;

	private void RefreshView(int difY) {
//		layout(0, getTop() + difY, getWidth(), getTop() + difY + getHeight());
		
		LayoutParams layoutParams=(LayoutParams) getLayoutParams();
		layoutParams.topMargin=getTop() + difY;
//		layoutParams.height=mBottomHeightTop+drag_title_text.getHeight();
		setLayoutParams(layoutParams);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = event.getRawY();
			isMove = true;
			if (mDragListener != null) {
				mDragListener.onStartMoveDragView();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float currentY = event.getRawY();
			difY = currentY - startY;
			if (!isMove) {
				break;
			}
			Log.d(TAG, "difY = " + difY + ", getTop = " + getTop() + ", mMaxHeightTop = " + mTopHeightTop);
			if (difY > 0) {
				if (getTop() >= mTopHeightTop && getTop() < mBottomHeightTop) {
					isPerformMove = true;
					RefreshView((int) difY);
					if (mDragListener != null) {
						mDragListener.onMoveDragView();
					}
				}
			} else {
				if (getTop() > mTopHeightTop && getTop() <= mBottomHeightTop) {
					RefreshView((int) difY);
					isPerformMove = true;
					if (mDragListener != null) {
						mDragListener.onMoveDragView();
					}
				}
			}
			startY = currentY;
			break;
		case MotionEvent.ACTION_UP:
			if (isPerformMove) {
				isMove = false;
				switch (mCurrentType) {
				case BOTTOM:
					if (mBottomHeightTop - getTop() >= getChangeHeigth()) {
						if (mHalfHeightTop - getTop() >= getChangeHeigth()) {
							topLayout();
						} else {
							halfLayout();
						}
					} else {
						bottomLayout();
					}
					break;
				case HALF:
					if (getTop() - mHalfHeightTop >= 0) {
						if (getTop() - mHalfHeightTop >= getChangeHeigth()) {
							bottomLayout();
						} else {
							halfLayout();
						}
					} else {
						if (mHalfHeightTop - getTop() >= getChangeHeigth()) {
							topLayout();
						} else {
							halfLayout();
						}
					}
					break;
				case TOP:
					if (getTop() - mTopHeightTop >= getChangeHeigth()) {
						if (getTop() - mHalfHeightTop >= getChangeHeigth()) {
							bottomLayout();
						} else {
							halfLayout();
						}
					} else {
						topLayout();
					}
					break;
				}
				if (mDragListener != null) {
					mDragListener.onStatusDragView(mCurrentType);
				}
			} else {
				performClick();
			}
			isPerformMove = false;
			break;
		}
		return true;
	}

	
	/**
	 * 滑动的变化状态临界�??
	 * @return
	 */
	private int getChangeHeigth() {
		return getHeight()/2/3;
	}

	/**
	 * 拖动到顶�? 可以在这里加动画
	 */
	private void topLayout() {
		mCurrentType = TOP;
		if (isMakeAnimation) {
			startAnimation();
		} else {
			onTop();
		}
	}

	private void onTop() {
		drag_title_text.setText("向下拖拽");
		Toast.makeText(getContext(), "bing go top", 1000).show();
		onTopLayout();
	}
	
	
	
	private void onTopLayout() {
//		layout(0, mTopHeightTop, getWidth(), mTopHeightTop + getHeight());
		LayoutParams layoutParams=(LayoutParams) getLayoutParams();
		layoutParams.topMargin=mTopHeightTop;
		setLayoutParams(layoutParams);
	}

	/**
	 * 拖动到z中间 可以在这里加动画
	 */
	private void halfLayout() {
		mCurrentType = HALF;
		if (isMakeAnimation) {
			startAnimation();
		} else {
			onHalf();
		}
	}

	private void onHalf() {
		drag_title_text.setText("向上或�?�向下拖�?");
		Toast.makeText(getContext(), "bing go half", 1000).show();
		onHalfLayout();
	}

	private void onHalfLayout() {
//		layout(0, mHalfHeightTop, getWidth(), mHalfHeightTop + getHeight());
		LayoutParams layoutParams=(LayoutParams) getLayoutParams();
		layoutParams.topMargin=mHalfHeightTop;
//		layoutParams.height=mHalfHeightTop+drag_title_text.getHeight();
		setLayoutParams(layoutParams);
	}
	
	/**
	 * 收到底部 可以在这里加动画
	 */
	private void bottomLayout() {
		mCurrentType = BOTTOM;
		if (isMakeAnimation) {
			startAnimation();
		} else {
			onBottom();
		}
	}

	private void onBottom() {
		drag_title_text.setText("向上拖拽");
		Toast.makeText(getContext(), "good bye", 1000).show();
		onBottomLayout();
	}

	private void onBottomLayout() {
//		layout(0, mBottomHeightTop, getWidth(), mBottomHeightTop + getHeight());
		LayoutParams layoutParams=(LayoutParams) getLayoutParams();
		layoutParams.topMargin=mBottomHeightTop;
		setLayoutParams(layoutParams);
	}

	/**
	 * �?要动画了改这�?
	 */
	private void startAnimation() {
		Animation animation = null;
		switch (mCurrentType) {
		case BOTTOM:
			animation = new TranslateAnimation(0, 0, 0, mBottomHeightTop - getTop());
			break;
		case HALF:
			animation = new TranslateAnimation(0, 0, 0, mHalfHeightTop - getTop());
			break;
		case TOP:
			animation = new TranslateAnimation(0, 0, 0, mTopHeightTop - getTop());
			break;
		}
		animation.setDuration(200);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				clearAnimation();
				switch (mCurrentType) {
				case BOTTOM:
					onBottom();
					break;
				case HALF:
					onHalf();
					break;
				case TOP:
					onTop();
					break;
				}
			}
		});
		startAnimation(animation);
	}

	@Override
	public void onClick(View v) {
		switch (mCurrentType) {
		case BELOW:
			halfLayout();
			break;
		case HALF:
			bottomLayout();
			break;
		case TOP:
			halfLayout();
			break;
		}
	}

	public boolean isMakeAnimation() {
		return isMakeAnimation;
	}

	public void setMakeAnimation(boolean isMakeAnimation) {
		this.isMakeAnimation = isMakeAnimation;
	}

	public OnDragListener getDragListener() {
		return mDragListener;
	}

	public void setDragListener(OnDragListener mDragListener) {
		this.mDragListener = mDragListener;
	}

	public interface OnDragListener {

		void onStatusDragView(int type);

		void onStartMoveDragView();

		void onMoveDragView();

	}
}
