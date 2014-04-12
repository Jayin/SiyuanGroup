package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.adapter.FootOnPageChangelistener;
import com.alumnigroup.api.GroupAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.AppCache;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.MGroup;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.widget.XListView;
import com.alumnigroup.widget.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 圈子页面
 * 
 * @author Jayin Ton
 * 
 */
public class Group extends BaseActivity implements OnItemClickListener {
	private List<View> btns = new ArrayList<View>();
	private View btn_back, btn_all, btn_myjoin, btn_more;
	private XListView lv_all, lv_myjoin;
	private ViewPager viewpager;
	private ArrayList<MGroup> data_all, data_myjoin;
	private GroupAdapter adapter_all, adapter_myjoin;
	private int page_all = 0, page_myjoin = 0;
	private GroupAPI api;
	private PopupWindow mPopupWindow;
	
    private ArrayList<MGroup> data_clicked = null;
	private int item_click =-1;
	private View viewClicked = null;// 当前点击的item(View),用来更新item用的
	private BroadcastReceiver mReceiver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_group);
		initData();
		initLayout();
		initController();
		openReceiver();
	}

	// 修改圈子分享 刷新UI,包括数据修改
	private void openReceiver() {
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				MGroup g = (MGroup) intent.getSerializableExtra("group");
				data_clicked.set(item_click, g);
				((TextView) viewClicked
						.findViewById(R.id.item_lv_acty_group_tv_ame))
						.setText(g.getName());
				((TextView) viewClicked
						.findViewById(R.id.item_lv_acty_group_tv_ownername))
						.setText(g.getOwner().getProfile().getName());
				((TextView) viewClicked
						.findViewById(R.id.item_lv_acty_group_tv_memberCount))
						.setText(g.getNumMembers()+"");
				((TextView) viewClicked
						.findViewById(R.id.item_lv_acty_group_tv_description))
						.setText(g.getDescription());
				ImageView iv_avatar = (ImageView) viewClicked
						.findViewById(R.id.item_lv_acty_circlelist_iv_avater);
				if (g.getAvatar() != null) {
					ImageLoader.getInstance().displayImage(
							RestClient.BASE_URL + g.getAvatar(), iv_avatar);
				} else {
					ImageLoader.getInstance().displayImage(
							"drawable://" + R.drawable.ic_img_group_defalut,
							iv_avatar);
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action_GroupInfo_Edit);
		registerReceiver(mReceiver, filter);
	}

	private void initController() {
		lv_all.setPullRefreshEnable(true);
		lv_all.setPullLoadEnable(true);
		lv_myjoin.setPullRefreshEnable(true);
		lv_myjoin.setPullLoadEnable(true);
		lv_all.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				api.getGroupList(1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<MGroup> newData_all = MGroup
								.create_by_jsonarray(obj.toString());
						if (newData_all == null) {
							toast("网络异常 解析错误");
						} else if (newData_all.size() == 0) {
							toast("还没有圈子");
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
							AppCache.setGroupAll(getContext(), data_all);
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
				api.getGroupList(page_all + 1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<MGroup> newData_all = MGroup
								.create_by_jsonarray(obj.toString());
						if (newData_all == null) {
							toast("网络异常,解析错误");
						} else if (newData_all.size() == 0) {
							toast("没有更多了!");
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
				api.getMyGroupList(1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<MGroup> newData_my = MGroup
								.create_by_jsonarray(obj.toString());
						if (newData_my == null) {
							toast("网络异常 解析错误");
						} else if (newData_my.size() == 0) {
							toast("你还没有加入任何圈子");
							lv_myjoin.setPullLoadEnable(false);
						} else {
							page_myjoin = 1;
							data_myjoin.clear();
							data_myjoin.addAll(newData_my);
							adapter_myjoin.notifyDataSetChanged();
							if (data_myjoin.size() < 10) {
								lv_myjoin.setPullLoadEnable(false);
							} else {
								lv_myjoin.setPullLoadEnable(true);
							}
							AppCache.setGroupMy(getContext(), data_myjoin);
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
				api.getMyGroupList(page_myjoin + 1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<MGroup> newData_my = MGroup
								.create_by_jsonarray(obj.toString());
						if (newData_my == null) {
							toast("网络异常,解析错误");
						} else if (newData_my.size() == 0) {
							toast("没有更多了!");
							lv_myjoin.setPullLoadEnable(false);
						} else {
							page_myjoin++;
							data_myjoin.addAll(newData_my);
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

		lv_all.setOnItemClickListener(this);
		lv_myjoin.setOnItemClickListener(this);

		lv_all.startRefresh();
	}

	private void initViewPager() {
		viewpager = (ViewPager) _getView(R.id.acty_group_content);
		View all = getLayoutInflater().inflate(R.layout.frame_acty_group, null);
		View myjoin = getLayoutInflater().inflate(R.layout.frame_acty_group,
				null);

		lv_all = (XListView) all.findViewById(R.id.frame_acty_group_listview);
		lv_myjoin = (XListView) myjoin
				.findViewById(R.id.frame_acty_group_listview);

		adapter_all = new GroupAdapter(data_all);
		adapter_myjoin = new GroupAdapter(data_myjoin);

		lv_all.setAdapter(adapter_all);
		lv_myjoin.setAdapter(adapter_myjoin);

		List<View> views = new ArrayList<View>();
		views.add(all);
		views.add(myjoin);

		List<XListView> listviews = new ArrayList<XListView>();
		listviews.add(lv_all);
		listviews.add(lv_myjoin);

		List<GroupAdapter> adapters = new ArrayList<GroupAdapter>();
		adapters.add(adapter_all);
		adapters.add(adapter_myjoin);

		viewpager.setAdapter(new BaseViewPagerAdapter(views));
		viewpager.setOnPageChangeListener(new FootOnPageChangelistener(btns,
				listviews, adapters));
	}

	@Override
	protected void initData() {
		api = new GroupAPI();
		if(AppCache.getGroupAll(getContext())!=null){
			data_all =AppCache.getGroupAll(getContext());
		}else{
			data_all = new ArrayList<MGroup>();
		}
		if(AppCache.getGroupMy(getContext())!=null){
			data_myjoin = AppCache.getGroupMy(getContext());
		}else{
			data_myjoin = new ArrayList<MGroup>();
		}
	}

	@Override
	protected void initLayout() {
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_all = _getView(R.id.acty_group_footer_all);
		btn_myjoin = _getView(R.id.acty_group_footer_myjoin);
		btn_more = _getView(R.id.acty_head_btn_more);

		btns.add(btn_all);
		btns.add(btn_myjoin);

		btn_back.setOnClickListener(this);
		btn_all.setOnClickListener(this);
		btn_myjoin.setOnClickListener(this);
		btn_more.setOnClickListener(this);

		initViewPager();
		initPopupWindow();
	}

	private void initPopupWindow() {
		View view = getLayoutInflater()
				.inflate(R.layout.popup_acty_group, null);
		View btn_search = view.findViewById(R.id.search), btn_create = view
				.findViewById(R.id.create);
		btn_search.setOnClickListener(this);
		btn_create.setOnClickListener(this);
		mPopupWindow = new PopupWindow(view);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setOutsideTouchable(true);

		// 控制popupwindow的宽度和高度自适应
		view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		mPopupWindow.setWidth(view.getMeasuredWidth());
		mPopupWindow.setHeight(view.getMeasuredHeight());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_head_btn_more:
			mPopupWindow.showAsDropDown(btn_more);
			break;
		case R.id.acty_group_footer_all:
			if (viewpager.getCurrentItem() == 0) {
				lv_all.startRefresh();
			} else {
				viewpager.setCurrentItem(0, true);
			}
			break;
		case R.id.acty_group_footer_myjoin:
			if (viewpager.getCurrentItem() == 1) {
				lv_myjoin.startRefresh();
			} else {
				viewpager.setCurrentItem(1, true);
			}
			break;
		case R.id.search:
			mPopupWindow.dismiss();
			break;
		case R.id.create:
			openActivity(GroupCreate.class);
			mPopupWindow.dismiss();
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
		item_click = position-1;
		Intent intent = new Intent(this, GroupInfo.class);
		if (parent == lv_all) {
			intent.putExtra("group", data_all.get(position - 1));
			data_clicked = data_all;
		} else {
			intent.putExtra("group", data_myjoin.get(position - 1));
			data_clicked = data_myjoin;
		}
		openActivity(intent);
	}

	class GroupAdapter extends BaseAdapter {
		private List<MGroup> data;

		public GroupAdapter(List<MGroup> data) {
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
			ViewHodler h;
			if (convertView == null) {
				h = new ViewHodler();
				convertView = getLayoutInflater().inflate(
						R.layout.item_lv_acty_group, null);
				h.name = (TextView) convertView
						.findViewById(R.id.item_lv_acty_group_tv_ame);
				h.username = (TextView) convertView
						.findViewById(R.id.item_lv_acty_group_tv_ownername);
				h.memberCount = (TextView) convertView
						.findViewById(R.id.item_lv_acty_group_tv_memberCount);
				h.description = (TextView) convertView
						.findViewById(R.id.item_lv_acty_group_tv_description);
				h.avatar = (ImageView) convertView
						.findViewById(R.id.item_lv_acty_circlelist_iv_avater);
				convertView.setTag(h);
			} else {
				h = (ViewHodler) convertView.getTag();
			}
			MGroup group = data.get(position);
			h.name.setText(group.getName());
			h.username.setText(group.getOwner().getProfile().getName());
			h.memberCount.setText(group.getNumMembers() + "名会员");
			h.description.setText(group.getDescription());
			if (group.getAvatar() != null) {
				ImageLoader.getInstance().displayImage(
						RestClient.BASE_URL + group.getAvatar(), h.avatar);
			} else {
				ImageLoader.getInstance().displayImage(
						"drawable://" + R.drawable.ic_img_group_defalut,
						h.avatar);
			}

			return convertView;
		}

		class ViewHodler {
			TextView name, username, memberCount, description;
			ImageView avatar;
		}
	}

}
