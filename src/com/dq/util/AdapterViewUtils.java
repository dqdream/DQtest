package com.dq.util;

import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 
 * @author dq
 *
 */
public class AdapterViewUtils {
	/**
	 * º∆À„ adapterview ∏ﬂ∂»
	 * 
	 * @param adapterview
	 */
	public static void getTotalHeightofAdapterView(AdapterView<ListAdapter> adapterview) {
		int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		adapterview.measure(width, height);
		ListAdapter mAdapter = adapterview.getAdapter();
		if (mAdapter == null) {
			return;
		}
		int totalHeight = 0;
		int columns = 1;
		if (adapterview instanceof ListView) {
			columns = 1;
		} else if (adapterview instanceof GridView) {
			columns = ((GridView) adapterview).getNumColumns();
		}
		for (int i = 0; i < mAdapter.getCount() && columns > 0; i += columns) {
			View mView = mAdapter.getView(i, null, adapterview);
			mView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			totalHeight += mView.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = adapterview.getLayoutParams();
		int dividerheight = 0;
		if (adapterview instanceof ListView) {
			dividerheight = (((ListView) adapterview).getDividerHeight() * (mAdapter.getCount() - 1));
		} else if (adapterview instanceof GridView) {
			GridView view = (GridView) adapterview;
			dividerheight = (view.getVerticalSpacing() * (view.getCount() / view.getNumColumns()));
		}
		params.height = totalHeight + dividerheight;
		adapterview.setLayoutParams(params);
		adapterview.requestLayout();
	}
}
