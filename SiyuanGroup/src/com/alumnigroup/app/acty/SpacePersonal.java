package com.alumnigroup.app.acty;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Dynamic;
import com.alumnigroup.entity.User;
import com.alumnigroup.utils.JsonUtils;
import com.alumnigroup.utils.L;
import com.alumnigroup.widget.OutoLinefeedLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 个人空间,进来要传递一个user 对象，用来代表自己, key = myself,，不然就不要进了
 * 
 * @author vector
 * 
 */
public class SpacePersonal extends BaseActivity {
	/**
	 * 代表自己的对象
	 */
	private User myself;
	/**
	 * 整个界面
	 */
	private View viewParent;

	/**
	 * header
	 */
	private View btnBack, btnMore;
	private TextView tvHeaderTitle;

	/**
	 * 顶部
	 */
	private TextView etLeave2Visitor;
	private String oldLeave2VisitorContent;
	private ImageView ivBackgroup;
	private Bitmap backgroupBitmap;
	private byte[] backgroupData;

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

	/**
	 * 访客
	 */
	private OutoLinefeedLayout lyVisitors;

	private UserAPI api = new UserAPI();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_space_personal);
		initData();
		initLayout();
		addData();
	}

	protected void initData() {
		myself = (User) getIntent().getSerializableExtra("myself");
		if (myself == null) {
			toast("出错了，需要myself 为key的user");
			finish();
		}
	}

	protected void initLayout() {

		viewParent = _getView(R.id.viewparent);

		/**
		 * header
		 */
		btnBack = _getView(R.id.acty_head_btn_back);
		btnBack.setOnClickListener(this);
		btnMore = _getView(R.id.acty_head_btn_more);
		btnMore.setOnClickListener(this);
		tvHeaderTitle = (TextView) _getView(R.id.acty_head_tv_title);
		tvHeaderTitle.setText("我的空间");

		/**
		 * 顶部
		 */
		etLeave2Visitor = (TextView) _getView(R.id.acty_space_personal_top_et_leave2visitor);
		// etLeave2Visitor.setText(myself.getSignature());
		etLeave2Visitor.setText(myself.getProfile().getSummary()); // 签名已经移除
		oldLeave2VisitorContent = etLeave2Visitor.getText().toString();
		ivBackgroup = (ImageView) _getView(R.id.acty_space_personal_top_iv_backgroup);
		ivBackgroup.setOnClickListener(this);
		ImageLoader.getInstance().displayImage(
				RestClient.BASE_URL + myself.getCover(), ivBackgroup);

		ivPortrait = (ImageView) _getView(R.id.acty_space_personal_top_iv_portrait);
		ImageLoader.getInstance().displayImage(
				RestClient.BASE_URL + myself.getAvatar(), ivPortrait);

		tvUsername = (TextView) _getView(R.id.acty_space_personal_top_tv_name);
		tvUsername.setText(myself.getUsername());

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

		/**
		 * 最近访客
		 */
		lyVisitors = (OutoLinefeedLayout) _getView(R.id.acty_space_personal_visitor_outoLinefeed);

	}

	/**
	 * 测试用的
	 */
	private void addData() {
		initPersonalData();
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
		DynamicAPI api = new DynamicAPI();
		/**
		 */
		final LayoutInflater inflater = LayoutInflater.from(this);
		api.getAll(1, 13/* myself.getId() */, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] data, Throwable err) {
				toast("网络异常 错误码:" + statusCode);
				if (data != null)
					L.i(new String(data));
				if (err != null)
					L.i(err.toString());
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
							name.setText(dynamic.getUser().getUsername());
							TextView content = (TextView) convertView
									.findViewById(R.id.item_lv_alldynamic_tv_content);
							content.setText(dynamic.getMessage());
							TextView time = (TextView) convertView
									.findViewById(R.id.item_lv_alldynamic_tv_datetime);
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

		addPersonalData("昵称", myself.getProfile().getNickname());
		addPersonalData("用户名", myself.getProfile().getName());
		addPersonalData("性别", myself.getProfile().getGender());
		addPersonalData("年龄", myself.getProfile().getAge() + "");
		addPersonalData("大学", myself.getProfile().getUniversity());
		addPersonalData("毕业届数", myself.getProfile().getGrade() + "");
		addPersonalData("专业", myself.getProfile().getMajor());
	}

	/**
	 * 增加,
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

		ContentResolver resolver = getContentResolver();
		/**
		 * 因为两种方式都用到了startActivityForResult方法， 这个方法执行完后都会执行onActivityResult方法，
		 * 所以为了区别到底选择了那个方式获取图片要进行判断，
		 * 这里的requestCode跟startActivityForResult里面第二个参数对应
		 */
		if (requestCode == 0) {
			try {
				// 获得图片的uri
				Uri originalUri = data.getData();
				// 将图片内容解析成字节数组
				backgroupData = readStream(resolver.openInputStream(Uri
						.parse(originalUri.toString())));
				// 将字节数组转换为ImageView可调用的Bitmap对象
				backgroupBitmap = getPicFromBytes(backgroupData, null);
				// 把得到的图片绑定在控件上显示
				ivBackgroup.setImageBitmap(backgroupBitmap);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else if (requestCode == 1) {
			try {
				super.onActivityResult(requestCode, resultCode, data);
				Bundle extras = data.getExtras();
				backgroupBitmap = (Bitmap) extras.get("data");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				backgroupBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				backgroupData = baos.toByteArray();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 把得到的图片绑定在控件上显示
			ivBackgroup.setImageBitmap(backgroupBitmap);
		}
		api.updateCover(myself.getId(), backgroupData,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] data, Throwable err) {
						toast("网络异常 错误码:" + statusCode);
						if (data != null)
							L.i(new String(data));
						if (err != null)
							L.i(err.toString());
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] data) {
						toast("更新完成");
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
			final CharSequence[] items = { "相册", "拍照" };
			AlertDialog dlg = new AlertDialog.Builder(SpacePersonal.this)
					.setTitle("选择图片")
					.setItems(items, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							// 这里item是根据选择的方式，
							// 在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法
							if (item == 1) {
								Intent getImageByCamera = new Intent(
										"android.media.action.IMAGE_CAPTURE");
								startActivityForResult(getImageByCamera, 1);
							} else {
								Intent getImage = new Intent(
										Intent.ACTION_GET_CONTENT);
								getImage.addCategory(Intent.CATEGORY_OPENABLE);
								getImage.setType("image/jpeg");
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

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}

	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

}
