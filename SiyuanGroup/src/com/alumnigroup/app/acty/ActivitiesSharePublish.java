package com.alumnigroup.app.acty;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alumnigroup.api.ActivityShareAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.Issue;
import com.alumnigroup.entity.MActivity;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.utils.EditTextUtils;
/**
 * 活动分享发布
 * @author Jayin Ton
 *
 */
public class ActivitiesSharePublish extends BaseActivity {

	private View btn_back, btn_post, btn_permission, btn_mention;
	private EditText et_title, et_content;
	private ActivityShareAPI api;
	private Issue mIssue;
    private MActivity activity; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_activitiessharepublish);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		mIssue = (Issue) getSerializableExtra("issue");
		activity = (MActivity) getSerializableExtra("activity");
		api = new ActivityShareAPI();
	}

	@Override
	protected void initLayout() {
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_post = _getView(R.id.acty_head_btn_post);
		btn_permission = _getView(R.id.permission);
		btn_mention = _getView(R.id.mention);
		et_title = (EditText) _getView(R.id.et_title);
		et_content = (EditText) _getView(R.id.et_content);

		if (mIssue != null) {
			et_title.setText(mIssue.getTitle());
			et_content.setText(mIssue.getBody());
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
			if (mIssue == null) { // 发布
				 post(title,body);
			} else { // 更新
				updateShare(title,body);
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

	private void post(final String title,final String body) {
		api.postShare(activity.getId(),title, body, new JsonResponseHandler() {

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
		
	}

	private void updateShare(final String title,final String body) {
		api.updateShare(activity.getId(),mIssue.getId(), title, body,
				new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						toast("更新成功");
						closeActivity();
						//发送修改广播
						Intent intent = new Intent(Constants.Action_ActivityShare_Edit);
					    Issue issue = new Issue();
					    issue.setBody(body);
					    issue.setId(mIssue.getId());
					    issue.setNumComments(mIssue.getNumComments());
					    issue.setPosttime(System.currentTimeMillis());
					    issue.setTitle(title);
					    issue.setUser(mIssue.getUser());
					    intent.putExtra("issue", issue);
					    sendBroadcast(intent);
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("更新失败 "
								+ ErrorCode.errorList.get(errorCode));
					}
				});
		
	}
}
