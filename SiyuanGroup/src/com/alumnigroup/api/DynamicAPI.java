package com.alumnigroup.api;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 
 * @author vector
 * 
 */
public class DynamicAPI {
	/**
	 * 拿到全站动态的列表
	 * @param page  页码
	 * @param responseHandler
	 */
	public void getAll(int page, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("page", page + "");
		RestClient.get("/api/events/list", params, responseHandler);
	}
	/**
	 *  拿到指定用户的动态
	 * @param page 页码
	 * @param userid 用户id
	 * @param responseHandler
	 */
	public void get(int page, int userid,AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("page", page + "");
		params.add("userid", userid + "");
		RestClient.get("/api/events/list", params, responseHandler);
	}
	/**
	 * 拿到指定用户的签名 limit 条动态
	 * @param limit
	 * @param userid
	 * @param responseHandler
	 */
	public void getLimit(int limit, int userid,AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("userid", userid + "");
		params.add("limit", limit + "");
		RestClient.get("/api/events/list", params, responseHandler);
	}
	/**
	 * 拿到我关注的用户的动态
	 * @param page
	 * @param responseHandler
	 */
	public void getMyFollowing(int page ,AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("page", page + "");
		RestClient.get("/api/events/following", params, responseHandler);
	}
	
	
}
