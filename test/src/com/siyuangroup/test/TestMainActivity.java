package com.siyuangroup.test;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.api.RestClient;
import com.api.UserAPI;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.utils.L;

public class TestMainActivity extends Activity {

	private View btn;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_main);

		btn = findViewById(R.id.button1);
		tv = (TextView) findViewById(R.id.textview1);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//register();
				// find();
				//login();
				getAll();
			}
		});
	}
	
	public void getAll() {
		UserAPI api = new UserAPI();
		api.getAllMember(1, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		       L.i(new String(arg2));
		       tv.setText(new String(arg2));
			}
		});
	}

	public void login() {
		String username = "test1";
		String password = "123456";
		RequestParams params = new RequestParams();
		params.add("username", username);
		params.add("password", password);
		RestClient.post("/api/users/login", params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] data) {
                              tv.setText(new String(data));
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] data,
							Throwable arg3) {
						 tv.setText(new String(data));
					}
				});
	}

	public void find() {
		RestClient.get("/api/users/find", null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] data) {
				tv.setText(new String(data));
			}

			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
			};
		});
	}

	public void register() {

		String username = "test1";
		String password = "1234567";
		String name = "Test name";// profile[name]
		String nickname = "Test1";
		String email = "t@mz.com";
        //profile[grade] 
		RequestParams params = new RequestParams();
		params.add("username", username);
		params.add("password", password);
		params.add("profile[name]", name);
		params.add("profile[nickname]", nickname);
		params.add("profile[email]", email);
		RestClient.post("/api/users/register", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						L.i(new String(arg2));
						// tv.setText(arg0);

					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] data) {

						L.i(new String(data));
						tv.setText(new String(data));
					}

				});

	}

}
