package com.api;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 话题API
 * 
 * @author Jayin Ton
 * 
 */
public class IssuesAPI {
	/**
	 * 获得第page页的话题<br>
	 * @param page
	 *            第page页
	 * @param responseHandler
	 *            处理器
	 */
	public void getIssueList(int page, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("page", page + "");
		RestClient.get("/api/issues/find", params, responseHandler);
	}

	/**
	 * 可以根据'id', 'userid', 'title' 来获取话题列表<br>
	 * 基本上没用，不知道为啥要给这个。不过注意给出page参数
	 * 
	 * @param params
	 *            参数
	 * @param responseHandler
	 *            处理器
	 */
	public void getIssueList(RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		RestClient.get("/api/issues/find", params, responseHandler);
	}

	/**
	 * 搜索
	 * 
	 * @param page
	 *            第几页
	 * @param title
	 *            标题中包含的关键字
	 * @param body
	 *            话题内容包含关键字
	 * @param responseHandler
	 *            处理器
	 */
	public void search(int page, String title, String body,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("page", page + "");
		if (title != null && title.trim().length() > 0)
			params.add("title", title);
		if (body != null && body.trim().length() > 0)
			params.add("body", body);
		RestClient.get("/api/issues/search", params, responseHandler);
	}

	/**
	 * 发起话题<br>
	 * 微博就是只有标题么有内容的话题
	 * 
	 * @param title
	 *            标题
	 * @param body
	 *            话题内容
	 * @param responseHandler
	 *            处理器
	 */
	public void postIssue(String title, String body,
			AsyncHttpResponseHandler responseHandler) {
		if (title == null)
			throw new IllegalArgumentException("title can't be null !");
		RequestParams params = new RequestParams();
		params.add("title", title);
		if (body != null)
			params.add("body", body);
		RestClient.post("/api/issues/post", params, responseHandler);
	}
   /**
    * 更新话题
    * @param issueId 话题id
    * @param title 话题标题  can be null
    * @param body  话题内容 can be null
    * @param responseHandler   处理器
    */
	public void updateIssue(int issueId, String title, String body,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", issueId+"");
		if (title != null)
			params.add("title", title);
		if (body != null)
			params.add("body", body);
		RestClient.post("/api/issues/update", params, responseHandler);
	}
	/**
	 * 删除话题
	 * @param issueid 话题id
	 * @param responseHandler  处理器
	 */
	public void deleteIssue(int issueid,AsyncHttpResponseHandler responseHandler){
		RequestParams params = new RequestParams();
		params.add("id", issueid+"");
		RestClient.post("/api/issues/delete", params, responseHandler);
	}
	/**
	 * 话题详情
	 * @param id issueid 
	 * @param responseHandler 处理器
	 */
	public void commentIssue(int id,String body,AsyncHttpResponseHandler responseHandler){
		RequestParams params = new RequestParams();
		params.add("id", id+"");
		params.add("body", body);
		RestClient.post("/api/issues/comment", params, responseHandler);
	}
	/**
	 * 话题详情 
	 * @param id 话题id
	 * @param responseHandler
	 */
	public void view(int id,AsyncHttpResponseHandler responseHandler){
		RequestParams params = new RequestParams();
		params.add("id", id+"");
		RestClient.get("/api/issues/view", params, responseHandler);
	}
}
