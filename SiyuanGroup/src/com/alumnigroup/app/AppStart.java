package com.alumnigroup.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.alumnigroup.app.acty.ActivitiesPublish;
import com.alumnigroup.app.acty.Login;
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
				Intent intent = new Intent(AppStart.this, Login.class);
				// startActivity(intent);
				if (checkLoginInfo()) {
					openActivity(ActivitiesPublish.class);
				} else {
					openActivity(Login.class);
				}

				closeActivity();
			}

		}, 1000);
	}

	private boolean checkLoginInfo() {
		DataPool dp = new DataPool(DataPool.SP_Name_User,this);
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
