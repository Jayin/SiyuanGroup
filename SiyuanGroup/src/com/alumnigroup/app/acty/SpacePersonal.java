package com.alumnigroup.app.acty;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alumnigroup.api.DynamicAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.api.UserAPI;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Dynamic;
import com.alumnigroup.entity.User;
import com.alumnigroup.utils.BitmapUtils;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.FilePath;
import com.alumnigroup.utils.ImageUtils;
import com.alumnigroup.utils.JsonUtils;
import com.alumnigroup.widget.OutoLinefeedLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author vector
 * 
 */
public class SpacePersonal extends BaseActivity {

	private User myself;

	private View btnBack, btnMore;
	private TextView tvHeaderTitle;

	/**
	 * 顶部
	 */
	private TextView tvLeave2Visitor;
	private ImageView ivBackgroup;
	private Bitmap backgroupBitmap;

	private ImageView ivPortrait;
	private TextView tvUsername;

	/**
	 * 个人资料
	 */
	private LinearLayout llPersonalData;
	private View btnPersonalDataEdit;
	/**
	 * 关键字
	 */
	private OutoLinefeedLayout lyKeyword;
	private View btnEditKeywork;
	/**
	 * 相册
	 */
	private OutoLinefeedLayout lyAlbum;
	private View btnEditAlbum;

	/**
	 * 新动态
	 */
	private LinearLayout llNewDynamic;

