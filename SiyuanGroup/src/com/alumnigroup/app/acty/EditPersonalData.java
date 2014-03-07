package com.alumnigroup.app.acty;

import java.io.File;
import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.alumnigroup.api.RestClient;
import com.alumnigroup.api.UserAPI;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.User;
import com.alumnigroup.utils.BitmapUtils;
import com.alumnigroup.utils.FilePath;
import com.alumnigroup.utils.ImageUtils;
import com.alumnigroup.utils.JsonUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 修改个人资料
 * 
 * @author vector
 * 
 */
public class EditPersonalData extends BaseActivity {

	private View btnBack, btnOK;

	private ImageView ivPortrait;
	private RadioButton rbtnBoy, rbtnGril;
	private EditText etAge, etGrade, etUniversity, etMajor, etSummary, etName;
	private User myself;

	private UserAPI api = new UserAPI();

	private Bitmap portraitBitmap;

	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_edit_personaldata);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		myself = AppInfo.getUser(this);

		AlertDialog.Builder builder = new AlertDialog.Builder(
				EditPersonalData.this);
		builder.setMessage("更新中···");
		dialog = builder.create();
	}

	@Override
	protected void initLayout() {

		btnBack = _getView(R.id.acty_head_btn_back);
		btnBack.setOnClickListener(this);
		btnOK = _getView(R.id.acty_head_btn_release);
		btnOK.setOnClickListener(this);

		ivPortrait = (ImageView) _getView(R.id.acty_edit_personaldata_iv_portrait);
		ImageLoader.getInstance().displayImage(
				RestClient.BASE_URL + myself.getAvatar(), ivPortrait);
		ivPortrait.setOnClickListener(this);

		rbtnBoy = (RadioButton) _getView(R.id.acty_edit_personaldata_rg_sex_boy);
		rbtnGril = (RadioButton) _getView(R.id.acty_edit_personaldata_rg_sex_gril);

		if ("m".equalsIgnoreCase(myself.getProfile().getGender())) {
			rbtnBoy.setChecked(true);
		}
		if ("f".equalsIgnoreCase(myself.getProfile().getGender())) {
			rbtnGril.setChecked(true);
		}

		etName = (EditText) _getView(R.id.acty_edit_personaldata_ev_name);
		etName.setText(myself.getProfile().getName() == null ? "" : myself
				.getProfile().getName());

		etAge = (EditText) _getView(R.id.acty_edit_personaldata_ev_age);
		etAge.setText(myself.getProfile().getAge() + "");

		etGrade = (EditText) _getView(R.id.acty_edit_personaldata_ev_grade);
		etGrade.setText(myself.getProfile().getGrade() + "");

		etMajor = (EditText) _getView(R.id.acty_edit_personaldata_ev_major);
		etMajor.setText(myself.getProfile().getMajor() == null ? "" : myself
				.getProfile().getMajor());

		etSummary = (EditText) _getView(R.id.acty_edit_personaldata_ev_summary);
		etSummary.setText(myself.getProfile().getSummary() == null ? ""
				: myself.getProfile().getSummary());

		etUniversity = (EditText) _getView(R.id.acty_edit_personaldata_ev_university);
		etUniversity.setText(myself.getProfile().getUniversity() == null ? ""
				: myself.getProfile().getUniversity());
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.acty_head_btn_back:
			finish();
			break;

		case R.id.acty_head_btn_release:
			updateProfile();
			break;

		case R.id.acty_edit_personaldata_iv_portrait:
			final CharSequence[] items = { "相册", "拍照" };
			AlertDialog dlg = new AlertDialog.Builder(EditPersonalData.this)
					.setTitle("更新头像")
					.setItems(items, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
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

	private void updateProfile() {

		String name = etName.getText().toString();

		String gender = null;
		if (rbtnBoy.isChecked()) {
			gender = "m";
		}
		if (rbtnGril.isChecked()) {
			gender = "f";
		}

		int age = Integer.parseInt(etAge.getText().toString());
		int grade = Integer.parseInt(etGrade.getText().toString());
		String university = etUniversity.getText().toString();
		String major = etMajor.getText().toString();
		String summary = etSummary.getText().toString();

		api.updateProfile(myself.getId(), name, gender, age, grade, university,
				major, summary, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] data, Throwable err) {
						toast("更新失败");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] data) {
						String json = new String(data);// jsonarray
						System.out.println(json);
						if (JsonUtils.isOK(json)) {
							toast("更新完成");
							updateSPUser();
						} else {
							toast("更新失败");
						}
					}

					@Override
					public void onFinish() {
						dialog.cancel();
					}
				});
		dialog.show();
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
					portraitBitmap=ImageUtils.getBitmapByPath( FilePath.getImageFilePath()
										+ "cache_space_back.jpg");
				} else if (requestCode == 1) {
					Bundle extras = data.getExtras();
					portraitBitmap = (Bitmap) extras.get("data");
				}
			} catch (Exception e) {

			}

			api.updatePortrait(
					BitmapUtils.getBitmapInputStream(portraitBitmap),
					new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] data, Throwable err) {
							toast("网络异常 错误码:" + statusCode);
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] data) {
							String json = new String(data);
							if (JsonUtils.isOK(json)) {
								updateSPUser();
								ivPortrait.setImageBitmap(portraitBitmap);
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
	private void updateSPUser() {
		api.find(new RequestParams("id", myself.getId()),
				new AsyncHttpResponseHandler() {
					@Override
					public void onFinish() {
						dialog.cancel();
						finish();
					}

					public void onSuccess(int statusCode, Header[] headers,
							byte[] data) {
						String json = new String(data);
						if (JsonUtils.isOK(json)) {
							try {
								AppInfo.setUser(json, EditPersonalData.this);
							} catch (ClientProtocolException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

}
