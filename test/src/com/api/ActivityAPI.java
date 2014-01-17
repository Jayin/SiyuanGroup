package com.api;

import android.location.Address;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 活动系统API
 * 
 * @author Jayin Ton 
 *                      
 */
public class ActivityAPI {
	public ActivityAPI() {

	}

	/**
	 * 根据给定的条件查找活动列表
	 * 
	 * @param page
	 *            第几页数
	 * @param activityId
	 *            活动id
	 * @param ownerid
	 *            活动发起人id
	 * @param groupid
	 *            圈子id
	 * @param content
	 *            内容
	 * @param statusid
	 *            状态id
	 * @param responseHandler
	 *            处理器
	 */
	public void find(int page, int activityId, int ownerid, int groupid,
			String content, int statusid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		if (activityId > 0)
			params.add("id", activityId + "");
		if (ownerid > 0)
			params.add("ownerid", ownerid + "");
		if (groupid > 0)
			params.add("groupid", groupid + "");
		if (statusid > 0)
			params.add("statusid", statusid + ""); // 这里得注意了statusid的取值
		if (content != null)
			params.add("content", content);
		RestClient.get("/api/activities/find", params, responseHandler);
	}
    /**
     * 获取活动列表 
     * @param page 第几页
     * @param responseHandler 处理器
     */
	public void getActivityList(int page,
			AsyncHttpResponseHandler responseHandler) {
		find(page, 0, 0, 0, null, 0, responseHandler);
	}
	/**
	 * 加入一个活动
	 * @param activityId 活动id
	 * @param responseHandler 处理器
	 */
	public void joinActivity(int activityId,AsyncHttpResponseHandler responseHandler){
		RequestParams params = new RequestParams();
		params.add("id", activityId+"");
		RestClient.post("/api/activities/join", params, responseHandler);
	}
	
	public void accept(){
		
	}
	
	
	
	
	
}
