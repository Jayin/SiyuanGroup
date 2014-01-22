package com.alumnigroup.imple;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;
/**
 * 一个简单的AsyncHttpResponseHandler
 * 主要是为了提醒实现这两个方法
 * @author Jayin Ton
 *
 */
public abstract class ResponseHandler extends AsyncHttpResponseHandler {
	@Override
	public abstract void onFailure(int statusCode, Header[] header,
			byte[] data, Throwable err);

	@Override
	public abstract void onSuccess(int statusCode, Header[] headers, byte[] data);
}
