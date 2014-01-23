package com.alumnigroup.app.acty;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.MGroup;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_groupinfo);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		group = (MGroup) getSerializableExtra("group");
	}

	@Override
	protected void initLayout() {
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_edit = _getView(R.id.acty_head_btn_compose);
		btn_info = _getView(R.id.acty_circle_footer_groupInfo);
		btn_member = _getView(R.id.acty_circle_footer_groupMenber);
		btn_share = _getView(R.id.acty_circle_footer_groupShare);

		btn_join = _getView(R.id.frame_acty_group_groupinfo_btn_joingroup);
		btn_invite = _getView(R.id.frame_acty_group_groupinfo_btn_invite);
		btn_exitGroup = _getView(R.id.frame_acty_group_groupinfo_btn_exitgroup);
		btn_deleteGroup = _getView(R.id.frame_acty_group_groupinfo_btn_deletegroup);

		tv_owner = (TextView) _getView(R.id.frame_acty_group_groupinfo_tv_owner);
		tv_numMember = (TextView) _getView(R.id.frame_acty_group_groupinfo_tv_numMember);
		tv_description = (TextView) _getView(R.id.frame_acty_group_groupinfo_tv_description);
		tv_groupName = (TextView) _getView(R.id.frame_acty_group_groupinfo_iv_avater);

		iv_avatar = (ImageView) _getView(R.id.frame_acty_group_groupinfo_iv_avater);

		ImageLoader.getInstance().displayImage(
				RestClient.BASE_URL + group.getAvatar(), iv_avatar);
		tv_owner.setText(group.getOwner().getProfile().getName());
		tv_numMember.setText(group.getNumMembers());
		tv_description.setText(group.getDescription());
		tv_groupName.setText(group.getName());

		btn_back.setOnClickListener(this);
		btn_edit.setOnClickListener(this);
		btn_info.setOnClickListener(this);
		btn_member.setOnClickListener(this);
		btn_share.setOnClickListener(this);
		btn_join.setOnClickListener(this);
		btn_invite.setOnClickListener(this);
		btn_exitGroup.setOnClickListener(this);
		btn_deleteGroup.setOnClickListener(this);
		
		

	}

	@Override
	public void onClick(View v) {

	}

}
