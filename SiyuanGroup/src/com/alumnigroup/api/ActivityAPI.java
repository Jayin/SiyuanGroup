package com.alumnigroup.api;

import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.L;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 活动系统API
 * 
 * @author Jayin Ton
 * 
 */
public class ActivityAPI {
	public ActivityAPI() {

	}

	/**
	 * 根据给定的条件查找活动列表 String name should be appended!
	 * 
	 * @param page
	 *            第几页数
	 * @param activityId
	 *            活动id
	 * @param ownerid
	 *            活动发起人id
	 * @param groupid
	 *            圈子id
	 * @param name
	 *            活动名称
	 * @param content
	 *            内容
	 * @param statusid
	 *            状态id
	 * @param responseHandler
	 *            处理器
	 * 
	 */
	public void find(int page, int activityId, int ownerid, int groupid,
			int statusid, String name, String content,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		if (page > 0)
			params.add("page", page + "");
		else
			params.add("page", "1");
		if (activityId > 0)
			params.add("id", activityId + "");
		if (ownerid > 0)
			params.add("ownerid", ownerid + "");
		if (groupid > 0)
			params.add("groupid", groupid + "");
		if (statusid > 0)
			params.add("statusid", statusid + ""); // 这里得注意了statusid的取值
		if (name != null)
			params.add("name", name);
		if (content != null)
			params.add("content", content);
		RestClient.get("/api/activities/list", params, responseHandler);
	}

	/**
	 * 获取活动列表
	 * 
	 * @param page
	 *            第几页
	 * @param responseHandler
	 *            处理器
	 */
	public void getActivityList(int page,
			AsyncHttpResponseHandler responseHandler) {
		find(page, 0, 0, 0, -1, null, null, responseHandler);
	}

	/**
	 * 加入一个活动
	 * 
	 * @param activityId
	 *            活动id
	 * @param responseHandler
	 *            处理器
	 */
	public void joinActivity(int activityId,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", activityId + "");
		RestClient.post("/api/activities/join", params, responseHandler);
	}

	/**
	 * 发起人接受申请人
	 * 
	 * @param id
	 *            这里是userlist(可以理解成报名列表的id)接口里面的那个id,不是userid <li>see
	 *            {@link#getUserList()}
	 * @param activityid
	 *            活动id
	 * @param responseHandler
	 *            处理器
	 */
	public void accept(int id, int activityid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", id + "");
		params.add("activityid", activityid + "");
		RestClient.post("/api/activities/accept", params, responseHandler);
	}

	/**
	 * 发起者更新活动资料
	 * 
	 * @param id
	 *            活动id
	 * @param maxnum
	 *            最大人数
	 * @param duration
	 *            持续时间,单位为分钟
	 * @param regdeadline
	 *            注册时间
	 * @param statusid
	 *            活动状态 1接受报名、2截止报名、3活动结束、4活动取消]
	 * @param money
	 *            活动费用
	 * @param name
	 *            活动名称
	 * @param content
	 *            活动内容
	 * @param responseHandler
	 *            处理器
	 */
	public void update(int id, int maxnum, long duration, long regdeadline,
			int statusid, long money, String name, String content, String site,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", id + "");
		params.add("maxnum", maxnum + "");
		params.add("duration", duration + "");
		params.add("statusid", statusid + "");
		params.add("money", money + "");
		params.add("name", name);
		params.add("content", content);
		params.add("site", site);
		params.add("regdeadline", regdeadline + "");
		RestClient.post("/api/activities/update", params, responseHandler);
	}

	/**
	 * 发起者终止活动
	 * 
	 * @param id
	 *            活动id
	 * @param responseHandler
	 *            处理器
	 */
	public void endActivity(int id, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", id + "");
		RestClient.post("/api/activities/end", params, responseHandler);
	}

	/**
	 * 成员取消参加活动
	 * 
	 * @param id
	 *            活动id
	 * @param responseHandler
	 *            处理器
	 */
	public void cancelActivity(int id, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", id + "");
		RestClient.post("/api/activities/cancel", params, responseHandler);
	}

	/**
	 * 更新活动图片 params包含ken="avatar"
	 * 
	 * @param id
	 *            活动id
	 * @param params
	 *            avatar-File
	 * @param responseHandler
	 *            处理器
	 */
	public void updateAvatar(int id, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		params.put("id", id + "");
		RestClient.post("/api/activities/avatar/update", params,
				responseHandler);
	}

