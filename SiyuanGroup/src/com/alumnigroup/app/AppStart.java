package com.alumnigroup.app;

 

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.alumnigroup.app.acty.Login;
import com.alumnigroup.app.acty.Main;
/** 启动页面
 * @author Jayin Ton
 *
 */
public class AppStart extends BaseActivity  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_start);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent intent = new Intent(AppStart.this,Login.class);
			//	startActivity(intent);
				openActivity(Main.class);
				closeActivity();
			}
 
		}, 1000);
	}

	@Override
	protected void initData() {
		 
		
	}

	@Override
	protected void initLayout() {
		 
		
	}
 
}
