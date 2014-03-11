package com.alumnigroup.widget;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alumnigroup.api.FeedbackAPI;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.MFeedback;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.AndroidUtils;

public class FeedbackDialog extends Dialog {

	private Context context;
	private EditText et;

	public FeedbackDialog(Context context) {
		super(context, R.style.Dialog_Theme_BaseDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_sendmsg);
		((TextView) findViewById(R.id.tv_title)).setText("反馈");
		et = (EditText) findViewById(R.id.et_body);
		findViewById(R.id.btn_comfirm).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if(et.getText().toString().equals("")){
							Toast.makeText(context, "不能为空", Toast.LENGTH_SHORT).show();
							return;
						}
                         FeedbackAPI api = new FeedbackAPI();
                         int versioncode= 0;
						try {
							versioncode = AndroidUtils.getAppVersionCode(context);
						} catch (NameNotFoundException e) {
							e.printStackTrace();
						}
                         api.sendFeedback(MFeedback.TYPE_SUGGESTION, et.getText().toString(), MFeedback.TYPE_SUGGESTION, versioncode+"", "none", new JsonResponseHandler() {
							
							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								Toast.makeText(context, "感谢你的反馈", Toast.LENGTH_SHORT).show();
								FeedbackDialog.this.dismiss();
							}
							
							@Override
							public void onFaild(int errorType, int errorCode) {
								Toast.makeText(context, ErrorCode.errorList.get(errorCode), Toast.LENGTH_SHORT).show();
								FeedbackDialog.this.dismiss();
								
							}
						});
					}
				});
		findViewById(R.id.btn_cancle).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						FeedbackDialog.this.dismiss();
					}
				});
	}

}
