package com.alumnigroup.adapter;

import java.util.List;

import com.alumnigroup.app.R;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

public class BaseOnPageChangeListener implements OnPageChangeListener {
	private List<View> btns;

	public BaseOnPageChangeListener(List<View> btns){
		this.btns = btns;
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int positon) {
         for(View v : btns){
        	 v.setBackgroundResource(R.color.blue_nav_bg_nomal);
         }
         btns.get(positon).setBackgroundResource(R.color.blue_nav_bg_press);
	}

}
