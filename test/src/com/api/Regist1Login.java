package com.api;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.utils.L;

import android.test.AndroidTestCase;
import android.util.Log;

public class Regist1Login extends AndroidTestCase {

	public void preWork(){
		Thread t = new Thread();
		t.setDaemon(true);
		t.start();
	}
	public void register() {
		
		
		
		String username = "test1";
		String password = "123456";
		String name = "Test name";// profile[name]
		String nickname = "Test1";
		String email = "t@mz.com";

		RequestParams params = new RequestParams();
		params.add("username", username);
		params.add("password", password);
		params.add("profile[name]", name);
		params.add("profile[nickname]", nickname);
		params.add("profile[email]", email);
		RestClient.post("/api/users/reg", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						L.i(arg3.toString());
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] data) {

					L.i(new String(data));
					}

				});
		Log.e("debug","finish??????");
		System.out.println("**************");

	}
}
