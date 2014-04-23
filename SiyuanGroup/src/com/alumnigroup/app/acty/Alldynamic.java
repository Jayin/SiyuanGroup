package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.adapter.DynamicAdapter;
import com.alumnigroup.adapter.MyOnPageChangeListener;
import com.alumnigroup.api.DynamicAPI;
import com.alumnigroup.app.AppCache;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Dynamic;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.widget.XListView;
import com.alumnigroup.widget.XListView.IXListViewListener;
import com.astuetz.PagerSlidingTabStrip;

/**
 * 全部动态界面
 * 
 * @author vector;restructured by Jayin Ton
 * 
 */
public class Alldynamic extends BaseActivity implements OnClickListener {

//	private View btn_AllDynamic, btn_friendDynamic;
	PagerSlidingTabStrip tabs;
	private String[] titles ;
	/**
	 * 显示内容全部动态和好友动态ViewPeger
	 */
	private ViewPager viewpager;

	private XListView lv_AllDynamic, lv_friendDynamic;
	private ArrayList<Dynamic> data_alldynamic, data_frienddynamic;
	private DynamicAdapter adapter_all, adapter_friend;
	private DynamicAPI api;
	private int page_all = 0;
	private int page_myfriend = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_alldynamic);
		initData();
		initLayout();
		initController();
	}

	@Override
	protected void initData() {
		titles = getResources().getStringArray(R.array.title_alldymatic);
		api = new DynamicAPI();
		if (AppCache.getDynamicAll(getContext()) != null) {
			data_alldynamic = AppCache.getDynamicAll(getContext());
		} else {
			data_alldynamic = new ArrayList<Dynamic>();
		}
		if (AppCache.getDynamicFriend(getContext()) != null) {
			data_frienddynamic = AppCache.getDynamicFriend(getContext());
		} else {
			data_frienddynamic = new ArrayList<Dynamic>();
		}
		adapter_all = new DynamicAdapter(data_alldynamic, this);
		adapter_friend = new DynamicAdapter(data_frienddynamic, this);

	}

	@Override
	protected void initLayout() {
		_getView(R.id.acty_head_btn_back).setOnClickListener(this);
		initViewPager();
	}

	/**
	 * 初始化ViewPager
	 */
	private void initViewPager() {
		tabs = (PagerSlidingTabStrip)_getView(R.id.tabs);
		viewpager = (ViewPager) _getView(R.id.acty_alldynamic_vp_content);
		
		View allDynamic, friendDynamic;
		allDynamic = getLayoutInflater().inflate(
				R.layout.item_lv_acty_alldynamic_content, null);
		friendDynamic = getLayoutInflater().inflate(
				R.layout.item_lv_acty_frienddynamic_content, null);
		lv_AllDynamic = (XListView) allDynamic
				.findViewById(R.id.item_lv_alldynamic_content);
		lv_friendDynamic = (XListView) friendDynamic
				.findViewById(R.id.item_lv_frienddynamic_content);

		List<View> views = new ArrayList<View>();
		views.add(allDynamic);
		views.add(friendDynamic);

		List<XListView> listviews = new ArrayList<XListView>();
		listviews.add(lv_AllDynamic);
		listviews.add(lv_friendDynamic);

		viewpager.setAdapter(new BaseViewPagerAdapter(views,titles));
		tabs.setViewPager(viewpager);
		tabs.setOnPageChangeListener(new MyOnPageChangeListener(listviews));
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
	 * 为下拉刷新、加载更多绑定控制器
	 */
	private void initController() {
		lv_AllDynamic.setAdapter(adapter_all);
		lv_friendDynamic.setAdapter(adapter_friend);

		lv_AllDynamic.setPullLoadEnable(true);
		lv_friendDynamic.setPullLoadEnable(true);

		lv_AllDynamic.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {

				api.getAll(1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						ArrayList<Dynamic> newData = Dynamic
								.create_by_jsonarray(obj.toString());
						if (newData == null) {
							toast("网络异常，解析错误");
						} else if (newData.size() == 0) {
							toast("还没有任何动态");
							lv_AllDynamic.setPullLoadEnable(false);
						} else {
							page_all = 1;
							data_alldynamic.clear();
							data_alldynamic.addAll(newData);
							adapter_all.notifyDataSetChanged();
							lv_AllDynamic.setPullLoadEnable(true);
							AppCache.setDynamicAll(getContext(), newData);
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
							data_alldynamic.addAll(newData_alldynamic);
							adapter_all.notifyDataSetChanged();
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

		lv_friendDynamic.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {

				api.getMyFollowing(1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						ArrayList<Dynamic> newData = Dynamic
								.create_by_jsonarray(obj.toString());
						if (newData == null) {
							toast("网络异常，解析错误");
						} else if (newData.size() == 0) {
							toast("还没有任何动态");
							lv_friendDynamic.setPullLoadEnable(false);
						} else {

							page_myfriend = 1;
							data_frienddynamic.clear();
							data_frienddynamic.addAll(newData);
							adapter_friend.notifyDataSetChanged();
							lv_friendDynamic.setPullLoadEnable(true);
							AppCache.setDynamicFriend(getContext(), newData);
						}
						lv_friendDynamic.stopRefresh();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast(ErrorCode.errorList.get(errorCode));
						lv_friendDynamic.stopRefresh();
					}
				});

			}

			@Override
			public void onLoadMore() {

				if (page_myfriend == 0) {
					lv_friendDynamic.stopLoadMore();
					lv_friendDynamic.startRefresh();
					return;
				}
				api.getMyFollowing(page_myfriend + 1,
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<Dynamic> newData = Dynamic
										.create_by_jsonarray(obj.toString());
								if (newData == null) {
									toast("网络异常，解析错误");
								} else if (newData.size() == 0) {
									toast("没有更多");
									lv_friendDynamic.setPullLoadEnable(false);
								} else {

									page_myfriend++;
									data_frienddynamic.addAll(newData);
									adapter_friend.notifyDataSetChanged();
								}
								lv_friendDynamic.stopLoadMore();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast(ErrorCode.errorList.get(errorCode));
								lv_friendDynamic.stopLoadMore();
							}
						});

			}
		});
		lv_AllDynamic.startRefresh();
	}

}
