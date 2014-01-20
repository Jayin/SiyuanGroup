package com.alumnigroup.api;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 相册
 * 
 * @author Jayin Ton
 * 
 */
public class PhotoAPI {
	public PhotoAPI() {

	}

	/**
	 * 删除照片
	 * 
	 * @param id
	 *            相片ID
	 * @param responseHandler
	 *            处理器
	 */
	public void delete(int id, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", id + "");
		RestClient.post("/api/photos/delete", params, responseHandler);
	}

	/**
	 * 发布相片<br>
	 * <li>必须提交的参数 ：params。put("description","xxxx"); params。put("image ",
	 * File/Stream);
	 * 
	 * @param params
	 * @param responseHandler
	 */
	public void post(RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		RestClient.post("/api/photos/pos", params, responseHandler);
	}

	/**
	 * 更新相片
	 * 
	 * @param id
	 *            相片ID
	 * @param description
	 *            描述
	 * @param responseHandler
	 *            处理器
	 */
	public void update(int id, String description,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", id + "");
		params.add("description", description);
		RestClient.post("/api/photos/update", params, responseHandler);
	}

	/**
	 * 查找，id，userid无效时等同于{@link#getPhotoList()}
	 * 
	 * @param page
	 *            页码
	 * @param id
	 *            图片id optional
	 * @param userid
	 *            用户id optional
	 * @param responseHandler
	 *            处理器
	 */
	public void find(int page, int id, int userid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("page", page + "");
		if (id > 0)
			params.add("id", id + "");
		if (userid > 0)
			params.add("userid", userid + "");
		RestClient.get("/api/photos/find", params, responseHandler);
	}

	/**
	 * 获取相片列表
	 * 
	 * @param page
	 *            页码
	 * @param responseHandler
	 */
	public void getPhotoList(int page, AsyncHttpResponseHandler responseHandler) {
		find(page, 0, 0, responseHandler);
	}
}
