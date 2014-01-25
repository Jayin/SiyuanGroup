package com.alumnigroup.app.acty;

import android.os.Bundle;
import android.widget.TextView;

import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;

public class BusinessDetail extends BaseActivity {
	private Communication c;
	private TextView tv_username, tv_projectname, tv_deadline, tv_description;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_businessdetail);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		c = getSerializableExtra("cooperation");
	}

	@Override
	protected void initLayout() {

	}

}
