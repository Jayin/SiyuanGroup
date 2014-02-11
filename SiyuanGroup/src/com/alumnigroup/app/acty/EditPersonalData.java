package com.alumnigroup.app.acty;

import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alumnigroup.api.RestClient;
import com.alumnigroup.api.UserAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.User;
import com.alumnigroup.utils.DataPool;
import com.alumnigroup.utils.L;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EditPersonalData extends BaseActivity {

	private View btnBack, btnOK;

	private ImageView ivPortrait;
	private RadioGroup rgSex;
	private RadioButton rbtnBoy, rbtnGril;
	private EditText etName, etAge, etGrade, etUniversity, etMajor, etSummary;
	private User myself;

	private UserAPI api = new UserAPI();
	private DataPool dp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_edit_personaldata);
		dp = new DataPool(this);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		myself = (User) getIntent().getSerializableExtra("myself");
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

		rgSex = (RadioGroup) _getView(R.id.acty_edit_personaldata_rg_sex);
		rbtnBoy = (RadioButton) _getView(R.id.acty_edit_personaldata_rg_sex_boy);
		rbtnGril = (RadioButton) _getView(R.id.acty_edit_personaldata_rg_sex_gril);

		if ("m".equalsIgnoreCase(myself.getProfile().getGender())) {
			rbtnBoy.setChecked(true);
		}
		if ("f".equalsIgnoreCase(myself.getProfile().getGender())) {
			rbtnGril.setChecked(true);
		}

		etName = (EditText) _getView(R.id.acty_edit_personaldata_ev_name);
		etName.setText(myself.getProfile().getNickname());

		etAge = (EditText) _getView(R.id.acty_edit_personaldata_ev_age);
		etAge.setText(myself.getProfile().getAge() + "");

		etGrade = (EditText) _getView(R.id.acty_edit_personaldata_ev_grade);
		etGrade.setText(myself.getProfile().getGrade() + "");

		etMajor = (EditText) _getView(R.id.acty_edit_personaldata_ev_major);
		etMajor.setText(myself.getProfile().getMajor() + "");

		etSummary = (EditText) _getView(R.id.acty_edit_personaldata_ev_summary);
		etSummary.setText(myself.getProfile().getSummary() + "");

		etUniversity = (EditText) _getView(R.id.acty_edit_personaldata_ev_university);
		etUniversity.setText(myself.getProfile().getUniversity() + "");
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.acty_head_btn_back:
			finish();
			break;

		case R.id.acty_head_btn_release:
			update();
			break;

		default:
			break;
		}

	}

	private void update() {

		String name = etName.getText().toString();
		String gender = etGrade.getText().toString();
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
						toast("网络异常 错误码:" + statusCode);
						if (data != null)
							L.i(new String(data));
						if (err != null)
							L.i(err.toString());
						toast("更新失败");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] data) {
						updateIntent();
					}
				});
		toast("更新中···");
	}

	private void updateIntent() {
		api.find(new RequestParams("id", myself.getId()),
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] data) {
						List<User> userList = User
								.create_by_jsonarray(new String(data));
						if (userList == null || userList.size() == 0) {
							toast("登录失败 没有改用户信息");
						} else {
							if (dp.put(DataPool.SP_Key_User, userList.get(0))) {
								Intent intent = getIntent();
								intent.setClass(EditPersonalData.this, SpacePersonal.class);
								intent.putExtra("myself", userList.get(0));
								toast(userList.get(0).getProfile().getAge()+"");
								startActivity(intent);
								finish();
							}
						}
						
					}

					@Override
					public void onFailure(int statusCode, Header[] header,
							byte[] data, Throwable err) {
						toast("网络异常 错误码:" + statusCode);
					}
				});
	}
}
