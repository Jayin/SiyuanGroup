package com.alumnigroup.app.acty;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

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

import com.alumnigroup.api.ActivityAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.app.SyncData;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.MActivity;
import com.alumnigroup.entity.MGroup;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.BitmapUtils;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.EditTextUtils;
import com.alumnigroup.utils.FilePath;
import com.alumnigroup.utils.ImageUtils;
import com.alumnigroup.widget.LoadingDialog;
import com.alumnigroup.widget.TimePickDialog;
import com.alumnigroup.widget.TimePickDialog.OnSiglePickFinishedListener;
import com.custom.view.FlowLayout;

/**
 * 发起活动页面
 * 
 * @author Jayin Ton
 * 
 */
public class ActivitiesPublish extends BaseActivity {
	private int RequestCode_Pick_image = 1;
	private View btn_back, btn_create, btn_starttime, btn_deadline;
	private EditText et_name, et_description, et_maxNum, et_money, et_duration,
			et_site;
	private TextView tv_startTime, tv_deadline;

	private MGroup group;
	private ActivityAPI api;
	private TimePickDialog dialog;
	private int sy_pickwhat = 1; // 1 pick: tv_startTime ; 2 tv_deadline
	private long starttime = 0, regdeadline = 0;
	private MActivity acty;
	private FlowLayout flowLayout;
	private HashMap<View, Uri> bitmaps;
	private LoadingDialog loadingdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_activitiespublish);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		boolean hasParams = false;
		group = (MGroup) getSerializableExtra("group");
		api = new ActivityAPI();
		if (group != null) {
			hasParams = true;
		}
		if (getSerializableExtra("activity") != null) {
			acty = (MActivity) getSerializableExtra("activity");
			hasParams = true;
			starttime = acty.getStarttime();
			regdeadline= acty.getRegdeadline();
		}
		if (!hasParams) {
			closeActivity();
			toast("无参数");
		}
		
		bitmaps = new HashMap<View, Uri>();
	}

	@Override
	protected void initLayout() {
		loadingdialog  =new LoadingDialog(getContext());
		loadingdialog.setCancelable(false);
		
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_create = _getView(R.id.acty_head_btn_create);
		btn_starttime = _getView(R.id.btn_starttime);
		btn_deadline = _getView(R.id.btn_deadline);

		et_name = (EditText) _getView(R.id.et_name);
		et_description = (EditText) _getView(R.id.et_description);
		et_maxNum = (EditText) _getView(R.id.et_maxNum);
		et_money = (EditText) _getView(R.id.et_money);
		tv_startTime = (TextView) _getView(R.id.et_startTime);
		et_duration = (EditText) _getView(R.id.et_duration);
		tv_deadline = (TextView) _getView(R.id.et_deadline);
		et_site = (EditText) _getView(R.id.et_site);
		dialog = new TimePickDialog(this);
		dialog.setOnSiglePickFinishedListener(new OnSiglePickFinishedListener() {

			@Override
			public void onFinish(long selecttime) {
				if (sy_pickwhat == 1) {
					starttime = selecttime;
					tv_startTime.setText(CalendarUtils.getTimeFromat(
							selecttime, CalendarUtils.TYPE_TWO));
				} else if (sy_pickwhat == 2) {
					regdeadline = selecttime;
					tv_deadline.setText(CalendarUtils.getTimeFromat(selecttime,
							CalendarUtils.TYPE_TWO));
				}

			}
		});
		btn_back.setOnClickListener(this);
		btn_create.setOnClickListener(this);
		btn_starttime.setOnClickListener(this);
		btn_deadline.setOnClickListener(this);

		if (acty != null) {
			starttime = acty.getStarttime();
			regdeadline = acty.getRegdeadline();
			et_name.setText(acty.getName());
			et_description.setText(acty.getContent());
			et_maxNum.setText(acty.getMaxnum() + "");
			et_money.setText(acty.getMoney());
			tv_startTime.setText(CalendarUtils.getTimeFromat(
					acty.getStarttime(), CalendarUtils.TYPE_TWO));
			et_duration.setText(acty.getDuration() + "");
			tv_deadline.setText(CalendarUtils.getTimeFromat(
					acty.getRegdeadline(), CalendarUtils.TYPE_TWO));
			et_site.setText(acty.getSite());
		}
		
		flowLayout = (FlowLayout) _getView(R.id.flowlayout_container);
		_getView(R.id.btn_add_pic).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_head_btn_create:
			// create..
			if (check()) {

				int groupid = group == null ? 0 : group.getId();
				int maxnum = Integer.parseInt(EditTextUtils
						.getTextTrim(et_maxNum));
				int duration = Integer.parseInt(EditTextUtils
						.getTextTrim(et_duration));
				int statusid = 1;// 1接受报名、2截止报名、3活动结束、4活动取消
				long money = Integer.parseInt(EditTextUtils
						.getTextTrim(et_money));
				String name = EditTextUtils.getTextTrim(et_name);
				String content = EditTextUtils.getTextTrim(et_description);
				String site = EditTextUtils.getTextTrim(et_site);
				if (acty == null) {
					create(groupid, maxnum, duration, statusid, money, name,
							content, site);
				}

				else {
					update(acty.getId(), maxnum, duration, statusid, money,
							name, content, site);
				}

			}
			break;
		case R.id.btn_starttime:
			// select time
			sy_pickwhat = 1;
			dialog.show();
			break;
		case R.id.btn_deadline:
			// select time
			sy_pickwhat = 2;
			dialog.show();
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

	private void update(final int actyid, int maxnum, int duration, int statusid,
			long money, String name, String content, String site) {
		api.update(actyid, maxnum, starttime,duration, regdeadline, statusid, money,
				name, content, site, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						toast("已更新");
						closeActivity();
						SyncData.SyncActivityInfo(getContext(), actyid, null);
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("更新失败 " + ErrorCode.errorList.get(errorCode));
					}
					
					@Override
					public void onStart() {
						loadingdialog.setText("更新中...");
						loadingdialog.show();
					}
					
					@Override
					public void onFinish() {
						loadingdialog.dismiss();
					}
				});

	}

	private void create(int groupid, int maxnum, int duration, int statusid,
			long money, String name, String content, String site) {
		
		final File[] pics = new File[3];
		if (bitmaps.size() > 0) {
			int count = 0;
			for (View v : bitmaps.keySet()) {
				try {
					String fileName = FilePath.getImageFilePath()
							+ "activity_pic" + count + ".jpg";
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
		api.creatAcivity(groupid, maxnum, starttime, duration,

		regdeadline, statusid, money, name, content, site,pics[0], pics[1], pics[2],
				new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						toast("活动发起成功");
						closeActivity();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						//toast("网络异常 错误码:" + errorCode);
						toast("活动发起失败 "+ErrorCode.errorList.get(errorCode));
					}
					
					@Override
					public void onStart() {
						loadingdialog.setText("创建中...");
						loadingdialog.show();
					}
					
					@Override
					public void onFinish() {
						loadingdialog.dismiss();
					}
				});

	}

	private boolean check() {
		if (EditTextUtils.isEmpty(et_name)) {
			toast("活动名称不能为空");
			return false;
		}
		if (EditTextUtils.isEmpty(et_maxNum)) {
			toast("活动人数不能为空");
			return false;
		}
		if (EditTextUtils.isEmpty(et_money)) {
			toast("活动费用不能为空");
			return false;
		}
		if (EditTextUtils.isEmpty(et_duration)) {
			toast("活动时长不能为空");
			return false;
		}
		if (EditTextUtils.isEmpty(et_site)) {
			toast("活动地点不能为空");
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
					flowLayout.removeView(container);
					bitmaps.remove(container);
				}
			});
			try {
				Bitmap bitmap = BitmapUtils.getPicFromUri(photoUri,
						getContext());
				iv.setImageBitmap(ImageUtils.zoomBitmap(bitmap, 100, 100));
				bitmap = null;
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
