package com.alumnigroup.app.acty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;

/**
 * 通知用户去更新下载新版本
 * 
 * @author Jayin Ton
 * 
 */
public class AppUpdate extends BaseActivity {
	private TextView tv_updateInfo;
	private View btn_update, btn_cancel;
	private String updateInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_appupdate);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		updateInfo = getStringExtra("updateInfo");
		if (updateInfo == null ) {
			closeActivity();
		}
	}

	@Override
	protected void initLayout() {
		tv_updateInfo = (TextView) _getView(R.id.tv_updateInfo);
		btn_update = _getView(R.id.btn_update);
		btn_cancel = _getView(R.id.btn_cancel);
		tv_updateInfo.setText(updateInfo);
		
		btn_update.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_update:
			Uri uri = Uri.parse(RestClient.BASE_URL+"/api/clients/download");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			openActivity(intent);
			closeActivity();
			break;
		case R.id.btn_cancel:
			closeActivity();
			break;
		default:
			break;
		}
	}
}
