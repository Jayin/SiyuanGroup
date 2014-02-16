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
import com.alumnigroup.api.BusinessAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.api.StarAPI;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Cooperation;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.MActivity;
import com.alumnigroup.entity.Starring;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.L;
import com.alumnigroup.widget.XListView;
import com.alumnigroup.widget.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 商务合作页面
 * 
 * @author Jayin Ton
 * 
 */
public class Business extends BaseActivity implements OnItemClickListener {
	private List<View> btns = new ArrayList<View>();
	private View btn_back, btn_all, btn_favourite, btn_myjoin, btn_compose;
	private ViewPager viewpager;
	private List<Cooperation> data_all, data_myjoin, data_favourite;
	private BusinessAdapter adapter_all, adapter_myjoin, adapter_favourite;
	private int page_all = 0, page_myjoin = 0, page_favourit = 0;//可能因为网络原因没有加载到第一页
	private BusinessAPI api;
	private User user;
	private XListView lv_myjoin,lv_all,  lv_favourit;
	private StarAPI starAPI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_business);
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
				api.getCooperationList(1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<Cooperation> newData_all = Cooperation
								.create_by_jsonarray(obj.toString());
						if (newData_all == null) {
							toast("网络异常，解析错误");
						} else if (newData_all.size() == 0) {
							toast("没有更多");
						} else {
							page_all = 1;
							data_all.clear();
							data_all.addAll(newData_all);
							adapter_all.notifyDataSetChanged();
						}
						lv_all.stopRefresh();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("网络异常  错误码:" + errorCode);
						lv_all.stopRefresh();
					}
				});
				
			}
			
			@Override
			public void onLoadMore() {
				if(page_all==0){
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
							toast("没有更多了");
						} else {
							page_all++;
							data_all.addAll(newData_all);
							adapter_all.notifyDataSetChanged();
						}
						lv_all.stopLoadMore();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("网络异常  错误码:" + errorCode);
						lv_all.stopLoadMore();
					}
				});
				
			}
		});
		// 搜索我发布的
		lv_myjoin.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				api.search(1, user.getId(), null, null,
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<Cooperation> newData_myjoin = Cooperation
										.create_by_jsonarray(obj.toString());
								if (newData_myjoin == null) {
									toast("网络异常，解析错误");
								} else if (newData_myjoin.size() == 0) {
									toast("没有更多");
								} else {
									page_myjoin = 1;
									data_myjoin.clear();
									data_myjoin.addAll(newData_myjoin);
									adapter_myjoin.notifyDataSetChanged();
								}
								lv_myjoin.stopRefresh();

							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast("网络异常  错误码:" + errorCode);
								lv_myjoin.stopRefresh();
							}
						});
				
			}
			
			@Override
			public void onLoadMore() {
				if(page_myjoin==0){
					lv_myjoin.startRefresh();
					lv_myjoin.stopLoadMore();
					return;
				}
				api.search(page_myjoin + 1, user.getId(), null, null,
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<Cooperation> newData_myjoin = Cooperation
										.create_by_jsonarray(obj.toString());
								if (newData_myjoin == null) {
									toast("网络异常，解析错误");
								} else if (newData_myjoin.size() == 0) {
									toast("没有更多了");
								} else {
									page_myjoin++;
									data_myjoin.addAll(newData_myjoin);
									adapter_myjoin.notifyDataSetChanged();
								}
								lv_myjoin.stopLoadMore();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast("网络异常  错误码:" + errorCode);
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
										toast("没有更多");
									} else {
										page_favourit = 1;
										data_favourite.clear();
										data_favourite.addAll(newData_faviour);
										adapter_favourite
												.notifyDataSetChanged();
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
		
		lv_all.startRefresh();
	}

	@Override
	protected void initData() {
		user = AppInfo.getUser(getContext());
		if (user == null) {
			toast("无用户信息，请登录");
		}
		api = new BusinessAPI();
		starAPI = new StarAPI();
		data_all = new ArrayList<Cooperation>();
		data_myjoin = new ArrayList<Cooperation>();
		data_favourite = new ArrayList<Cooperation>();

	}

	private void initViewPager() {
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
		viewpager.setAdapter(new BaseViewPagerAdapter(views));
		viewpager.setOnPageChangeListener(new BaseOnPageChangeListener(btns));
	}

	@Override
	protected void initLayout() {
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_all = _getView(R.id.acty_business_footer_all);
		btn_myjoin = _getView(R.id.acty_business_footer_myjoin);
		btn_favourite = _getView(R.id.acty_business_footer_favourite);
		btn_compose = _getView(R.id.acty_head_btn_compose);

		btns.add(btn_all);
		btns.add(btn_myjoin);
		btns.add(btn_favourite);

		btn_back.setOnClickListener(this);
		btn_all.setOnClickListener(this);
		btn_myjoin.setOnClickListener(this);
		btn_favourite.setOnClickListener(this);
		btn_compose.setOnClickListener(this);

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
		case R.id.acty_business_footer_all:
			viewpager.setCurrentItem(0, true);
			break;
		case R.id.acty_business_footer_myjoin:
			viewpager.setCurrentItem(1, true);
			break;
		case R.id.acty_business_footer_favourite:
			viewpager.setCurrentItem(2, true);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int real_position = position - 1;
		
		toast("position-->"+position);
		L.i("position-->"+position);
		if (parent == lv_all) {
			Intent intent = new Intent(this, BusinessDetail.class);
			intent.putExtra("cooperation", data_all.get(real_position));
			openActivity(intent);
		}
		if (parent == lv_favourit) {
			Intent intent = new Intent(this, BusinessDetail.class);
			intent.putExtra("cooperation", data_myjoin.get(real_position));
			openActivity(intent);
		}
		if (parent == lv_myjoin) {

		}
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
				h.deadline = (TextView) convertView
						.findViewById(R.id.tv_deadline);
				h.major = (TextView) convertView.findViewById(R.id.tv_major);
				h.name_user = (TextView) convertView
						.findViewById(R.id.tv_username);
				h.description = (TextView) convertView
						.findViewById(R.id.tv_description);
				h.numFavour = (TextView) convertView
						.findViewById(R.id.tv_numFavour);
				h.commentNum = (TextView) convertView
						.findViewById(R.id.tv_numComment);
				h.avatar = (ImageView) convertView.findViewById(R.id.iv_avater);
				h.status = (ImageView) convertView.findViewById(R.id.iv_status);
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}
			Cooperation c = data.get(position);
			h.name_project.setText(c.getName());
			h.deadline.setText(CalendarUtils.getTimeFromat(c.getRegdeadline(),
					CalendarUtils.TYPE_TWO));
			h.major.setText(c.getUser().getProfile().getMajor());
			h.name_user.setText(c.getUser().getProfile().getName());
			h.description.setText(c.getDescription());
			h.numFavour.setText(14 + ""); // should change
			h.commentNum.setText(c.getNumComments() + "");
            if(c.getUser().getAvatar()!=null){
            	ImageLoader.getInstance().displayImage(
    					RestClient.BASE_URL + c.getUser().getAvatar(), h.avatar);
            }else{
            	ImageLoader.getInstance().displayImage(
    					"drawable://" + R.drawable.ic_image_load_normal, h.avatar);
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
			return convertView;
		}

		class ViewHolder {
			TextView name_project, deadline, major, name_user, description,
					numFavour, commentNum;
			ImageView avatar, status;
		}

	}

}
