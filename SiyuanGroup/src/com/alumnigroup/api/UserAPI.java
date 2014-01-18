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
         * 注册
         * 
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
}