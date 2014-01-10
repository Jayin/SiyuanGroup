package com.alumnigroup.app;

 

import android.os.Bundle;
import android.os.Handler;

import com.alumnigroup.app.acty.Allmember;
import com.alumnigroup.app.acty.Login;
 
/**
 * 启动页面
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
				openActivity(Allmember.class);
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
