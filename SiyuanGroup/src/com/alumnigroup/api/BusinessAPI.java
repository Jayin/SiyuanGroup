package com.alumnigroup.api;

import com.alumnigroup.utils.CalendarUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 商务合作API
 * 
 * @author Jayin Ton
 * 
 */
public class BusinessAPI {
	public BusinessAPI() {

	}

	/**
	 * 删除评论
	 * 
	 * @param id
	 *            评论ID
	 * @param responseHandler
	 *            处理器
	 */
	public void deleteComment(int id, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", id + "");
		RestClient.get("/api/cooperations/comments/delete", params,
				responseHandler);
	}

	/**
	 * 加入合作
	 * 
	 * @param id
	 *            合作ID
	 * @param responseHandler
	 *            处理器
	 */
	public void join(int id, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", id + "");
		RestClient.get("/api/cooperations/join", params, responseHandler);
	}

	/**
	 * 获得合作历史
	 * 
	 * @param id
	 *            申请id,就是usership的id optional
	 * @param userid
	 *            用户ID optional
	 * @param cooperationid
	 *            合作ID optional
	 * @param responseHandler
	 *            处理器
	 */
	public void getHistory(int id, int userid, int cooperationid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		if (id > 0)
			params.add("id", id + "");
		if (userid > 0)
			params.add("userid", userid + "");
		if (cooperationid > 0)
			params.add("cooperationid", cooperationid + "");
		RestClient.get("/api/cooperations/history", params, responseHandler);

	}

	/**
	 * 获得一用户的合作历史
	 * 
	 * @param userid
	 *            用户ID
	 * @param responseHandler
	 *            处理器
	 */
	public void getUserHistory(int userid,
			AsyncHttpResponseHandler responseHandler) {
		getHistory(0, userid, 0, responseHandler);
	}

	/**
	 * 发起人接受申请人
	 * 
	 * @param id
	 *            userslist接口里面的那个id,不是userid
	 * @param cooperationid
	 *            合作ID
	 * @param responseHandler
	 *            处理器
	 */
	public void accept(int id, int cooperationid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", id + "");
		params.add("cooperationid", cooperationid + "");
		RestClient.post("/api/cooperations/accept", params, responseHandler);
	}

	/**
	 * 发起合作
	 * 
	 * @param name
	 *            合作名称
	 * @param description
	 *            合作简介
	 * @param company
	 *            公司或组织
	 * @param regdeadline
	 *            合作截止时间
	 * @param statusid
	 *            1发布 2结束
	 * @param isprivate
	 *            是否私有
	 * @param responseHandler
	 *            处理器
	 */
	public void create(String name, String description, String company,
			long regdeadline, int statusid, int isprivate,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("name", name);
		params.add("description", description);
		params.add("company", company);
		params.add("regdeadline", CalendarUtils.getTimeFromat(regdeadline,
				CalendarUtils.TYPE_THIRD));
		params.add("statusid", statusid + "");
		params.add("isprivate", isprivate + "");
		RestClient.post("/api/cooperations/create", params, responseHandler);
	}

	/**
	 * 发起者更新合作资料
	 * 
	 * @param id
	 *            合作id
	 * @param name
	 *            合作名称
	 * @param description
	 *            合作简介
	 * @param company
	 *            公司或组织
	 * @param regdeadline
	 *            截止日期
	 * @param statusid
	 *            1发布 2结束
	 * @param isprivate
	 *            是否私有
	 * @param responseHandler
	 *            处理器
	 */
	public void update(int id, String name, String description, String company,
			long regdeadline, int statusid, int isprivate,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", id + "");
		params.add("name", name);
		params.add("description", description);
		params.add("company", company);
		params.add("regdeadline", CalendarUtils.getTimeFromat(regdeadline,
				CalendarUtils.TYPE_THIRD));
		params.add("statusid", statusid + "");
		params.add("isprivate", isprivate + "");
		RestClient.post("/api/cooperations/update", params, responseHandler);
	}

	/**
	 * 发起者终止合作
	 * 
	 * @param id
	 *            合作ID
	 * @param responseHandler
	 *            处理器
	 */
	public void end(int id, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", id + "");
		RestClient.post("/api/cooperations/end", params, responseHandler);
	}

