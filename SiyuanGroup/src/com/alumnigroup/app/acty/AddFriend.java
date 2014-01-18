package com.alumnigroup.app.acty;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;

/**
 * 添加好友
 * 
 * @author vector
 * 
 */
public class AddFriend extends BaseActivity {

	private Button btnNext, btnCancel;
	private Button btnOK;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_addfriend);
		initLayout();
	}

	protected void initData() {

	}

	protected void initLayout() {
		btnNext = (Button) _getView(R.id.acty_addfriend_btn_next);
		btnNext.setOnClickListener(this);

		btnCancel = (Button) _getView(R.id.acty_addfriend_btn_cancel);
		btnCancel.setOnClickListener(this);
	}

	public void onClick(View v) {
		int id = v.getId();

		switch (id) {
		case R.id.acty_addfriend_btn_cancel:
			finish();
			break;
		case R.id.acty_addfriend_btn_next:
			setContentView(R.layout.acty_addfriend_next);
			btnCancel= (Button) _getView(R.id.acty_addfriend_btn_cancel);
			btnCancel.setOnClickListener(this);
			btnOK = (Button) _getView(R.id.acty_addfriend_next_btn_ok);
			btnOK.setOnClickListener(this);
			break;
		case R.id.acty_addfriend_next_btn_ok:
			toast("请求已经发出");
			finish();
			break;

		default:
			break;
		}

	}
}
