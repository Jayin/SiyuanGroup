package com.alumnigroup.app.acty;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
//		vgImages4Introduce = (ViewPager) _getView(R.id.acty_introduce_vg_images);
//		vgImages4Introduce.setAdapter(new ImagePagerAdapter());
//		vgImages4Introduce.setCurrentItem(0);
		((TextView)_getView(R.id.tv_introduce)).setText(Html.fromHtml("<p>全站会员<br>在这里你可以查看全部校友信息，看过信息后如果你对该校友感兴趣可以加为好友</p><p>全站动态<br>在这里你可以看到全站最新的动态，你也可以搜索你喜欢的动态</p><p> 校友交流区<br>你可以在这个发送自己的微博，如果你想到一个话题，你可以发起话题邀请校友一起讨论，让校友交流更具有针对性</p><p>我的空间<br>标志性的签名，个性化的标签让你的门面更有特色</p><p>圈子系统<br>寻找你的志同道合朋友</p><p>活动系统<br>增进与统一圈子的朋友感情 </p><p>商务合作<br>校友之间相互帮助，合作共赢</p>"));
        _getView(R.id.acty_head_btn_back).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;

		default:
			break;
		}
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
