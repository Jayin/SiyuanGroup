package com.alumnigroup.app.acty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.User;
import com.alumnigroup.widget.OutoLinefeedLayout;

/**
 * 查看别人的空间
 * 
 * @author vector
 * 
 */
public class SpaceOther extends BaseActivity {

	/**
	 * 用户数据
	 */
	private User user;

	/**
	 * header
	 */
	private View btnBack, btnMore;
	private TextView tvHeaderTitle;

	/**
	 * top
	 */
	private View btnAddfriend;
	private ImageView ivPortrait, ivBackgroupImage;
	private TextView tvLeave2Visitor;
	private TextView tvName;

	/**
	 * 个人资料
	 */
	private LinearLayout llPersonalData;
	private View btnPersonalDataMore;

	/**
	 * keyword 布局：一个AutoLinefeedLayout .自动换行布局
	 */
	private OutoLinefeedLayout lyKeyword;

	/**
	 * 相册
	 */
	private OutoLinefeedLayout lyAlbum;
	private View btnAlbumMore;

	/**
	 * 新动态
	 */
	private LinearLayout llNewDynamic;

	/**
	 * 最近访客
	 */

	private OutoLinefeedLayout lyVisitors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_space_other);
		initData();
		initLayout();
		/**
		 * 初始化keyword 消息
		 */
		initKeyWord();
		initVisitor();
		initAlbum();
		initPersonalData();
		initNewDynamic();
	}

	@Override
	protected void initData() {
		user = (User) getIntent().getSerializableExtra("user");
		if (user == null) {
			toast("出错了");
			finish();
		}
	}

	@Override
	protected void initLayout() {

		/**
		 * header
		 */
		btnBack = _getView(R.id.acty_head_btn_back);
		btnBack.setOnClickListener(this);
		tvHeaderTitle = (TextView) _getView(R.id.acty_head_tv_title);
		tvHeaderTitle.setText(user.getName() + "的空间");
		/**
		 * 个人资料
		 */
		llPersonalData = (LinearLayout) _getView(R.id.acty_space_other_personaldata_ll_content);
		btnPersonalDataMore = _getView(R.id.acty_space_other_personaldata_btn_more);
		btnPersonalDataMore.setOnClickListener(this);

		/**
		 * 关键字
		 */
		lyKeyword = (OutoLinefeedLayout) _getView(R.id.acty_space_other_keyword_OutoLinefeed);
		lyKeyword.setMargin(10);

		/**
		 * 相册
		 */
		lyAlbum = (OutoLinefeedLayout) _getView(R.id.acty_space_other_album_outoLinefeed);
		btnAlbumMore = _getView(R.id.acty_space_other_album_btn_more);
		btnAlbumMore.setOnClickListener(this);

		/**
		 * 最近访客
		 */
		lyVisitors = (OutoLinefeedLayout) _getView(R.id.acty_space_other_visitor_outoLinefeed);

		/**
		 * 新动态
		 */
		llNewDynamic = (LinearLayout) _getView(R.id.acty_space_other_new_dynamica_ll_content);

		/**
		 * top
		 */
		btnAddfriend = _getView(R.id.acty_space_other_top_btn_add_friend);
		btnAddfriend.setOnClickListener(this);
		tvLeave2Visitor = (TextView) _getView(R.id.acty_space_other_top_tv_leave2visitor);
		tvLeave2Visitor.setText(user.getSignature());
		tvName = (TextView) _getView(R.id.acty_space_other_top_tv_name);
		tvName.setText(user.getName());
	}

	/**
	 * 初始化关键字
	 */
	private void initKeyWord() {
		for (int i = 0; i < 1; i++) {
			addKeyWord("大是字          sdf", i / 2);
		}
	}

	/**
	 * 初始化最新动态
	 */
	private void initNewDynamic() {
		for (int i = 0; i < 5; i++) {
			addNewDynamic(i);
		}
	}

	/**
	 * 初始化访客
	 */
	private void initVisitor() {
		for (int i = 0; i < 5; i++) {
			addVisitor(i);
		}
	}

	/**
	 * 初始化相册
	 */
	private void initAlbum() {
		for (int i = 0; i < 5; i++) {
			addAlbum(i);
		}
	}

	/**
	 * 初始化个人资料
	 */
	private void initPersonalData() {
		for (int i = 0; i < 5; i++) {
			addPersonalData(i);
		}
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
		 * 加入新的关键字
		 */
		llPersonalData.addView(convertView);
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

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent intent = new Intent();
		switch (id) {
		case R.id.acty_space_other_personaldata_btn_more:
			intent.setClass(SpaceOther.this, PersonalData.class);
			startActivity(intent);
			break;

		case R.id.acty_space_other_top_btn_add_friend:
			intent.setClass(SpaceOther.this, AddFriend.class);
			startActivity(intent);
			break;

		case R.id.acty_space_other_album_btn_more:
			intent.setClass(SpaceOther.this, AlbumDisplay.class);
			startActivity(intent);
			break;

		case R.id.acty_head_btn_back:
			finish();
			break;

		default:
			break;
		}

	}
}
