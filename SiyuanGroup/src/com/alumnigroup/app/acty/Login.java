package com.alumnigroup.app.acty;

import org.apache.http.Header;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.alumnigroup.api.UserAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.utils.L;
import com.alumnigroup.utils.StringUtils;
import com.alumnigroup.utils.WidgetUtils;
import com.alumnigroup.widget.MyProgressDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

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
			if (StringUtils.isEmpty(WidgetUtils.getText(et_log_username))
					|| StringUtils.isEmpty(WidgetUtils
							.getText(et_login_password))) {
				toast("输入不能为空 ");
				return;
			}

			api.login(WidgetUtils.getText(et_log_username),
					WidgetUtils.getText(et_login_password),
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
							// 登录成功。。。save login info here..
						  	dialog.dismiss();
							String json = new String(data);
							toast(json);
						}

					});
			break;
		case R.id.acty_login_btn_regist:
			flipper.showNext();
			break;
		case R.id.acty_register_btn_regist:
			if (StringUtils.isEmpty(WidgetUtils.getText(et_reg_confirm))
					|| StringUtils
							.isEmpty(WidgetUtils.getText(et_reg_password))
					|| StringUtils.isEmpty(WidgetUtils.getText(et_reg_email))
					|| StringUtils
							.isEmpty(WidgetUtils.getText(et_reg_username))
					|| StringUtils.isEmpty(WidgetUtils.getText(et_reg_name))) {
				toast("输入不能为空 ");
				return;
			}
			if (!WidgetUtils.getText(et_reg_password).equals(
					WidgetUtils.getTextTrim(et_reg_confirm))) {
				toast("输入密码不一致!");
				return;
			}
			if (!StringUtils.isEmail(WidgetUtils.getText(et_reg_email))) {
				toast("邮箱不符合格式！");
				return;
			}
			if (!StringUtils.isNickname(WidgetUtils.getText(et_reg_name))) {
				toast("姓名请填写中文或英文");
				return;
			}
			api.regist(WidgetUtils.getText(et_reg_username),
					WidgetUtils.getText(et_reg_password),
					WidgetUtils.getText(et_reg_name),
					WidgetUtils.getText(et_reg_email),
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
							//toast(json);
							toast("注册成功!");
							flipper.showPrevious();
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

	private void initFlipper() {
		flipper = (ViewFlipper) _getView(R.id.acty_login_flipper);
		flipper.setInAnimation(this, R.anim.push_up_in);
		flipper.setOutAnimation(this, R.anim.push_up_out);
	}

}
