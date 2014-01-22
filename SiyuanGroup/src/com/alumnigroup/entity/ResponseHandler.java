package com.alumnigroup.entity;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;

public abstract class ResponseHandler extends AsyncHttpResponseHandler {
	@Override
	public abstract void onFailure(int statusCode, Header[] header,
			byte[] data, Throwable err);

	@Override
	public abstract void onSuccess(int statusCode, Header[] headers, byte[] data);
}
