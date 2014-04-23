package com.alumnigroup.adapter;

import java.util.List;

import com.alumnigroup.widget.XListView;

import android.support.v4.view.ViewPager.OnPageChangeListener;

public class MyOnPageChangeListener implements OnPageChangeListener {

	private List<XListView> listviews;

	public MyOnPageChangeListener(List<XListView> listviews) {
		this.listviews = listviews;
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onPageScrolled(int positon, float positionOffset,
			int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int positon) {
		if (listviews != null && listviews.get(positon) != null)
			listviews.get(positon).startRefresh();

	}

}
