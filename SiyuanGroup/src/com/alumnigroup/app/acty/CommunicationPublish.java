package com.alumnigroup.app.acty;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alumnigroup.api.IssuesAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.WidgetUtils;

public class CommunicationPublish extends BaseActivity {
	private View btn_back, btn_post, btn_permission, btn_mention;
	private EditText et_title, et_content;
	private IssuesAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_communicationpublish);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		api = new IssuesAPI();
	}

	@Override
	protected void initLayout() {
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_post = _getView(R.id.acty_head_btn_post);
		btn_permission = _getView(R.id.permission);
		btn_mention = _getView(R.id.mention);
		et_title = (EditText) _getView(R.id.et_title);
		et_content = (EditText) _getView(R.id.et_content);
		
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
			String title = WidgetUtils.getTextTrim(et_title);
			String body = WidgetUtils.getTextTrim(et_content);
			if (title == null || title.equals("")) {
				toast("标题不能为空!");
			}
			api.postIssue(title, body, new JsonResponseHandler() {

				@Override
				public void onOK(Header[] headers, JSONObject obj) {
					toast("发布成功");
					closeActivity();
				}

				@Override
				public void onFaild(int errorType, int errorCode) {
					if(errorCode==21301){
						toast("身份验证失败，请重新登录");
					}else{
						toast("发送失败 错误码:" + errorCode);
					}
					
				}
			});
			break;
		case R.id.permission:
			toast("permission");
			break;
		case R.id.mention:
			toast("mention");
			break;
		default:
			break;
		}
	}

}
