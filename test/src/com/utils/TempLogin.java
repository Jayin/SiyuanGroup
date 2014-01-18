package com.utils;

import org.apache.http.Header;

import com.api.UserAPI;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class TempLogin {
	public static void register() {
		final UserAPI _api = new UserAPI();
		_api.regist("test1", "12345678", "jayinton", "g@py.com",
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						L.i("regist ok!\n"+new String(arg2));
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						L.i("regist Failure!\n"+new String(arg2));
					}
				});
	}
	
	public static void login(){
		final UserAPI _api = new UserAPI();
		_api.login("test1", "12345678",
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0,
							Header[] arg1, byte[] arg2) {
						L.i("login success!\n"+new String(arg2));
					}

					@Override
					public void onFailure(int arg0,
							Header[] arg1, byte[] arg2,
							Throwable arg3) {
						L.i("login Failure!\n"+new String(arg2));
					}
				});
	}
}
