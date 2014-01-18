package com.siyuangroup.test;

import com.api.GroupAPI;
import com.siyuangroup.test.BaseTestActivity.Response;
import com.utils.TempLogin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class GroupTest extends BaseTestActivity {
    private int count1 = 0;
    private int count2 = 1;
    private int count3 = 1;
	@Override
	public void test1Click() {
		toast("createGroup");
		switch (count1) {
		case 0:
			count1++;
			 createGroup( "node fun",   "programe in node.js  is very funny!");
			break;
		case 1:
			 count1++;
			 createGroup( "nodejs爱好者",   "来吧，加入node,赢取白富美,出任CEO");
		case 2:
			 count1++;
			 createGroup( "nodejs爱好者",   "来吧，加入node,赢取白富美,出任CEO");
		case 3:
			 count1++;
			 createGroup( "Java",   "来吧，加入Java,赢取白富美,出任CEO");
		default:
			break;
		}
     
	}

	private void createGroup(String name, String description) {
		GroupAPI api = new GroupAPI();
		api.createGroup(name, description, new Response());
	}

	@Override
	public void test2Click() {
		toast("getMyGroupList");
		switch (count2) {
		case 1:
			getMyGroupList(count2++);
			break;
		case 2:
			getMyGroupList(count2++);
		default:
			break;
		}
		
	}

	private void getMyGroupList(int page) {
		GroupAPI api = new GroupAPI();
		api.getMyGroupList(page, new Response());
		
	}

	@Override
	public void test3Click() {
		toast("getGroupList   page="+count3);
		getGroupList(count3);
	}

	private void getGroupList(int count3) {
		GroupAPI api = new GroupAPI();
		api.getGroupList(count3++, new Response());
	}

	@Override
	public void test4Click() {
//        toast("login");
//       TempLogin.register();
//        TempLogin.login();
//		joinGroup(8);
//		joinGroup(9);
		toast("exit");
		exitGroup(8);
	}

	private void joinGroup(int groupid) {
		 GroupAPI api = new GroupAPI();
		 api.join(groupid, new Response());
		
	}
	
	private void exitGroup(int groupid){
		GroupAPI api = new GroupAPI();
		api.exit(groupid, new Response());

	}
	

	@Override
	public void onInit() {

	}

	@Override
	public String setTitle() {
		return "GroupAPI test";
	}

	@Override
	public String test1Title() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String test2Title() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String test3Title() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String test4Title() {
		// TODO Auto-generated method stub
		return null;
	}

	 

}
