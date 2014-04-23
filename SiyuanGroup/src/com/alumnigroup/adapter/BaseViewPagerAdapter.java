package com.alumnigroup.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class BaseViewPagerAdapter extends PagerAdapter {
	private List<View> views;
	private String[] titles;
	public BaseViewPagerAdapter(List<View> views,String[] titles) {
		this.views = views;
		this.titles = titles;
	}
	
	public BaseViewPagerAdapter(List<View> views) {
		this.views = views;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	// 删除卡片
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	}

	// 实例化卡片
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(views.get(position));
		return views.get(position);
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}
	
	
}
