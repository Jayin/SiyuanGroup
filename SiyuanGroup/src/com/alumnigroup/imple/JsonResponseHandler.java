package com.alumnigroup.imple;

import org.apache.http.Header;
import org.json.JSONObject;

import com.alumnigroup.utils.JsonUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * AsyncHttpResponseHandler封装 <br>

 * @author Jayin Ton
 * 
 */
public abstract class JsonResponseHandler extends AsyncHttpResponseHandler {
	/** 网络连接异常*/
	public static final int Error_IOException = 1; // 网络连接异常
	/** obj = new JSONObject(new String(data)); json构建对象失败*/
	public static final int Error_JsonParse = 2;  
	/** 返回不是状态码为200的错误*/
	public static final int Error_Http = 3;  
	/**正确返回json并解析成功，但有error（服务端返回错误信息给客户端） */
	public static final int Error_Response = 4; 

	@Override
	public void onFailure(int statusCode, Header[] headers, byte[] data,
			Throwable err) {
		// IOException
		if (statusCode == 0) {
			onFaild(Error_IOException,"bad newwork");
		} else {
			//status code > 300
			onFaild(Error_Http,"http error");
		}
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, byte[] data) {
		if (statusCode == 200) {
			JSONObject obj = null;
			try {
			    String json = new String(data);
				obj = new JSONObject(json);
				if (JsonUtils.isOK(json)) {
					onOK(headers, obj);
				} else {
					//json string include `error_code`
					onFaild(Error_Response,JsonUtils.getErrorString(json));
				}
			} catch (Exception e) {
				e.printStackTrace();
				//can't not create JSONObject , server response the wrong format json string !
				onFaild(Error_JsonParse,"server response the wrong format json string");
			}
		} else {
			// statusCode:201-204
			onFaild(Error_Http,"http error");
		}
	}

	/**
	 *成功请求并没有错误信息返回是
	 * @param headers  所有请求头信息
	 * @param obj json对象
	 */
	public abstract void onOK(Header[] headers, JSONObject obj);
    /**
     * 调用此方法，当<br>
     * <li>出现网络异常，
     * <li>http状态码不为200
     * <li>json构建JSONObjec对象失败，
     * <li>json字符串解析（JsonUtils.isOK(json)}）出现错误代码
     * @param errorType 对应上述的错误类型{@link#Error_IOException}  {@link#Error_Http}  {@link#Error_JsonParse}  {@link#Error_Response}  
     * @param errorMsg error message
     */ 
	public abstract void onFaild(int errorType,String errorMsg);
	
}
