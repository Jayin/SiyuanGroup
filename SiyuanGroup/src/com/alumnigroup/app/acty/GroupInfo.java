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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.adapter.FootOnPageChangelistener;
import com.alumnigroup.adapter.IssueAdapter;
import com.alumnigroup.adapter.MemberAdapter;
import com.alumnigroup.api.GroupAPI;
import com.alumnigroup.api.GroupShareAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.app.SyncData;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.Issue;
import com.alumnigroup.entity.MGroup;
import com.alumnigroup.entity.MMemberships;
import com.alumnigroup.entity.MPicture;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.utils.DataPool;
import com.alumnigroup.utils.L;
import com.alumnigroup.widget.XListView;
import com.alumnigroup.widget.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 圈子列表
 * 
 * @author Jayin Ton
 * 
 */
public class GroupInfo extends BaseActivity {
	private MGroup group;
	private View btn_back, btn_info, btn_member, btn_share, btn_more;
	private TextView tv_owner, tv_numMember, tv_description, tv_groupName;
	private ImageView iv_avatar;
	private User user;
	private ViewPager viewpager;
	private List<View> btns = new ArrayList<View>();
	private GroupAPI api;
	private List<User> data_user;
	private MemberAdapter adapter_member;
	private PopupWindow mPopupWindow;
	private XListView lv_share, lv_member;
	private IssueAdapter adapter_share;
	private int page_share = 0, page_member = 0;
	private List<Issue> data_share;
	private GroupShareAPI shareAPI;
	private BroadcastReceiver mReceiver = null;

