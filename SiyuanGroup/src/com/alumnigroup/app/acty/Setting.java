package com.alumnigroup.app.acty;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.alumnigroup.app.App;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.utils.BitmapUtils;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.utils.ImageUtils;

/**
 * 设置
 * 
 * @author vector,Jayin
 * 
 */
public class Setting extends BaseActivity {
	public static final int Request_Pick_Image = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_setting);
		initData();
		initLayout();
	}

	protected void initData() {

	}

	protected void initLayout() {
		_getView(R.id.acty_setting_btn_changeBackgroud)
				.setOnClickListener(this);

		_getView(R.id.acty_head_btn_back).setOnClickListener(this);

		_getView(R.id.acty_setting_btn_about).setOnClickListener(this);

		_getView(R.id.acty_setting_btn_introduce).setOnClickListener(this);

		_getView(R.id.acty_setting_btn_quit).setOnClickListener(this);

		_getView(R.id.acty_setting_btn_tickling).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent intent = new Intent();
		switch (id) {

		case R.id.acty_head_btn_back:
			finish();
			break;
		case R.id.acty_setting_btn_about:
			intent.setClass(Setting.this, About.class);
			startActivity(intent);
			break;

		case R.id.acty_setting_btn_introduce:
			intent.setClass(Setting.this, Introduce.class);
			startActivity(intent);
			break;

		case R.id.acty_setting_btn_quit:
			toast("正在退出");
			((App) getApplication()).cleanUpInfo();
			toast("已经退出当前用户");
			openActivity(Login.class);
			finish();
			break;

		case R.id.acty_setting_btn_tickling:
			intent.setClass(Setting.this, Tickling.class);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == Request_Pick_Image) {
			Bitmap photo = null;
			Uri photoUri = data.getData();
				
			if (photoUri != null) {
				try {
					photo = BitmapUtils.getPicFromUri(photoUri, getContext());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				 
			}
			if (photo != null) {
			try {
					ImageUtils.saveImage(getContext(),"main_backgroud.jpg", photo, 75);
					sendBroadcast(new Intent(Constants.Action_Backgroud_switch));
					debug("IOException--ok");
				} catch (IOException e) {
					e.printStackTrace();
					debug("IOException--start");
				}

//				saveFile = new File(getContext().getFilesDir() + File.separator
//						+ "img" + File.separator + "main_backgroud.jpg");
//				FileOutputStream stream;
//				try {
//					debug("saving");
//					stream = new FileOutputStream(saveFile);
//					photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
//					sendBroadcast(new Intent(Constants.Action_Backgroud_switch));
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//					debug("FileNotFoundException");
//				}

			}
		}
	}
}
