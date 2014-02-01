package com.alumnigroup.app.acty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.User;
import com.alumnigroup.widget.OutoLinefeedLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 查看别人的空间, 进入这个界面，必须intent.putExtra("user", User对象);
 * 
 * @author vector
 * 
 */
public class SpaceOther extends BaseActivity {

	private View btnAddfriend;

	private User user;

	/**
	 * header
	 */
	private View btnBack, btnMore;
	private TextView tvHeaderTitle;
	
	/**
	 * top
	 */
	private ImageView ivBackground,ivPortrait;
	private TextView tvTopName;
	private TextView tvTopSummary;
	/**
	 * 个人资料
	 */
	private LinearLayout llPersonalData;

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
		initTop();
	}

	private void initTop() {
		ivBackground = (ImageView) _getView(R.id.acty_space_other_top_iv_background);
		ImageLoader.getInstance().displayImage(
			    RestClient.BASE_URL + user.getCover(), ivBackground); 
		ivPortrait = (ImageView) _getView(R.id.acty_space_other_top_iv_portrait);
		ImageLoader.getInstance().displayImage(
			    RestClient.BASE_URL + user.getAvatar(), ivPortrait); 
		
		tvTopName = (TextView) _getView(R.id.acty_space_other_top_tv_name);
		tvTopName.setText(user.getProfile().getName());
		
		tvTopSummary = (TextView) _getView(R.id.acty_space_other_top_tv_leave2visitor);
		tvTopSummary.setText(user.getProfile().getSummary());
	}

	@Override
	protected void initData() {
		user = (User) getIntent().getSerializableExtra("user");
	}

	@Override
	protected void initLayout() {

		/**
		 * header
		 */
		btnBack = _getView(R.id.acty_head_btn_back);
		btnBack.setOnClickListener(this);
		tvHeaderTitle = (TextView) _getView(R.id.acty_head_tv_title);
		tvHeaderTitle.setText(user.getProfile().getName());

		/**
		 * 个人资料
		 */
		llPersonalData = (LinearLayout) _getView(R.id.acty_space_other_personaldata_ll_content);

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

		btnAddfriend = _getView(R.id.acty_space_other_top_btn_add_friend);
		btnAddfriend.setOnClickListener(this);
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

		addPersonalData("昵称", user.getProfile().getNickname());
		addPersonalData("用户名", user.getProfile().getName());
		addPersonalData("性别", user.getProfile().getGender());
		addPersonalData("年龄", user.getProfile().getAge() + "");
		addPersonalData("大学", user.getProfile().getUniversity());
		addPersonalData("毕业届数", user.getProfile().getGrade() + "");
		addPersonalData("专业", user.getProfile().getMajor());
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
	private void addPersonalData(String name, String value) {
		/**
		 */
		LayoutInflater inflater = null;
		inflater = LayoutInflater.from(this);
		View convertView = null;

		convertView = inflater.inflate(
				R.layout.itme_lv_space_other_personaldata, null);
		TextView tvName = (TextView) convertView
				.findViewById(R.id.item_lv_space_other_personaldata_name);
		tvName.setText(name);
		TextView tvValue = (TextView) convertView
				.findViewById(R.id.item_lv_space_other_personaldata_value);
		tvValue.setText(value);

		/**
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
