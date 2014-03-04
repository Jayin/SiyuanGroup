package com.alumnigroup.app.acty;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageDisplay extends BaseActivity {
	private String url;
	private ImageView iv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_imagedisplay);
		initData();
		initLayout();
		
	}

	@Override
	protected void initData() {
		url  = getStringExtra("url");
	}

	@Override
	protected void initLayout() {
		iv = (ImageView)_getView(R.id.iv_display);
		_getView(R.id.acty_head_btn_back).setOnClickListener(this);
		
		ImageLoader.getInstance().displayImage(url, iv);
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
