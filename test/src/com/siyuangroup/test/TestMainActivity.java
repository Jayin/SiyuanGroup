package com.siyuangroup.test;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.api.IssuesAPI;
import com.api.RestClient;
import com.api.UserAPI;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.utils.L;

public class TestMainActivity extends Activity {

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
		final UserAPI _api = new UserAPI();
		_api.regist("test1", "12345678", "jayinton", "g@py.com",
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						L.i("regist ok!");
						_api.login("test1", "12345678",
								new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(int arg0,
											Header[] arg1, byte[] arg2) {
										L.i("login success!");
									}
									
									@Override
									public void onFailure(int arg0,
											Header[] arg1, byte[] arg2,
											Throwable arg3) {
										L.i("login Failure!");
									}
								});
						
						
						 
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						L.i("regist Failure!");
					}
				});

		test1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// getIssueList();
				// search(1, null, "ni");
				post();

			}
		});

		test2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				update();
			}
		});
		test3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				delete();
			}
		});
	}

	public void delete() {
		IssuesAPI api = new IssuesAPI();
		api.deleteIssue(604, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				tv.setText(new String(arg2));
			}
		});
	}

	public void update() {
		IssuesAPI api = new IssuesAPI();
		api.updateIssue(604, "你妹！", "nice", new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				tv.setText(new String(arg2));
			}
		});
	}

	public void post() {
		IssuesAPI api = new IssuesAPI();
		api.postIssue("jayint", "碉堡了是不？", new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				tv.setText(new String(arg2));
			}
		});
	}

	protected void search(int page, String title, String body) {
		IssuesAPI api = new IssuesAPI();
		api.search(page, title, body, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				tv.setText(new String(arg2));
			}
		});
	}

	public void getIssueList() {
		IssuesAPI api = new IssuesAPI();
		api.getIssueList(1, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				tv.setText(new String(arg2));
			}
		});
	}

}
