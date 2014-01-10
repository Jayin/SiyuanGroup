package com.alumnigroup.app.acty;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alumnigroup.adapter.SpaceOtherPersonalDataAdapter;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;

/**
 * 显示个人全部资料的
 * 
 * @author vector
 * 
 */
public class PersonalData extends BaseActivity {

	private ListView lvContent;
	private View btnBack;
	private TextView tvTitle;
	
	private ArrayList<SpaceOtherPersonalDataAdapter.PersonalData> personalDatas;
	private SpaceOtherPersonalDataAdapter adapterPersonalData;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_personaldata);
		initData();
		initLayout();
	}

	protected void initData() {
		personalDatas = new ArrayList<SpaceOtherPersonalDataAdapter.PersonalData>(){{
			add(new SpaceOtherPersonalDataAdapter.PersonalData("毕业学校", "五邑大学"));
			add(new SpaceOtherPersonalDataAdapter.PersonalData("毕业学校", "五邑大学"));
			add(new SpaceOtherPersonalDataAdapter.PersonalData("毕业学校", "五邑大学"));
			add(new SpaceOtherPersonalDataAdapter.PersonalData("毕业学校", "五邑大学"));
			add(new SpaceOtherPersonalDataAdapter.PersonalData("毕业学校", "五邑大学"));
			add(new SpaceOtherPersonalDataAdapter.PersonalData("毕业学校", "五邑大学"));
			add(new SpaceOtherPersonalDataAdapter.PersonalData("毕业学校", "五邑大学"));
			add(new SpaceOtherPersonalDataAdapter.PersonalData("毕业学校", "五邑大学"));
		}};
	}

	protected void initLayout() {
		lvContent = (ListView) _getView(R.id.acty_personaldata_lv_content);
		adapterPersonalData = new SpaceOtherPersonalDataAdapter(personalDatas, this);
		lvContent.setAdapter(adapterPersonalData);
		
		btnBack = _getView(R.id.acty_head_btn_back);
		btnBack.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		finish();
	}
}
