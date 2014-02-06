package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alumnigroup.adapter.BaseOnPageChangeListener;
import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.adapter.MemberAdapter;
import com.alumnigroup.api.ActivityAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.api.StarAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.MActivity;

import com.alumnigroup.entity.User;
import com.alumnigroup.entity.Userships;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.widget.PullAndLoadListView;
import com.alumnigroup.widget.PullAndLoadListView.OnLoadMoreListener;
import com.alumnigroup.widget.PullToRefreshListView.OnRefreshListener;
import com.loopj.android.http.JsonHttpResponseHandler;
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
	private PullAndLoadListView lv_member, lv_share;
	private List<User> data_member, data_share;
	private MemberAdapter adapter_member, adapter_share;
	private ViewPager viewpager;
	private PopupWindow mPopupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_activitiesinfo);
		initData();
		initPopupWindow();
		initLayout();
		initController();
	}

	private void initPopupWindow() {
		View view = getLayoutInflater().inflate(
				R.layout.popup_acty_activitiesinfo, null);

		(view.findViewById(R.id.manage)).setOnClickListener(this);
		(view.findViewById(R.id.join)).setOnClickListener(this);
		(view.findViewById(R.id.exit)).setOnClickListener(this);
		(view.findViewById(R.id.favourite)).setOnClickListener(this);

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
		lv_member.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				api.getUserList(acty.getId(), new JsonResponseHandler() {
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
							data_member.addAll(newData_member);
							adapter_member.notifyDataSetChanged();
						}
						lv_member.setCanRefresh(false, false);
						lv_member.onRefreshComplete();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("网络异常 错误代码:" + errorCode);
						lv_member.onRefreshComplete();

					}
				});
			}
		});
		lv_member.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

			}
		});
		lv_member.setCanLoadMore(false);
		lv_member.toRefresh();
	}

	@Override
	protected void initData() {
		acty = (MActivity) getSerializableExtra("activity");
		api = new ActivityAPI();
		data_member = new ArrayList<User>();
		adapter_member = new MemberAdapter(data_member, getContext());
	}

	private void initViewPager() {
		viewpager = (ViewPager) _getView(R.id.acty_activitiesinfo_content);
		View info = getLayoutInflater().inflate(
				R.layout.frame_acty_activitiesinfo_introduce, null);
		View member = getLayoutInflater().inflate(
				R.layout.frame_acty_activitiesinfo_userlist, null);
		View share = getLayoutInflater().inflate(
				R.layout.frame_acty_activitiesinfo_share, null);

		lv_member = (PullAndLoadListView) member
				.findViewById(R.id.frame_acty_activitiesinfo_userlist_listview);
		lv_share = (PullAndLoadListView) share
				.findViewById(R.id.frame_acty_activitiesinfo_share_listview);

		tv_starttime = (TextView) info.findViewById(R.id.tv_starttime);
		tv_applyDeadline = (TextView) info.findViewById(R.id.tv_deadline);// 结束日期
		tv_money = (TextView) info.findViewById(R.id.tv_money);
		tv_applyNum = (TextView) info.findViewById(R.id.tv_applyNum);
		tv_site = (TextView) info.findViewById(R.id.tv_address);
		tv_description = (TextView) info.findViewById(R.id.tv_description);
		tv_duration = (TextView) info.findViewById(R.id.tv_duration);
		tv_applyDeadline.setText(CalendarUtils.getTimeFromat(
				acty.getRegdeadline(), CalendarUtils.TYPE_TWO));

		iv_avatar = (ImageView) info.findViewById(R.id.iv_avatar);
		ImageLoader.getInstance().displayImage(RestClient.BASE_URL +acty.getAvatar(), iv_avatar);

		tv_starttime.setText(CalendarUtils.getTimeFromat(acty.getStarttime(),
				CalendarUtils.TYPE_TWO));
		tv_site.setText(acty.getSite());
		tv_description.setText(acty.getContent());
		tv_applyNum.setText(acty.getNumUsership() + "/" + acty.getMaxnum());
		tv_duration.setText(acty.getDuration() + "天");
		tv_money.setText(acty.getMoney() + "RMB");

		btns.add(btn_info);
		btns.add(btn_userlist);
		btns.add(btn_share);

		List<View> views = new ArrayList<View>();
		views.add(info);
		views.add(member);
		views.add(share);
		viewpager.setAdapter(new BaseViewPagerAdapter(views));
		viewpager.setOnPageChangeListener(new BaseOnPageChangeListener(btns));
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
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.btn_info:
			viewpager.setCurrentItem(0);
			break;
		case R.id.btn_userlist:
			viewpager.setCurrentItem(1);
			break;
		case R.id.btn_share:
			viewpager.setCurrentItem(2);
			break;
		case R.id.acty_head_btn_more:
			if (!mPopupWindow.isShowing())
				mPopupWindow.showAsDropDown(btn_more);
			break;
		case R.id.manage:
			toast("manage");
			mPopupWindow.dismiss();
		   //to manage..
			break;
		case R.id.join:
			mPopupWindow.dismiss();
			joinActivity();
			break;
		case R.id.exit:
			mPopupWindow.dismiss();
			cancelActivity();
			break;
		case R.id.favourite:
			mPopupWindow.dismiss();
			favourite();
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
				toast("退出活动失败,错误码:" + errorCode);
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
				toast("报名失败,错误码:" + errorCode);
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
						toast("收藏失败 错误码:" + errorCode);

					}
				});

	}

}
