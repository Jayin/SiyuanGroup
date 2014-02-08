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
	 * @param page
	 * @param responseHandler
	 */
	public void getAll(int page, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("page", page + "");
		RestClient.get("/api/events/find", params, responseHandler);
	}
	/**
	 * 空间时候拿全站动态的列表
	 * @param page
	 * @param responseHandler
	 */
	public void getAll(int page, int userid,AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("page", page + "");
		params.add("userid", userid + "");
		params.add("limit", 5 + "");
		RestClient.get("/api/events/find", params, responseHandler);
	}
}
