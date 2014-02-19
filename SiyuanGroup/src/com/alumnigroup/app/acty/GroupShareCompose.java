package com.alumnigroup.app.acty;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.alumnigroup.api.GroupShareAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.Issue;
import com.alumnigroup.entity.MGroup;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.EditTextUtils;

/**
 * 圈子分享发布
 * 
 * @author Jayin Ton
 * 
 */
public class GroupShareCompose extends BaseActivity {
	private View btn_back, btn_post, btn_permission, btn_mention;
	private EditText et_title, et_content;
	private GroupShareAPI api;
	private Issue issue;
	private MGroup group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_groupsharecompose);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		issue = (Issue) getSerializableExtra("issue");
		group = (MGroup) getSerializableExtra("group");
		api = new GroupShareAPI();
	}

	@Override
	protected void initLayout() {
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_post = _getView(R.id.acty_head_btn_post);
		btn_permission = _getView(R.id.permission);
		btn_mention = _getView(R.id.mention);
		et_title = (EditText) _getView(R.id.et_title);
		et_content = (EditText) _getView(R.id.et_content);

		if (issue != null) {
			et_title.setText(issue.getTitle());
			et_content.setText(issue.getBody());
		}

		btn_back.setOnClickListener(this);
		btn_post.setOnClickListener(this);
		btn_permission.setOnClickListener(this);
		btn_mention.setOnClickListener(this);

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
				api.postShare(group.getId(),title, body, new JsonResponseHandler() {

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
				api.updateShare(group.getId(),issue.getId(), title, body,
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								toast("发布成功");
								closeActivity();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast("发布失败 "
										+ ErrorCode.errorList.get(errorCode));
							}
						});
			}
			break;
		case R.id.permission:
			toast("permission");
			break;
		case R.id.mention:
			// toast("mention");
//			Intent intent = new Intent(this, FollowingList.class);
//			intent.putExtra("userid", 1);
//			openActivity(intent);
			break;
		default:
			break;
		}
	}

}
