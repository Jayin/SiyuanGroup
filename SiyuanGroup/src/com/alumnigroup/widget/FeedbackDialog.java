package com.alumnigroup.widget;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alumnigroup.api.FeedbackAPI;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.MFeedback;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.AndroidUtils;
import com.alumnigroup.utils.L;

public class FeedbackDialog extends Dialog {

	private Context context;
	private EditText et;
	private FeedbackLinstener listenser;
	private RadioGroup radioGroup;
	private int kind = 0;//0:软件意见 1:bug反馈
	   private LoadingDialog loadingDialog;

	public FeedbackDialog(Context context,FeedbackLinstener listenser) {
		super(context, R.style.Dialog_Theme_BaseDialog);
		this.context = context;
		this.listenser = listenser;
	    loadingDialog = new LoadingDialog(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_feedback);
		((TextView) findViewById(R.id.tv_title)).setText("反馈");
		et = (EditText) findViewById(R.id.et_body);
		radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
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
						String type = kind ==0 ?MFeedback.TYPE_SUGGESTION:MFeedback.TYPE_BUG;
                         api.sendFeedback(type, et.getText().toString(), type, versioncode+"", "none", new JsonResponseHandler() {
							@Override
							public void onStart() {
								loadingDialog.show();
							}
							
							@Override
							public void onFinish() {
								loadingDialog.dismiss();
							}
							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								Toast.makeText(context, "感谢你的反馈", Toast.LENGTH_SHORT).show();
								FeedbackDialog.this.dismiss();
								if(listenser!=null)listenser.onFinish();
								et.setText("");
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
		radioGroup.check(R.id.btn_advise);
		kind = 0;
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
			    if(checkedId == R.id.btn_advise){
			    	kind = 0;
			    	L.i("ad");
			    }else{
			    	kind = 1;
			    	L.i("bug");
			    }
				
			}
		});
	}
	
	public interface FeedbackLinstener{
		public void onFinish();
	}

}
