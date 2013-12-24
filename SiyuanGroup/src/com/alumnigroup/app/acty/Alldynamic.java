package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alumnigroup.adapter.DynamicAdapter;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Tweet;

/**
 * 全部动态界面
 * @author vector
 *
 */
public class Alldynamic extends BaseActivity implements OnClickListener{

	/**
	 * 返回按钮，全部动态按钮，好友动态按钮
	 */
	private View btnBack,btnAllDynamic,btnFriendDynamic;
	/**
	 * 头上的标题
	 */
	private TextView tvHeadTitle;
	/**
	 * 改变动态类型，移动的游标
	 */
	private ImageView ivCursor;
	/**
	 * 动画图片偏移量
	 */
	private int offset = 0;
	/**
	 * 当前页卡编号
	 */
	private int currIndex = 0;
	/**
	 * 游标图片宽度
	 */
	private int bmpWidth;
	/**
	 * 全部动态和好友动态页面
	 */
	private View allDynamic, friendDynamic;

	/**
	 * 显示内容全部动态和好友动态ViewPeger
	 */
	private ViewPager vpDynamicContent;
	
	/**
	 * 要显示的页卡
	 */
	private ArrayList<View> alDynamicView;
	
	
	/**
	 * 页卡的listview
	 */
	private ListView lvAllDynamic,lvFriendDynamic;
	/**
	 * 页卡的数据 -- listview
	 */
	private ArrayList<Tweet> alAllDynamicContent,alFriendDynamicContent;
	
	/**
	 * 页卡的数据适配器
	 */
	private DynamicAdapter aptAllDynamic,aptFriendDynamic;
	
	/**
	 * 页卡的适配器
	 */
	private DynamicViewPagerAdapter viewAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_alldynamic);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		
		//这里模拟一些数据
		alAllDynamicContent = new ArrayList<Tweet>(){{
			add(new Tweet());
			add(new Tweet());
			add(new Tweet());
			add(new Tweet());
			add(new Tweet());
			add(new Tweet());
			add(new Tweet());
		}}; 
		alFriendDynamicContent = new ArrayList<Tweet>(){{
			add(new Tweet());
			add(new Tweet());
			add(new Tweet());
			add(new Tweet());
			add(new Tweet());
			add(new Tweet());
			add(new Tweet());
		}}; 
	}

	@Override
	protected void initLayout() {
		/**
		 * 主要就是初始化数据，四大步骤
		 */
		initHead();
		initCursor();
		initDynamicName();
		initViewPager();
		initDynamicDate();
	}
	
	private void initDynamicDate() {
		aptAllDynamic = new DynamicAdapter(alAllDynamicContent, this);
		aptFriendDynamic = new DynamicAdapter(alFriendDynamicContent, this);
		
		lvAllDynamic.setAdapter(aptAllDynamic);
		lvFriendDynamic.setAdapter(aptFriendDynamic);
	}

	/**
	 * 初始化头部控件
	 */
	private void initHead() {
		btnBack = _getView(R.id.acty_head_btn_back);
		btnBack.setOnClickListener(this);
		tvHeadTitle = (TextView) _getView(R.id.acty_head_tv_title);
	}

	/**
	 * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
	 */
	private void initCursor() {
		ivCursor = (ImageView) findViewById(R.id.cursor);
		bmpWidth = BitmapFactory.decodeResource(getResources(), R.drawable.alldynamic_cursor).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 2 - bmpWidth) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		ivCursor.setImageMatrix(matrix);// 设置动画初始位置
	}
	
	/**
	 * 初始化头标, 头标也有点击事件
	 */
	private void initDynamicName() {
		btnAllDynamic = _getView(R.id.acty_alldynamic_alldynamic_name);
		btnFriendDynamic = _getView(R.id.acty_alldynamic_frienddynamic_name);
		
		btnAllDynamic.setOnClickListener(this);
		btnFriendDynamic.setOnClickListener(this);
	}

	/**
	 * 初始化ViewPager
	 */
	private void initViewPager() {
		
		vpDynamicContent = (ViewPager) _getView(R.id.acty_alldynamic_vp_content);
		alDynamicView = new ArrayList<View>();
		/**
		 * alldynamic and listview
		 */
		LayoutInflater inflater = getLayoutInflater();
		allDynamic = inflater.inflate(R.layout.item_lv_acty_alldynamic_content, null);
		lvAllDynamic = (ListView) allDynamic.findViewById(R.id.item_lv_alldynamic_content);
		
		/**
		 * frienddynamic and listview
		 */
		friendDynamic = inflater.inflate(R.layout.item_lv_acty_frienddynamic_content, null);
		lvFriendDynamic = (ListView) friendDynamic.findViewById(R.id.item_lv_frienddynamic_content);
		
		alDynamicView.add(allDynamic);
		alDynamicView.add(friendDynamic);
		viewAdapter = new DynamicViewPagerAdapter(alDynamicView);
		vpDynamicContent.setAdapter(viewAdapter);
		vpDynamicContent.setCurrentItem(0);
		// 需要监听页卡改变事件
		vpDynamicContent.setOnPageChangeListener(new DynamicOnPageChangeListener());
	}
	/**
	 * 监听页卡的改变
	 * @author vector
	 *
	 */
	class DynamicOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpWidth;// 页卡1 -> 页卡2 偏移量

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageSelected(int arg0) {
			/*
			 * 两种方法，这个是一种，下面还有一种，显然这个比较麻烦 Animation animation = null; switch
			 * (arg0) { case 0: if (currIndex == 1) { animation = new
			 * TranslateAnimation(one, 0, 0, 0); } else if (currIndex == 2) {
			 * animation = new TranslateAnimation(two, 0, 0, 0); } break; case
			 * 1: if (currIndex == 0) { animation = new
			 * TranslateAnimation(offset, one, 0, 0); } else if (currIndex == 2)
			 * { animation = new TranslateAnimation(two, one, 0, 0); } break;
			 * case 2: if (currIndex == 0) { animation = new
			 * TranslateAnimation(offset, two, 0, 0); } else if (currIndex == 1)
			 * { animation = new TranslateAnimation(one, two, 0, 0); } break;
			 * 
			 * }
			 */
			Animation animation = new TranslateAnimation(one * currIndex, one
					* arg0, 0, 0);// 显然这个比较简洁，只有一行代码。
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			ivCursor.startAnimation(animation);
		}

	}
	
	
	/**
	 * ViewPager 的适配器
	 * @author vector
	 *
	 */
	class DynamicViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public DynamicViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;// 构造方法，参数是我们的页卡，这样比较方便。
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));// 删除页卡
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
			container.addView(mListViews.get(position), 0);// 添加页卡
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();// 返回页卡的数量
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// 官方提示这样写
		}
	}

	
	@Override
	public void onClick(View v) {
		toast("点击了...");
		int id = v.getId();
		switch (id) {
		case R.id.acty_alldynamic_alldynamic_name:
			vpDynamicContent.setCurrentItem(0);
			break;

		case R.id.acty_alldynamic_frienddynamic_name:
			vpDynamicContent.setCurrentItem(1);
			break;
			
		default:
			break;
		}
	}
	
}
