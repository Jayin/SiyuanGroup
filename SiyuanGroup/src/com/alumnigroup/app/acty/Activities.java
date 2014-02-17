package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.adapter.BaseOnPageChangeListener;
import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.api.ActivityAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.api.StarAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.MActivity;
import com.alumnigroup.entity.Starring;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.DataPool;
import com.alumnigroup.utils.L;
import com.alumnigroup.widget.PullAndLoadListView.OnLoadMoreListener;
import com.alumnigroup.widget.PullToRefreshListView.OnRefreshListener;
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
	private List<MActivity> data_all, data_myjoin, data_favourite;
	private ActivitiesAdapter adapter_all, adapter_myjoin, adapter_favourite;
	private int page_all = 0, page_myjoin = 0, page_favourit = 0;
	private ActivityAPI api;
	private StarAPI starAPI;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_activities);
		initData();
		initLayout();
		initController();
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
						List<MActivity> newData_all = MActivity
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
							lv_all.setPullLoadEnable(true);
						}
						lv_all.stopRefresh();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("网络异常 " + ErrorCode.errorList.get(errorCode));
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
						toast("网络异常 " + ErrorCode.errorList.get(errorCode));
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
							lv_myjoin.setPullLoadEnable(true);
						}
						lv_myjoin.stopRefresh();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("网络异常 " + ErrorCode.errorList.get(errorCode));
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
						toast("网络异常 " + ErrorCode.errorList.get(errorCode));
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
										lv_favourit.setPullLoadEnable(true);
									}
								}
								lv_favourit.stopRefresh();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast("网络异常 "
										+ ErrorCode.errorList.get(errorCode));
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
								toast("网络异常 "
										+ ErrorCode.errorList.get(errorCode));
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
			L.i("muser is null");
			toast("lol");
		}

		data_all = new ArrayList<MActivity>();
		data_myjoin = new ArrayList<MActivity>();
		data_favourite = new ArrayList<MActivity>();
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
		viewpager.setAdapter(new BaseViewPagerAdapter(views));
		viewpager.setOnPageChangeListener(new BaseOnPageChangeListener(btns));
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
			viewpager.setCurrentItem(0, true);
			break;
		case R.id.acty_activities_footer_myjoin:
			viewpager.setCurrentItem(1, true);
			break;
		case R.id.acty_activities_footer_favourite:
			viewpager.setCurrentItem(2, true);
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
			h.ownername.setText("name" + acty.getOwnerid());
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
		if (parent == lv_all) {
			Intent intent = new Intent(this, ActivitiesInfo.class);
			intent.putExtra("activity", data_all.get(position - 1));
			openActivity(intent);
		}
		if (parent == lv_myjoin) {

		}
		if (parent == lv_favourit) {

		}
	}

}
