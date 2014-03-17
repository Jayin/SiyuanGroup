package com.alumnigroup.app.acty;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alumnigroup.api.DynamicAPI;
import com.alumnigroup.api.FollowshipAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.AppCache;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Dynamic;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.utils.JsonUtils;
import com.alumnigroup.utils.L;
import com.alumnigroup.widget.OutoLinefeedLayout;
import com.alumnigroup.widget.SendMsgDialog;
import com.custom.view.FlowLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 他人空间
 * 
 * @author vector;Jayin Ton sice 2014.3.7
 * 
 */
public class SpaceOther extends BaseActivity {

//	private View btnAddfriend;

	private User user;

	private SendMsgDialog dialog;

	/**
	 * header
	 */
	private View btnBack, btnMore;
	private TextView tvHeaderTitle;

	/**
	 * top
	 */
	private ImageView ivBackground, ivPortrait;
	private TextView tvTopName;
	private TextView tvTopSummary;
	private View btnFollow;
	private TextView tvBtnFollowContent;
	/**
	 * 个人资料
	 */
	private LinearLayout llPersonalData;

	/**
	 * keyword 布局：一个AutoLinefeedLayout .自动换行布局
	 */
	private FlowLayout flowlayout_keyword;

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

		ivPortrait = (ImageView) _getView(R.id.acty_space_other_top_iv_portrait);

		if (user.getCover() != null) {
			ImageLoader.getInstance().displayImage(
					RestClient.BASE_URL + user.getCover(), ivBackground);
		} else {
			ImageLoader.getInstance().displayImage(
					"drawable://" + R.drawable.ic_image_load_normal,
					ivBackground);
		}

		if (user.getCover() != null) {
			ImageLoader.getInstance().displayImage(
					RestClient.BASE_URL + user.getAvatar(), ivPortrait);
		} else {
			ImageLoader.getInstance()
					.displayImage(
							"drawable://" + R.drawable.ic_image_load_normal,
							ivPortrait);
		}

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
		dialog = new SendMsgDialog(getContext());
		dialog.setReceiverId(user.getId());

		/**
		 * header
		 */
		btnBack = _getView(R.id.acty_head_btn_back);
		btnBack.setOnClickListener(this);
		tvHeaderTitle = (TextView) _getView(R.id.acty_head_tv_title);
		tvHeaderTitle.setText(user.getProfile().getName());

		btnFollow = _getView(R.id.acty_space_other_top_btn_add_friend);
		tvBtnFollowContent = (TextView) _getView(R.id.acty_space_other_top_btn_add_friend_content);
		L.i("is followed-->:"+user.getIsfollowed());
		if (user.getIsfollowed() == 1) {
			tvBtnFollowContent.setText("取消关注");
		} else {
			tvBtnFollowContent.setText("关 注");
		}
		btnFollow.setOnClickListener(this);

		/**
		 * 个人资料
		 */
		llPersonalData = (LinearLayout) _getView(R.id.acty_space_other_personaldata_ll_content);

		/**
		 * 关键字
		 */
		flowlayout_keyword = (FlowLayout) _getView(R.id.flowlayout_keyword);

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

