package com.alumnigroup.app.acty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;
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
import com.alumnigroup.utils.FilePath;
import com.loopj.android.http.RequestParams;

public class GroupManage extends BaseActivity {
	private int RequestCode_invite = 1;
	private int RequestCode_Pick_image = 2;
	private View btn_back, btn_invite, btn_edit, btn_updateAvatar;
	private MGroup group;
	private GroupAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_groupmanage);
		initData();
		initLayout();
		uploadFile();
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
		} else if (requestCode == RequestCode_Pick_image
				&& resultCode == RESULT_OK) {
			uploadFile();
		}
	}

	private void uploadFile() {
		final File f = new File(FilePath.getImageFilePath() + "cache_face.jpg");
		FileInputStream fin;
		try {
			fin = new FileInputStream(f);
			RequestParams params = new RequestParams();
			params.put("avatar", f);
			params.put("avatar", f, "image/jpeg");
			toast(FilePath.getImageFilePath() + "cache_face.jpg");
			api.updateAvatar(group.getId(), params, new JsonResponseHandler() {
				@Override
				public void onStart() {
					toast("图片上传中..");
				}

				@Override
				public void onOK(Header[] headers, JSONObject obj) {
					toast("头像上传成功");
					f.delete();// 删除
				}

				@Override
				public void onFaild(int errorType, int errorCode) {
					toast("网络异常 错误码:" + errorCode);
				}
			});

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			toast("图片资源不存在");
		}

	}

	private void editGroup() {
		Intent intent = new Intent(this, GroupCreate.class);
		intent.putExtra("group", group);
		openActivity(intent);
	}

	private void invite() {
		Intent intent = new Intent(this, FollowingList.class);
		User u = AppInfo.getUser(getContext());
		intent.putExtra("userid", u.getId());
		startActivityForResult(intent, RequestCode_invite);
	}

	private void updateAvater() {
		// 输出裁剪的临时文件
		String path = FilePath.getImageFilePath() + "cache_face.jpg";
		File protraitFile = new File(path);
		Uri uri = Uri.fromFile(protraitFile);
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		intent.putExtra("output", uri);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 100);// 输出图片大小
		intent.putExtra("outputY", 100);
		startActivityForResult(intent, RequestCode_Pick_image);
	}
}
