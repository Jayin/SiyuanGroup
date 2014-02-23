package com.alumnigroup.app.acty;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;

public class Introduce extends BaseActivity {
	private WebView webview;

	// private ViewPager vgImages4Introduce;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_introduce);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initLayout() {
		webview =(WebView) _getView(R.id.webview_about);
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setLoadsImagesAutomatically(true); // 自动加载图片
		webSettings.setBuiltInZoomControls(true);
		webSettings.setUseWideViewPort(true);
		webview.setInitialScale(50);
		webview.loadUrl(RestClient.BASE_URL+"/setting/about.html");
		
		_getView(R.id.acty_head_btn_back).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;

		default:
			break;
		}
	}
}
