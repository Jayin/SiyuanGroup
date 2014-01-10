package com.siyuangroup.test;

import com.api.RestClient;

import android.app.Application;

public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		RestClient.init(getApplicationContext());
	}
}
