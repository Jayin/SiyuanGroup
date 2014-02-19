package com.alumnigroup.api;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * 反馈
 * @author vector
 *
 */
public class TicklingAPI {
	
	/**
	 * 发送反馈内容
	 * @param content
	 * @param responseHandler
	 */
	public void sendTickling(String content,int versioncode,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("body", content);
		params.add("type", 1+"");
		params.add("versioncode", versioncode+"");
		RestClient.post("/api/feedbacks/post", params, responseHandler);
	}

}
