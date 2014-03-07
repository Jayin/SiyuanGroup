package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alumnigroup.adapter.DynamicAdapter;
import com.alumnigroup.api.DynamicAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Dynamic;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.DataPool;
import com.alumnigroup.widget.XListView;
import com.alumnigroup.widget.XListView.IXListViewListener;

/**
 * 全部动态界面
 * 
 * @author vector
 * 
 */
public class Alldynamic extends BaseActivity implements OnClickListener {

	/**
	 * 返回按钮，全部动态按钮，好友动态按钮
	 */
	private View btnBack, btnAllDynamic, btnFriendDynamic;
	/**
	 * 头上的标题
	 */
	private TextView tvHeadTitle;
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
	 * 页卡的PullAndLoadListView
	 */
	private XListView lv_AllDynamic, lvFriendDynamic;
	/**
	 * 页卡的数据 -- listview
	 */
	private ArrayList<Dynamic> alAllDynamicContent, alFriendDynamicContent;

	/**
	 * 页卡的数据适配器
	 */
	private DynamicAdapter aptAllDynamic, aptFriendDynamic;

	/**
	 * 页卡的适配器
	 */
	private DynamicViewPagerAdapter viewAdapter;

	private DynamicAPI api = new DynamicAPI();
	private int page_all = 1;
	private int page_myfriend = 1;
	private DataPool dp;
	
