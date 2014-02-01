package com.alumnigroup.app.acty;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.alumnigroup.api.GroupAPI;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.MGroup;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.loopj.android.http.RequestParams;

public class GroupManage extends BaseActivity {
	private int RequestCode_invite = 1;
	private int RequestCode_Pick_imagge = 2;
	private View btn_back, btn_invite, btn_edit, btn_updateAvatar;
	private MGroup group;
	private GroupAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_groupmanage);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		group = (MGroup) getSerializableExtra("group");
		if (group == null) {
			toast("没有数据");
			closeActivity();
		}
		api = new GroupAPI();
	}

	@Override
	protected void initLayout() {
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_invite = _getView(R.id.btn_invite);
		btn_edit = _getView(R.id.btn_edit);
		btn_updateAvatar = _getView(R.id.btn_updateAvater);

		btn_invite.setOnClickListener(this);
		btn_edit.setOnClickListener(this);
		btn_updateAvatar.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.btn_edit:
			editGroup();
			break;
		case R.id.btn_updateAvater:
			updateAvater();
			break;
		case R.id.btn_invite:
			invite();
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RequestCode_invite && resultCode == RESULT_OK) {
			// to post.
			if (data.getSerializableExtra("result") != null) {
				ArrayList<User> userList = (ArrayList<User>) data
						.getSerializableExtra("result");
				api.invite(userList, group.getId(), new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						toast("已发出邀请");
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("邀请失败  错误码:" + errorCode);
					}
				});
			}
		} else if (requestCode == RequestCode_Pick_imagge
				&& resultCode == RESULT_OK) {
			Uri uri = data.getData();
			ContentResolver resolver = getContentResolver(); 
			RequestParams params = new RequestParams();
			try {
				params.put("avatar", resolver.openInputStream(uri));
				api.updateAvatar(group.getId(), params, new JsonResponseHandler() {
					
					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						  toast("头像上传成功");
					}
					
					@Override
					public void onFaild(int errorType, int errorCode) {
                      toast("网络异常 错误码:"+errorCode);						
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
				toast("图片资源不存在");
			}
		}
	}

	private void editGroup() {
		Intent intent = new Intent(this, GroupCreate.class);
		intent.putExtra("group", group);
		openActivity(intent);
	}

	private void invite() {
		Intent intent = new Intent(this, FollowingList.class);
		User u = new AppInfo(getContext()).getUser();
		intent.putExtra("userid", u.getId());
		startActivityForResult(intent, RequestCode_invite);
	}

	private void updateAvater() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");// 相片类型
		startActivityForResult(intent, RequestCode_Pick_imagge);
	}
}
