package com.alumnigroup.app.acty;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ViewFlipper;

import com.alumnigroup.api.UserAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.ResponseHandler;
import com.alumnigroup.utils.DataPool;
import com.alumnigroup.utils.JsonUtils;
import com.alumnigroup.utils.L;
import com.alumnigroup.utils.StringUtils;
import com.alumnigroup.utils.EditTextUtils;
import com.alumnigroup.widget.MyProgressDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 登录注册页面
 * 
 * @author Jayin Ton
 * 
 */
public class Login extends BaseActivity {
	private ViewFlipper flipper;
	private EditText et_log_username, et_login_password;
	private EditText et_reg_username, et_reg_password, et_reg_confirm,
			et_reg_name, et_reg_email;
	private View btn_login, btn_regitst, btn_ok, btn_cancle;
	private UserAPI api;
	private MyProgressDialog dialog;
	private DataPool dp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_login);
		initData();
		initLayout();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_login_btn_login:
			if (StringUtils.isEmpty(EditTextUtils.getText(et_log_username))
					|| StringUtils.isEmpty(EditTextUtils
							.getText(et_login_password))) {
				toast("输入不能为空 ");
				return;
			}

			api.login(EditTextUtils.getText(et_log_username),
					EditTextUtils.getText(et_login_password),
					new AsyncHttpResponseHandler() {
						@Override
						public void onStart() {
							dialog.show();
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] data, Throwable arg3) {
							dialog.dismiss();
							toast("网络异常  错误码:" + statusCode);
							if (data != null)
								L.i(new String(data));
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] data) {
							dialog.dismiss();
							// 登录成功。。。save login info here..
							
							String json = new String(data);
							debug("onSuccess-->"+json);
							// toast(json);
							// succeed(json);
							if (JsonUtils.isOK(json)) {
								saveLoginInfo(json);
							} else {
								toast("Error:" + JsonUtils.getErrorString(json));
							}

						}

					});
			break;
		case R.id.acty_login_btn_regist:
			flipper.showNext();
			break;
		case R.id.acty_register_btn_regist:
			if (StringUtils.isEmpty(EditTextUtils.getText(et_reg_confirm))
					|| StringUtils
							.isEmpty(EditTextUtils.getText(et_reg_password))
					|| StringUtils.isEmpty(EditTextUtils.getText(et_reg_email))
					|| StringUtils
							.isEmpty(EditTextUtils.getText(et_reg_username))
					|| StringUtils.isEmpty(EditTextUtils.getText(et_reg_name))) {
				toast("输入不能为空 ");
				return;
			}
			if (!EditTextUtils.getText(et_reg_password).equals(
					EditTextUtils.getTextTrim(et_reg_confirm))) {
				toast("输入密码不一致!");
				return;
			}
			if (!StringUtils.isEmail(EditTextUtils.getText(et_reg_email))) {
				toast("邮箱不符合格式！");
				return;
			}
			if (!StringUtils.isNickname(EditTextUtils.getText(et_reg_name))) {
				toast("姓名请填写中文或英文");
				return;
			}
			api.regist(EditTextUtils.getText(et_reg_username),
					EditTextUtils.getText(et_reg_password),
					EditTextUtils.getText(et_reg_name),
					EditTextUtils.getText(et_reg_email),
					new AsyncHttpResponseHandler() {
						@Override
						public void onStart() {
							dialog.show();
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] data, Throwable err) {
							dialog.dismiss();
							toast("网络异常  错误码:" + statusCode);
							if (data != null)
								L.i(new String(data));
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] data) {
							// regist successfully!
							dialog.dismiss();
							String json = new String(data);
							if (JsonUtils.isOK(json)) {
								toast("注册成功!");
								flipper.showPrevious();
							} else {
								toast("Error:" + JsonUtils.getErrorString(json));
							}
						}
					});
			break;
		case R.id.acty_register_btn_cancle:
			flipper.showPrevious();
			break;
		default:
			break;
		}
	}

	@Override
	protected void initData() {
		api = new UserAPI();
		dp = new DataPool(DataPool.SP_Name_User,this);
	}

	@Override
	protected void initLayout() {
		initFlipper();
		et_log_username = (EditText) _getView(R.id.acty_login_et_username);
		et_login_password = (EditText) _getView(R.id.acty_login_et_password);
		et_reg_username = (EditText) _getView(R.id.acty_register_et_username);
		et_reg_password = (EditText) _getView(R.id.acty_register_et_password);
		et_reg_confirm = (EditText) _getView(R.id.acty_register_et_comfirmpassword);
		et_reg_name = (EditText) _getView(R.id.acty_register_et_name);
		et_reg_email = (EditText) _getView(R.id.acty_register_et_email);

		btn_login = _getView(R.id.acty_login_btn_login);
		btn_regitst = _getView(R.id.acty_login_btn_regist);
		btn_ok = _getView(R.id.acty_register_btn_regist);
		btn_cancle = _getView(R.id.acty_register_btn_cancle);

		btn_login.setOnClickListener(this);
		btn_regitst.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		btn_cancle.setOnClickListener(this);

		dialog = new MyProgressDialog(getContext());

	}

	private void saveLoginInfo(String json) {
		int userid = JsonUtils.getInt(json, "id");
		// 正确解析
		if (userid > 0) {
			api.find(new RequestParams("id", userid), new ResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] data) {
					List<User> userList = User.create_by_jsonarray(new String(
							data));
					if (userList == null || userList.size() == 0) {
						toast("登录失败 没有改用户信息");
					} else {
						if (dp.put(DataPool.SP_Key_User, userList.get(0))) {
							Intent intent = new Intent(Login.this, Main.class);
							intent.putExtra("myself", userList.get(0));
							openActivity(intent);
							closeActivity();
						}
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] header,
						byte[] data, Throwable err) {
					toast("网络异常 错误码:"+statusCode);
				}
			});
		}

	}

	/**
	 * 请求成功后
	 */
	private void succeed(String json) {
		System.out.println(json);
		final Intent intent = new Intent(Login.this, Main.class);
		int id = -1;
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(json);
			id = JsonUtils.getInt(jsonObject, "id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		api.find(new RequestParams("id", id), new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] data, Throwable arg3) {
				dialog.dismiss();
				toast("网络异常  错误码:" + statusCode);
				if (data != null)
					L.i(new String(data));
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] data) {
				String json = new String(data);
				User myself = new Gson().fromJson(getJsonEmement(json),
						User.class);
				intent.putExtra("myself", myself);
				startActivity(intent);
				dialog.dismiss();
				finish();
			}

		});
	}

	private void initFlipper() {
		flipper = (ViewFlipper) _getView(R.id.acty_login_flipper);
		flipper.setInAnimation(this, R.anim.push_up_in);
		flipper.setOutAnimation(this, R.anim.push_up_out);
	}

	private String getJsonEmement(String json) {
		String jsonJsonEmement = "";
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(json);
			jsonJsonEmement = jsonObject.getJSONArray("users").getJSONObject(0)
					.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonJsonEmement;
	}

}
