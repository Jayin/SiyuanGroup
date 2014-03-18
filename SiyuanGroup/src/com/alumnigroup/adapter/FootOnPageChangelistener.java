package com.alumnigroup.adapter;

import java.util.List;

import com.alumnigroup.app.R;
import com.alumnigroup.widget.XListView;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.BaseAdapter;

/**
 * 底部tab
 * 
 * @author Jayin Ton
 * 
 */
public class FootOnPageChangelistener implements OnPageChangeListener {
	private List<View> btns;
	private List<XListView> listviews;
	private List<? extends BaseAdapter> adapters;

	public FootOnPageChangelistener(List<View> btns, List<XListView> listviews,
			List<? extends BaseAdapter> adapters) {
		this.btns = btns;
		this.listviews = listviews;
		this.adapters = adapters;
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
		for (View v : btns) {
			v.setBackgroundResource(R.color.blue_nav_bg_nomal);
		}
		btns.get(positon).setBackgroundResource(R.color.blue_nav_bg_press);
		if (listviews != null && listviews.get(positon) != null)
			listviews.get(positon).startRefresh();

	}

}
