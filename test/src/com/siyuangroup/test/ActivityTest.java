package com.siyuangroup.test;

import org.apache.http.Header;

import com.api.ActivityAPI;
import com.api.GroupAPI;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.utils.TempLogin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ActivityTest extends Activity {
	private View test1, test2, test3;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_main);

		test1 = findViewById(R.id.button1);
		test2 = findViewById(R.id.button2);
		test3 = findViewById(R.id.button3);
		tv = (TextView) findViewById(R.id.textview1);
		test1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivityList();
			}
		});

		test2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TempLogin.register();
				TempLogin.login();

			}
		});
		test3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				joinActivity();  //faild
			}
		});
	}

	private void joinActivity() {
		ActivityAPI api = new ActivityAPI();
		api.joinActivity(2, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				tv.setText(new String(arg2));
			}
		});
	}

	private void getActivityList() {
		ActivityAPI api = new ActivityAPI();
		api.getActivityList(1, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				tv.setText(new String(arg2));
			}
		});
	}
}
