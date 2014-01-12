package com.api;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 圈子系统API  
 * @author Jayin Ton
 *
 */
public class GroupAPI {

	public GroupAPI(){
		
	}
	/**
	 * 
	 * @param id
	 * @param ownerid
	 * @param name
	 * @param responseHandler
	 */
	public void find(int id,int ownerid,String name,AsyncHttpResponseHandler responseHandler){
		RequestParams params  = new RequestParams();
		params.add("id", id+"");
		params.add("ownerid", ownerid+"");
		params.add("name", name);
		RestClient.get("/api/groups/find", params, responseHandler);		
	}
	
}
