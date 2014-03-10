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
	 * 
	 * @param receiver
	 *            收件人id
	 * @param title
	 *            标题
	 * @param body
	 *            内容
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
	 * 获取未读消息数量<br>
	 * 需登录
	 * 
	 * @param responseHandler
	 */
	public void getUnreadCount(AsyncHttpResponseHandler responseHandler) {
		RestClient.get("/api/messages/unreadcount", null, responseHandler);
	}
    /**
     * 消息列表
     * @param page
     * @param responseHandler
     */
	public void getMessageList(int page,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		if(page>0)
			params.add("page", page+"");
		RestClient.get("/api/messages/list", params, responseHandler);
	}
	/**
	 * 获得聊天记录
	 * @param page 页码
	 * @param friendid 对方id
	 * @param responseHandler
	 */
	public void getChatRecord(int page,int friendid,
			AsyncHttpResponseHandler responseHandler){
		RequestParams params = new RequestParams();
		if(page>0)
			params.add("page", page+"");
		if(friendid>0)
			params.add("friendid", friendid+"");
		RestClient.get("/api/messages/record", params, responseHandler);
	}

}
