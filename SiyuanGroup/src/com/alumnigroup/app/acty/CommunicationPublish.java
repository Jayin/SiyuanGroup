package com.alumnigroup.app.acty;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;

import com.alumnigroup.api.IssuesAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.Issue;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.utils.EditTextUtils;
import com.custom.view.FlowLayout;

public class CommunicationPublish extends BaseActivity {
	private EditText et_title, et_content;
	private IssuesAPI api;
	private Issue issue;
	private FlowLayout flowLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_communicationpublish);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		issue = (Issue) getSerializableExtra("issue");
		api = new IssuesAPI();
	}

	@Override
	protected void initLayout() {
		_getView(R.id.acty_head_btn_back).setOnClickListener(this);
		_getView(R.id.acty_head_btn_post).setOnClickListener(this);
		_getView(R.id.btn_add_pic).setOnClickListener(this);
		et_title = (EditText) _getView(R.id.et_title);
		et_content = (EditText) _getView(R.id.et_content);

		if (issue != null) {
			et_title.setText(issue.getTitle());
			et_content.setText(issue.getBody());
		}
		flowLayout = (FlowLayout) _getView(R.id.flowlayout_container);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_head_btn_post:
			String title = EditTextUtils.getTextTrim(et_title);
			String body = EditTextUtils.getTextTrim(et_content);
			if (title == null || title.equals("")) {
				toast("标题不能为空!");
			}
			if (issue == null) { // 发布
				api.postIssue(title, body, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						toast("发布成功");
						closeActivity();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("发布失败 " + ErrorCode.errorList.get(errorCode));
					}
				});
			} else { // 更新
				api.updateIssue(issue.getId(), title, body,
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								toast("更新成功");
								closeActivity();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast("更新失败 "
										+ ErrorCode.errorList.get(errorCode));
							}
						});
			}
			break;
		// case R.id.mention:
		// // toast("mention");
		// Intent intent = new Intent(this, FollowingList.class);
		// intent.putExtra("userid", 1);
		// openActivity(intent);
		// break;

		case R.id.btn_add_pic:
			ImageView iv = new ImageView(getContext());
			iv.setBackgroundResource(R.drawable.ic_image_load_normal);
			flowLayout.addView(iv);
			// to pick a pic & add...
			break;
		default:
			break;
		}
	}

}
