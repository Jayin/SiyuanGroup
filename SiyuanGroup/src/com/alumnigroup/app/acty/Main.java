package com.alumnigroup.app.acty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.User;
import com.alumnigroup.utils.AndroidUtils;
import com.alumnigroup.utils.L;
import com.alumnigroup.widget.ADView;

/**
 * 主界面
 * 
 * @author Jayin Ton
 * @since 2013.12.11;
 * 
 */
public class Main extends BaseActivity implements OnClickListener {
	private ADView adview;
	private WebView webview;
	private LinearLayout content;
	private RelativeLayout parent_content;
	private int width = 0, height = 0;;

	private View btn_Message, btn_Setting, btn_OneSpace, btn_allMember,
			btn_communication, btn_activities,btn_group,acty_business;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_main);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		width = AndroidUtils.getScreenSize(getContext())[0];
		height = AndroidUtils.getScreenSize(getContext())[1];
	}

	@Override
	protected void initLayout() {
		adapteScreent();

		btn_OneSpace = _getView(R.id.frame_main_one_myspace);
		btn_OneSpace.setOnClickListener(this);

		btn_Setting = _getView(R.id.frame_main_one_setting);
		btn_Setting.setOnClickListener(this);

		btn_Message = _getView(R.id.frame_main_one_message);
		btn_Message.setOnClickListener(this);

		btn_allMember = _getView(R.id.frame_main_one_allmember);
		btn_allMember.setOnClickListener(this);

		btn_communication = _getView(R.id.frame_main_one_communication);
		btn_communication.setOnClickListener(this);
		
		btn_activities = _getView(R.id.frame_main_one_activities);
		btn_activities.setOnClickListener(this);
		
		btn_group = _getView(R.id.frame_main_one_group);
		btn_group.setOnClickListener(this);

		acty_business = _getView(R.id.frame_main_one_business);
		acty_business.setOnClickListener(this);

		initWebView();
	}
    //初始化广告栏
	private void initWebView() {
		webview = (WebView) _getView(R.id.acty_main_webview);
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		webSettings.setLoadsImagesAutomatically(true); // 自动加载图片
		webSettings.setBuiltInZoomControls(false);

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {
				// toast("onLoadResource-->"+url);
			}
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// toast("shouldOverrideUrlLoading-->"+url);
				 //自行处理点击事件！
				Intent intent = new Intent(Main.this, Browser.class);
				intent.putExtra("url", url);
				openActivity(intent);
				return true;
			}
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				view.setVisibility(View.INVISIBLE);
				L.i("failingUrl-->"+failingUrl);
				L.i("errorCode-->"+errorCode);
				
			}
		});
		
		webview.loadUrl(RestClient.BASE_URL+"/ad/index.html");
		
	}

	// 适配屏幕
	private void adapteScreent() {

		content = (LinearLayout) _getView(R.id.acty_main_content_one);
		parent_content = (RelativeLayout) _getView(R.id.acty_main_content);
		android.view.ViewGroup.LayoutParams params = content.getLayoutParams();
		params.height = width;
		RelativeLayout.LayoutParams rely_params = (RelativeLayout.LayoutParams) content
				.getLayoutParams();
		rely_params.addRule(RelativeLayout.CENTER_VERTICAL);

		content.setLayoutParams(params);
		content.setLayoutParams(rely_params);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.frame_main_one_myspace:
			openActivity(SpacePersonal.class);
			break;

		case R.id.frame_main_one_setting:
			openActivity(Setting.class);
			break;

		case R.id.frame_main_one_message:
			openActivity(Message.class);
			break;
		case R.id.frame_main_one_allmember:
			openActivity(Allmember.class);
			break;
		case R.id.frame_main_one_communication:
			openActivity(Communication.class);
			break;
		case R.id.frame_main_one_activities:
			openActivity(Activities.class);
			break;
		case R.id.frame_main_one_group:
			openActivity(Group.class);
			break;
		case R.id.frame_main_one_business:
			openActivity(Business.class);
			break;
		default:
			break;
		}
	}

}