	private UserAPI api;
	private AlertDialog dialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_space_personal);
		initLayout();
	}

	@Override
	protected void onStart() {
		super.onStart();
		initData();
	}

	protected void initData() {
		myself = (User) AppInfo.getUser(SpacePersonal.this);

		tvLeave2Visitor.setText(myself.getProfile().getSummary());
		ImageLoader.getInstance().displayImage(
				RestClient.BASE_URL + myself.getCover(), ivBackgroup);
		ImageLoader.getInstance().displayImage(
				RestClient.BASE_URL + myself.getAvatar(), ivPortrait);
		tvUsername.setText(myself.getProfile().getName());

		addData();
	}

	protected void initLayout() {
		api = new UserAPI();
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("更新中···");
		dialog = builder.create();

		btnBack = _getView(R.id.acty_head_btn_back);
		btnBack.setOnClickListener(this);

		btnMore = _getView(R.id.acty_head_btn_more);
		btnMore.setOnClickListener(this);

		tvHeaderTitle = (TextView) _getView(R.id.acty_head_tv_title);
		tvHeaderTitle.setText("我的空间");

		/**
		 * 顶部
		 */
		tvLeave2Visitor = (TextView) _getView(R.id.acty_space_personal_top_et_leave2visitor);

		ivBackgroup = (ImageView) _getView(R.id.acty_space_personal_top_iv_backgroup);
		ivBackgroup.setOnClickListener(this);

		ivPortrait = (ImageView) _getView(R.id.acty_space_personal_top_iv_portrait);

		tvUsername = (TextView) _getView(R.id.acty_space_personal_top_tv_name);

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

		btnEditKeywork = _getView(R.id.acty_space_personal_keyword_btn_edit);
		btnEditKeywork.setOnClickListener(this);

		/**
		 * 相册
		 */
		lyAlbum = (OutoLinefeedLayout) _getView(R.id.acty_space_personal_album_outoLinefeed);
		btnEditAlbum = _getView(R.id.acty_space_personal_album_btn_edit);
		btnEditAlbum.setOnClickListener(this);

		/**
		 * 新动态
		 */
		llNewDynamic = (LinearLayout) _getView(R.id.acty_space_personal_new_dynamic_ll_content);
	}

	private void addData() {
		initPersonalData();

		lyKeyword.removeAllViews();
		if (myself.getProfile().getTag() != null
				&& !myself.getProfile().getTag().equals("")) {
			String tags[] = myself.getProfile().getTag().split(",");
			int color = 0;
			for (String tag : tags) {
				addKeyWord(tag, color++ % 2);
			}
		}
		lyAlbum.removeAllViews();
		for (int i = 0; i < 5; i++) {
			addAlbum(i);
		}
		addNewDynamic();
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
		api.getLimit(5, myself.getId(), new AsyncHttpResponseHandler() {

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

							ImageLoader.getInstance().displayImage(
									RestClient.BASE_URL
											+ dynamic.getUser().getAvatar(),
									portrait);
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
							/**
							 */
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
	 * 增加相册 , 参数使用来测试的，到时候填充数据的时候要改
	 */
	private void addAlbum(int i) {
		/**
		 */
		LayoutInflater inflater = null;
		inflater = LayoutInflater.from(this);
		View convertView = null;

		convertView = inflater.inflate(R.layout.item_acty_space_other_ly_image,
				null);

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
	 * 初始化个人资料
	 */
	private void initPersonalData() {
		llPersonalData.removeAllViews();
//		addPersonalData("真实姓名", myself.getProfile().getName());
		String gender = null;
		if ("m".equalsIgnoreCase(myself.getProfile().getGender())) {
			gender = "男";
		}
		if ("f".equalsIgnoreCase(myself.getProfile().getGender())) {
			gender = "女";
		}
		addPersonalData("性别", gender);
		addPersonalData("年龄", myself.getProfile().getAge() + "");
		addPersonalData("大学", myself.getProfile().getUniversity());
		addPersonalData("毕业届数", myself.getProfile().getGrade() + "");
		addPersonalData("专业", myself.getProfile().getMajor());
	}

	/**
	 * 增加
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			dialog.show();

			/**
			 * 因为两种方式都用到了startActivityForResult方法，
			 * 这个方法执行完后都会执行onActivityResult方法， 所以为了区别到底选择了那个方式获取图片要进行判断，
			 * 这里的requestCode跟startActivityForResult里面第二个参数对应
			 */
			try {
				if (requestCode == 0) {
					backgroupBitmap = ImageUtils.getBitmapByPath(FilePath
							.getImageFilePath() + "cache_space_back.jpg");

				} else if (requestCode == 1) {
					Bundle extras = data.getExtras();
					backgroupBitmap = (Bitmap) extras.get("data");
				}
			} catch (Exception e) {
			   e.printStackTrace();
			}

			api.updateCover(BitmapUtils.getBitmapInputStream(backgroupBitmap),
					new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] data, Throwable err) {
							toast("网络异常 错误码:" + statusCode);
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] data) {
							String json = new String(data);// jsonarray
							if (JsonUtils.isOK(json)) {
								updatePdUser();
								// ImageLoader.getInstance().displayImage(
								// RestClient.BASE_URL + myself.getCover(),
								// ivBackgroup);
								ivBackgroup.setImageBitmap(backgroupBitmap);
								toast("更新成功");
							} else {
								toast("更新失败");
							}

						}
					});
		}
	}

	/**
	 * 更新user 数据
	 */
	private void updatePdUser() {
		api.find(new RequestParams("id", myself.getId()),
				new AsyncHttpResponseHandler() {
					@Override
					public void onFinish() {
						dialog.cancel();
					}

					public void onSuccess(int statusCode, Header[] headers,
							byte[] data) {
						String json = new String(data);
						if (JsonUtils.isOK(json)) {
							try {
								AppInfo.setUser(json, SpacePersonal.this);
							} catch (ClientProtocolException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent intent = getIntent();
		switch (id) {
		case R.id.acty_head_btn_back:
			finish();
			break;

		case R.id.acty_head_btn_more:
			intent.setClass(SpacePersonal.this, SpaceOther.class);
			intent.putExtra("user", new User());
			startActivity(intent);
			break;

		case R.id.acty_space_personal_personaldata_btn_edit:
			intent.setClass(SpacePersonal.this, EditPersonalData.class);
			intent.putExtra("user", new User());
			startActivity(intent);
			break;

		case R.id.acty_space_personal_keyword_btn_edit:
			intent.setClass(SpacePersonal.this, EditKeyword.class);
			intent.putExtra("user", new User());
			startActivity(intent);
			break;

		case R.id.acty_space_personal_album_btn_edit:
			intent.setClass(SpacePersonal.this, EditAlbum.class);
			startActivity(intent);
			break;

		case R.id.acty_space_personal_top_iv_backgroup:
			final CharSequence[] items = { "相册" };
			AlertDialog dlg = new AlertDialog.Builder(SpacePersonal.this)
					.setTitle("更新背景图")
					.setItems(items, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							// 这里item是根据选择的方式，
							// 在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法
							if (item == 1) {
								Intent getImageByCamera = new Intent(
										"android.media.action.IMAGE_CAPTURE");
								startActivityForResult(getImageByCamera, 1);
							} else {
								Intent getImage = new Intent(Intent.ACTION_PICK);
								String path = FilePath.getImageFilePath()
										+ "cache_space_back.jpg";
								File protraitFile = new File(path);
								Uri uri = Uri.fromFile(protraitFile);
								getImage.setType("image/*");
								getImage.putExtra("output", uri);
								getImage.putExtra("crop", "true");
								getImage.putExtra("aspectX", 2);
								getImage.putExtra("aspectY", 1);
								getImage.putExtra("outputX", 200);
								getImage.putExtra("outputY", 100);
								startActivityForResult(getImage, 0);
							}
						}
					}).create();
			dlg.show();
			break;

		default:
			break;
		}
	}

}
