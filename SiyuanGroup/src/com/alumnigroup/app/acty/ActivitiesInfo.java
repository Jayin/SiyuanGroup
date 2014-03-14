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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.adapter.FootOnPageChangelistener;
import com.alumnigroup.adapter.MemberAdapter;
import com.alumnigroup.api.ActivityAPI;
import com.alumnigroup.api.ActivityShareAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.api.StarAPI;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.Issue;
import com.alumnigroup.entity.MActivity;
import com.alumnigroup.entity.User;
import com.alumnigroup.entity.Userships;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.widget.XListView;
import com.alumnigroup.widget.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 活动详情页面
 * 
 * @author Jayin Ton
 * 
 */
public class ActivitiesInfo extends BaseActivity {
	private View btn_back, btn_info, btn_userlist, btn_share, btn_more;
	private TextView tv_starttime, tv_applyDeadline, tv_money, tv_applyNum,
			tv_site, tv_description, tv_name, tv_duration;
	private ImageView iv_avatar;
	private MActivity acty;
	private ActivityAPI api;
	private List<View> btns = new ArrayList<View>();
	private XListView lv_member, lv_share;
	private List<User> data_member;
	private MemberAdapter adapter_member;
	private ViewPager viewpager;
	private PopupWindow mPopupWindow;
	private User user;
	private ActivityShareAPI shareAPI;
	private List<Issue> data_share;
	private IssueAdapter adapter_share;
	private int page_share = 0,page_member = 0;
	private ImageView iv_pic1, iv_pic2, iv_pic3;
	private BroadcastReceiver mReceiver = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_activitiesinfo);
		initData();
		initPopupWindow();
		initLayout();
		initController();
		openReceiver();
	}

	private void openReceiver() {
		mReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				 MActivity a = (MActivity)intent.getSerializableExtra("activity");
				 acty = a;
				 fillInData();
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action_ActivityInfo_Edit);
		registerReceiver(mReceiver, filter);
	}

	private void initPopupWindow() {
		View view = getLayoutInflater().inflate(
				R.layout.popup_acty_activitiesinfo, null);

		(view.findViewById(R.id.btn_manage)).setOnClickListener(this);
		(view.findViewById(R.id.btn_join)).setOnClickListener(this);
		(view.findViewById(R.id.btn_exit)).setOnClickListener(this);
		(view.findViewById(R.id.btn_favourite)).setOnClickListener(this);
		(view.findViewById(R.id.btn_activity_share)).setOnClickListener(this);
		

		mPopupWindow = new PopupWindow(view);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setOutsideTouchable(true);

		// 控制popupwindow的宽度和高度自适应
		view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		mPopupWindow.setWidth(view.getMeasuredWidth());
		mPopupWindow.setHeight(view.getMeasuredHeight());
	}

	private void initController() {
		lv_member.setPullRefreshEnable(true);
		lv_member.setPullLoadEnable(false);//一次性加载全部用户
		
		lv_share.setPullRefreshEnable(true);
		lv_share.setPullLoadEnable(true); 
		
		lv_member.setAdapter(adapter_member);
		lv_share.setAdapter(adapter_share);
		lv_member.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				//一次性加载全部用户
				api.getUserListAll(acty.getNumUsership(),acty.getId(), new JsonResponseHandler() {
					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<Userships> us = Userships.create_by_jsonarray(obj
								.toString());
						List<User> newData_member = new ArrayList<User>();
						for (Userships _us : us) {
							newData_member.add(_us.getUser());
						}
						if (newData_member.size() == 0) {
							toast("还没人参加这活动");
						} else {
							data_member.clear();
							data_member.addAll(newData_member);
							adapter_member.notifyDataSetChanged();
							page_member =1;
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
				
			}
		});
		
		lv_share.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				shareAPI.getShareList(1, acty.getId(),
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<Issue> newData_share = Issue
										.create_by_jsonarray(obj.toString());
								if (newData_share == null) {
									toast("网络异常 解析错误");
								} else if (newData_share.size() == 0) {
									toast("还没有分享");
									lv_share.setPullLoadEnable(false);
								} else {
									page_share = 1;
									data_share.clear();
									data_share.addAll(newData_share);
									adapter_share.notifyDataSetChanged();
									lv_share.setPullLoadEnable(true);
								}
								lv_share.stopRefresh();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast( ErrorCode.errorList.get(errorCode));
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
				shareAPI.getShareList(page_share + 1, acty.getId(),
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
                       Intent intent = new Intent(getContext(), ActivitiesShareDetail.class);
                       intent.putExtra("issue", data_share.get(position-1));
                       intent.putExtra("activity", acty);
                       openActivity(intent);
			}
		});
	}

	@Override
	protected void initData() {
		acty = (MActivity) getSerializableExtra("activity");
		user = AppInfo.getUser(getContext());
		if(user==null){
			toast("无用户信息，请重新登录");
			closeActivity();
		}
		api = new ActivityAPI();
		shareAPI = new ActivityShareAPI();
		data_member = new ArrayList<User>();
		data_share = new ArrayList<Issue>();
		adapter_member = new MemberAdapter(data_member, getContext());
		adapter_share= new IssueAdapter(getContext(), data_share);
	}

	private void initViewPager() {
		viewpager = (ViewPager) _getView(R.id.acty_activitiesinfo_content);
		View info = getLayoutInflater().inflate(
				R.layout.frame_acty_activitiesinfo_introduce, null);
		View member = getLayoutInflater().inflate(
				R.layout.frame_acty_activitiesinfo_userlist, null);
		View share = getLayoutInflater().inflate(
				R.layout.frame_acty_activitiesinfo_share, null);

		lv_member = (XListView) member
				.findViewById(R.id.frame_acty_activitiesinfo_userlist_listview);
		lv_share = (XListView) share
				.findViewById(R.id.frame_acty_activitiesinfo_share_listview);

		tv_starttime = (TextView) info.findViewById(R.id.tv_starttime);
		tv_applyDeadline = (TextView) info.findViewById(R.id.tv_deadline);// 结束日期
		tv_money = (TextView) info.findViewById(R.id.tv_money);
		tv_applyNum = (TextView) info.findViewById(R.id.tv_applyNum);
		tv_site = (TextView) info.findViewById(R.id.tv_address);
		tv_description = (TextView) info.findViewById(R.id.tv_description);
		tv_duration = (TextView) info.findViewById(R.id.tv_duration);
		tv_name = (TextView)info.findViewById(R.id.frame_acty_activityinfo_introduce_name);
		
		iv_avatar = (ImageView) info.findViewById(R.id.iv_avatar);
		
		fillInData();

		btns.add(btn_info);
		btns.add(btn_userlist);
		btns.add(btn_share);

		List<View> views = new ArrayList<View>();
		views.add(info);
		views.add(member);
		views.add(share);
		
		List<XListView> listviews = new ArrayList<XListView>();
		listviews.add(null);  listviews.add(lv_member);listviews.add(lv_share); 
		
		List<BaseAdapter>  adapters = new ArrayList<BaseAdapter>();
		adapters.add(null);  adapters.add(adapter_member);adapters.add(adapter_share); 
		
		viewpager.setAdapter(new BaseViewPagerAdapter(views));
		viewpager.setOnPageChangeListener(new FootOnPageChangelistener(btns, listviews, adapters));
		
		iv_pic1 = (ImageView) info.findViewById(R.id.iv_pic1);
		iv_pic2 = (ImageView) info.findViewById(R.id.iv_pic2);
		iv_pic3 = (ImageView) info.findViewById(R.id.iv_pic3);

		for (int i = 0; i < acty.getPictures().size(); i++) {
			switch (i) {
			case 0:
				iv_pic1.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(
						RestClient.BASE_URL + acty.getPictures().get(i).getPath(),
						iv_pic1);
				iv_pic1.setOnClickListener(this);

				break;
			case 1:
				iv_pic2.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(
						RestClient.BASE_URL + acty.getPictures().get(i).getPath(),
						iv_pic2);
				iv_pic2.setOnClickListener(this);
				break;
			case 2:
				iv_pic3.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(
						RestClient.BASE_URL + acty.getPictures().get(i).getPath(),
						iv_pic3);
				iv_pic3.setOnClickListener(this);
				break;
			default:
				break;
			}
		}
	}
	//填充数据
	private void fillInData(){
		if(acty.getAvatar()!=null){
			ImageLoader.getInstance().displayImage(RestClient.BASE_URL +acty.getAvatar(), iv_avatar);
		}else{
			ImageLoader.getInstance().displayImage(
					"drawable://"+R.drawable.ic_image_load_normal, iv_avatar);
		}
		
		tv_name.setText(acty.getName());
		tv_applyDeadline.setText(CalendarUtils.getTimeFromat(
				acty.getRegdeadline(), CalendarUtils.TYPE_TWO));
		tv_starttime.setText(CalendarUtils.getTimeFromat(acty.getStarttime(),
				CalendarUtils.TYPE_TWO));
		tv_site.setText(acty.getSite());
		tv_description.setText(acty.getContent());
		tv_applyNum.setText(acty.getNumUsership() + "/" + acty.getMaxnum());
		tv_duration.setText(acty.getDuration() + "天");
		tv_money.setText(acty.getMoney() + "RMB");
	}

	@Override
	protected void initLayout() {
		btn_more = _getView(R.id.acty_head_btn_more);
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_info = _getView(R.id.btn_info);
		btn_userlist = _getView(R.id.btn_userlist);
		btn_share = _getView(R.id.btn_share);

		btn_more.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_info.setOnClickListener(this);
		btn_userlist.setOnClickListener(this);
		btn_share.setOnClickListener(this);
		initViewPager();
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.btn_info:
			viewpager.setCurrentItem(0);
			break;
		case R.id.btn_userlist:
			if(viewpager.getCurrentItem()==1){
				lv_member.startRefresh();
			}else{
				viewpager.setCurrentItem(1);
			}
			break;
		case R.id.btn_share:
			if(viewpager.getCurrentItem()==2){
				lv_share.startRefresh();
			}else{
				viewpager.setCurrentItem(2);
			}
			break;
		case R.id.acty_head_btn_more:
			if (!mPopupWindow.isShowing())
				mPopupWindow.showAsDropDown(btn_more);
			break;
		case R.id.btn_manage:
			mPopupWindow.dismiss();
			if(acty.getOwnerid()==user.getId()){
				intent = new Intent(getContext(), ActivitiesManage.class);
				intent.putExtra("activity", acty);
				openActivity(intent);
			}else{
				toast("无权管理该活动");
			}
			break;
		case R.id.btn_join:
			mPopupWindow.dismiss();
			joinActivity();
			break;
		case R.id.btn_exit:
			mPopupWindow.dismiss();
			cancelActivity();
			break;
		case R.id.btn_favourite:
			mPopupWindow.dismiss();
			favourite();
			break;
		case R.id.btn_activity_share:
			mPopupWindow.dismiss();
			intent = new Intent(this, ActivitiesSharePublish.class);
			intent.putExtra("activity",acty);
			openActivity(intent);
			break;
		case R.id.iv_pic1:
			intent = new Intent(getContext(), ImageDisplay.class);
			intent.putExtra("url", RestClient.BASE_URL
					+ acty.getPictures().get(0).getPath());
			openActivity(intent);
			break;
		case R.id.iv_pic2:
			intent = new Intent(getContext(), ImageDisplay.class);
			intent.putExtra("url", RestClient.BASE_URL
					+ acty.getPictures().get(1).getPath());
			openActivity(intent);
			break;
		case R.id.iv_pic3:
			intent = new Intent(getContext(), ImageDisplay.class);
			intent.putExtra("url", RestClient.BASE_URL
					+ acty.getPictures().get(2).getPath());
			openActivity(intent);
			break;
		default:
			break;
		}
	}



	private void cancelActivity() {
		api.cancelActivity(acty.getId(), new JsonResponseHandler() {

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				toast("已退出活动");
			}

			@Override
			public void onFaild(int errorType, int errorCode) {
				toast("退出活动失败 " + ErrorCode.errorList.get(errorCode));
			}
		});

	}

	private void joinActivity() {
		api.joinActivity(acty.getId(), new JsonResponseHandler() {

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				toast("已报名参加");
			}

			@Override
			public void onFaild(int errorType, int errorCode) {
				toast("报名失败 " + ErrorCode.errorList.get(errorCode));
			}
		});

	}



	// 收藏的remark默认为期类型名
	private void favourite() {
		StarAPI starapi = new StarAPI();
		starapi.star(StarAPI.Item_type_activity, acty.getId(), "activity",
				new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						toast("收藏成功");

					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("收藏失败 "+ErrorCode.errorList.get(errorCode));

					}
				});

	}

}
