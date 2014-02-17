package com.alumnigroup.api;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ActivityShareAPI {
	/**
	 * 获得第page页的分享<br>
	 * 
	 * @param page
	 *            第page页
	 * @param activityid 活动id
	 * @param responseHandler
	 *            处理器
	 */
	public void getShareList(int page,int activityid, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		if(page>0)params.add("page", page + "");
        if(activityid>0)params.add("activityid", activityid+"");  
		RestClient.get("/api/issues/find", params, responseHandler);
	}
	/**
	 * 发起话题<br>
	 * 微博就是只有标题么有内容的话题
	 * @param activityid
	 *       活动id
	 * @param title
	 *            标题
	 * @param body
	 *            话题内容
	 * @param responseHandler
	 *            处理器
	 */
	public void postShare(int activityid,String title, String body,
			AsyncHttpResponseHandler responseHandler) {
		if (title == null)
			throw new IllegalArgumentException("title can't be null !");
		RequestParams params = new RequestParams();
		params.add("activityid", activityid+"");
		params.add("title", title);
		if (body != null)
			params.add("body", body);
		RestClient.post("/api/issues/post", params, responseHandler);
	}

	/**
	 * 更新话题
	 * @parms activityid
	 *          活动id
	 * @param issueId
	 *            话题id
	 * @param title
	 *            话题标题 can be null
	 * @param body
	 *            话题内容 can be null
	 * @param responseHandler
	 *            处理器
	 */
	public void updateShare(int activityid,int issueId, String title, String body,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", issueId + "");
		if(activityid>0)params.add("activityid", activityid+"");
		if (title != null)
			params.add("title", title);
		if (body != null)
			params.add("body", body);
		RestClient.post("/api/issues/update", params, responseHandler);
	}

	/**
	 * 删除话题
	 * @param activityid 圈子id
	 * @param issueid
	 *            话题id
	 * @param responseHandler
	 *            处理器
	 */
	public void deleteShare(int activityid,int issueid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("activityid", activityid+"");
		params.add("id", issueid + "");
		RestClient.post("/api/issues/delete", params, responseHandler);
	}

	/**
	 * 评论话题（分享）
	 * @param activityid 活动id
	 * @param id
	 *            issueid
	 * @param responseHandler
	 *            处理器
	 */
	public void commentShare(int activityid,int id, String body,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("activityid", activityid+"");
		params.add("issueid", id + "");
		params.add("body", body);
		RestClient.post("/api/issues/comments/post", params, responseHandler);
	}

	/**
	 * 话题详情
	 * @param activityid 活动id
	 * @param id
	 *            话题id
	 * @param responseHandler
	 */
	public void view(int activityid,int id, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("activityid", activityid+"");
		params.add("id", id + "");
		RestClient.get("/api/issues/view", params, responseHandler);
	}
}
