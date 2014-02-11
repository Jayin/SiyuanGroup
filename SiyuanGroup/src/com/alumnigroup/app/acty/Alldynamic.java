package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

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
import android.widget.TextView;

import com.alumnigroup.adapter.DynamicAdapter;
import com.alumnigroup.api.DynamicAPI;
import com.alumnigroup.api.UserAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Dynamic;
import com.alumnigroup.entity.Tweet;
import com.alumnigroup.entity.User;
import com.alumnigroup.utils.DataPool;
import com.alumnigroup.utils.JsonUtils;
import com.alumnigroup.utils.L;
import com.alumnigroup.widget.PullAndLoadListView;
import com.alumnigroup.widget.PullAndLoadListView.OnLoadMoreListener;
import com.alumnigroup.widget.PullToRefreshListView.OnRefreshListener;
import com.loopj.android.http.AsyncHttpResponseHandler;

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
	 * 页卡的PullAndLoadListView
	 */
	private PullAndLoadListView lvAllDynamic, lvFriendDynamic;
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
			Dynamic dynamic = (Dynamic) dp.get(DataPool.SP_KEY_FRIEND_DUNAMIC + i);
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
		initCursor();
		initDynamicName();
		initViewPager();
		initDynamicDate();
		initController();
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
		bmpWidth = BitmapFactory.decodeResource(getResources(),
				R.drawable.alldynamic_cursor).getWidth();// 获取图片宽度
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
		allDynamic = inflater.inflate(R.layout.item_lv_acty_alldynamic_content,
				null);
		lvAllDynamic = (PullAndLoadListView) allDynamic
				.findViewById(R.id.item_lv_alldynamic_content);

		/**
		 * frienddynamic and listview
		 */
		friendDynamic = inflater.inflate(
				R.layout.item_lv_acty_frienddynamic_content, null);
		lvFriendDynamic = (PullAndLoadListView) friendDynamic
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
		case R.id.acty_alldynamic_alldynamic_name:
			vpDynamicContent.setCurrentItem(0);
			break;

		case R.id.acty_alldynamic_frienddynamic_name:
			vpDynamicContent.setCurrentItem(1);
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
		lvAllDynamic.setOnRefreshListener(new OnRefreshListener() {


			@Override
			public void onRefresh() {
				// page=1?
				api.getAll(1, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] data, Throwable err) {
						toast("网络异常 错误码:" + statusCode);
						if (data != null)
							L.i(new String(data));
						if (err != null)
							L.i(err.toString());
						lvAllDynamic.onRefreshComplete();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] data) {
						// 还要判断是否有error_code
						String json = new String(data);// jsonarray
						if (JsonUtils.isOK(json)) {
							List<Dynamic> newData_all = Dynamic
									.create_by_jsonarray(json);
							if (newData_all != null) {
								page_all = 1;
								alAllDynamicContent.clear();
								alAllDynamicContent.addAll(newData_all);
								saveAllData2SP(newData_all);
								aptAllDynamic.notifyDataSetChanged();
							}
						} else {
							toast("Error:" + JsonUtils.getErrorString(json));
						}
						lvAllDynamic.setCanLoadMore(true);// 因为下拉到最低的时候，再下拉刷新，相当于继续可以下拉刷新
						lvAllDynamic.onRefreshComplete();
					}
				});
			}
		});

		lvAllDynamic.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				api.getAll(page_all + 1,
						new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(int statusCode,
									Header[] headers, byte[] data, Throwable err) {
								toast("网络异常 错误码:" + statusCode);
								if (data != null)
									L.i(new String(data));
								if (err != null)
									L.i(err.toString());
								lvAllDynamic.onLoadMoreComplete();
							}

							@Override
							public void onSuccess(int statusCode,
									Header[] headers, byte[] data) {

								// L.i(new String(data));
								String json = new String(data);// json array
								if (JsonUtils.isOK(json)) {
									List<Dynamic> newData_allmember = Dynamic
											.create_by_jsonarray(json);
									if (newData_allmember != null
											&& newData_allmember.size() > 0) {
										page_all++;
										alAllDynamicContent
												.addAll(newData_allmember);
										aptAllDynamic
												.notifyDataSetChanged();
									} else {
										if (newData_allmember == null) {
											toast("网络异常,解析错误");
										} else if (newData_allmember.size() == 0) {
											toast("没有更多了!");
											lvAllDynamic.setCanLoadMore(false);
										}
									}
								} else {
									toast("Error:"
											+ JsonUtils.getErrorString(json));
								}
								lvAllDynamic.onLoadMoreComplete();

							}
						});
			}
		});

		lvFriendDynamic.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				api.getAll(1, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] data, Throwable err) {
						toast("网络异常 错误码:" + statusCode);
						if (data != null)
							L.i(new String(data));
						if (err != null)
							L.i(err.toString());
						lvFriendDynamic.onRefreshComplete();
					}

					// page=1?
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] data) {
						// 还要判断是否有error_code
						String json = new String(data);// jsonarray
						if (JsonUtils.isOK(json)) {
							List<Dynamic> newData_myfriend = Dynamic
									.create_by_jsonarray(json);
							if (newData_myfriend != null) {
								page_myfriend = 1;
								alFriendDynamicContent.clear();
								alFriendDynamicContent.addAll(newData_myfriend);
								saveMyFriendData2SP(newData_myfriend);
								aptFriendDynamic.notifyDataSetChanged();
							}
						} else {
							toast("Error:" + JsonUtils.getErrorString(json));
						}
						lvFriendDynamic.setCanLoadMore(true);// 因为下拉到最低的时候，再下拉刷新，相当于继续可以下拉刷新
						lvFriendDynamic.onRefreshComplete();
					}
				});
			}
		});

		lvFriendDynamic.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				api.getAll(page_myfriend + 1,
						new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(int statusCode,
									Header[] headers, byte[] data, Throwable err) {
								toast("网络异常 错误码:" + statusCode);
								if (data != null)
									L.i(new String(data));
								if (err != null)
									L.i(err.toString());
								lvFriendDynamic.onLoadMoreComplete();
							}

							@Override
							public void onSuccess(int statusCode,
									Header[] headers, byte[] data) {

								// L.i(new String(data));
								String json = new String(data);// json array
								if (JsonUtils.isOK(json)) {
									List<Dynamic> newData_myFriend = Dynamic
											.create_by_jsonarray(json);
									if (newData_myFriend != null
											&& newData_myFriend.size() > 0) {
										page_myfriend++;
										alFriendDynamicContent.addAll(newData_myFriend);
										aptFriendDynamic.notifyDataSetChanged();
									} else {
										if (newData_myFriend == null) {
											toast("网络异常,解析错误");
										} else if (newData_myFriend.size() == 0) {
											toast("没有更多了!");
											lvFriendDynamic.setCanLoadMore(false);
										}
									}
								} else {
									toast("Error:"
											+ JsonUtils.getErrorString(json));
								}
								lvFriendDynamic.onLoadMoreComplete();

							}
						});
			}
		});

	}

}
