package com.alumnigroup.app.acty;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

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
		mAttacher = new PhotoViewAttacher(iv);
		ImageLoader.getInstance().displayImage(url, iv,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				mAttacher.update();
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				
			}
		});

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
