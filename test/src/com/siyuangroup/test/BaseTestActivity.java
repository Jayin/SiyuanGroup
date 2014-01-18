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
		_setTitle(test1, test1Title());
		_setTitle(test2, test2Title());
		_setTitle(test3, test3Title());
		_setTitle(test4, test4Title());
		
	}

	public void _setTitle(String title) {
		if (title != null)
			this.setTitle(title);
	}
	
	public void _setTitle(TextView tv,String title){
		if(tv!=null && title!=null){
			tv.setText(title);
		}
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
	
	public abstract String test1Title();

	public abstract String test2Title();

	public abstract String test3Title();

	public abstract String test4Title();

	public abstract void onInit();

	public abstract String setTitle();
	
	public void toast(String text){
		Toast.makeText(this, text, 1).show();
	}
}
