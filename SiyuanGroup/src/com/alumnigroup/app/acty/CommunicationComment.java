package com.alumnigroup.app.acty;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alumnigroup.api.IssuesAPI;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Comment;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.Issue;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.utils.EditTextUtils;
import com.alumnigroup.utils.StringUtils;
import com.alumnigroup.widget.LoadingDialog;

/**
 * 校友交流，评论页面
 * 
 * @author Jayin Ton
 * 
 */
public class CommunicationComment extends BaseActivity {
	private View btn_back, btn_post, btn_mention;
	private EditText et_content;
	private IssuesAPI api;
	private Issue issue;
	private LoadingDialog loadingdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_communicationcomment);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		api = new IssuesAPI();
		issue = (Issue) getSerializableExtra("issue");
	}

	@Override
	protected void initLayout() {
		loadingdialog  =new LoadingDialog(getContext());
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_post = _getView(R.id.acty_head_btn_post);
		btn_mention = _getView(R.id.mention);
		et_content = (EditText) _getView(R.id.et_content);

		btn_back.setOnClickListener(this);
		btn_post.setOnClickListener(this);
		btn_mention.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_head_btn_post:
			final String body = EditTextUtils.getTextTrim(et_content);
			if (body == null || StringUtils.isEmpty(body)) {
				toast("还是写点东西吧！");
				return;
			}
			api.commentIssue(issue.getId(), body, new JsonResponseHandler() {

				@Override
				public void onOK(Header[] headers, JSONObject obj) {
                       toast("评论成功");
                       closeActivity();
                       //发送广播通知更新
                       Comment comment = new Comment();
                       comment.setBody(body);
                       comment.setPosttime(System.currentTimeMillis());
                       comment.setUser(AppInfo.getUser(getContext()));
                       Intent intent = new Intent(Constants.Action_Issue_Comment_Ok);
                       intent.putExtra("comment", comment);
                       sendBroadcast(intent);
				}

				@Override
				public void onFaild(int errorType, int errorCode) {
                    toast("评论失败 "+ErrorCode.errorList.get(errorCode));
				}
				
				@Override
				public void onStart() {
					loadingdialog.setText("发布中...");
					loadingdialog.show();
				}
				
				@Override
				public void onFinish() {
					loadingdialog.dismiss();
				}
			});
			break;
		case R.id.mention:
			toast("memtion");
			break;
		default:
			break;
		}
	}
}
