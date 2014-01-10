package com.alumnigroup.api;

import org.apache.http.client.CookieStore;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * Restful api 客户端<br>
 * 请求前要初始化,设置cookie,timeout
 * problem left: 没有自动保存cookie的 后面封装api时用到
 * PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
   myClient.setCookieStore(myCookieStore);
 * @author Jayin Ton
 *
 */
public class RestClient {
	public static final String BASE_URL = "http://10.10.65.165:8088";
	private static final int HTTP_Timeout = 6*1000; //链接超时
	public static CookieStore cookieStore;
    
	private static AsyncHttpClient client = new AsyncHttpClient();
    /**
     * get method
     * @param url  相对的url
     * @param params
     * @param responseHandler 
     */
	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		initClient(); 
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}
    /**
     * post method
     * @param url
     * @param params
     * @param responseHandler
     */
	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		initClient(); 
		client.post(getAbsoluteUrl(url), params, responseHandler);

	}
    /** 
     * 初始化
     */
	private static void initClient() {
		
		if (cookieStore != null)
			client.setCookieStore(cookieStore);
		client.setTimeout(HTTP_Timeout);
		
	}
    /**
     * set CookieStore
     * @param cookieStore
     */
	public static void setCookieStore(CookieStore cookieStore) {
		RestClient.cookieStore = cookieStore;
	}
    /**
     * 获得绝对url
     * @param relativeUrl
     * @return
     */
	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
}