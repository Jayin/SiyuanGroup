package com.alumnigroup.app.acty;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;

/**
 * 意见反馈
 * @author vector
 *
 */
public class Tickling extends BaseActivity {

	private View btnBack,btnSend;
	private EditText etContent;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_tickling);
		initData();
		initLayout();
	}
	
	@Override
	protected void initData() {

	}

	@Override
	protected void initLayout() {
		btnBack = _getView(R.id.acty_head_btn_back);
		btnBack.setOnClickListener(this);
		
		btnSend = _getView(R.id.acty_head_btn_send);
		btnSend.setOnClickListener(this);
		
		etContent = (EditText) _getView(R.id.acty_tickling_et_content);
	}

	@Override
	public void onClick(View v) {
		
		int id =v.getId();
		switch (id) {
		case R.id.acty_head_btn_back:
			finish();
			break;
			
		case R.id.acty_head_btn_send:
			String content = etContent.getText().toString();
			if(content.length()<10){
				toast("内容不能少于10个字");
				break;
			}
			v.setClickable(false);
			toast("发送成功");
			finish();
			break;

		default:
			break;
		}
	}
}
