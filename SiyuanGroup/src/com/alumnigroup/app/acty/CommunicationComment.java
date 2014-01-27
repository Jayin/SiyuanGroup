package com.alumnigroup.app.acty;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alumnigroup.api.IssuesAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.Issue;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.StringUtils;
import com.alumnigroup.utils.EditTextUtils;

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
			String body = EditTextUtils.getTextTrim(et_content);
			if (body == null || StringUtils.isEmpty(body)) {
				toast("写点东西吧！");
				return;
			}
			api.commentIssue(issue.getId(), body, new JsonResponseHandler() {

				@Override
				public void onOK(Header[] headers, JSONObject obj) {
                       toast("评论成功");
                       closeActivity();
				}

				@Override
				public void onFaild(int errorType, int errorCode) {
                    toast("网络异常 错误代码:"+ErrorCode.errorList.get(errorCode));
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
