package com.alumnigroup.app.acty;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alumnigroup.api.BusinessAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.EditTextUtils;
import com.alumnigroup.utils.StringUtils;
import com.alumnigroup.widget.TimePickDialog;
import com.alumnigroup.widget.TimePickDialog.OnSiglePickFinishedListener;

public class BusinessPublish extends BaseActivity {
	private View btn_back, btn_create, btn_deadline, btn_isprivate;
	private EditText et_name, et_description, et_company;
	private TextView tv_deadline, tv_isPrivate;
	private TimePickDialog dialog;
	private long regdeadline;
	private BusinessAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_businesspublish);
		initData();
		initLayout();

	}

	@Override
	protected void initData() {
		api = new BusinessAPI();
	}

	@Override
	protected void initLayout() {
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_create = _getView(R.id.acty_head_btn_create);
		btn_deadline = _getView(R.id.btn_deadline);
		btn_isprivate = _getView(R.id.isprivate);

		et_name = (EditText) _getView(R.id.et_name);
		et_description = (EditText) _getView(R.id.et_description);
		et_company = (EditText) _getView(R.id.et_company);

		tv_deadline = (TextView) _getView(R.id.tv_deadline);
		tv_isPrivate = (TextView) _getView(R.id.tv_isprivate);

		btn_back.setOnClickListener(this);
		btn_create.setOnClickListener(this);
		btn_deadline.setOnClickListener(this);
		btn_isprivate.setOnClickListener(this);

		dialog = new TimePickDialog(getContext());
		dialog.setOnSiglePickFinishedListener(new OnSiglePickFinishedListener() {

			@Override
			public void onFinish(long selecttime) {
				regdeadline = selecttime;
				tv_deadline.setText(CalendarUtils.getTimeFromat(selecttime,
						CalendarUtils.TYPE_TWO));

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_head_btn_create:
			if (check()) {
				String name = EditTextUtils.getTextTrim(et_name);
				String description = EditTextUtils.getTextTrim(et_description);
				String company = EditTextUtils.getTextTrim(et_company);
				int statusid = 1;
				int isprivate = tv_isPrivate.getText().equals("是") ? 1 : 0;
				api.create(name, description, company, regdeadline, statusid,
						isprivate, new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								toast("发布成功");
								closeActivity();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast("网络异常 错误码:" + errorCode);

							}
						});
			}
			break;
		case R.id.btn_deadline:
			dialog.show();
			break;
		case R.id.isprivate:
			if (tv_isPrivate.getText().equals("是")) {
				tv_isPrivate.setText("否");
			} else {
				tv_isPrivate.setText("是");
			}
			break;

		default:
			break;
		}
	}

	private boolean check() {
		if (EditTextUtils.isEmpty(et_name)) {
			toast("项目标题不能为空");
			return false;
		}
		if (EditTextUtils.isEmpty(et_description)) {
			toast("项目简介不能为空");
			return false;
		}
		if (EditTextUtils.isEmpty(et_company)) {
			toast("公司组织不能为空");
			return false;
		}
		if (StringUtils.isEmpty(tv_deadline.getText().toString())) {
			toast("截止日期不能为空");
			return false;
		}
		return true;
	}

}
