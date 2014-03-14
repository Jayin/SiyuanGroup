package com.alumnigroup.app.acty;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ViewFlipper;

import com.alumnigroup.api.UserAPI;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.JsonUtils;
import com.alumnigroup.utils.StringUtils;
import com.alumnigroup.utils.EditTextUtils;
import com.alumnigroup.widget.MyProgressDialog;

/**
 * 登录注册页面
 * 
 * @author Jayin Ton
 * 
 */
public class Login extends BaseActivity {
	private ViewFlipper flipper;
	private EditText et_login_username, et_login_password;
	private EditText et_reg_username, et_reg_password, et_reg_confirm,
			et_reg_name, et_reg_email, et_reg_university, et_reg_major,
			et_reg_summary, et_reg_grade;
	private View btn_login, btn_regitst, btn_ok, btn_cancle;
	private UserAPI api;
	private MyProgressDialog dialog;

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
			if (StringUtils.isEmpty(EditTextUtils.getText(et_login_username))
					|| StringUtils.isEmpty(EditTextUtils
							.getText(et_login_password))) {
				toast("输入不能为空 ");
				return;
			}

			api.login(EditTextUtils.getText(et_login_username),
					EditTextUtils.getText(et_login_password),
					new JsonResponseHandler() {

						@Override
						public void onStart() {
							dialog.show();
						}

						@Override
						public void onOK(Header[] headers, JSONObject obj) {
							dialog.dismiss();
							saveLoginInfo(obj.toString());
						}

						@Override
						public void onFaild(int errorType, int errorCode) {
							dialog.dismiss();
							toast(ErrorCode.errorList.get(errorCode));
						}
					});
			break;
		case R.id.acty_login_btn_regist:
			flipper.showNext();
			break;
		case R.id.acty_register_btn_regist:
			if (StringUtils.isEmpty(EditTextUtils.getText(et_reg_confirm))
					|| StringUtils.isEmpty(EditTextUtils
							.getText(et_reg_password))
					|| StringUtils.isEmpty(EditTextUtils.getText(et_reg_email))
					|| StringUtils.isEmpty(EditTextUtils
							.getText(et_reg_username))
					|| StringUtils.isEmpty(EditTextUtils
							.getText(et_reg_university))
					|| StringUtils.isEmpty(EditTextUtils.getText(et_reg_major))
					|| StringUtils.isEmpty(EditTextUtils
							.getText(et_reg_summary))
					|| StringUtils.isEmpty(EditTextUtils.getText(et_reg_grade))) {
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
			String username = EditTextUtils.getText(et_reg_username);
			String password = EditTextUtils.getText(et_reg_password);
			String name = EditTextUtils.getText(et_reg_name);
			String email = EditTextUtils.getText(et_reg_email);
			int grade = Integer.parseInt(EditTextUtils.getText(et_reg_grade));
			String university = EditTextUtils.getText(et_reg_university);
			String major = EditTextUtils.getText(et_reg_major);
			String summary = EditTextUtils.getText(et_reg_summary);
			api.regist(username, password, name, email, "m", 0, grade,
					university, major, summary, new JsonResponseHandler() {
						@Override
						public void onStart() {
							dialog.show();
						}

						@Override
						public void onOK(Header[] headers, JSONObject obj) {
							dialog.dismiss();
							toast("注册成功!");
							flipper.showPrevious();

						}

						@Override
						public void onFaild(int errorType, int errorCode) {
							toast("注册失败" + ErrorCode.errorList.get(errorCode));
							dialog.dismiss();
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
	}

	@Override
	protected void initLayout() {
		initFlipper();
		et_login_username = (EditText) _getView(R.id.acty_login_et_username);
		et_login_password = (EditText) _getView(R.id.acty_login_et_password);
		et_reg_username = (EditText) _getView(R.id.acty_register_et_username);
		et_reg_password = (EditText) _getView(R.id.acty_register_et_password);
		et_reg_confirm = (EditText) _getView(R.id.acty_register_et_comfirmpassword);
		et_reg_name = (EditText) _getView(R.id.acty_register_et_name);
		et_reg_email = (EditText) _getView(R.id.acty_register_et_email);

		et_reg_university = (EditText) _getView(R.id.acty_register_et_university);
		et_reg_major = (EditText) _getView(R.id.acty_register_et_major);
		et_reg_summary = (EditText) _getView(R.id.acty_register_et_summary);
		et_reg_grade = (EditText) _getView(R.id.acty_register_et_grade);

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
		int id = JsonUtils.getInt(json, "id");
		UserAPI api = new UserAPI();
		api.view(id, new JsonResponseHandler() {

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				try {
					User user = User.create_by_json(obj.getJSONObject("user")
							.toString());
					AppInfo.setUser(getContext(), user);
					// 保存用户账号密码
					AppInfo.setUsername(getContext(), et_login_username
							.getText().toString());
					AppInfo.setUserPSW(getContext(), et_login_password
							.getText().toString());

					openActivity(Main.class);
					closeActivity();
				} catch (JSONException e) {
					e.printStackTrace();
					toast("网络异常 解析错误");
				}
			}

			@Override
			public void onFaild(int errorType, int errorCode) {
				toast(ErrorCode.errorList.get(errorCode));
			}
		});
	}

	private void initFlipper() {
		flipper = (ViewFlipper) _getView(R.id.acty_login_flipper);
		flipper.setInAnimation(this, R.anim.push_up_in);
		flipper.setOutAnimation(this, R.anim.push_up_out);
	}
}
