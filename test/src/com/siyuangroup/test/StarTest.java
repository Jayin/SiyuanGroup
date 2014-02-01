package com.siyuangroup.test;

import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.api.StarAPI;
import com.utils.L;
import com.utils.TempLogin;

public class StarTest extends BaseTestActivity {

	@Override
	public void test1Click() {
		TempLogin.login();

	}

	@Override
	public void test2Click() {
		getList();

	}

	private void getList() {
		StarAPI api = new StarAPI();
		api.getStarList(1, StarAPI.Item_type_issue, new Response());
	}

	@Override
	public void test3Click() {
		regist();
	}

	private void regist() {
		TempLogin.register();

	}

	@Override
	public void test4Click() {
		getpicFormLoacl();
	}

	int REQUEST_CODE_PICK_IMAGE = 2;
	private void getpicFormLoacl() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");// 相片类型
		startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==REQUEST_CODE_PICK_IMAGE && resultCode==RESULT_OK){
			 Uri uri = data.getData();  
			 L.i(uri.toString());
			 
		}
	}

	@Override
	public String test1Title() {
		return "login";
	}

	@Override
	public String test2Title() {
		return "收藏列表";
	}

	@Override
	public String test3Title() {
		return "register";
	}

	@Override
	public String test4Title() {
		return "getpic";
	}

	@Override
	public void onInit() {

	}

	@Override
	public String setTitle() {
		return this.getClass().getName();
	}

}
