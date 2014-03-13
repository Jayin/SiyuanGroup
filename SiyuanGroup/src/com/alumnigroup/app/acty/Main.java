package com.alumnigroup.app.acty;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.CoreService;
import com.alumnigroup.app.MessageCache;
import com.alumnigroup.app.R;
import com.alumnigroup.utils.AndroidUtils;
import com.alumnigroup.utils.Constants;

/**
 * 主界面
 * 
 * @author Jayin Ton
 * @since 2013.12.11;
 * 
 */
public class Main extends BaseActivity implements OnClickListener {
	private WebView webview;
	private LinearLayout content;
	private int width = 0, height = 0;
	private BroadcastReceiver mRecevier;
	private TextView tv_unreadCount;
	private boolean isError = false;
	private ImageView iv_backgroud;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_main);
		initData();
		initLayout();
		checkVerison();
		startPolling();
		openReceiver();
	}

	private void startPolling() {
		 Intent service =new Intent(getContext(), CoreService.class);
		 service.setAction(Constants.Action_Start_Receive_UnreadCount);
		 startService(service);
	}


	// 接收到
	private void openReceiver() {
		mRecevier = new MainRecevier();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action_Receive_UnreadCount);// 有未读消息
		filter.addAction(Constants.Action_Backgroud_switch);// 背景图切换
		filter.addAction(Constants.Action_User_Login_Out);//用户登出
		registerReceiver(mRecevier, filter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mRecevier != null) {
			unregisterReceiver(mRecevier);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (MessageCache.getUnreadCount(getContext()) == 0) {
			tv_unreadCount.setVisibility(View.INVISIBLE);
		} else {
			tv_unreadCount.setText(MessageCache.getUnreadCount(getContext())
					+ "");
			tv_unreadCount.setVisibility(View.VISIBLE);
		}

	}

	private void checkVerison() {
		Intent service = new Intent(getContext(), CoreService.class);
		service.setAction(Constants.Action_checkVersion);
		startService(service);
	}

	@Override
	protected void initData() {
		width = AndroidUtils.getScreenSize(getContext())[0];
		height = AndroidUtils.getScreenSize(getContext())[1];
	}

	@Override
	protected void initLayout() {
		adapteScreent();

		_getView(R.id.frame_main_one_myspace).setOnClickListener(this);

		_getView(R.id.frame_main_one_setting).setOnClickListener(this);

		_getView(R.id.frame_main_one_message).setOnClickListener(this);

		_getView(R.id.frame_main_one_allmember).setOnClickListener(this);

		_getView(R.id.frame_main_one_communication).setOnClickListener(this);

		_getView(R.id.frame_main_one_activities).setOnClickListener(this);

		_getView(R.id.frame_main_one_group).setOnClickListener(this);

		_getView(R.id.frame_main_one_business).setOnClickListener(this);

		_getView(R.id.frame_main_one_allactivity).setOnClickListener(this);
		tv_unreadCount = (TextView) _getView(R.id.tv_unreadcount);
		iv_backgroud = (ImageView) _getView(R.id.iv_main_bg);
		if (AppInfo.getBackgroudPath(getContext()) != null) {
			iv_backgroud.setImageBitmap(BitmapFactory.decodeFile(AppInfo
					.getBackgroudPath(getContext())));
		}
		initWebView();
	}

	// 初始化广告栏
	@SuppressLint("SetJavaScriptEnabled")
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
				// 自行处理点击事件！
				Intent intent = new Intent(Main.this, Browser.class);
				intent.putExtra("url", url);
				openActivity(intent);
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				webview.setVisibility(View.INVISIBLE);
				isError = true;
			}
		});
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100 && !isError) {
					webview.setVisibility(View.VISIBLE);
				} else {
					webview.setVisibility(View.INVISIBLE);
				}
			}
		});
		webview.loadUrl(RestClient.BASE_URL + "/ad/index.html");
	}

	// 适配屏幕
	private void adapteScreent() {
		content = (LinearLayout) _getView(R.id.acty_main_content_one);
		android.view.ViewGroup.LayoutParams params = content.getLayoutParams();
		params.height = width;
		content.setLayoutParams(params);
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
			openActivity(MessageCenter.class);
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
		case R.id.frame_main_one_allactivity:
			openActivity(Alldynamic.class);
			break;
		default:
			break;
		}
	}

	class MainRecevier extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constants.Action_Receive_UnreadCount)) {// got unread
																					 
				if (MessageCache.getUnreadCount(getContext()) > 0) {
					tv_unreadCount.setText(MessageCache
							.getUnreadCount(getContext()) + "");
					tv_unreadCount.setVisibility(View.VISIBLE);
				}
			} else if (intent.getAction().equals(
					Constants.Action_Backgroud_switch)) {
				if (AppInfo.getBackgroudPath(getContext()) != null) {
					iv_backgroud
							.setImageBitmap(BitmapFactory.decodeFile(AppInfo
									.getBackgroudPath(getContext())));
				}
			}else if(intent.getAction().equals(Constants.Action_User_Login_Out)){
				closeActivity();
			}
		}
	}
}
