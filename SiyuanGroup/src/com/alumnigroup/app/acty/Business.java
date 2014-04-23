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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.adapter.MyOnPageChangeListener;
import com.alumnigroup.api.BusinessAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.api.StarAPI;
import com.alumnigroup.app.AppCache;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Cooperation;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.MPicture;
import com.alumnigroup.entity.Starring;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.widget.XListView;
import com.alumnigroup.widget.XListView.IXListViewListener;
import com.astuetz.PagerSlidingTabStrip;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 商务合作页面
 * 
 * @author Jayin Ton
 * 
 */
public class Business extends BaseActivity implements OnItemClickListener {
	PagerSlidingTabStrip tabs;
	private String[] titles ;
	private ViewPager viewpager;
	private ArrayList<Cooperation> data_all, data_myjoin, data_favourite;
	private BusinessAdapter adapter_all, adapter_myjoin, adapter_favourite;
	private int page_all = 0, page_myjoin = 0, page_favourit = 0;// 可能因为网络原因没有加载到第一页
	private BusinessAPI api;
	private User user;
	private XListView lv_myjoin, lv_all, lv_favourit;
	private StarAPI starAPI;

	private ArrayList<Cooperation> data_clicked = null;
	private int item_click = -1;
	private View viewClicked = null;// 当前点击的item(View),用来更新item用的
	private BroadcastReceiver mReceiver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_business);
		initData();
		initLayout();
		initController();
		openReceiver();
	}

	private void openReceiver() {
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(Constants.Action_Bussiness_Edit)) {

					Cooperation c = (Cooperation) intent
							.getSerializableExtra("cooperation");
					data_clicked.set(item_click, c);
					((TextView) viewClicked.findViewById(R.id.tv_projectname))
							.setText(c.getName());
					((TextView) viewClicked.findViewById(R.id.tv_createtime))
							.setText(CalendarUtils.getTimeFromat(
									c.getCreatetime(), CalendarUtils.TYPE_TWO));
					((TextView) viewClicked.findViewById(R.id.tv_major))
							.setText(c.getUser().getProfile().getMajor());
					((TextView) viewClicked.findViewById(R.id.tv_username))
							.setText(c.getUser().getProfile().getName());
					((TextView) viewClicked.findViewById(R.id.tv_description))
							.setText(c.getDescription());
					((TextView) viewClicked.findViewById(R.id.tv_numComment))
							.setText(c.getNumComments() + "");

					ImageView iv_avatar = (ImageView) viewClicked
							.findViewById(R.id.iv_avater);
					ImageView iv_status = (ImageView) viewClicked
							.findViewById(R.id.iv_status);
					if (c.getUser().getAvatar() != null) {
						ImageLoader.getInstance().displayImage(
								RestClient.BASE_URL + c.getUser().getAvatar(),
								iv_avatar);
					} else {
						ImageLoader
								.getInstance()
								.displayImage(
										"drawable://"
												+ R.drawable.ic_image_load_normal,
										iv_avatar);
					}

					if (c.getStatusid() != 2) {
						ImageLoader.getInstance().displayImage(
								"drawable://" + R.drawable.ic_image_status_on,
								iv_status);
					} else {
						ImageLoader.getInstance().displayImage(
								"drawable://" + R.drawable.ic_image_status_off,
								iv_status);
					}
				}else if(intent.getAction().equals(Constants.Action_Bussiness_unfavourite)){
					lv_favourit.startRefresh();
				}

			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action_Bussiness_Edit);
		filter.addAction(Constants.Action_Bussiness_unfavourite);
		registerReceiver(mReceiver, filter);
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
				api.getCooperationList(1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<Cooperation> newData_all = Cooperation
								.create_by_jsonarray(obj.toString());
						if (newData_all == null) {
							toast("网络异常，解析错误");
						} else if (newData_all.size() == 0) {
							toast("还没有人发布项目");
							data_all.clear();
							adapter_all.notifyDataSetChanged();
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
							AppCache.setBusinessAll(getContext(), data_all);
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
					lv_all.startRefresh();
					lv_all.stopLoadMore();
					return;
				}
				api.getCooperationList(page_all + 1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<Cooperation> newData_all = Cooperation
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
		// 搜索我发布的
		lv_myjoin.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				api.getMyCooperationList(1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<Cooperation> newData_myjoin = Cooperation
								.create_by_jsonarray(obj.toString());
						if (newData_myjoin == null) {
							toast("网络异常，解析错误");
						} else if (newData_myjoin.size() == 0) {
							toast("你还没有发布任何项目");
							data_myjoin.clear();
							adapter_myjoin.notifyDataSetChanged();
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
							AppCache.setBusinessMy(getContext(), data_myjoin);
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
					lv_myjoin.startRefresh();
					lv_myjoin.stopLoadMore();
					return;
				}
				api.getMyCooperationList(page_myjoin + 1,
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<Cooperation> newData_myjoin = Cooperation
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
				starAPI.getMyStarList(1, StarAPI.Item_type_business,
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<Starring> stars = Starring
										.create_by_jsonarray(obj.toString());
								List<Cooperation> newData_faviour = new ArrayList<Cooperation>();
								if (stars == null) {
									toast("网络异常，解析错误");
								} else {
									for (Starring s : stars) {
										newData_faviour.add((Cooperation) s
												.getItem());
									}
									if (newData_faviour.size() == 0) {
										toast("你还没有收藏任何项目");
										data_favourite.clear();
										adapter_favourite.notifyDataSetChanged();
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
										AppCache.setBusinessFavourite(
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
						StarAPI.Item_type_business, new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<Starring> stars = Starring
										.create_by_jsonarray(obj.toString());
								List<Cooperation> newData_faviour = new ArrayList<Cooperation>();
								if (stars == null) {
									toast("网络异常，解析错误");
								} else {
									for (Starring s : stars) {
										newData_faviour.add((Cooperation) s
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

		lv_all.startRefresh();
	}

	@Override
	protected void initData() {
		titles = getResources().getStringArray(R.array.title_business);
		user = AppInfo.getUser(getContext());
		if (user == null) {
			toast("无用户信息，请登录");
		}
		api = new BusinessAPI();
		starAPI = new StarAPI();
		if (AppCache.getBusinessAll(getContext()) != null) {
			data_all = AppCache.getBusinessAll(getContext());
		} else {
			data_all = new ArrayList<Cooperation>();
		}
		if (AppCache.getBusinessMy(getContext()) != null) {
			data_myjoin = AppCache.getBusinessMy(getContext());
		} else {
			data_myjoin = new ArrayList<Cooperation>();
		}
		if (AppCache.getBusinessFavourite(getContext()) != null) {
			data_favourite = AppCache.getBusinessFavourite(getContext());
		} else {
			data_favourite = new ArrayList<Cooperation>();
		}
	}

	private void initViewPager() {
		tabs = (PagerSlidingTabStrip)_getView(R.id.tabs);
		viewpager = (ViewPager) _getView(R.id.acty_business_content);
		View all = getLayoutInflater().inflate(R.layout.frame_acty_business,
				null);
		View myjoin = getLayoutInflater().inflate(R.layout.frame_acty_business,
				null);
		View favourit = getLayoutInflater().inflate(
				R.layout.frame_acty_business, null);
		lv_all = (XListView) all
				.findViewById(R.id.frame_acty_business_listview);
		lv_myjoin = (XListView) myjoin
				.findViewById(R.id.frame_acty_business_listview);
		lv_favourit = (XListView) favourit
				.findViewById(R.id.frame_acty_business_listview);
		lv_all.setOnItemClickListener(this);
		lv_myjoin.setOnItemClickListener(this);
		lv_favourit.setOnItemClickListener(this);

		adapter_all = new BusinessAdapter(data_all);
		adapter_myjoin = new BusinessAdapter(data_myjoin);
		adapter_favourite = new BusinessAdapter(data_favourite);

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

		viewpager.setAdapter(new BaseViewPagerAdapter(views,titles));
		tabs.setViewPager(viewpager);
		tabs.setOnPageChangeListener(new MyOnPageChangeListener(listviews));
		
	}

	@Override
	protected void initLayout() {
		_getView(R.id.acty_head_btn_compose).setOnClickListener(this);
		_getView(R.id.acty_head_btn_back).setOnClickListener(this);
		initViewPager();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_head_btn_compose:
			openActivity(BusinessPublish.class);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position - 1 == -1)
			return;
		viewClicked = view;
		item_click = position - 1;
		Intent intent = new Intent(this, BusinessDetail.class);
		if (parent == lv_all) {
			intent.putExtra("cooperation", data_all.get(position - 1));
			data_clicked = data_all;
		}
		if (parent == lv_myjoin) {
			intent.putExtra("cooperation", data_myjoin.get(position - 1));
			data_clicked = data_myjoin;
		}
		if (parent == lv_favourit) {
			intent.putExtra("cooperation", data_favourite.get(position - 1));
			data_clicked = data_favourite;
			intent.putExtra("isFavourite", 1);
		}
		openActivity(intent);
	}

	class BusinessAdapter extends BaseAdapter {
		private List<Cooperation> data;

		public BusinessAdapter(List<Cooperation> data) {
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
						R.layout.item_lv_acty_business, null);
				h.name_project = (TextView) convertView
						.findViewById(R.id.tv_projectname);
				h.createtime = (TextView) convertView
						.findViewById(R.id.tv_createtime);
				h.major = (TextView) convertView.findViewById(R.id.tv_major);
				h.name_user = (TextView) convertView
						.findViewById(R.id.tv_username);
				h.description = (TextView) convertView
						.findViewById(R.id.tv_description);
				h.commentNum = (TextView) convertView
						.findViewById(R.id.tv_numComment);
				h.avatar = (ImageView) convertView.findViewById(R.id.iv_avater);
				h.status = (ImageView) convertView.findViewById(R.id.iv_status);
				h.pic1 = (ImageView) convertView.findViewById(R.id.iv_pic1);
				h.pic2 = (ImageView) convertView.findViewById(R.id.iv_pic2);
				h.pic3 = (ImageView) convertView.findViewById(R.id.iv_pic3);
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}
			Cooperation c = data.get(position);
			h.name_project.setText(c.getName());
			h.createtime.setText(CalendarUtils.getTimeFromat(c.getCreatetime(),
					CalendarUtils.TYPE_TWO));
			h.major.setText(c.getUser().getProfile().getMajor());
			h.name_user.setText(c.getUser().getProfile().getName());
			h.description.setText(c.getDescription());
			h.commentNum.setText(c.getNumComments() + "");
			if (c.getUser().getAvatar() != null) {
				ImageLoader.getInstance()
						.displayImage(
								RestClient.BASE_URL + c.getUser().getAvatar(),
								h.avatar);
			} else {
				ImageLoader.getInstance().displayImage(
						"drawable://" + R.drawable.ic_img_avatar_default,
						h.avatar);
			}

			if (c.getStatusid() != 2) {
				ImageLoader.getInstance()
						.displayImage(
								"drawable://" + R.drawable.ic_image_status_on,
								h.status);
			} else {
				ImageLoader.getInstance().displayImage(
						"drawable://" + R.drawable.ic_image_status_off,
						h.status);
			}

			// 暂时1张图片
			h.pic1.setVisibility(View.GONE);
			h.pic2.setVisibility(View.GONE);
			h.pic3.setVisibility(View.GONE);
			if (c.getPictures() != null && c.getPictures().size() > 0) {
				h.pic1.setVisibility(View.VISIBLE);
				for (int i = 0; i < c.getNumPictures(); i++) {
					final MPicture pic = c.getPictures().get(i);
					switch (i) {
					case 0:
						h.pic1.setVisibility(View.VISIBLE);
						ImageLoader.getInstance().displayImage(
								RestClient.BASE_URL + pic.getPath(), h.pic1);

						h.pic1.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(getContext(),
										ImageDisplay.class);
								intent.putExtra("url", RestClient.BASE_URL
										+ pic.getPath());
								openActivity(intent);
							}
						});
						break;
					case 1:
						h.pic2.setVisibility(View.VISIBLE);
						ImageLoader.getInstance().displayImage(
								RestClient.BASE_URL
										+ c.getPictures().get(i).getPath(),
								h.pic2);

						h.pic2.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(getContext(),
										ImageDisplay.class);
								intent.putExtra("url", RestClient.BASE_URL
										+ pic.getPath());
								openActivity(intent);
							}
						});
						break;
					case 2:
						h.pic3.setVisibility(View.VISIBLE);
						ImageLoader.getInstance().displayImage(
								RestClient.BASE_URL
										+ c.getPictures().get(i).getPath(),
								h.pic3);

						h.pic3.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(getContext(),
										ImageDisplay.class);
								intent.putExtra("url", RestClient.BASE_URL
										+ pic.getPath());
								openActivity(intent);
							}
						});
						break;
					default:
						break;
					}
				}
			}
			return convertView;
		}

		class ViewHolder {
			TextView name_project, createtime, major, name_user, description,
					commentNum;
			ImageView avatar, status, pic1, pic2, pic3;
		}

	}

}
