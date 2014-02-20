package com.alumnigroup.app.acty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alumnigroup.app.App;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;

/**
 * 设置
 * @author vector
 *
 */
public class Setting extends BaseActivity {
	
	
	private View btnIntroduce,btnTickling,btnAbout,btnQuit,btnBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_setting);
		initData();
		initLayout();
	}
	
	protected void initData() {

	}

	protected void initLayout() {
		
		btnBack =_getView(R.id.acty_head_btn_back);
		btnBack.setOnClickListener(this);
		
		btnAbout = _getView(R.id.acty_setting_btn_about);
		btnAbout.setOnClickListener(this);
		
		btnIntroduce = _getView(R.id.acty_setting_btn_introduce);
		btnIntroduce.setOnClickListener(this);
		
		btnQuit = _getView(R.id.acty_setting_btn_quit);
		btnQuit.setOnClickListener(this);
		
		btnTickling = _getView(R.id.acty_setting_btn_tickling);
		btnTickling.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent intent = new Intent();
		switch (id) {
		
		case R.id.acty_head_btn_back:
			finish();
			break;
		
		case R.id.acty_setting_btn_about:
			intent.setClass(Setting.this, About.class);
			startActivity(intent);
			break;
			
		case R.id.acty_setting_btn_introduce:
			intent.setClass(Setting.this, Introduce.class);
			startActivity(intent);
			break;
			
		case R.id.acty_setting_btn_quit:
			toast("正在退出");
			((App)getApplication()).cleanUpInfo();
			toast("已经退出当前用户");
			openActivity(Login.class);
			finish();
			break;
			
		case R.id.acty_setting_btn_tickling:
			intent.setClass(Setting.this, Tickling.class);
			startActivity(intent);

			break;

		default:
			break;
		}
	}

}
