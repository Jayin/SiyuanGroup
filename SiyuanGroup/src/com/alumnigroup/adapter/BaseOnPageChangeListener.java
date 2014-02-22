package com.alumnigroup.adapter;

import java.util.List;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.alumnigroup.app.R;
import com.alumnigroup.widget.XListView;
/**
 * 即将废弃 使用 FootOnPageChangelistener来替换
 * @author Jayin Ton
 *
 */
public class BaseOnPageChangeListener implements OnPageChangeListener {
	private List<View> btns;

	public BaseOnPageChangeListener(List<View> btns){
		this.btns = btns;
		
	}
	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int positon) {
         for(View v : btns){
        	 v.setBackgroundResource(R.color.blue_nav_bg_nomal);
         }
         btns.get(positon).setBackgroundResource(R.color.blue_nav_bg_press);
	}

}
