package com.alumnigroup.api;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 收藏starAPI
 * 
 * @author Jayin Ton
 * 
 */   
public class StarAPI {
	 /** 收藏类别：话题*/
	public static final int Item_type_issue= 1;
	 /** 收藏类别：活动*/
	public static final int Item_type_activity= 2;
	 /** 收藏类别：商务合作*/
	public static final int Item_type_business= 3;
	public StarAPI() {

	}
    /**
     * 修改备注
     * @param id 收藏ID
     * @param remark 备注
     * @param responseHandler
     */
	public void updateRemark(int id,String remark,AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", id + "");
		params.add("remark", remark);
		RestClient.post("/api/starship/remark", params, responseHandler);
	}
    /**
     * 取消收藏
     * @param id  收藏ID
     * @param responseHandler
     */
	public void unStar(int id,AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", id + "");
		RestClient.post("/api/starship/unstar", params, responseHandler);
	}
    /**
     * 获得收藏列表
     * @param page 页码
     * @param id 收藏ID optional
     * @param userid 用户ID optional
     * @param itemtype 类别ID - 1用户, 2话题, 3活动, 4商务合作 optional
     * @param itemid 资源ID optional
     * @param responseHandler
     */
	public void getStarList(int page, int id, int userid, int itemtype,
			int itemid, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		if(page>0)params.add("page", page + "");
		if(id>0)params.add("id", id + "");
		if(userid>0)params.add("itemtype", itemtype + "");
		if(itemtype>0)params.add("itemid", itemid + "");
		RestClient.get("/api/starship/find", params, responseHandler);
	}
	/**
	 * 
	 * @param page  页码
	 * @param itemtype 类别ID - 1用户, 2话题, 3活动, 4商务合作  
	 * @param responseHandler 
	 */
	public void getStarList(int page,int itemtype, AsyncHttpResponseHandler responseHandler){
		getStarList(page, 0, 0, itemtype, 0, responseHandler);
	}

	/**
	 * 收藏资源
	 * 
	 * @param itemtype
	 *            类别ID 类别ID - 1用户, 2话题, 3活动, 4商务合作
	 * @param itemid
	 *            资源ID
	 * @param remark
	 *            备注 optional
	 * @param responseHandler
	 */
	public void star(int itemtype, int itemid, String remark,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("itemtype", itemtype + "");
		params.add("itemid", itemid + "");
		if (remark != null)
			params.add("remark", remark);
		RestClient.post("/api/starship/star", params, responseHandler);
	}

	/**
	 * 收藏资源
	 * 
	 * @param itemtype
	 *            类别ID 类别ID - 1用户, 2话题, 3活动, 4商务合作
	 * @param itemid
	 *            资源ID
	 * @param responseHandler
	 */
	public void star(int itemtype, int itemid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("itemtype", itemtype + "");
		params.add("itemid", itemid + "");
		RestClient.post("/api/starship/star", params, responseHandler);
	}

}
