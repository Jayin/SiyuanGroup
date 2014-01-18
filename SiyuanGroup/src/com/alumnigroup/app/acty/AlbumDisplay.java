package com.alumnigroup.app.acty;

import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;

/**
 * 演示查看图片，用ViewPager 方式，进来是要传图片的地址，以数组的方式传递 显示传递过来的图片， 也用传递位置，就是一开始要看见第几个
 * 
 * @author vector
 * 
 */
public class AlbumDisplay extends BaseActivity {

	private String[] imageUrls = {"","","","",""};
	private int pagerPosition = 2;

	private ViewPager vgImageList;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
//			pagerPosition = savedInstanceState
//					.getInt(Constants.ImagePager.IMAGE_POSITION);
		}
		setContentView(R.layout.acty_albumdisplay);
		initData();
		initLayout();
	}

	/**
	 * 初始化传来的数据
	 */
	protected void initData() {
		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		/**
		 * 这里增加数据的
		 */
//		imageUrls = bundle.getStringArray(Constants.ImagePager.IMAGES_KEY);
//		pagerPosition = bundle.getInt(Constants.ImagePager.IMAGE_POSITION, 0);

	}

	protected void initLayout() {
		vgImageList = (ViewPager) findViewById(R.id.acty_albumdisplay_vg_images);
		vgImageList.setAdapter(new ImagePagerAdapter(imageUrls));
		vgImageList.setCurrentItem(pagerPosition);
	}

	private class ImagePagerAdapter extends PagerAdapter {

		private String[] images;
		private LayoutInflater inflater;

		ImagePagerAdapter(String[] images) {
			this.images = images;
			inflater = getLayoutInflater();
		}

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
			View imageLayout = inflater.inflate(R.layout.item_acty_albumdisplay,
					view, false);
			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

}