		_getView(R.id.acty_head_btn_more).setOnClickListener(this);
	}

	/**
	 * 初始化关键字
	 */
	private void initKeyWord() {
		if (user.getProfile().getTag() != null
				&& !user.getProfile().getTag().equals("")) {
			String tags[] = user.getProfile().getTag().split(",");
			int color = 0;
			for (String tag : tags) {
				addKeyWord(tag, color++ % 2);
			}
		}
	}

	/**
	 * 初始化最新动态
	 */
	private void initNewDynamic() {
		addNewDynamic();
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

		// addPersonalData("真实姓名", user.getProfile().getName());
		addPersonalData("性别", user.getProfile().getGender().equals("m")?"男":"女");
		addPersonalData("年龄", user.getProfile().getAge()==0?"保密":user.getProfile().getAge()+"");
		addPersonalData("大学", user.getProfile().getUniversity());
		addPersonalData("毕业届数", user.getProfile().getGrade() + "");
		addPersonalData("专业", user.getProfile().getMajor());
	}

	/**
	 * 增加最新动态 , 到时候填充数据的时候要改
	 */
	private void addNewDynamic() {
		DynamicAPI api = new DynamicAPI();
		llNewDynamic.removeAllViews();
		/**
		 */
		final LayoutInflater inflater = LayoutInflater.from(this);
		api.getLimit(5, user.getId(), new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] data, Throwable err) {
				toast("网络异常 错误码:" + statusCode);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] data) {
				// 还要判断是否有error_code
				String json = new String(data);// jsonarray
				if (JsonUtils.isOK(json)) {
					List<Dynamic> newData_all = Dynamic
							.create_by_jsonarray(json);
					if (newData_all != null) {
						for (Dynamic dynamic : newData_all) {

							View convertView = null;

							convertView = inflater.inflate(
									R.layout.item_lv_alldynamic_update_spatial,
									null);

							ImageView portrait = (ImageView) convertView
									.findViewById(R.id.item_lv_alldynamic_iv_portrait);
							if (dynamic.getUser().getAvatar() != null) {
								ImageLoader.getInstance()
										.displayImage(
												RestClient.BASE_URL
														+ dynamic.getUser()
																.getAvatar(),
												portrait);
							} else {
								ImageLoader
										.getInstance()
										.displayImage(
												"drawable://"
														+ R.drawable.ic_image_load_normal,
												portrait);
							}

							TextView name = (TextView) convertView
									.findViewById(R.id.item_lv_alldynamic_tv_name);
							name.setText(dynamic.getUser().getProfile()
									.getName());
							TextView content = (TextView) convertView
									.findViewById(R.id.item_lv_alldynamic_tv_content);
							content.setText(dynamic.getMessage());
							TextView time = (TextView) convertView
									.findViewById(R.id.item_lv_alldynamic_tv_datetime);
							time.setText(CalendarUtils.getTimeFromat(
									dynamic.getCreatetime(),
									CalendarUtils.TYPE_timeline));
							llNewDynamic.addView(convertView);
						}
					}
				} else {
					toast("Error:" + JsonUtils.getErrorString(json));
				}
			}
		});

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
		flowlayout_keyword.addView(convertView);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent intent = new Intent();
		switch (id) {

		case R.id.acty_space_other_top_btn_add_friend:
			if (tvBtnFollowContent.getText().toString().equals("关 注")) {
				follow();
			} else {
				unFollow();
			}
			btnFollow.setClickable(false);
			break;

		case R.id.acty_space_other_album_btn_more:
			intent.setClass(SpaceOther.this, AlbumDisplay.class);
			startActivity(intent);
			break;

		case R.id.acty_head_btn_back:
			finish();
			break;
		case R.id.acty_head_btn_more:
			dialog.show();
			break;
		default:
			break;
		}

	}

	private void follow() {
		tvBtnFollowContent.setText("关注中···");
		FollowshipAPI api = new FollowshipAPI();
		
		api.follow(user.getId(), user.getUsername(), new JsonResponseHandler() {
			
			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				tvBtnFollowContent.setText("取消关注");
//				AppCache.changeAllmemberAll(getContext(), user);
				//全部数据刷新一次
				user.setIsfollowed(1);
				AppCache.chengeAllmember(getContext(), user);
				Intent intent = new Intent(Constants.Action_User_edit);
				intent.putExtra("user", user);
				getContext().sendBroadcast(intent);
			}
			
			@Override
			public void onFaild(int errorType, int errorCode) {
				if(20506 == errorCode){
					toast("已关注");
				}else{
					toast("关注失败 "+ErrorCode.errorList.get(errorCode));
				}
				tvBtnFollowContent.setText("关 注");
			}
			
			@Override
			public void onFinish() {
				btnFollow.setClickable(true);
			}
		});
		
	}

	private void unFollow() {
		tvBtnFollowContent.setText("取消中···");
		FollowshipAPI api = new FollowshipAPI();
		
		api.unfollow(user.getId(), new JsonResponseHandler() {
			
			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				tvBtnFollowContent.setText("关 注");
				//粉丝列表删除
				AppCache.removeAllmemberFollowing(SpaceOther.this, user);
				Intent intent = new Intent(Constants.Action_User_unfollow);
				intent.putExtra("user", user);
				getContext().sendBroadcast(intent);
				//全部数据刷新一次
				user.setIsfollowed(0);
				AppCache.chengeAllmember(getContext(), user);
				intent = new Intent(Constants.Action_User_edit);
				intent.putExtra("user", user);
				getContext().sendBroadcast(intent);
			}
			
			@Override
			public void onFaild(int errorType, int errorCode) {
				toast("取消失败");
				tvBtnFollowContent.setText("取消关注");
			}
			
			@Override
			public void onFinish() {
				btnFollow.setClickable(true);
			}
		});
	}
}
