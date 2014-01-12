package com.alumnigroup.app.acty;

import android.os.Bundle;
import android.view.View;

import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;

public class About extends BaseActivity {
	
	private View btnBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_about);
		initLayout();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initLayout() {
		btnBack = _getView(R.id.acty_head_btn_back);
		btnBack.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		finish();
	}

}
