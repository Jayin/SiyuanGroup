package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.adapter.FootOnPageChangelistener;
import com.alumnigroup.api.ActivityAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.api.StarAPI;
import com.alumnigroup.app.AppCache;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.MActivity;
import com.alumnigroup.entity.Starring;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.utils.DataPool;
import com.alumnigroup.widget.XListView;
import com.alumnigroup.widget.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 活动页面
 * 
 * @author Jayin Ton
 * 
 */
public class Activities extends BaseActivity implements OnItemClickListener {
	private List<View> btns = new ArrayList<View>();
	private View btn_back, btn_all, btn_favourite, btn_myjoin;
	private XListView lv_all, lv_myjoin, lv_favourit;
	private ViewPager viewpager;
	private ArrayList<MActivity> data_all, data_myjoin, data_favourite;
	private ActivitiesAdapter adapter_all, adapter_myjoin, adapter_favourite;
	private int page_all = 0, page_myjoin = 0, page_favourit = 0;
	private ActivityAPI api;
	private StarAPI starAPI;
	private User user;

	private ArrayList<MActivity> data_clicked = null;
	private int item_click = -1;
	private View viewClicked = null;// 当前点击的item(View),用来更新item用的
	private BroadcastReceiver mReceiver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_activities);
		initData();
		initLayout();
		initController();
		openReceiver();
	}
	
	private void openReceiver() {
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				MActivity a = (MActivity) intent
						.getSerializableExtra("activity");
				data_clicked.set(item_click,a);
				((TextView) viewClicked
						.findViewById(R.id.item_lv_acty_activities_actyname))
						.setText(a.getName());
				((TextView) viewClicked
						.findViewById(R.id.item_lv_acty_activities_site))
						.setText(a.getSite());
				((TextView) viewClicked
						.findViewById(R.id.item_lv_acty_activities_starttime))
						.setText("时间:"
								+ CalendarUtils.getTimeFromat(a.getStarttime(),
										CalendarUtils.TYPE_TWO));
				((TextView) viewClicked
						.findViewById(R.id.item_lv_acty_activities_ownername))
						.setText(a.getUser().getProfile().getName());
				((TextView) viewClicked
						.findViewById(R.id.item_lv_acty_activities_applyCount))
						.setText(a.getNumUsership() + "人报名");
				((ImageView) viewClicked
						.findViewById(R.id.item_lv_acty_activities_iv_status))
						.setImageResource(a.getStatus().getId() == 1 ? R.drawable.ic_image_status_on
								: R.drawable.ic_image_status_off);
				ImageView iv_avatar = (ImageView) viewClicked
						.findViewById(R.id.item_lv_acty_activities_iv_avater);
				if (a.getAvatar() != null) {
					ImageLoader.getInstance().displayImage(
							RestClient.BASE_URL + a.getAvatar(), iv_avatar);
				} else {
					ImageLoader.getInstance().displayImage(
							"drawable://" + R.drawable.ic_image_load_normal,
							iv_avatar);
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action_ActivityInfo_Edit);
		registerReceiver(mReceiver, filter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mReceiver!=null){
			unregisterReceiver(mReceiver);
		}
	}

	private void initController() {
		lv_all.setPullRefreshEnable(true);
		lv_all.setPullLoadEnable(true);
		lv_myjoin.setPullRefreshEnable(true);
		lv_myjoin.setPullLoadEnable(true);
		lv_favourit.setPullRefreshEnable(true);
		lv_favourit.setPullLoadEnable(true);
		lv_all.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				api.getActivityList(1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						ArrayList<MActivity> newData_all = MActivity
								.create_by_jsonarray(obj.toString());
						if (newData_all == null) {
							toast("网络异常，解析错误");
						} else if (newData_all.size() == 0) {
							toast("还没有活动");
							lv_all.setPullLoadEnable(false);
						} else {
							page_all = 1;
							data_all.clear();
							data_all.addAll(newData_all);
							adapter_all.notifyDataSetChanged();
							if (data_all.size() < 10) {
								lv_all.setPullLoadEnable(false);
							} else {
								lv_all.setPullLoadEnable(true);
							}
							AppCache.setActivityAll(getContext(), data_all);
						}
						lv_all.stopRefresh();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast(ErrorCode.errorList.get(errorCode));
						lv_all.stopRefresh();
					}
				});
			}

			@Override
			public void onLoadMore() {
				if (page_all == 0) {
					lv_all.stopLoadMore();
					lv_all.startRefresh();
					return;
				}
				api.getActivityList(page_all + 1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<MActivity> newData_all = MActivity
								.create_by_jsonarray(obj.toString());
						if (newData_all == null) {
							toast("网络异常，解析错误");
						} else if (newData_all.size() == 0) {
							toast("没有更多");
							lv_all.setPullLoadEnable(false);
						} else {
							page_all++;
							data_all.addAll(newData_all);
							adapter_all.notifyDataSetChanged();
						}
						lv_all.stopLoadMore();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast(ErrorCode.errorList.get(errorCode));
						lv_all.stopLoadMore();
					}
				});
			}
		});

		lv_myjoin.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				api.getMyActivity(1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<MActivity> newData_myjoin = MActivity
								.create_by_jsonarray(obj.toString());
						if (newData_myjoin == null) {
							toast("网络异常，解析错误");
						} else if (newData_myjoin.size() == 0) {
							toast("你还没有参与任何活动");
							lv_myjoin.setPullLoadEnable(false);
						} else {
							page_myjoin = 1;
							data_myjoin.clear();
							data_myjoin.addAll(newData_myjoin);
							adapter_myjoin.notifyDataSetChanged();
							if (data_myjoin.size() < 10) {
								lv_myjoin.setPullLoadEnable(false);
							} else {
								lv_myjoin.setPullLoadEnable(true);
							}
							AppCache.setActivityMy(getContext(), data_myjoin);
						}
						lv_myjoin.stopRefresh();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast(ErrorCode.errorList.get(errorCode));
						lv_myjoin.stopRefresh();
					}
				});

			}

			@Override
			public void onLoadMore() {
				if (page_myjoin == 0) {
					lv_myjoin.stopLoadMore();
					lv_myjoin.startRefresh();
					return;
				}
				api.getMyActivity(page_myjoin + 1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<MActivity> newData_myjoin = MActivity
								.create_by_jsonarray(obj.toString());
						if (newData_myjoin == null) {
							toast("网络异常，解析错误");
						} else if (newData_myjoin.size() == 0) {
							toast("没有更多");
							lv_myjoin.setPullLoadEnable(false);
						} else {
							page_myjoin++;
							data_myjoin.addAll(newData_myjoin);
							adapter_myjoin.notifyDataSetChanged();
						}
						lv_myjoin.stopLoadMore();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast(ErrorCode.errorList.get(errorCode));
						lv_myjoin.stopLoadMore();
					}
				});
			}
		});
		lv_favourit.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				starAPI.getMyStarList(1, StarAPI.Item_type_activity,
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<Starring> stars = Starring
										.create_by_jsonarray(obj.toString());
								List<MActivity> newData_faviour = new ArrayList<MActivity>();
								if (stars == null) {
									toast("网络异常，解析错误");
								} else {
									for (Starring s : stars) {
										newData_faviour.add((MActivity) s
												.getItem());
									}
									if (newData_faviour.size() == 0) {
										toast("你还没有收藏任何活动");
										lv_favourit.setPullLoadEnable(false);
									} else {
										page_favourit = 1;
										data_favourite.clear();
										data_favourite.addAll(newData_faviour);
										adapter_favourite
												.notifyDataSetChanged();
										if (data_favourite.size() < 10) {
											lv_favourit
													.setPullLoadEnable(false);
										} else {
											lv_favourit.setPullLoadEnable(true);
										}
										AppCache.setActivityFavourite(
												getContext(), data_favourite);
									}
								}
								lv_favourit.stopRefresh();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast(ErrorCode.errorList.get(errorCode));
								lv_favourit.stopRefresh();
							}
						});
			}

			@Override
			public void onLoadMore() {
				if (page_favourit == 0) {
					lv_favourit.stopLoadMore();
					lv_favourit.startRefresh();
					return;
				}
				starAPI.getMyStarList(page_favourit + 1,
						StarAPI.Item_type_activity, new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<Starring> stars = Starring
										.create_by_jsonarray(obj.toString());
								List<MActivity> newData_faviour = new ArrayList<MActivity>();
								if (stars == null) {
									toast("网络异常，解析错误");
								} else {
									for (Starring s : stars) {
										newData_faviour.add((MActivity) s
												.getItem());
									}
									if (newData_faviour.size() == 0) {
										toast("没有更多");
										lv_favourit.setPullLoadEnable(false);
									} else {
										page_favourit++;
										data_favourite.addAll(newData_faviour);
										adapter_favourite
												.notifyDataSetChanged();
									}
								}
								lv_favourit.stopLoadMore();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast(ErrorCode.errorList.get(errorCode));
								lv_favourit.stopLoadMore();
							}
						});
			}
		});
		lv_all.setOnItemClickListener(this);
		lv_myjoin.setOnItemClickListener(this);
		lv_favourit.setOnItemClickListener(this);

		lv_all.startRefresh();
	}

	@Override
	protected void initData() {
		DataPool dp = new DataPool(DataPool.SP_Name_User, getContext());
		user = (User) dp.get(DataPool.SP_Key_User);
		api = new ActivityAPI();
		starAPI = new StarAPI();
		if (user == null) {
			toast("无用户信息，请重新登录");
		}
		if (AppCache.getActivityAll(getContext()) != null)
			data_all = AppCache.getActivityAll(getContext());
		else {
			data_all = new ArrayList<MActivity>();
		}

		if (AppCache.getActivityMy(getContext()) != null)
			data_myjoin = AppCache.getActivityMy(getContext());
		else {
			data_myjoin = new ArrayList<MActivity>();
		}

		if (AppCache.getActivityFavourite(getContext()) != null)
			data_favourite = AppCache.getActivityFavourite(getContext());
		else {
			data_favourite = new ArrayList<MActivity>();
		}

	}

	private void initViewPager() {
		viewpager = (ViewPager) _getView(R.id.acty_activities_content);
		View all = getLayoutInflater().inflate(
				R.layout.frame_acty_activities_all, null);
		View myjoin = getLayoutInflater().inflate(
				R.layout.frame_acty_activities_myjoin, null);
		View favourit = getLayoutInflater().inflate(
				R.layout.frame_acty_activities_favourite, null);
		lv_all = (XListView) all
				.findViewById(R.id.frame_acty_activities_all_listview);
		lv_myjoin = (XListView) myjoin
				.findViewById(R.id.frame_acty_activities_myjoin_listview);
		lv_favourit = (XListView) favourit
				.findViewById(R.id.frame_acty_activities_favourite_listview);

		adapter_all = new ActivitiesAdapter(data_all);
		adapter_myjoin = new ActivitiesAdapter(data_myjoin);
		adapter_favourite = new ActivitiesAdapter(data_favourite);

		lv_all.setAdapter(adapter_all);
		lv_myjoin.setAdapter(adapter_myjoin);
		lv_favourit.setAdapter(adapter_favourite);

		List<View> views = new ArrayList<View>();
		views.add(all);
		views.add(myjoin);
		views.add(favourit);

		List<XListView> listviews = new ArrayList<XListView>();
		listviews.add(lv_all);
		listviews.add(lv_myjoin);
		listviews.add(lv_favourit);

		List<ActivitiesAdapter> adapters = new ArrayList<ActivitiesAdapter>();
		adapters.add(adapter_all);
		adapters.add(adapter_myjoin);
		adapters.add(adapter_favourite);
		viewpager.setAdapter(new BaseViewPagerAdapter(views));
		viewpager.setOnPageChangeListener(new FootOnPageChangelistener(btns,
				listviews, adapters));
	}

	@Override
	protected void initLayout() {

		btn_back = _getView(R.id.acty_head_btn_back);
		btn_all = _getView(R.id.acty_activities_footer_all);
		btn_myjoin = _getView(R.id.acty_activities_footer_myjoin);
		btn_favourite = _getView(R.id.acty_activities_footer_favourite);

		btns.add(btn_all);
		btns.add(btn_myjoin);
		btns.add(btn_favourite);

		btn_back.setOnClickListener(this);
		btn_all.setOnClickListener(this);
		btn_myjoin.setOnClickListener(this);
		btn_favourite.setOnClickListener(this);

		initViewPager();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_activities_footer_all:
			if (viewpager.getCurrentItem() == 0) {
				lv_all.startRefresh();
			} else {
				viewpager.setCurrentItem(0, true);
			}
			break;
		case R.id.acty_activities_footer_myjoin:
			if (viewpager.getCurrentItem() == 1) {
				lv_myjoin.startRefresh();
			} else {
				viewpager.setCurrentItem(1, true);
			}
			break;
		case R.id.acty_activities_footer_favourite:
			if (viewpager.getCurrentItem() == 2) {
				lv_favourit.startRefresh();
			} else {
				viewpager.setCurrentItem(2, true);
			}
			break;

		default:
			break;
		}
	}

	class ActivitiesAdapter extends BaseAdapter {
		private List<MActivity> data;

		public ActivitiesAdapter(List<MActivity> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder h = null;
			if (convertView == null) {
				h = new ViewHolder();
				convertView = getLayoutInflater().inflate(
						R.layout.item_lv_acty_activities, null);
				h.actyName = (TextView) convertView
						.findViewById(R.id.item_lv_acty_activities_actyname);
				h.site = (TextView) convertView
						.findViewById(R.id.item_lv_acty_activities_site);
				h.starttime = (TextView) convertView
						.findViewById(R.id.item_lv_acty_activities_starttime);
				h.ownername = (TextView) convertView
						.findViewById(R.id.item_lv_acty_activities_ownername);
				h.applyCount = (TextView) convertView
						.findViewById(R.id.item_lv_acty_activities_applyCount);
				h.status = (ImageView) convertView
						.findViewById(R.id.item_lv_acty_activities_iv_status);
				h.avater = (ImageView) convertView
						.findViewById(R.id.item_lv_acty_activities_iv_avater);
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}
			MActivity acty = data.get(position);
			h.actyName.setText(acty.getName());
			h.site.setText(acty.getSite());
			h.starttime.setText("时间:"
					+ CalendarUtils.getTimeFromat(acty.getStarttime(),
							CalendarUtils.TYPE_TWO));
			h.ownername.setText(acty.getUser().getProfile().getName());
			h.applyCount.setText(acty.getNumUsership() + "人报名");
			h.status.setImageResource(acty.getStatus().getId() == 1 ? R.drawable.ic_image_status_on
					: R.drawable.ic_image_status_off);
			// h.favourite.setText(data.get(position).getFavourite()+"");
			if (acty.getAvatar() != null) {
				ImageLoader.getInstance().displayImage(
						RestClient.BASE_URL + acty.getAvatar(), h.avater);
			} else {
				ImageLoader.getInstance().displayImage(
						"drawable://" + R.drawable.ic_image_load_normal,
						h.avater);
			}
			return convertView;
		}

		class ViewHolder {
			ImageView avater, status;
			TextView actyName, site, starttime, ownername, applyCount;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position - 1 == -1)
			return;
		viewClicked = view;
		item_click = position - 1;
		Intent intent = new Intent(this, ActivitiesInfo.class);
		if (parent == lv_all) {
			intent.putExtra("activity", data_all.get(position - 1));
			data_clicked = data_all;
		}
		if (parent == lv_myjoin) {
			intent.putExtra("activity", data_myjoin.get(position - 1));
			data_clicked = data_myjoin;
		}
		if (parent == lv_favourit) {
			intent.putExtra("activity", data_favourite.get(position - 1));
			data_clicked = data_favourite;

		}
		openActivity(intent);
	}

}
