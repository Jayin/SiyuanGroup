package com.alumnigroup.imple;

import android.graphics.Bitmap;
import android.view.View;

import com.alumnigroup.app.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
/**
 * ImageLoadingListener 的实现类
 * 
 * @author Jayin Ton
 *
 */
public class ImageLoadingListenerImple implements ImageLoadingListener {
	@Override
	public void onLoadingStarted(String imageUri, View view) {
	}

	@Override
	public void onLoadingFailed(String imageUri, View view,
			FailReason failReason) {
		view.setBackgroundResource(R.drawable.ic_image_load_faild);
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {
		view.setBackgroundResource(R.drawable.ic_image_load_normal);
	}

}
