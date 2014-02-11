package com.alumnigroup.api;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 版本接口
 * 
 * @author Jayin Ton
 * 
 */
public class VersionAPI {

	public VersionAPI() {

	}

	/**
	 * 最新版本信息
	 * 
	 * @param responseHandler
	 */
	public void getLatestInfo(AsyncHttpResponseHandler responseHandler) {
		RestClient.get("/api/clients/latest", null, responseHandler);
	}

}
