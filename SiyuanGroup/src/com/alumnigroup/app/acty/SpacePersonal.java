package com.alumnigroup.app.acty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.widget.OutoLinefeedLayout;

/**
 * 个人空间
 * 
 * @author vector
 * 
 */
public class SpacePersonal extends BaseActivity implements
		OnFocusChangeListener, OnTouchListener {

	/**
	 * 整个界面
	 */
	private View viewParent;
	
	/**
	 * header
	 */
	private View btnBack,btnMore;
	private TextView tvHeaderTitle;

	/**
	 * 顶部
	 */
	private EditText etLeave2Visitor;
	private String oldLeave2VisitorContent;

	/**
	 * 个人资料
	 */
	private LinearLayout  llPersonalData;
	private View btnPersonalDataEdit;

	/**
	 * 关键字
	 */
	private OutoLinefeedLayout lyKeyword;
	/**
	 * 相册
	 */
	private OutoLinefeedLayout lyAlbum;

	/**
	 * 新动态
	 */
	private LinearLayout llNewDynamic;

	/**
	 * 访客
	 */
	private OutoLinefeedLayout lyVisitors;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_space_personal);
		initData();
		initLayout();
		addData();
	}

	protected void initData() {

	}

	protected void initLayout() {

		viewParent = _getView(R.id.viewparent);
		viewParent.setOnTouchListener(this);
		
		/**
		 * header
		 */
		btnBack = _getView(R.id.acty_head_btn_back);
		btnBack.setOnClickListener(this);
		btnMore = _getView(R.id.acty_head_btn_more);
		btnMore.setOnClickListener(this);

		/**
		 * 顶部
		 */
		etLeave2Visitor = (EditText) _getView(R.id.acty_space_personal_top_et_leave2visitor);
		etLeave2Visitor.setOnFocusChangeListener(this);
		etLeave2Visitor.setOnTouchListener(this);
		oldLeave2VisitorContent = etLeave2Visitor.getText().toString();

		/**
		 * 个人资料
		 */
		llPersonalData = (LinearLayout) _getView(R.id.acty_space_personal_personaldata_ll_content);
		btnPersonalDataEdit = _getView(R.id.acty_space_personal_personaldata_btn_edit);
		btnPersonalDataEdit.setOnClickListener(this);

		/**
		 * 关键字
		 */
		lyKeyword = (OutoLinefeedLayout) _getView(R.id.acty_space_personal_keyword_OutoLinefeed);
		lyKeyword.setMargin(10);

		/**
		 * 相册
		 */
		lyAlbum = (OutoLinefeedLayout) _getView(R.id.acty_space_personal_album_outoLinefeed);

		/**
		 * 新动态
		 */
		llNewDynamic = (LinearLayout) _getView(R.id.acty_space_personal_new_dynamic_ll_content);

		/**
		 * 最近访客
		 */
		lyVisitors = (OutoLinefeedLayout) _getView(R.id.acty_space_personal_visitor_outoLinefeed);

	}

	/**
	 * 测试用的
	 */
	private void addData() {
		for (int i = 0; i < 5; i++) {
			addPersonalData(i);
		}
		for (int i = 0; i < 5; i++) {
			addKeyWord("sdfsdfsdfsdfsd", i % 2);
		}
		for (int i = 0; i < 5; i++) {
			addAlbum(i);
		}
		for (int i = 0; i < 5; i++) {
			addNewDynamic(i);
		}
		for (int i = 0; i < 5; i++) {
			addVisitor(i);
		}
	}

	/**
	 * 增加访客 , 参数使用来测试的，到时候填充数据的时候要改
	 */
	private void addVisitor(int id) {
		/**
		 * 装上TextView
		 */
		LayoutInflater inflater = null;
		inflater = LayoutInflater.from(this);
		View convertView = null;

		convertView = inflater.inflate(R.layout.item_space_other_visitor, null);

		/**
		 * 加入新的关键字
		 */
		lyVisitors.addView(convertView);
	}

	/**
	 * 增加最新动态 , 参数使用来测试的，到时候填充数据的时候要改
	 */
	private void addNewDynamic(int i) {
		/**
		 */
		LayoutInflater inflater = null;
		inflater = LayoutInflater.from(this);
		View convertView = null;

		convertView = inflater.inflate(R.layout.item_lv_alldynamic, null);

		/**
		 */
		llNewDynamic.addView(convertView);
	}

	/**
	 * 增加相册 , 参数使用来测试的，到时候填充数据的时候要改
	 */
	private void addAlbum(int i) {
		/**
		 */
		LayoutInflater inflater = null;
		inflater = LayoutInflater.from(this);
		View convertView = null;

		convertView = inflater.inflate(
				R.layout.item_space_other_album_gv_image, null);

		/**
		 * 加入新的关键字
		 */
		lyAlbum.addView(convertView);
	}

	/**
	 * 增加一个keyword
	 * 
	 * @param content
	 *            要增加的内容
	 * @param background
	 *            使用的背景色 0为yellow 1为green
	 */
	@SuppressLint("NewApi")
	private void addKeyWord(String content, int background) {
		/**
		 * 装上TextView
		 */
		LayoutInflater inflater = null;
		inflater = LayoutInflater.from(this);
		View convertView = null;

		if (background == 0) {
			convertView = inflater.inflate(R.layout.item_keyword_yellow, null);
		} else {
			convertView = inflater.inflate(R.layout.item_keyword_green, null);

		}
		TextView tvKeyWord = (TextView) convertView
				.findViewById(R.id.item_keyword_tv_value);
		tvKeyWord.setText(content);

		/**
		 * 加入新的关键字
		 */
		lyKeyword.addView(convertView);
	}

	/**
	 * 增加, 参数使用来测试的，到时候填充数据的时候要改
	 */
	private void addPersonalData(int i) {
		/**
		 */
		LayoutInflater inflater = null;
		inflater = LayoutInflater.from(this);
		View convertView = null;

		convertView = inflater.inflate(
				R.layout.itme_lv_space_other_personaldata, null);

		/**
		 * 
		 */
		llPersonalData.addView(convertView);
	}

	/**
	 * 写的访客话的EditText 如果失去焦点，表明编辑完了
	 */
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			String nowContent = etLeave2Visitor.getText().toString();
			if (!nowContent.equals(oldLeave2VisitorContent)) {
				toast("更新完成");
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		if ((event.getAction() & MotionEvent.ACTION_MASK) != MotionEvent.ACTION_DOWN) {
			return false;
		}
		
		int id = v.getId();
		switch (id) {
		case R.id.acty_space_personal_top_et_leave2visitor:
			//toast("touch  ---  et");
			etLeave2Visitor.setFocusableInTouchMode(true);
			etLeave2Visitor.requestFocus();
			return false;

		case R.id.viewparent:
			//toast("touch  ---  view");
			etLeave2Visitor.setFocusable(false);
			return false;
		default:
			break;
		}
		return false;
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent intent = new Intent();
		switch (id) {
		case R.id.acty_head_btn_back:
			finish();
			break;
			
		case R.id.acty_head_btn_more:
			intent.setClass(SpacePersonal.this, SpaceOther.class);
			startActivity(intent);
			break;
			
			
		default:
			break;
		}
	}
}
