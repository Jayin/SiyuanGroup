package com.siyuangroup.test;


import com.api.ActivityAPI;
import com.api.GroupAPI;
import com.utils.TempLogin;
import android.view.View;
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
		api.creatAcivity(1, 50, System.currentTimeMillis(), 5, 0, 1000, "HKU Visition", "visite hku","HK",new Response()); 
		
	}

	@Override
	public void test2Click() {
            GroupAPI api = new GroupAPI();
            api.join(1, new Response());
	}

	@Override
	public void test3Click() {
       TempLogin.login();
	}

	@Override
	public void test4Click() {
       ActivityAPI api  =new ActivityAPI();
       api.getUserList(1, new Response());
	}

	@Override
	public String test1Title() {
		return "create";
	}

	@Override
	public String test2Title() {
		return "joinGroup1";
	}

	@Override
	public String test3Title() {
		return "login";
	}

	@Override
	public String test4Title() {
		return "获取活动名单";
	}

	@Override
	public void onInit() {

	}

	@Override
	public String setTitle() {
		return "Activity api test";
	}
}
