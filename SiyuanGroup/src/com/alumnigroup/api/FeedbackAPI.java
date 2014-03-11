package com.alumnigroup.api;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 反馈接口
 * 
 * @author Jayin Ton
 * 
 */
public class FeedbackAPI {

	public FeedbackAPI() {

	}

	/**
	 * 获得反馈列表
	 * 
	 * @param page
	 * @param responseHandler
	 */
	public void getFeedbackList(int page,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("page", page + "");
		RestClient.get("/api/feedbacks/list", params, responseHandler);
	}
   /**
    * 发送反馈
    * @param title
    * @param body
    * @param type
    * @param versioncode
    * @param device
    * @param responseHandler
    */
	public void sendFeedback(String title, String body, String type,
			String versioncode, String device,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("title", title);
		params.add("body", body);
		params.add("type", type);
		params.add("versioncode", versioncode);
		params.add("device", device);
		RestClient.post("/api/feedbacks/post", params, responseHandler);
	}
}
