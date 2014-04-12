package com.alumnigroup.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alumnigroup.app.App;
import com.alumnigroup.app.CoreService;
import com.alumnigroup.app.R;
import com.alumnigroup.app.acty.About;
import com.alumnigroup.app.acty.Feedback;
import com.alumnigroup.app.acty.Introduce;
import com.alumnigroup.app.acty.Login;
import com.alumnigroup.app.acty.SpacePersonal;
import com.alumnigroup.utils.Constants;

/**
 * 首页-我的页面
 * 
 * @author Jayin Ton
 * 
 */
public class Mine extends BaseFragment {
	public static final int Request_Pick_Image = 1;
	private static final int status_start = 0;
	private static final int status_faild = 1;
	private static final int status_finish = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_mine, container, false);
		v.findViewById(R.id.acty_setting_btn_changeBackgroud)
				.setOnClickListener(this);

		v.findViewById(R.id.acty_setting_btn_about).setOnClickListener(this);

		v.findViewById(R.id.acty_setting_btn_introduce)
				.setOnClickListener(this);

		v.findViewById(R.id.acty_setting_btn_quit).setOnClickListener(this);

		v.findViewById(R.id.acty_setting_btn_tickling).setOnClickListener(this);
		v.findViewById(R.id.btn_spacePersonal).setOnClickListener(this);
		return v;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btn_spacePersonal:
			 getActivity().startActivity(new Intent(getActivity(), SpacePersonal.class));
			break;
		case R.id.acty_setting_btn_about:
			intent.setClass(getActivity(), About.class);
			startActivity(intent);
			break;

		case R.id.acty_setting_btn_introduce:
			intent.setClass(getActivity(), Introduce.class);
			startActivity(intent);
			break;

		case R.id.acty_setting_btn_quit:
			quitApp();
			break;

		case R.id.acty_setting_btn_tickling:
			intent.setClass(getActivity(), Feedback.class);
			startActivity(intent);
			break;
		case R.id.acty_setting_btn_changeBackgroud:
			// pic a photo
			intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent, Request_Pick_Image);
			break;
		default:
			break;
		}
	}

	private void quitApp() {

		final Handler h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case status_start:
					toast("正在退出...");
					break;
				case status_faild:
					break;
				case status_finish:
					Intent service = new Intent(getActivity(),
							CoreService.class);
					service.setAction(Constants.Action_Stop_Receive_UnreadCount);
					getActivity().startService(service);
					toast("已退出");
					getActivity().startActivity(
							new Intent(getActivity(), Login.class));
					getActivity().finish();
					break;
				default:
					break;
				}
			}
		};

		new Thread(new Runnable() {

			@Override
			public void run() {
				h.sendEmptyMessage(status_start);
				((App) getActivity().getApplication()).cleanUpInfo();
				getActivity().sendBroadcast(
						new Intent(Constants.Action_User_Login_Out));// 发广播
				h.sendEmptyMessage(status_finish);
			}
		}).start();
	}
}
