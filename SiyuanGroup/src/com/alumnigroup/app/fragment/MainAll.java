package com.alumnigroup.app.fragment;

import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.MessageCache;
import com.alumnigroup.app.R;
import com.alumnigroup.app.acty.Activities;
import com.alumnigroup.app.acty.Alldynamic;
import com.alumnigroup.app.acty.Allmember;
import com.alumnigroup.app.acty.Browser;
import com.alumnigroup.app.acty.Business;
import com.alumnigroup.app.acty.Communication;
import com.alumnigroup.app.acty.Group;
import com.alumnigroup.app.acty.MessageCenter;
import com.alumnigroup.app.acty.Setting;
import com.alumnigroup.app.acty.SpacePersonal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class MainAll extends Fragment implements View.OnClickListener {
	OnMainAllFragmentUpdate mCallback = null;
	WebView webview;
	TextView tv_unreadCount;
	boolean isError = false;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnMainAllFragmentUpdate) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnMainAllFragmentUpdate");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main_all, container, false);
		v.findViewById(R.id.frame_main_one_myspace).setOnClickListener(this);

		v.findViewById(R.id.frame_main_one_setting).setOnClickListener(this);

		v.findViewById(R.id.frame_main_one_message).setOnClickListener(this);

		v.findViewById(R.id.frame_main_one_allmember).setOnClickListener(this);

		v.findViewById(R.id.frame_main_one_communication).setOnClickListener(
				this);

		v.findViewById(R.id.frame_main_one_activities).setOnClickListener(this);

		v.findViewById(R.id.frame_main_one_group).setOnClickListener(this);

		v.findViewById(R.id.frame_main_one_business).setOnClickListener(this);

		v.findViewById(R.id.frame_main_one_allactivity)
				.setOnClickListener(this);
		webview = (WebView) v.findViewById(R.id.acty_main_webview);
		initWebView();
		tv_unreadCount = (TextView) v.findViewById(R.id.tv_unreadcount);
		return v;
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
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
				Intent intent = new Intent(getActivity(), Browser.class);
				intent.putExtra("url", url);
				startActivity(intent);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.frame_main_one_myspace:
			startActivity(new Intent(getActivity(), SpacePersonal.class));
			break;
		case R.id.frame_main_one_setting:
			startActivity(new Intent(getActivity(), Setting.class));
			break;
		case R.id.frame_main_one_message:
			startActivity(new Intent(getActivity(), MessageCenter.class));
			break;
		case R.id.frame_main_one_allmember:
			startActivity(new Intent(getActivity(), Allmember.class));
			break;
		case R.id.frame_main_one_communication:
			startActivity(new Intent(getActivity(), Communication.class));
			break;
		case R.id.frame_main_one_activities:
			startActivity(new Intent(getActivity(), Activities.class));
			break;
		case R.id.frame_main_one_group:
			startActivity(new Intent(getActivity(), Group.class));
			break;
		case R.id.frame_main_one_business:
			startActivity(new Intent(getActivity(), Business.class));
			break;
		case R.id.frame_main_one_allactivity:
			startActivity(new Intent(getActivity(), Alldynamic.class));
			break;
		default:
			break;
		}

	}

	public void updateUnreadCount(int unreadCount) {
		if (unreadCount <= 0) {
			tv_unreadCount.setVisibility(View.INVISIBLE);
		} else {
			tv_unreadCount.setText(unreadCount + "");
			tv_unreadCount.setVisibility(View.VISIBLE);
		}
	}

	public interface OnMainAllFragmentUpdate {
		public void onUnreadChange(int unreadCount);
	}
}