	/**
	 * 我的好友开始时候刷新了
	 */
	private boolean isRe = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_alldynamic);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		dp = new DataPool(this);

		alAllDynamicContent = new ArrayList<Dynamic>();
		loadAllData(alAllDynamicContent);
		alFriendDynamicContent = new ArrayList<Dynamic>();
		loadMyFriendData(alFriendDynamicContent);

	}

	/**
	 * 保存缓存数据进sharepreferenses 中,最多保存20 条记录
	 */
	private void saveAllData2SP(List<Dynamic> dynamics) {
		if (dynamics == null || dynamics.size() == 0) {
			return;
		}
		for (int i = 1; i <= 20 && dynamics.size() >= i
				&& dynamics.get(i - 1) != null; i++) {
			dp.remove(DataPool.SP_KEY_ALL_DUNAMIC + i);
			dp.put(DataPool.SP_KEY_ALL_DUNAMIC + i, dynamics.get(i - 1));
		}
	}

	/**
	 * 加载缓存数据进users 中,最多20 条记录
	 */
	private void loadAllData(List<Dynamic> dynamics) {
		if (dynamics == null) {
			return;
		}
		for (int i = 1; i <= 20; i++) {
			Dynamic dynamic = (Dynamic) dp.get(DataPool.SP_KEY_ALL_DUNAMIC + i);
			if (dynamic != null) {
				dynamics.add(dynamic);
			}
		}
	}

	/**
	 * 保存缓存数据进sharepreferenses 中,最多保存20 条记录
	 */
	private void saveMyFriendData2SP(List<Dynamic> dynamics) {
		if (dynamics == null || dynamics.size() == 0) {
			return;
		}
		for (int i = 1; i <= 20 && dynamics.size() >= i
				&& dynamics.get(i - 1) != null; i++) {
			dp.remove(DataPool.SP_KEY_FRIEND_DUNAMIC + i);
			dp.put(DataPool.SP_KEY_FRIEND_DUNAMIC + i, dynamics.get(i - 1));
		}
	}

	/**
	 * 加载缓存数据,最多20 条记录
	 */
	private void loadMyFriendData(List<Dynamic> dynamics) {
		if (dynamics == null) {
			return;
		}
		for (int i = 1; i <= 20; i++) {
			Dynamic dynamic = (Dynamic) dp.get(DataPool.SP_KEY_FRIEND_DUNAMIC
					+ i);
			if (dynamic != null) {
				dynamics.add(dynamic);
			}
		}
	}

	@Override
	protected void initLayout() {
		/**
		 * 主要就是初始化数据，四大步骤
		 */
		initHead();
		initDynamicName();
		initViewPager();
		initDynamicDate();
		initController();
	}

	private void initDynamicDate() {
		aptAllDynamic = new DynamicAdapter(alAllDynamicContent, this);
		aptFriendDynamic = new DynamicAdapter(alFriendDynamicContent, this);

		lv_AllDynamic.setAdapter(aptAllDynamic);
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
	 * 初始化头标, 头标也有点击事件
	 */
	private void initDynamicName() {
		btnAllDynamic = _getView(R.id.acty_alldynamic_footer_alldynamic);
		btnFriendDynamic = _getView(R.id.acty_alldynamic_footer_friend_dynamic);

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
		allDynamic = inflater.inflate(R.layout.item_lv_acty_alldynamic_content,
				null);
		lv_AllDynamic = (XListView) allDynamic
				.findViewById(R.id.item_lv_alldynamic_content);

		/**
		 * frienddynamic and listview
		 */
		friendDynamic = inflater.inflate(
				R.layout.item_lv_acty_frienddynamic_content, null);
		lvFriendDynamic = (XListView) friendDynamic
				.findViewById(R.id.item_lv_frienddynamic_content);

		alDynamicView.add(allDynamic);
		alDynamicView.add(friendDynamic);
		viewAdapter = new DynamicViewPagerAdapter(alDynamicView);
		vpDynamicContent.setAdapter(viewAdapter);
		vpDynamicContent.setCurrentItem(0);
		// 需要监听页卡改变事件
		vpDynamicContent
				.setOnPageChangeListener(new DynamicOnPageChangeListener());
	}

	/**
	 * 监听页卡的改变
	 * 
	 * @author vector
	 * 
	 */
	class DynamicOnPageChangeListener implements OnPageChangeListener {

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		/**
		 * 滑动变为另外一个View 的时候，改变footer
		 */
		public void onPageSelected(int position) {
			changeFooter(position);
		}

	}

	private void changeFooter(int position) {
		if (position == 0) {
			btnAllDynamic.setBackgroundResource(R.color.blue_nav_bg_press);
			btnFriendDynamic.setBackgroundResource(R.color.blue_nav_bg_nomal);
			tvHeadTitle.setText("全站动态");
		}
		if (position == 1) {
			btnAllDynamic.setBackgroundResource(R.color.blue_nav_bg_nomal);
			btnFriendDynamic.setBackgroundResource(R.color.blue_nav_bg_press);
			tvHeadTitle.setText("好友动态");
		}
	}

	/**
	 * ViewPager 的适配器
	 * 
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
		int id = v.getId();
		switch (id) {
		case R.id.acty_alldynamic_footer_alldynamic:
			vpDynamicContent.setCurrentItem(0);
			changeFooter(0);
			break;

		case R.id.acty_alldynamic_footer_friend_dynamic:
			if(!isRe){
				lvFriendDynamic.startRefresh();
				isRe = !isRe;
			}
			vpDynamicContent.setCurrentItem(1);
			changeFooter(1);
			break;

		case R.id.acty_head_btn_back:
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 为下拉刷新、加载更多绑定控制器
	 */
	private void initController() {
		lv_AllDynamic.setPullLoadEnable(true);
		lvFriendDynamic.setPullLoadEnable(true);

		lv_AllDynamic.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {

				api.getAll(1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<Dynamic> newData_alldynamic = Dynamic
								.create_by_jsonarray(obj.toString());
						if (newData_alldynamic == null) {
							toast("网络异常，解析错误");
						} else if (newData_alldynamic.size() == 0) {
							toast("还没有任何动态");
							lv_AllDynamic.setPullLoadEnable(false);
						} else {

							page_all = 1;
							alAllDynamicContent.clear();
							alAllDynamicContent.addAll(newData_alldynamic);
							saveAllData2SP(newData_alldynamic);
							aptAllDynamic.notifyDataSetChanged();

							lv_AllDynamic.setPullLoadEnable(true);
						}
						lv_AllDynamic.stopRefresh();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast(ErrorCode.errorList.get(errorCode));
						lv_AllDynamic.stopRefresh();
					}
				});

			}

			@Override
			public void onLoadMore() {

				if (page_all == 0) {
					lv_AllDynamic.stopLoadMore();
					lv_AllDynamic.startRefresh();
					return;
				}
				api.getAll(page_all + 1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<Dynamic> newData_alldynamic = Dynamic
								.create_by_jsonarray(obj.toString());
						if (newData_alldynamic == null) {
							toast("网络异常，解析错误");
						} else if (newData_alldynamic.size() == 0) {
							toast("没有更多");
							lv_AllDynamic.setPullLoadEnable(false);
						} else {
							page_all++;
							alAllDynamicContent.addAll(newData_alldynamic);
							aptAllDynamic.notifyDataSetChanged();
						}
						lv_AllDynamic.stopLoadMore();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast(ErrorCode.errorList.get(errorCode));
						lv_AllDynamic.stopLoadMore();
					}
				});

			}
		});

		lvFriendDynamic.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {

				api.getMyFollowing(1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<Dynamic> newData_alldynamic = Dynamic
								.create_by_jsonarray(obj.toString());
						if (newData_alldynamic == null) {
							toast("网络异常，解析错误");
						} else if (newData_alldynamic.size() == 0) {
							toast("还没有任何动态");
							lvFriendDynamic.setPullLoadEnable(false);
						} else {

							page_myfriend = 1;
							alFriendDynamicContent.clear();
							alFriendDynamicContent.addAll(newData_alldynamic);
							saveMyFriendData2SP(newData_alldynamic);
							aptFriendDynamic.notifyDataSetChanged();

							lvFriendDynamic.setPullLoadEnable(true);
						}
						lvFriendDynamic.stopRefresh();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast(ErrorCode.errorList.get(errorCode));
						lvFriendDynamic.stopRefresh();
					}
				});

			}

			@Override
			public void onLoadMore() {

				if (page_myfriend == 0) {
					lvFriendDynamic.stopLoadMore();
					lvFriendDynamic.startRefresh();
					return;
				}
				api.getMyFollowing(page_myfriend + 1,
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<Dynamic> newData_alldynamic = Dynamic
										.create_by_jsonarray(obj.toString());
								if (newData_alldynamic == null) {
									toast("网络异常，解析错误");
								} else if (newData_alldynamic.size() == 0) {
									toast("没有更多");
									lvFriendDynamic.setPullLoadEnable(false);
								} else {

									page_myfriend++;
									alFriendDynamicContent
											.addAll(newData_alldynamic);
									aptFriendDynamic.notifyDataSetChanged();
								}
								lvFriendDynamic.stopLoadMore();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast(ErrorCode.errorList.get(errorCode));
								lvFriendDynamic.stopLoadMore();
							}
						});

			}
		});
		
		lv_AllDynamic.startRefresh();
	}

}
