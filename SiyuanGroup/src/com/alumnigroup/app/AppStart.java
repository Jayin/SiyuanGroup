package com.alumnigroup.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.alumnigroup.app.acty.Login;
import com.alumnigroup.app.acty.Main;
import com.alumnigroup.utils.DataPool;

/**
 * 启动页面
 * 
 * @author Jayin Ton
 * 
 */
public class AppStart extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_start);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				init();
				if (checkLoginInfo()) {
					openActivity(Main.class);
				} else {
					openActivity(Login.class);
				}
				closeActivity();
			}
		}, 1500);
	}

	// 初始化工作
	protected void init() {
		// start up the core service
		Intent intent = new Intent(getContext(), CoreService.class);
		sendBroadcast(intent);
	}

	private boolean checkLoginInfo() {
		DataPool dp = new DataPool(DataPool.SP_Name_User, this);
		if (dp.contains(DataPool.SP_Key_User))
			return true;
		else
			return false;
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initLayout() {

	}

}
