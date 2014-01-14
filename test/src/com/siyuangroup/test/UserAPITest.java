package com.siyuangroup.test;


import com.api.UserAPI;

public class UserAPITest extends BaseTestActivity {
	private String username = "test11";
	private String password = "12345678";
	private String profile_name = "jayin";
	private String profile_nickname = "ton";
	private String profile_email = "g@py.cn";

	@Override
	public void test1Click() {
		register();
	}

	private void register() {
		UserAPI api = new UserAPI();
		api.regist(username, password, profile_nickname, profile_email,
				new Response());
	}

	@Override
	public void test2Click() {
		login();
	}

	private void login() {
		UserAPI api = new UserAPI();
		api.login(username, "123456", new Response());

	}

	@Override
	public void test3Click() {
		resetpsw("12345678", "123456");
		
	}
	
	private void resetpsw(String oldpsw, String newpsw) {
		UserAPI api = new UserAPI();
		api.resetPassword(oldpsw, newpsw, new Response());
	}
	@Override
	public void test4Click() {
		 //login out
		UserAPI api = new UserAPI();
		api.logout(new Response());
	}
	

	@Override
	public void onInit() {

	}

	@Override
	public String setTitle() {
		return "UserApiTest";
	}

	

}