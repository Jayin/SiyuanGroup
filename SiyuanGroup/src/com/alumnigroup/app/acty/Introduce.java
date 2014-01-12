package com.alumnigroup.app.acty;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;

public class Introduce extends BaseActivity {

	private ViewPager vgImages4Introduce;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_introduce);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initLayout() {
		vgImages4Introduce = (ViewPager) _getView(R.id.acty_introduce_vg_images);
		vgImages4Introduce.setAdapter(new ImagePagerAdapter());
		vgImages4Introduce.setCurrentItem(0);
	}

	
	/**
	 * 功能介绍的图片适配器，是固定是形式
	 * @author vector
	 *
	 */
	private class ImagePagerAdapter extends PagerAdapter {

		private int[] images = {R.drawable.a_lyf_1,R.drawable.a_lyf_1,R.drawable.a_lyf_1,R.drawable.a_lyf_1};
		private LayoutInflater inflater = inflater = getLayoutInflater();;
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return images.length;
		}

		/**
		 * 这里填充数据
		 */
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(
					R.layout.item_acty_introduce_image, view, false);
			/**
			 * 设置图片
			 */
			ImageView  image = (ImageView) imageLayout.findViewById(R.id.item_acty_introduce_image);
			image.setImageDrawable(getResources().getDrawable(images[position]));
			
			/**
			 * 如果是最后一张图片就多一个按钮
			 */
			if(position == images.length-1){
				View btnBack = imageLayout.findViewById(R.id.item_acty_introduce_btn_back);
				btnBack.setVisibility(View.VISIBLE);
				btnBack.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						finish();
					}
				});
			}
			
			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

}
