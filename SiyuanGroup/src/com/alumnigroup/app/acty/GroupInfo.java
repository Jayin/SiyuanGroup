package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.adapter.BaseOnPageChangeListener;
import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.api.GroupAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.MGroup;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.ResponseHandler;
import com.alumnigroup.utils.DataPool;
import com.alumnigroup.utils.JsonUtils;
import com.alumnigroup.utils.L;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 圈子列表
 * 
 * @author Jayin Ton
 * 
 */
public class GroupInfo extends BaseActivity {
	private MGroup group;
	private View btn_back, btn_edit, btn_info, btn_member, btn_share, btn_join,
			btn_invite, btn_exitGroup, btn_deleteGroup;
	private TextView tv_owner, tv_numMember, tv_description, tv_groupName;
	private ImageView iv_avatar;
	private User user;
	private ViewPager viewpager;
	private List<View> btns = new ArrayList<View>();
	private GroupAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_groupinfo);
		initData();
		initLayout();
		initController();
	}

	private void initController() {

	}

	@Override
	protected void initData() {
		api = new GroupAPI();
		group = (MGroup) getSerializableExtra("group");
		DataPool dp = new DataPool(DataPool.SP_Name_User, this);
		user = (User) dp.get(DataPool.SP_Key_User);
	}

	@Override
	protected void initLayout() {
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_edit = _getView(R.id.acty_head_btn_compose);
		btn_info = _getView(R.id.acty_groupinfo_footer_groupInfo);
		btn_member = _getView(R.id.acty_groupinfo_footer_groupMenber);
		btn_share = _getView(R.id.acty_groupinfo_footer_groupShare);

		btn_back.setOnClickListener(this);
		btn_edit.setOnClickListener(this);
		btn_info.setOnClickListener(this);
		btn_member.setOnClickListener(this);
		btn_share.setOnClickListener(this);
		initViewPager();
	}

	private void initViewPager() {
		viewpager = (ViewPager) _getView(R.id.acty_group_content);
		View info = getLayoutInflater().inflate(
				R.layout.frame_acty_group_groupinfo, null);
		View member = getLayoutInflater().inflate(
				R.layout.frame_acty_group_groupmember, null);
		View share = getLayoutInflater().inflate(
				R.layout.frame_acty_group_groupshare, null);

		tv_owner = (TextView) info
				.findViewById(R.id.frame_acty_group_groupinfo_tv_owner);
		tv_numMember = (TextView) info
				.findViewById(R.id.frame_acty_group_groupinfo_tv_numMember);
		tv_description = (TextView) info
				.findViewById(R.id.frame_acty_group_groupinfo_tv_description);
		tv_groupName = (TextView) info
				.findViewById(R.id.frame_acty_group_groupinfo_tv_groupName);
		iv_avatar = (ImageView) info
				.findViewById(R.id.frame_acty_group_groupinfo_iv_avater);

		btn_join = info
				.findViewById(R.id.frame_acty_group_groupinfo_btn_joingroup);
		btn_invite = info
				.findViewById(R.id.frame_acty_group_groupinfo_btn_invite);
		btn_exitGroup = info
				.findViewById(R.id.frame_acty_group_groupinfo_btn_exitgroup);
		btn_deleteGroup = info
				.findViewById(R.id.frame_acty_group_groupinfo_btn_deletegroup);
		btn_join.setOnClickListener(this);
		btn_invite.setOnClickListener(this);
		btn_exitGroup.setOnClickListener(this);
		btn_deleteGroup.setOnClickListener(this);

		btns.add(btn_info);
		btns.add(btn_member);
		btns.add(btn_share);
		L.i(user.getId() + "");
		// 不是圈子拥有者
		if (user.getId() != group.getOwner().getId()) {
			btn_deleteGroup.setVisibility(View.GONE);
			btn_edit.setVisibility(View.INVISIBLE);
		}

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
		case R.id.frame_acty_group_groupinfo_btn_joingroup:
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
		case R.id.frame_acty_group_groupinfo_btn_invite:
			toast("邀请");
			break;
		case R.id.frame_acty_group_groupinfo_btn_exitgroup:
			toast("退出圈子");

			break;
		case R.id.frame_acty_group_groupinfo_btn_deletegroup:
			toast("删除 圈子");
			break;
		default:
			break;
		}
	}

}