	private int item_click = -1;
	private View viewClicked = null;// 当前点击的item(View),用来更新item用的

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_groupinfo);
		initData();
		initPopupWindow();
		initLayout();
		initController();
		openReceiver();
	}

	// 修改圈子分享 刷新UI
	private void openReceiver() {
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(Constants.Action_GroupInfo_Edit)) {
					group = (MGroup) intent.getSerializableExtra("group");
					fillInData();
				} else if (intent.getAction().equals(
						Constants.Action_Issue_Edit)) { // 更新话题修改
					Issue issue = (Issue) intent.getSerializableExtra("issue");
					SyncData.updateEditIssue(data_share, adapter_share, issue);
				}else if(intent.getAction().equals(Constants.Action_Issue_delete)){
					Issue deleteItem = (Issue)intent.getSerializableExtra("issue");
					SyncData.updateDeleteIssue(data_share, adapter_share, deleteItem);
				}

			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action_GroupInfo_Edit);
		filter.addAction(Constants.Action_Issue_Edit);
		filter.addAction(Constants.Action_Issue_delete);
		registerReceiver(mReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
	}

	private void initPopupWindow() {
		View view = getLayoutInflater().inflate(R.layout.popup_acty_groupinfo,
				null);

		(view.findViewById(R.id.btn_manage)).setOnClickListener(this);
		(view.findViewById(R.id.btn_join)).setOnClickListener(this);
		(view.findViewById(R.id.btn_exit)).setOnClickListener(this);
		(view.findViewById(R.id.btn_createActivity)).setOnClickListener(this);
		(view.findViewById(R.id.btn_share)).setOnClickListener(this);
		mPopupWindow = new PopupWindow(view);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setOutsideTouchable(true);

		// 控制popupwindow的宽度和高度自适应
		view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		mPopupWindow.setWidth(view.getMeasuredWidth());
		mPopupWindow.setHeight(view.getMeasuredHeight());

	}

	private void initController() {
		lv_member.setAdapter(adapter_member);
		lv_share.setAdapter(adapter_share);

		lv_member.setPullRefreshEnable(true);
		lv_member.setPullLoadEnable(false);// 一次性加载
		lv_share.setPullRefreshEnable(true);
		lv_share.setPullLoadEnable(true);

		lv_member.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				api.getMembersAccepted(1, group.getId(),
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<MMemberships> memberships = MMemberships
										.create_by_jsonarray(obj.toString());
								List<User> newData = new ArrayList<User>();
								if (memberships == null) {
									toast("网络异常  解析错误");
								} else {
									for (MMemberships mm : memberships) {
										newData.add(mm.getUser());
									}
									if (newData.size() == 0) {
										toast("还没有会员");
									} else {
										page_member = 1;
										data_user.clear();
										data_user.addAll(newData);
										adapter_member.notifyDataSetChanged();
									}
								}
								lv_member.stopRefresh();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast(ErrorCode.errorList.get(errorCode));
								lv_member.stopRefresh();

							}
						});

			}

			@Override
			public void onLoadMore() {
				if (page_member == 0) {
					lv_member.stopLoadMore();
					lv_member.startRefresh();
					return;
				}
				api.getMembersAccepted(page_member, group.getId(),
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<MMemberships> memberships = MMemberships
										.create_by_jsonarray(obj.toString());
								List<User> newData = new ArrayList<User>();
								if (memberships == null) {
									toast("网络异常  解析错误");
								} else {
									for (MMemberships mm : memberships) {
										newData.add(mm.getUser());
									}
									if (newData.size() == 0) {
										toast("没有更多了");
										lv_member.setPullLoadEnable(false);
									} else {
										page_member++;
										data_user.clear();
										data_user.addAll(newData);
										adapter_member.notifyDataSetChanged();
									}
								}
								lv_member.stopLoadMore();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast(ErrorCode.errorList.get(errorCode));
								lv_member.stopLoadMore();

							}
						});

			}
		});

		lv_share.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				shareAPI.getShareList(1, group.getId(),
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<Issue> newData_share = Issue
										.create_by_jsonarray(obj.toString());
								if (newData_share == null) {
									toast("网络异常 解析错误");
								} else if (newData_share.size() == 0) {
									toast("还没有讨论");
									lv_share.setPullLoadEnable(false);
								} else {
									page_share = 1;
									data_share.clear();
									data_share.addAll(newData_share);
									adapter_share.notifyDataSetChanged();
									if(newData_share.size()<10){
										lv_share.setPullLoadEnable(false);
									}else{
										lv_share.setPullLoadEnable(true);
									}
									
								}
								lv_share.stopRefresh();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast(ErrorCode.errorList.get(errorCode));
								lv_share.stopRefresh();
							}
						});
			}

			@Override
			public void onLoadMore() {
				if (page_share == 0) {
					lv_share.stopLoadMore();
					lv_share.startRefresh();
					return;
				}
				shareAPI.getShareList(page_share + 1, group.getId(),
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<Issue> newData_share = Issue
										.create_by_jsonarray(obj.toString());
								if (newData_share == null) {
									toast("网络异常,解析错误");
								} else if (newData_share.size() == 0) {
									toast("没有更多");
									lv_share.setPullLoadEnable(false);
								} else {
									page_share++;
									data_share.addAll(newData_share);
									adapter_share.notifyDataSetChanged();
									if(newData_share.size()<10){
										lv_share.setPullLoadEnable(false);
									}else{
										lv_share.setPullLoadEnable(true);
									}
								}
								lv_share.stopLoadMore();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast(ErrorCode.errorList.get(errorCode));
								lv_share.stopLoadMore();

							}
						});

			}
		});

		lv_share.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position - 1 == -1)
					return;
				viewClicked = view;
				item_click = position - 1;
				Intent intent = new Intent(getContext(), GroupShareDetail.class);
				intent.putExtra("issue", data_share.get(position - 1));
				intent.putExtra("group", group);
				openActivity(intent);
			}
		});
	}

	@Override
	protected void initData() {
		api = new GroupAPI();
		group = (MGroup) getSerializableExtra("group");
		DataPool dp = new DataPool(DataPool.SP_Name_User, this);
		user = (User) dp.get(DataPool.SP_Key_User);

		data_user = new ArrayList<User>();
		adapter_member = new MemberAdapter(data_user, getContext());

		data_share = new ArrayList<Issue>();
		adapter_share = new IssueAdapter(getContext(), data_share);

		shareAPI = new GroupShareAPI();
	}

	@Override
	protected void initLayout() {
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_info = _getView(R.id.acty_groupinfo_footer_groupInfo);
		btn_member = _getView(R.id.acty_groupinfo_footer_groupMenber);
		btn_share = _getView(R.id.acty_groupinfo_footer_groupShare);
		btn_more = _getView(R.id.acty_head_btn_more);

		btn_back.setOnClickListener(this);
		btn_info.setOnClickListener(this);
		btn_member.setOnClickListener(this);
		btn_share.setOnClickListener(this);
		btn_more.setOnClickListener(this);
		initViewPager();
	}

	private void initViewPager() {
		viewpager = (ViewPager) _getView(R.id.acty_group_content);
		View info = getLayoutInflater().inflate(
				R.layout.frame_acty_groupinfo_groupinfo, null);
		View member = getLayoutInflater().inflate(
				R.layout.frame_acty_groupinfo_groupmember, null);
		View share = getLayoutInflater().inflate(
				R.layout.frame_acty_groupinfo_groupshare, null);

		lv_member = (XListView) member.findViewById(R.id.lv_listview);

		lv_share = (XListView) share.findViewById(R.id.lv_listview);

		tv_owner = (TextView) info
				.findViewById(R.id.frame_acty_groupinfo_groupinfo_tv_owner);
		tv_numMember = (TextView) info
				.findViewById(R.id.frame_acty_groupinfo_groupinfo_tv_numMember);
		tv_description = (TextView) info
				.findViewById(R.id.frame_acty_groupinfo_groupinfo_tv_description);
		tv_groupName = (TextView) info
				.findViewById(R.id.frame_acty_groupinfo_groupinfo_tv_groupName);
		iv_avatar = (ImageView) info
				.findViewById(R.id.frame_acty_groupinfo_groupinfo_iv_avater);

		btns.add(btn_info);
		btns.add(btn_member);
		btns.add(btn_share);
		L.i(user.getId() + "");
		// // 不是圈子拥有者
		// if (user.getId() != group.getOwner().getId()) {
		// btn_deleteGroup.setVisibility(View.GONE);
		// btn_edit.setVisibility(View.INVISIBLE);
		// }
		fillInData();

		List<View> views = new ArrayList<View>();
		views.add(info);
		views.add(member);
		views.add(share);

		List<XListView> listviews = new ArrayList<XListView>();
		listviews.add(null);
		listviews.add(lv_member);
		listviews.add(lv_share);

		List<BaseAdapter> adapters = new ArrayList<BaseAdapter>();
		adapters.add(null);
		adapters.add(adapter_member);
		adapters.add(adapter_share);

		viewpager.setAdapter(new BaseViewPagerAdapter(views));
		viewpager.setOnPageChangeListener(new FootOnPageChangelistener(btns,
				listviews, adapters));

	}

	private void fillInData() {
		if (group.getAvatar() != null) {
			ImageLoader.getInstance().displayImage(
					RestClient.BASE_URL + group.getAvatar(), iv_avatar);
		} else {
			ImageLoader.getInstance().displayImage(
					"drawable://" + R.drawable.ic_image_load_normal, iv_avatar);
		}
		tv_owner.setText(group.getOwner().getProfile().getName());
		tv_numMember.setText(group.getNumMembers() + "");
		tv_description.setText(group.getDescription());
		tv_groupName.setText(group.getName());
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_head_btn_compose:
			toast("compose");
			break;
		case R.id.acty_groupinfo_footer_groupInfo:
			viewpager.setCurrentItem(0);
			break;
		case R.id.acty_groupinfo_footer_groupMenber:
			if (viewpager.getCurrentItem() == 1) {
				lv_member.startRefresh();
			} else {
				viewpager.setCurrentItem(1);
			}
			break;
		case R.id.acty_groupinfo_footer_groupShare:
			if (viewpager.getCurrentItem() == 2) {
				lv_share.startRefresh();
			} else {
				viewpager.setCurrentItem(2);
			}
			break;
		case R.id.acty_head_btn_more:
			mPopupWindow.showAsDropDown(btn_more);
			break;
		case R.id.btn_manage:
			mPopupWindow.dismiss();
			if (group.getOwnerid() == AppInfo.getUser(getContext()).getId()) {
				toManagePage();
			} else {
				toast("圈子不属于你的");
			}
			break;
		case R.id.btn_join:
			mPopupWindow.dismiss();
			joinActivity();
			break;
		case R.id.btn_exit:
			mPopupWindow.dismiss();
			exitGroup();
			// editGroup();
			break;
		case R.id.btn_createActivity:
			mPopupWindow.dismiss();
			intent = new Intent(this, ActivitiesPublish.class);
			intent.putExtra("group", group);
			openActivity(intent);
			break;

		case R.id.btn_share:
			mPopupWindow.dismiss();
			intent = new Intent(this, GroupSharePublish.class);
			intent.putExtra("group", group);
			openActivity(intent);
			break;
		default:
			break;
		}
	}

	private void toManagePage() {
		Intent intent = new Intent(this, GroupManage.class);
		intent.putExtra("group", group);
		openActivity(intent);
	}

	private void joinActivity() {
		api.join(group.getId(), new JsonResponseHandler() {

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				toast("申请成功");
			}

			@Override
			public void onFaild(int errorType, int errorCode) {
				if (errorCode == 20506) {
					toast("已加入");
				} else {
					toast("申请失败 " + ErrorCode.errorList.get(errorCode));
				}
			}
		});
	}

	private void exitGroup() {
		api.exit(group.getId(), new JsonResponseHandler() {

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				toast("已退出该群");
			}

			@Override
			public void onFaild(int errorType, int errorCode) {
				// toast("退出失败 错误码:" + errorCode);
				toast("退出失败" + ErrorCode.errorList.get(errorCode));
			}
		});

	}

}
