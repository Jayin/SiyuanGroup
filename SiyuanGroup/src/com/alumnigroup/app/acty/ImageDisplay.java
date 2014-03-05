package com.alumnigroup.app.acty;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import android.os.Bundle;
import android.view.View;

import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 查看图片详情，支持放缩
 * 
 * @author Jayin Ton
 * 
 */
public class ImageDisplay extends BaseActivity {
	private String url;
	private PhotoView iv;
	PhotoViewAttacher mAttacher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_imagedisplay);
		initData();
		initLayout();

	}

	@Override
	protected void initData() {
		url = getStringExtra("url");
	}

	@Override
	protected void initLayout() {
		iv = (PhotoView) _getView(R.id.iv_display);
		_getView(R.id.acty_head_btn_back).setOnClickListener(this);

		ImageLoader.getInstance().displayImage(url, iv);

		mAttacher = new PhotoViewAttacher(iv);

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
