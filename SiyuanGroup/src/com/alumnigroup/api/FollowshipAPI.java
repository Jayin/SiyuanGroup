package com.alumnigroup.api;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 关注API<br>
 * following=关注  follower=粉丝
 * @author Jayin Ton
 * 
 */
public class FollowshipAPI {
	
	public FollowshipAPI() {

	}

	/**
	 * 修改备注
	 * 
	 * @param followid
	 *            粉丝ID
	 * @param remark
	 *            备注名
	 * @param responseHandler
	 *            处理器
	 */
	public void updateRemark(int followid, String remark,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("followid", followid+"");
		params.add("remark", remark+"");
		RestClient.post("/api/followship/remark", params, responseHandler);
	}
    /**
     * 关注用户 
     * @param followid 被关注者ID
     * @param remark 备注名
     * @param responseHandler 处理器
     */
	public void follow(int followid, String remark,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("followid", followid+"");
		params.add("remark", remark+"");
		RestClient.post("/api/followship/follow", params, responseHandler);
	}
	/**
	 * 取消关注
	 * @param followid 被关注者ID
	 * @param responseHandler 处理器
	 */
	public void unfollow(int followid,AsyncHttpResponseHandler responseHandler){
		RequestParams params = new RequestParams();
		params.add("followid", followid+"");
		RestClient.post("/api/followship/unfollow", params, responseHandler);
	}
	/**
	 * 获得用户的关注列表
	 * @param page 页码
	 * @param userid 关注者ID
	 * @param responseHandler 处理器
	 */
	public void getFollowingList(int page ,int userid ,AsyncHttpResponseHandler responseHandler){
		RequestParams params = new RequestParams();
		params.add("userid", userid+"");
		RestClient.post("/api/followship/following", params, responseHandler);
	}
	/**
	 * 用户的粉丝列表 
	 * @param page 页码
	 * @param followid 被关注者ID
	 * @param responseHandler 处理器
	 */
	public void getFollowerList(int page ,int followid ,AsyncHttpResponseHandler responseHandler){
		RequestParams params = new RequestParams();
		params.add("page", page+"");
		params.add("followid", followid+"");
		RestClient.post("/api/followship/followers", params, responseHandler);
	}
	

}
