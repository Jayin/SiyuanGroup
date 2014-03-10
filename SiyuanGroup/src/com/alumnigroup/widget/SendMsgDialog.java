package com.alumnigroup.widget;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alumnigroup.api.MessageAPI;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.L;

/**
 * 私信发送窗口
 * 
 * @author Jayin Ton
 * 
 */
public class SendMsgDialog extends Dialog {
	private Context context;
	private EditText et;
	private int receiverid; // 收件人

	public SendMsgDialog(Context context) {
		super(context, R.style.Dialog_Theme_BaseDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_sendmsg);
		et = (EditText) findViewById(R.id.et_body);
		findViewById(R.id.btn_comfirm).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (et.getText().toString().equals("")) {
							Toast.makeText(getContext(), "不能为空",
									Toast.LENGTH_SHORT).show();
							return;
						}
						MessageAPI api = new MessageAPI();
                        String body  = et.getText().toString();
						api.send(receiverid, "title", body,
								new JsonResponseHandler() {

									@Override
									public void onOK(Header[] headers, JSONObject obj) {
										Toast.makeText(getContext(), "发送成功", Toast.LENGTH_SHORT).show();
										SendMsgDialog.this.dismiss();
									}

									@Override
									public void onFaild(int errorType, int errorCode) {
										Toast.makeText( getContext(), ErrorCode.errorList .get(errorCode), Toast.LENGTH_SHORT).show();
										SendMsgDialog.this.dismiss();
									}
								});
					}
				});
		findViewById(R.id.btn_cancle).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						SendMsgDialog.this.dismiss();
					}
				});
	}

	public void setReceiverId(int receiverid) {
		this.receiverid = receiverid;
	}
}
