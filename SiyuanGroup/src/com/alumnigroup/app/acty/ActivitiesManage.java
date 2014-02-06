package com.alumnigroup.app.acty;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.alumnigroup.api.ActivityAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.MActivity;
import com.alumnigroup.entity.Userships;
import com.alumnigroup.imple.JsonResponseHandler;

/**
 * 活动管理页面
 * 
 * @author Jayin Ton
 * 
 */
public class ActivitiesManage extends BaseActivity {
	private int RequestCode_Manage_Userships = 1;
	private int RequestCode_Pick_image = 2;
	private View btn_back, btn_edit, btn_updateAvatar, btn_end, btn_manage;
	private MActivity acty;
	private ActivityAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_activitiesmanage);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		acty = (MActivity) getSerializableExtra("activity");
		if (acty == null) {
			toast("没有数据");
			closeActivity();
		}
		api = new ActivityAPI();
	}

	@Override
	protected void initLayout() {
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_end = _getView(R.id.btn_end);
		btn_edit = _getView(R.id.btn_edit);
		btn_updateAvatar = _getView(R.id.btn_updateAvatar);
		btn_manage = _getView(R.id.btn_manage);

		btn_end.setOnClickListener(this);
		btn_edit.setOnClickListener(this);
		btn_updateAvatar.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_manage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.btn_edit:
			editActivity();
			break;
		case R.id.btn_updateAvatar:
			updateAvater();
			break;
		case R.id.btn_end:
			endActivity();
			break;
		case R.id.btn_manage:
			Intent intent = new Intent(this, ActivitiesUserShip.class);
			intent.putExtra("activities", acty);
			startActivityForResult(intent, RequestCode_Manage_Userships);
			break;
		default:
			break;
		}
	}

	private void updateAvater() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");// 相片类型
		startActivityForResult(intent, RequestCode_Pick_image);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RequestCode_Pick_image && resultCode == RESULT_OK) {
			Uri uri = data.getData();
			// api.updateAvatar(params, responseHandler)
		} else if (requestCode == RequestCode_Manage_Userships
				&& resultCode == RESULT_OK) {
			ArrayList<Userships> result = (ArrayList<Userships>)data.getSerializableExtra("result");
			toast("you select:" + result.size());
		}
	}

	private void editActivity() {
		Intent intent = new Intent(this, ActivitiesPublish.class);
		intent.putExtra("activity", acty);
		openActivity(intent);
	}

	private void endActivity() {
		api.endActivity(acty.getId(), new JsonResponseHandler() {

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				toast("已结束活动");
			}

			@Override
			public void onFaild(int errorType, int errorCode) {
				toast("结束活动失败,错误码:" + errorCode);
			}
		});
	}

}
