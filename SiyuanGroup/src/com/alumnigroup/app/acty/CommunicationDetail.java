package com.alumnigroup.app.acty;

import android.os.Bundle;

import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Issue;

public class CommunicationDetail extends BaseActivity {
	private Issue issue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_communicationdetail);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
        issue = (Issue)getSerializableExtra("issue");
	}

	@Override
	protected void initLayout() {
         
	}

}
