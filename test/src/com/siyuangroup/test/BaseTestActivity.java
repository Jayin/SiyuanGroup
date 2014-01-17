package com.siyuangroup.test;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

public abstract class BaseTestActivity extends Activity {
	public Button test1, test2, test3, test4;
	public TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_basetest);
		test1 = (Button) findViewById(R.id.button1);
		test2 = (Button) findViewById(R.id.button2);
		test3 = (Button) findViewById(R.id.button3);
		test4 = (Button) findViewById(R.id.button4);
		tv = (TextView) findViewById(R.id.textview1);
		test1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				test1Click();
			}
		});

		test2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				test2Click();
			}
		});
		test3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				test3Click();
			}
		});
		test4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				test4Click();
			}
		});
		onInit();
		_setTitle(setTitle());
	}

	public void _setTitle(String title) {
		if (title != null)
			this.setTitle(title);
	}

	class Response extends AsyncHttpResponseHandler {
		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			if (arg2 != null)
				tv.setText("Response--->onSuccess\n" + new String(arg2));

		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable arg3) {
			if (arg2 != null)
				tv.setText("Response--->onFailure\n" + new String(arg2));
		}
	}

	public abstract void test1Click();

	public abstract void test2Click();

	public abstract void test3Click();

	public abstract void test4Click();

	public abstract void onInit();

	public abstract String setTitle();
	
	public void toast(String text){
		Toast.makeText(this, text, 1).show();
	}
}
