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
                 IssuesAPI api = new IssuesAPI();
                 api.getIssueList(1, new AsyncHttpResponseHandler(){
                	 @Override
                	public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                		 tv.setText(new String(arg2));
                	}
                 });
			}
		});
	}

}