	/**
	 * 群成员发起活动
	 * 
	 * @param groupid
	 *            群id
	 * @param maxnum
	 *            活动最大人数
	 * @param starttime
	 *            活动开始时间
	 * @param duration
	 *            活动持续时间,单位为天
	 * @param statusid
	 *            活动状态 1接受报名、2截止报名、3活动结束、4活动取消
	 * @param money
	 *            花费
	 * @param name
	 *            活动名称
	 * @param content
	 *            活动描述
	 * @param site
	 *            活动地点
	 * @param regdeadline
	 *            截止报名日期
	 * @param responseHandler
	 *            处理器
	 */
	public void creatAcivity(int groupid, int maxnum, long starttime,
			long duration, long regdeadline, int statusid, long money,
			String name, String content, String site,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("groupid", groupid + "");
		params.add("maxnum", maxnum + "");
		params.add("starttime", CalendarUtils.getTimeFromat(starttime,
				CalendarUtils.TYPE_THIRD));
		params.add("duration", duration + "");
		params.add("statusid", statusid + "");
		params.add("money", money + "");
		params.add("name", name);
		params.add("content", content);
		params.add("site", site);
		params.add("regdeadline", CalendarUtils.getTimeFromat(regdeadline,
				CalendarUtils.TYPE_THIRD));
		L.i("regdeadline__>"
				+ CalendarUtils.getTimeFromat(regdeadline,
						CalendarUtils.TYPE_THIRD));
		L.i("starttime>"
				+ CalendarUtils.getTimeFromat(starttime,
						CalendarUtils.TYPE_THIRD));
		RestClient.post("/api/activities/create", params, responseHandler);
	}

	/**
	 * 获取活动人员名单(分页)<br>
	 * 不分页{@linkplain#getUserListAll()}
	 * 
	 * @param page
	 *            页码
	 * @param id
	 *            活动id
	 * 
	 * @param responseHandler
	 *            处理器
	 */
	public void getUserList(int page, int id,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("page", page + "");
		params.add("id", id + "");
		RestClient.post("/api/activities/userslist", params, responseHandler);
	}

	/**
	 * 获得全部的活动人员名单
	 * 
	 * @param limit
	 * @param id
	 * @param responseHandler
	 */
	public void getUserListAll(int limit, int id,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("page", limit + "");
		params.add("id", id + "");
		RestClient.post("/api/activities/userslist", params, responseHandler);
	}

	/**
	 * 获取活动参加历史列表
	 * 
	 * @param page
	 *            页码
	 * @param id
	 *            申请id,就是usership的id
	 * @param userid
	 *            用户id
	 * @param activityid
	 *            活动id
	 * @param responseHandler
	 *            处理器
	 */
	public void getHistory(int page, int id, int userid, int activityid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		if (page > 0)
			params.add("page", page + "");
		if (id > 0)
			params.add("id", id + "");
		if (userid > 0)
			params.add("userid", userid + "");
		if (activityid > 0)
			params.add("activityid", activityid + "");
		RestClient.get("/api/activities/history", params, responseHandler);
	}

	/**
	 * 获取一用户曾经参加过的活动
	 * 
	 * @param page
	 *            页码
	 * @param userid
	 *            user用户id
	 * @param responseHandler
	 *            处理器
	 */
	public void getUserHistory(int page, int userid,
			AsyncHttpResponseHandler responseHandler) {
		getHistory(page, userid, 0, 0, responseHandler);
	}
    /**
     * 活动搜索
     * @param page 页码
     * @param ownerid 作者id
     * @param name  标题关键字
     * @param content 内容关键字
     * @param responseHandler
     */
	public void search(int page, int ownerid, String name, String content,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		if (page > 0)
			params.add("page", page + "");
		if (ownerid > 0)
			params.add("ownerid", ownerid + "");
		if (name != null)
			params.add("name", name);
		if (content != null)
			params.add("content", content);
		params.add("fuzzy", "1"); 
		RestClient.get("/api/activities/list", params, responseHandler);
	}
    
	/**
	 * 获得用户的主策活动
	 * @param page
	 * @param ownerid
	 * @param responseHandler
	 */
	public void getMyActivity(int page ,AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		if(page>0)params.add("page", page+"");
		RestClient.get("/api/activities/my", params, responseHandler);
	}
	
 
}
