package com.alumnigroup.app.acty;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alumnigroup.api.TicklingAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.AndroidUtils;

/**
 * 意见反馈
 * 
 * @author vector
 * 
 */
public class Tickling extends BaseActivity {

	private View btnBack, btnSend;
	private EditText etContent;
	private AlertDialog dialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_tickling);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initLayout() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("发送中···");
		dialog = builder.create();
		
		btnBack = _getView(R.id.acty_head_btn_back);
		btnBack.setOnClickListener(this);

		btnSend = _getView(R.id.acty_head_btn_send);
		btnSend.setOnClickListener(this);

		etContent = (EditText) _getView(R.id.acty_tickling_et_content);
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.acty_head_btn_back:
			finish();
			break;

		case R.id.acty_head_btn_send:
			String content = etContent.getText().toString();
			if (content.length() < 10) {
				toast("内容不能少于10个字");
				break;
			}
			v.setClickable(false);
			sendTickling(content);
			break;

		default:
			break;
		}
	}

	private void sendTickling(String content) {
		TicklingAPI api = new TicklingAPI();
		try {
			api.sendTickling(content,AndroidUtils.getAppVersionCode(this), new JsonResponseHandler() {
				
				@Override
				public void onFinish() {
					super.onFinish();
					dialog.cancel();
					btnSend.setClickable(true);
				}

				@Override
				public void onStart() {
					super.onStart();
					dialog.show();
					
				}

				@Override
				public void onOK(Header[] headers, JSONObject obj) {
					toast("感谢您的宝贵意见");
					finish();
				}

				@Override
				public void onFaild(int errorType, int errorCode) {
					toast("发送失败");
				}
			});
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			toast("发送失败");
			btnSend.setClickable(true);
		}
	}
}