	/**
	 * 搜索合作列表
	 * 
	 * @param page
	 *            页码
	 * @param id
	 *            合作id
	 * @param name
	 *            合作名称
	 * @param responseHandler
	 *            处理器
	 * 
	 */
	public void find(int page, int id, String name,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		if (page > 0)
			params.add("page", page + "");
		if (id > 0)
			params.add("id", id + "");
		if (name != null)
			params.add("name", name);
		RestClient.get("/api/cooperations/list", params, responseHandler);
	}

	/**
	 * 获取合作列表
	 * 
	 * @param page
	 *            页码
	 * @param responseHandler
	 *            处理器
	 */
	public void getCooperationList(int page,
			AsyncHttpResponseHandler responseHandler) {
		find(page, 0, null, responseHandler);
	}
    /**
     * 合作搜索
     * @param page 页码
     * @param id  作者ID optional
     * @param name 标题关键字 optional
     * @param description  内容关键字 optional
     * @param responseHandler
     */
	public void search(int page, int ownerid, String name, String description,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		if (page > 0)
			params.add("page", page + "");
		if (ownerid > 0)
			params.add("ownerid", ownerid + "");
		if (name != null)
			params.add("name", name);
		if (description != null)
			params.add("description", description);
		params.add("fuzzy", "1");  
		RestClient.get("/api/cooperations/list", params, responseHandler);
	}

	/**
	 * 成员取消参加合作
	 * 
	 * @param id
	 *            合作ID
	 * @param responseHandler
	 *            处理器
	 */
	public void cancle(int id, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", id + "");
		RestClient.post("/api/cooperations/cancel", params, responseHandler);
	}

	/**
	 * 
	 * @param id
	 *            评论ID
	 * @param body
	 *            内容
	 * @param responseHandler
	 *            处理器
	 */
	public void updateComment(int id, String body,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", id + "");
		params.add("body", body);
		RestClient.post("/api/cooperations/comments/update", params,
				responseHandler);
	}

	/**
	 * 获取合作人员名单
	 * 
	 * @param page
	 *            页码
	 * @param id
	 *            合作ID
	 * @param responseHandler
	 *            处理器
	 */
	public void getUserlist(int page, int id,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		if (page > 0)
			params.add("page", page + "");
		if (id > 0)
			params.add("id", id + "");
		RestClient.post("/api/cooperations/userlist", params, responseHandler);
	}

	/**
	 * 评论
	 * 
	 * @param cooperationid
	 *            合作ID
	 * @param body
	 *            内容
	 * @param responseHandler
	 *            处理器
	 */
	public void comment(int cooperationid, String body,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("cooperationid", cooperationid + "");
		params.add("body", body);
		RestClient.post("/api/cooperations/comments/post", params,
				responseHandler);
	}

	/**
	 * 获取合作详情
	 * 
	 * @param id
	 *            合作ID
	 * @param responseHandler
	 */
	public void view(int id, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", id + "");
		RestClient.get("/api/cooperations/view", params, responseHandler);
	}
	/**
	 * 获取评论列表
	 * @param id 评论ID
	 * @param cooperationid 合作ID
	 * @param userid 用户ID
	 * @param responseHandler
	 */
	public void getCommentListBase(int id,int cooperationid,int userid,AsyncHttpResponseHandler responseHandler){
		RequestParams params = new RequestParams();
		if(id>0)params.add("id", id + "");
		if(cooperationid>0)params.add("cooperationid", cooperationid + "");
		if(userid>0)params.add("userid", userid + "");
		RestClient.get("/api/cooperations/comments/list", params, responseHandler);
	}
	/**
	 * 获得一个合作的评论列表
	 * @param cooperationid 合作id
	 * @param responseHandler
	 */
	public void getCommentList(int cooperationid,AsyncHttpResponseHandler responseHandler){
		getCommentListBase(0, cooperationid, 0, responseHandler);
	}
	/**
	 * 获得一个用户的合作合作列表
	 * @param page 页码 ，可选
	 * @param ownerid 用户的id 必填
	 * @param responseHandler
 	 */
	public void getUserCooperationList(int page ,int ownerid,AsyncHttpResponseHandler responseHandler){
		search(page, ownerid, null, null, responseHandler);
	}
	/**
	 * 获得我的合作列表
	 * @param page 页码 
	 * @param responseHandler
	 */
	public void getMyCooperationList(int page,AsyncHttpResponseHandler responseHandler){
		RequestParams params = new RequestParams();
		params.add("page",page+"");
		RestClient.get("/api/cooperations/my", params, responseHandler);
	}
}
