package com.siyuangroup.test;

import org.apache.http.Header;

import com.api.ActivityAPI;
import com.api.GroupAPI;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.utils.TempLogin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ActivityTest extends BaseTestActivity {
	private View test1, test2, test3;
	private TextView tv;

	private void joinActivity(int actyid) {
		ActivityAPI api = new ActivityAPI();
		api.joinActivity(actyid, new Response());
	}

	private void getActivityList(int page) {
		ActivityAPI api = new ActivityAPI();
		api.getActivityList(page, new Response());
	}

	@Override
	public void test1Click() {
      createActy();
	}

	private void createActy() {
		ActivityAPI api  = new ActivityAPI();
	}

	@Override
	public void test2Click() {

	}

	@Override
	public void test3Click() {

	}

	@Override
	public void test4Click() {

	}

	@Override
	public String test1Title() {
		return null;
	}

	@Override
	public String test2Title() {
		return null;
	}

	@Override
	public String test3Title() {
		return null;
	}

	@Override
	public String test4Title() {
		return null;
	}

	@Override
	public void onInit() {

	}

	@Override
	public String setTitle() {
		return "Activity api test";
	}
}
