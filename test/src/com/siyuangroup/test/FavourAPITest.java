package com.siyuangroup.test;

import com.api.StarAPI;

public class FavourAPITest extends BaseTestActivity {

	@Override
	public void test1Click() {

		getFavouriteList();
	}

	private void getFavouriteList() {
       
		StarAPI api = new StarAPI();
		api.getStarList(1, StarAPI.Item_type_user	, new Response());
	}

	@Override
	public void test2Click() {
	    
			StarAPI api = new StarAPI();
			api.getStarList(1, StarAPI.Item_type_issue	, new Response());

	}

	@Override
	public void test3Click() {
	    
			StarAPI api = new StarAPI();
			api.getStarList(1, StarAPI.Item_type_activity	, new Response());

	}

	@Override
	public void test4Click() {
	    
			StarAPI api = new StarAPI();
			api.getStarList(1, StarAPI.Item_type_business	, new Response());

	}

	@Override
	public String test1Title() {
		// TODO Auto-generated method stub
		return "user";
	}

	@Override
	public String test2Title() {
		// TODO Auto-generated method stub
		return "issue";
	}

	@Override
	public String test3Title() {
		// TODO Auto-generated method stub
		return "activity";
	}

	@Override
	public String test4Title() {
		// TODO Auto-generated method stub
		return "business";
	}

	@Override
	public void onInit() {

	}

	@Override
	public String setTitle() {
		// TODO Auto-generated method stub
		return "faviour api test";
	}

}
