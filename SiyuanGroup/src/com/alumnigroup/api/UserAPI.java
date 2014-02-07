package com.alumnigroup.api;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 用户系统接口
 * 
 * @author Jayin Ton
 * 
 */
public class UserAPI {
	public UserAPI() {

	}

	/**
	 * 注册<br>
	 * use:{@link#regist()}
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param name
	 *            姓名
	 * @param email
	 *            电子邮件
	 * @param responseHandler
	 *            响应处理器
	 */
	public void regist(String username, String password, String name,
			String email, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("username", username);
		params.add("password", password);
		params.add("profile[name]", name);
		params.add("profile[email]", email);
		RestClient.post("/api/users/register", params, responseHandler);
	}

	/**
	 * 注册
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param name
	 *            optional 姓名
	 * @param email
	 *            optional 邮箱
	 * @param gender
	 *            optional 性别
	 * @param age
	 *            optional 年龄
	 * @param grade
	 *            optional 入学级数
	 * @param university
	 *            optional 学校
	 * @param major
	 *            optional 专业
	 * @param summary
	 *            optional 个性签名
	 * @param responseHandler
	 */
	public void regist(String username, String password, String name,
			String email, String gender, int age, int grade, String university,
			String major, String summary,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("username", username);
		params.add("password", password);
		if (name != null)
			params.add("profile[name]", name);
		if (email != null)
			params.add("profile[email]", email);
		if (gender != null)
			params.add("profile[gender]", gender);
		if (age > 0)
			params.add("profile[age]", age + "");
		if (grade > 0)
			params.add("profile[grade]", grade + "");
		if (university != null)
			params.add("profile[university]", university);
		if (major != null)
			params.add("profile[major]", major);
		if (summary != null)
			params.add("profile[summary]", summary);
		RestClient.post("/api/users/register", params, responseHandler);
	}

	/**
	 * 登录账号
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param responseHandler
	 *            处理器
	 */
	public void login(String username, String password,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("username", username);
		params.add("password", password);
		RestClient.post("/api/users/login", params, responseHandler);
	}

	/**
	 * 登出账号
	 * 
	 * @param responseHandler
	 *            处理器
	 */
	public void logout(AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		RestClient.post("/api/users/logout", params, responseHandler);
	}

	/**
	 * 精确查找用户<br>
	 * id int 用户id <br>
	 * username string 用户名 <br>
	 * name string 真实姓名 <br>
	 * isonline int 是否在线<br>
	 * email string 邮箱 <br>
	 * gender string “m”男，“f”女
	 * 
	 * @param params
	 *            请求体
	 * @param responseHandler
	 *            处理器
	 */
	public void find(RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		RestClient.get("/api/users/find", params, responseHandler);
	}

	/**
	 * 重置密码
	 * 
	 * @param oldpsw
	 *            旧密码
	 * @param newpsw
	 *            新密码
	 * @param responseHandler
	 *            处理器
	 */
	public void resetPassword(String oldpsw, String newpsw,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("password", oldpsw);
		params.add("new-password", newpsw);
		RestClient.post("/api/users/password/reset", params, responseHandler);
	}

	/**
	 * 获取全站会员信息<br>
	 * 根据给的你给的页数 page<br>
	 * 
	 * @param page
	 * @param responseHandler
	 *            处理器
	 */
	public void getAllMember(int page, AsyncHttpResponseHandler responseHandler) {
		if (page <= 0)
			page = 1;
		find(new RequestParams("page", page + ""), responseHandler);
	}

	/**
	 * 获取我的好友信息<br>
	 * 根据给的你给的页数 page<br>
	 * 
	 * @param page
	 * @param responseHandler
	 *            处理器
	 */
	public void getMyFriend(int page,
			AsyncHttpResponseHandler responseHandler) {
		if (page <= 0)
			page = 1;
		find(new RequestParams("page", page + ""), responseHandler);
	}

}