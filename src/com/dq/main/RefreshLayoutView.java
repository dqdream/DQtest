package com.dq.main;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * @ DQ 
 * 
 * 刷新控件
 *  
 * version 1.0
 *	
 * 2016.11.8
 * 
 * 支持任何extends ViewGroup类型View�? 下拉刷新 上啦加载
 * * setIsAllowDropUp 上啦加载�?关（包括底部View的处理）,在不�?要时关闭，默认开�?
 * 
 * setIsSelectionLastPos 是否�?要滚到到底部，默认开�?
 *
 * setFootView 底部View的设置，允许为空
 * 
 * setFootAnimationView 添加底部刷新的View
 * 
 * setFootAnimation 添加底部刷新的View动画
 * 
 * setFootRefreshing �?�?/停止底部刷新
 */
public class RefreshLayoutView extends SwipeRefreshLayout {
	private static final String TAG = "RefreshLayoutView";
	private final int mTouchSlop;
	private ViewGroup mChildView;// must extends viewgroup
	private OnLoadListener mOnLoadListener;

	private float firstTouchY;
	private float lastTouchY;

	private boolean isLoading = false;

	private boolean isAllowDropUp = true;

	private boolean isSelectionLastPos = true;

	private View mFootView;

	private ViewGroup mFootAnimView;

	private Animation mFootAnimation;

	public RefreshLayoutView(Context context) {
		this(context, null);
	}

	public RefreshLayoutView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	public void setIsSelectionLastPos(boolean isSelectLast) {
		this.isSelectionLastPos = isSelectLast;
	}

	public boolean getIsAllowDropUp() {
		return this.isAllowDropUp;
	}

	/**
	 * 上啦加载的开�?
	 * 
	 * @param isAllow
	 */
	public void setIsAllowDropUp(boolean isAllow) {
		if (!isAllow && mFootView != null) {
			addFootView(mFootView);
		} else if (isAllow && mFootView != null) {
			removeFootView(mFootView);
		}
		this.isAllowDropUp = isAllow;
	}

	/**
	 * 添加底部View
	 * 
	 * @param view
	 *
	 */
	public void setFootView(View view) {
		this.mFootView = view;
	}

	/**
	 * 添加底部动画View
	 * 
	 * @param view
	 */
	public void setFootAnimationView(View view) {
		LinearLayout layout = new LinearLayout(getContext());
		android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 200);
		layout.setGravity(Gravity.CENTER);
		layout.setBackgroundColor(Color.WHITE);
		if (view != null) {
			layout.addView(view,params);
		}
		mFootAnimView = layout;
	}

	public void setFootAnimation(Animation mFootAnimation) {
		this.mFootAnimation = mFootAnimation;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			firstTouchY = event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			lastTouchY = event.getRawY();
			Log.e(TAG, "canLoadMore = " + canLoadMore());
			if (canLoadMore()) {
				loadData();
			}
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(event);
	}

	private boolean canLoadMore() {
		Log.e(TAG, "isAllowDropUp = " + isAllowDropUp + "isBottom = " + isBottom() + " !isLoading  = " + (!isLoading)
				+ " isPullingUp = " + isPullingUp());
		return isAllowDropUp && isBottom() && (!isLoading) && isPullingUp();
	}

	private boolean isBottom() {
		if (mChildView == null) {
			if (getChildCount() != 2)
				return false;
			if (getChildAt(0) instanceof ViewGroup)
				mChildView = (ViewGroup) getChildAt(0);
		}
		return isChildBottom();
	}

	private boolean isChildBottom() {
		if (mChildView instanceof ListView) {
			ListView listView = (ListView) mChildView;
			if (listView.getCount() > 0) {
				if (listView.getLastVisiblePosition() == listView.getAdapter().getCount() - 1
						&& listView.getChildAt(listView.getChildCount() - 1).getBottom() <= listView.getHeight()) {
					return true;
				}
			}
		} else {
			// other viewgroup
		}
		return false;
	}

	private boolean isPullingUp() {
		return (firstTouchY - lastTouchY) >= mTouchSlop;
	}

	private void loadData() {
		if (mChildView == null)
			return;
		if (isLoading) {
			return;
		}
		isLoading=true;
		if (mFootAnimView != null) {
			setRefreshing(false);
			setFootRefreshing(true);
			if (mOnLoadListener != null) {
				mOnLoadListener.onLoading();
			}
		} else {
			isLoading=false;
			setRefreshing(false);
			if (mOnLoadListener != null) {
				mOnLoadListener.onLoading();
				setSelectLast();
				mOnLoadListener.onLoadFinish();
			}
		}

	}

	public void setFootRefreshing(boolean refreshing) {
		if (refreshing) {
			isLoading = refreshing;
			startFootAnimation();
		} else {
			stopFootAnimation();
		}
	}

	private void startFootAnimation() {
		if (mFootAnimView != null && mFootAnimView.getChildCount() == 1) {
			addFootView(mFootAnimView);
			if (mFootAnimation != null) {
				mFootAnimView.getChildAt(0).startAnimation(mFootAnimation);
			}
		}
	}

	private void stopFootAnimation() {
		if (mFootAnimView != null && mFootAnimView.getChildCount() == 1) {
			isLoading=false;
			mFootAnimView.getChildAt(0).clearAnimation();
			removeFootView(mFootAnimView);
			setSelectLast();
			if (mOnLoadListener != null) {
				mOnLoadListener.onLoadFinish();
			}
		}
	}

	private void removeFootView(View mFootView) {
		if (mFootView != mFootAnimView) {
			removeView(mFootAnimView);
		}
		removeView(mFootView);
		// if (mChildView != null && mChildView instanceof ListView) {
		// ListView listview = (ListView) mChildView;
		// if (mFootView != mFootAnimView) {
		// listview.removeFooterView(mFootAnimView);
		// }
		// listview.removeFooterView(mFootView);
		// } else {
		// // other viewroup
		// }
	}

	private void addFootView(View view) {
		addView(view, LayoutParams.MATCH_PARENT, 200);
		view.layout(0, getHeight() - 200, getWidth(), getHeight());
		// if (mChildView != null && mChildView instanceof ListView) {
		// ListView listview = (ListView) mChildView;
		// listview.addFooterView(view);
		// } else {
		// // other viewroup
		// }
	}

	private void setSelectLast() {
		if (isSelectionLastPos) {
			if (mChildView instanceof ListView) {
				ListView listview = (ListView) mChildView;
				listview.setSelection(listview.getAdapter().getCount() - 1);
			} else {
				// other view
			}
		}
	}

	public void setOnLoadListener(OnLoadListener loadListener) {
		mOnLoadListener = loadListener;
	}

	public interface OnLoadListener {
		public void onLoading();

		public void onLoadFinish();
	}

}