package com.alumnigroup.api;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 消息接口
 * 
 * @author Jayin Ton
 * 
 */
public class MessageAPI {

	public MessageAPI() {

	}
    /**
     * 发消息
     * @param receiver 收件人id
     * @param title 标题
     * @param body 内容
     * @param responseHandler
     */
	public void send(int receiverid, String title, String body,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		if (receiverid > 0)
			params.add("receiverid", receiverid + "");
		if (title != null)
			params.add("title", title);
		if (body != null)
			params.add("body", body);
		RestClient.post("/api/messages/send", params, responseHandler);
	}
	/**
	 * 回复
	 * @param sourceid  要回复的消息id
	 * @param title  标题
	 * @param body 内容
	 * @param responseHandler
	 */
	public void reply(int sourceid ,String title,String body,AsyncHttpResponseHandler responseHandler){
		RequestParams params = new RequestParams();
		if (sourceid > 0)
			params.add("sourceid", sourceid + "");
		if (title != null)
			params.add("title", title);
		if (body != null)
			params.add("body", body);
		RestClient.post("/api/messages/reply", params, responseHandler);
	}
	/**
	 * 获取未读消息数量<br>
	 * 需登录
	 * @param responseHandler
	 */
	public void getUnreadCount(AsyncHttpResponseHandler responseHandler){
		RestClient.get("/api/messages/unreadcount", null, responseHandler);
	}
	/**
	 * 标记为已读
	 * @param id 消息id
	 * @param responseHandler
	 */
	public void markRead(int id  ,AsyncHttpResponseHandler responseHandler){
		RequestParams params = new RequestParams();
		if (id  > 0)
			params.add("id", id  + "");
		RestClient.post("/api/messages/markread", params, responseHandler);
	}
	/**
	 * 收消息列表
	 * @param limit 请求书
	 * @param responseHandler
	 */
	public void getReceiedMessageList(int limit,AsyncHttpResponseHandler responseHandler){
		RequestParams params =new RequestParams();
		params.add("limit", limit+"");
		RestClient.get("/api/messages/receivelist", params, responseHandler);
	}
	 /**
	  *  发消息列表
	  * @param page 页码
	  * @param responseHandler
	  */
	public void getSendMessageList(int page,AsyncHttpResponseHandler responseHandler){
		RequestParams params =new RequestParams();
		params.add("page", page+"");
		RestClient.get("/api/messages/sendlist", params, responseHandler);
	}
	
}







