package com.alumnigroup.app.acty;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alumnigroup.api.GroupAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.app.SynData;
import com.alumnigroup.app.SynData.SynDataListener;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.MGroup;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.EditTextUtils;
import com.alumnigroup.utils.StringUtils;
import com.alumnigroup.widget.LoadingDialog;

/**
 * 创建圈子页面
 * 
 * @author Jayin Ton
 * 
 */
public class GroupCreate extends BaseActivity {
	private View btn_back, btn_create, btn_invite;
	private EditText et_name, et_description;
	private GroupAPI api;
	private MGroup group = null;
	private TextView tv_title; // 创建圈子/编辑
	private LoadingDialog loadingdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_groupcreate);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		api = new GroupAPI();
		if (getSerializableExtra("group") != null) {
			group = (MGroup) getSerializableExtra("group");
		}
	}

	@Override
	protected void initLayout() {
		loadingdialog  =new LoadingDialog(getContext());
		loadingdialog.setCancelable(false);
		
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_create = _getView(R.id.acty_head_btn_create);
		btn_invite = _getView(R.id.invite);
		et_name = (EditText) _getView(R.id.et_name);
		et_description = (EditText) _getView(R.id.et_description);
		tv_title = (TextView) _getView(R.id.acty_head_tv_title);

		btn_back.setOnClickListener(this);
		btn_create.setOnClickListener(this);
		btn_invite.setOnClickListener(this);

		if (group != null) {
			et_name.setText(group.getName());
			et_description.setText(group.getDescription());
			tv_title.setText("编辑");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_head_btn_create:

			String name = EditTextUtils.getTextTrim(et_name);
			String description = EditTextUtils.getTextTrim(et_description);
			if (name == null || StringUtils.isEmpty(name)) {
				toast("圈子名称不能为空");
				return;
			}
			if (group == null)
				create(name, description);
			else
				update(name, description);

			break;
		case R.id.invite:
			toast("to friend list");
			break;

		default:
			break;
		}
	}

	// 创建
	private void create(String name, String description) {
		api.createGroup(name, description, new JsonResponseHandler() {

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				toast("创建成功");
				closeActivity();
			}

			@Override
			public void onFaild(int errorType, int errorCode) {
				toast("创建失败 " + ErrorCode.errorList.get(errorCode));
			}
			
			@Override
			public void onStart() {
				loadingdialog.setText("创建中...");
				loadingdialog.show();
			}
			
			@Override
			public void onFinish() {
				loadingdialog.dismiss();
			}

		});

	}

	// 编辑更新
	private void update(final String name, final String description) {
		api.updateInfo(group.getId(), name, description,
				new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						toast("修改成功");
						closeActivity();
						//同步数据
						SynData.SyncGroupInfo(getContext(), group.getId(), null);
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("修改失败 " + ErrorCode.errorList.get(errorCode));
					}
					
					@Override
					public void onStart() {
						loadingdialog.setText("更新中...");
						loadingdialog.show();
					}
					
					@Override
					public void onFinish() {
						loadingdialog.dismiss();
					}

				});
	}
}
