package com.alumnigroup.app.acty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.api.BusinessAPI;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Cooperation;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.BitmapUtils;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.EditTextUtils;
import com.alumnigroup.utils.FilePath;
import com.alumnigroup.utils.FileUtils;
import com.alumnigroup.utils.ImageUtils;
import com.alumnigroup.utils.StringUtils;
import com.alumnigroup.widget.TimePickDialog;
import com.alumnigroup.widget.TimePickDialog.OnSiglePickFinishedListener;
import com.custom.view.FlowLayout;

public class BusinessPublish extends BaseActivity {
	private int RequestCode_Pick_image = 1;
	private View btn_back, btn_create, btn_deadline, btn_isprivate;
	private EditText et_name, et_description, et_company;
	private TextView tv_deadline, tv_isPrivate;
	private TimePickDialog dialog;
	private long regdeadline;
	private BusinessAPI api;
	private Cooperation c;
	private FlowLayout flowLayout;
	private HashMap<View, Uri> bitmaps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_businesspublish);
		initData();
		initLayout();

	}

	@Override
	protected void initData() {
		api = new BusinessAPI();
		c = (Cooperation) getSerializableExtra("cooperation");
		bitmaps = new HashMap<View, Uri>();

	}

	@Override
	protected void initLayout() {
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_create = _getView(R.id.acty_head_btn_create);
		btn_deadline = _getView(R.id.btn_deadline);
		btn_isprivate = _getView(R.id.isprivate);

		flowLayout = (FlowLayout) _getView(R.id.flowlayout_container);
		_getView(R.id.btn_add_pic).setOnClickListener(this);

		et_name = (EditText) _getView(R.id.et_name);
		et_description = (EditText) _getView(R.id.et_description);
		et_company = (EditText) _getView(R.id.et_company);

		tv_deadline = (TextView) _getView(R.id.tv_deadline);
		tv_isPrivate = (TextView) _getView(R.id.tv_isprivate);
	
		btn_back.setOnClickListener(this);
		btn_create.setOnClickListener(this);
		btn_deadline.setOnClickListener(this);
		btn_isprivate.setOnClickListener(this);

		dialog = new TimePickDialog(getContext());
		dialog.setOnSiglePickFinishedListener(new OnSiglePickFinishedListener() {

			@Override
			public void onFinish(long selecttime) {
				regdeadline = selecttime;
				tv_deadline.setText(CalendarUtils.getTimeFromat(selecttime,
						CalendarUtils.TYPE_TWO));
			}
		});

		if (c != null) {
			regdeadline = c.getRegdeadline();
			tv_deadline.setText(CalendarUtils.getTimeFromat(c.getRegdeadline(),
					CalendarUtils.TYPE_TWO));
			tv_isPrivate.setText(c.getIsprivate() == 1 ? "是" : "否");
			et_name.setText(c.getName());
			et_description.setText(c.getDescription());
			et_company.setText(c.getCompany());
			// 编辑状态下不能换图片?
			if (c.getUser().getId() == AppInfo.getUser(getContext()).getId()) {
				_getView(R.id.tv_pic).setVisibility(View.GONE);
				flowLayout.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_head_btn_create:
			if (check()) {
				String name = EditTextUtils.getTextTrim(et_name);
				String description = EditTextUtils.getTextTrim(et_description);
				String company = EditTextUtils.getTextTrim(et_company);
				int statusid = 1;
				int isprivate = tv_isPrivate.getText().equals("是") ? 1 : 0;
				if (c == null) {
					create(name, description, company, statusid, isprivate);
				} else {
					update(name, description, company, statusid, isprivate);
				}
			}
			break;
		case R.id.btn_deadline:
			dialog.show();
			break;
		case R.id.isprivate:
			if (tv_isPrivate.getText().equals("是")) {
				tv_isPrivate.setText("否");
			} else {
				tv_isPrivate.setText("是");
			}
			break;
		case R.id.btn_add_pic:
			if (flowLayout.getChildCount() >= 4) {
				toast("目前最多能上传发3张图片");
				return;
			}
			// to pick a pic & add...
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			// openActivity(intent);
			startActivityForResult(intent, RequestCode_Pick_image);
			break;
		default:
			break;
		}
	}

	private void update(String name, String description, String company,
			int statusid, int isprivate) {
		api.update(c.getId(), name, description, company, regdeadline,
				statusid, isprivate, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						toast("已更新");
						closeActivity();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("更新失败 " + ErrorCode.errorList.get(errorCode));
					}
				});
	}

	private void create(String name, String description, String company,
			int statusid, int isprivate) {

		// File pic1 = withPic ? new File(FilePath.getImageFilePath()
		// + "cooperation_pic1.jpg") : null;
		final File[] pics = new File[3];
		if (bitmaps.size() > 0) {
			int count = 0;
			for (View v : bitmaps.keySet()) {
				try {
					String fileName = FilePath.getImageFilePath()
							+ "cooperation_pic" + count + ".jpg";
					ImageUtils.saveImageToSD(fileName, ImageUtils
							.createImageThumbnail(BitmapUtils.getPicFromUri(
									bitmaps.get(v), getContext()), 800), 80);
					pics[count] = new File(fileName);
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
//		if (pics[0] != null)
//			debug("pics[0]!=null-->" + pics[0].getAbsolutePath());
//		if (pics[1] != null)
//			debug("pics[1]!=null-->" + pics[1].getAbsolutePath());
//		if (pics[2] != null)
//			debug("pics[2]!=null-->" + pics[2].getAbsolutePath());
		api.create(name, description, company, regdeadline, statusid,
				isprivate, pics[0], pics[1], pics[2],
				new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						toast("发布成功");
						closeActivity();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("发布失败 " + ErrorCode.errorList.get(errorCode));
					}
				});

	}

	private boolean check() {
		if (EditTextUtils.isEmpty(et_name)) {
			toast("项目标题不能为空");
			return false;
		}
		if (EditTextUtils.isEmpty(et_description)) {
			toast("项目简介不能为空");
			return false;
		}
		if (EditTextUtils.isEmpty(et_company)) {
			toast("公司组织不能为空");
			return false;
		}
		if (StringUtils.isEmpty(tv_deadline.getText().toString())) {
			toast("截止日期不能为空");
			return false;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RequestCode_Pick_image && resultCode == RESULT_OK) {
			Uri photoUri = data.getData();
			final View container = getLayoutInflater().inflate(
					R.layout.item_flowlayout_image, null);
			ImageView iv = (ImageView) container.findViewById(R.id.iv_image);
			iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// FileUtils.deleteFile(FilePath.getImageFilePath()
					// + "cooperation_pic1.jpg");// 删除
					flowLayout.removeView(container);
					bitmaps.remove(container);
				}
			});
			try {
				Bitmap bitmap = BitmapUtils.getPicFromUri(photoUri,
						getContext());
				iv.setImageBitmap(ImageUtils.zoomBitmap(bitmap, 100, 100));
				bitmaps.put(container, photoUri);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			flowLayout.addView(container);
		}
	}

}
