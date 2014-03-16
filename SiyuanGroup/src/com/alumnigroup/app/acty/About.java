package com.alumnigroup.app.acty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.CoreService;
import com.alumnigroup.app.R;
import com.alumnigroup.utils.AndroidUtils;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.widget.LoadingDialog;

/**
 * 关于页面
 * 
 * @author crete by Vector finally coded by Jayin
 * 
 */
public class About extends BaseActivity {
	private LoadingDialog dialog;
	private BroadcastReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_about);
		initData();
		initLayout();
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				dialog.dismiss();
				int versioncode = intent.getIntExtra("versioncode", 0);
				try {
					if (versioncode == 0) {
						toast("检查版本失败");
					} else if (versioncode <= AndroidUtils
							.getAppVersionCode(getContext())) {
						toast("已经是最新版");
					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
					toast("检查版本失败");
				}

			}
		};
		registerReceiver(mReceiver, new IntentFilter(
				Constants.Action_Receive_VersionInfo));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initLayout() {
		_getView(R.id.acty_head_btn_back).setOnClickListener(this);
		_getView(R.id.btn_checkversion).setOnClickListener(this);
		try {
			((TextView) _getView(R.id.tv_verisonCode)).setText("version "
					+ AndroidUtils.getAppVersionName(getContext()));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		dialog = new LoadingDialog(getContext());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.btn_checkversion:
			checkVerison();
			dialog.show();
			break;
		default:
			break;
		}
	}

	private void checkVerison() {
		Intent service = new Intent(getContext(), CoreService.class);
		service.setAction(Constants.Action_checkVersion);
		startService(service);
	}

}
