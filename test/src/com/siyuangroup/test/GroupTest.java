package com.siyuangroup.test;

import com.api.GroupAPI;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class GroupTest extends Activity {
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
               GroupAPI api = new GroupAPI();
               
			}
		});

		test2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		test3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}
}
