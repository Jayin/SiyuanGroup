package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.adapter.BaseOnPageChangeListener;
import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.adapter.MemberAdapter;
import com.alumnigroup.api.GroupAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.MGroup;
import com.alumnigroup.entity.MGroup.Memberships;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.imple.ResponseHandler;
import com.alumnigroup.utils.DataPool;
import com.alumnigroup.utils.JsonUtils;
import com.alumnigroup.utils.L;
import com.alumnigroup.widget.PullAndLoadListView;
import com.alumnigroup.widget.PullAndLoadListView.OnLoadMoreListener;
import com.alumnigroup.widget.PullToRefreshListView.OnRefreshListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 圈子列表
 * 
 * @author Jayin Ton
 * 
 */
public class GroupInfo extends BaseActivity {
	private int RequestCode_invite = 1;
	private MGroup group;
	private View btn_back, btn_edit, btn_info, btn_member, btn_share, btn_join,
			btn_invite, btn_exitGroup, btn_deleteGroup, btn_createActivity;
	private TextView tv_owner, tv_numMember, tv_description, tv_groupName;
	private ImageView iv_avatar;
	private User user;
	private ViewPager viewpager;
	private List<View> btns = new ArrayList<View>();
	private GroupAPI api;
	private PullAndLoadListView lv_member;
	private List<User> data_user;
	private MemberAdapter adapter_member;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_groupinfo);
		initData();
		initLayout();
		initController();
	}

	private void initController() {
		lv_member.setAdapter(adapter_member);
		lv_member.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				api.view(group.getId(), new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						boolean canRefresh = true;
						MGroup mGroup = MGroup.create_by_json(obj.toString());
						List<User> newData = new ArrayList<User>();
						for (Memberships ms : mGroup.getMemberships()) {
							newData.add(ms.getUser());
						}
						if (newData != null) {
							if (newData.size() == 0) {
								toast("还没有会员!");
								canRefresh = false;
							} else {
								data_user.clear();
								data_user.addAll(newData);
								adapter_member.notifyDataSetChanged();
								canRefresh = false;
							}
						}
						if (!canRefresh)
							lv_member.setCanRefresh(false, false);
						lv_member.onRefreshComplete();
						// if(canRefresh)lv_member.setCanRefresh(false, "没有更多");
						// if(canRefresh)lv_member.setCanRefresh(false, false);

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
		api = new GroupAPI();
		group = (MGroup) getSerializableExtra("group");
		DataPool dp = new DataPool(DataPool.SP_Name_User, this);
		user = (User) dp.get(DataPool.SP_Key_User);

		data_user = new ArrayList<User>();
		adapter_member = new MemberAdapter(data_user, getContext());

	}

	@Override
	protected void initLayout() {
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_info = _getView(R.id.acty_groupinfo_footer_groupInfo);
		btn_member = _getView(R.id.acty_groupinfo_footer_groupMenber);
		btn_share = _getView(R.id.acty_groupinfo_footer_groupShare);
		btn_createActivity = _getView(R.id.acty_head_btn_createActiviy);

		btn_back.setOnClickListener(this);
		btn_info.setOnClickListener(this);
		btn_member.setOnClickListener(this);
		btn_share.setOnClickListener(this);
		btn_createActivity.setOnClickListener(this);
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

		lv_member = (PullAndLoadListView) member
				.findViewById(R.id.frame_acty_groupinfo_groupmember_listview);

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

		btn_join = info
				.findViewById(R.id.frame_acty_groupinfo_groupinfo_btn_joingroup);
		btn_invite = info
				.findViewById(R.id.frame_acty_groupinfo_groupinfo_btn_invite);
		btn_exitGroup = info
				.findViewById(R.id.frame_acty_groupinfo_groupinfo_btn_exitgroup);
		btn_deleteGroup = info
				.findViewById(R.id.frame_acty_groupinfo_groupinfo_btn_deletegroup);
		btn_edit = info
				.findViewById(R.id.frame_acty_groupinfo_groupinfo_btn_edit);

		btn_edit.setOnClickListener(this);
		btn_join.setOnClickListener(this);
		btn_invite.setOnClickListener(this);
		btn_exitGroup.setOnClickListener(this);
		btn_deleteGroup.setOnClickListener(this);

		btns.add(btn_info);
		btns.add(btn_member);
		btns.add(btn_share);
		L.i(user.getId() + "");
		// // 不是圈子拥有者
		// if (user.getId() != group.getOwner().getId()) {
		// btn_deleteGroup.setVisibility(View.GONE);
		// btn_edit.setVisibility(View.INVISIBLE);
		// }

		ImageLoader.getInstance().displayImage(
				RestClient.BASE_URL + group.getAvatar(), iv_avatar);
		tv_owner.setText(group.getOwner().getProfile().getName());
		tv_numMember.setText(group.getNumMembers() + "");
		tv_description.setText(group.getDescription());
		tv_groupName.setText(group.getName());

		List<View> views = new ArrayList<View>();
		views.add(info);
		views.add(member);
		views.add(share);
		viewpager.setAdapter(new BaseViewPagerAdapter(views));
		viewpager.setOnPageChangeListener(new BaseOnPageChangeListener(btns));
	}

	@Override
	public void onClick(View v) {
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
			viewpager.setCurrentItem(1);
			break;
		case R.id.acty_groupinfo_footer_groupShare:
			viewpager.setCurrentItem(2);
			break;
		case R.id.frame_acty_groupinfo_groupinfo_btn_joingroup:
			api.join(group.getId(), new ResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] data) {
					String json = new String(data);
					if (JsonUtils.isOK(json)) {
						toast("加入成功");
					} else {
						toast("Error:" + JsonUtils.getErrorString(json));
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] header,
						byte[] data, Throwable err) {
					toast("网络异常 错误代码:" + statusCode);
				}
			});
			break;
		case R.id.frame_acty_groupinfo_groupinfo_btn_invite:
			toast("邀请");
			invite();
			break;
		case R.id.frame_acty_groupinfo_groupinfo_btn_exitgroup:
			toast("退出圈子");
			exitGroup();
			break;
		case R.id.frame_acty_groupinfo_groupinfo_btn_deletegroup:
			toast("删除 圈子");
			break;
		case R.id.frame_acty_groupinfo_groupinfo_btn_edit:
			toast("编辑圈子");
			break;
		case R.id.acty_head_btn_createActiviy:
			Intent intent = new Intent(this, ActivitiesPublish.class);
			intent.putExtra("group", group);
			openActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   if(requestCode==RequestCode_invite && resultCode==RESULT_OK){
		   // to post.
		  if(data.getSerializableExtra("result")!=null){
 			  ArrayList<User> userList = (ArrayList<User>)data.getSerializableExtra("result");
 			  //add post data...
 			  toast("select count:"+userList.size());
		  }
	   }
	}

	private void invite() {
		Intent intent = new Intent(this, FollowingList.class);
        User u = new AppInfo(getContext()).getUser();
 		intent.putExtra("userid", u.getId());
		startActivityForResult(intent, RequestCode_invite);
	}

	private void exitGroup() {
		api.exit(group.getId(), new JsonResponseHandler() {

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				toast("已退出该群");
			}

			@Override
			public void onFaild(int errorType, int errorCode) {
				toast("退出失败 错误码:" + errorCode);

			}
		});

	}

}
