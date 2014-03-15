package com.alumnigroup.app.acty;

import java.io.File;
import java.io.FileNotFoundException;

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

import com.alumnigroup.api.ActivityShareAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.Issue;
import com.alumnigroup.entity.MActivity;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.BitmapUtils;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.utils.EditTextUtils;
import com.alumnigroup.utils.FilePath;
import com.alumnigroup.utils.FileUtils;
import com.alumnigroup.utils.ImageUtils;
import com.alumnigroup.widget.LoadingDialog;
import com.custom.view.FlowLayout;

/**
 * 活动分享发布
 * 
 * @author Jayin Ton
 * 
 */
public class ActivitiesSharePublish extends BaseActivity {
	private int RequestCode_Pick_image = 1;
	private EditText et_title, et_content;
	private ActivityShareAPI api;
	private Issue mIssue;
	private MActivity activity;
	private FlowLayout flowLayout;
	private boolean withPic = false;
	private LoadingDialog loadingdialog;
	private View tv_pic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_activitiessharepublish);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		mIssue = (Issue) getSerializableExtra("issue");
		activity = (MActivity) getSerializableExtra("activity");
		api = new ActivityShareAPI();
	}

	@Override
	protected void initLayout() {
		loadingdialog  =new LoadingDialog(getContext());
		loadingdialog.setCancelable(false);
		
		_getView(R.id.acty_head_btn_back).setOnClickListener(this);
		_getView(R.id.acty_head_btn_post).setOnClickListener(this);
		_getView(R.id.btn_add_pic).setOnClickListener(this);
		et_title = (EditText) _getView(R.id.et_title);
		et_content = (EditText) _getView(R.id.et_content);

		if (mIssue != null) {
			et_title.setText(mIssue.getTitle());
			et_content.setText(mIssue.getBody());
		}

		flowLayout = (FlowLayout) _getView(R.id.flowlayout_container);
		tv_pic = _getView(R.id.tv_pic);
		if(mIssue!=null){
			flowLayout.setVisibility(View.INVISIBLE);
			tv_pic.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_head_btn_post:
			String title = EditTextUtils.getTextTrim(et_title);
			String body = EditTextUtils.getTextTrim(et_content);
			if (title == null || title.equals("")) {
				toast("标题不能为空!");
			}
			if (mIssue == null) { // 发布
				post(title, body);
			} else { // 更新
				updateShare(title, body);
			}
			break;
		case R.id.btn_add_pic:
			if (flowLayout.getChildCount() >= 2) {
				toast("目前仅支持发一张图片");
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

	private void post(final String title, final String body) {
		File pic1 = withPic ? new File(FilePath.getImageFilePath()
				+ "issue_pic1.jpg") : null;
		api.postShare(activity.getId(), title, body, pic1, null, null, new JsonResponseHandler() {

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				toast("发布成功");
				closeActivity();
			}

			@Override
			public void onFaild(int errorType, int errorCode) {
				toast("发布失败 " + ErrorCode.errorList.get(errorCode));
			}
			
			@Override
			public void onStart() {
				loadingdialog.setText("发布中...");
				loadingdialog.show();
			}
			
			@Override
			public void onFinish() {
				loadingdialog.dismiss();
			}
		});

	}

	private void updateShare(final String title, final String body) {
		api.updateShare(activity.getId(), mIssue.getId(), title, body,
				new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						toast("更新成功");
						closeActivity();
						// 发送修改广播
						Intent intent = new Intent(
								Constants.Action_ActivityShare_Edit);
						Issue issue = new Issue();
						issue.setBody(body);
						issue.setId(mIssue.getId());
						issue.setNumComments(mIssue.getNumComments());
						issue.setPosttime(System.currentTimeMillis());
						issue.setTitle(title);
						issue.setUser(mIssue.getUser());
						intent.putExtra("issue", issue);
						sendBroadcast(intent);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RequestCode_Pick_image && resultCode == RESULT_OK) {
			Uri photoUri = data.getData();
			View container = getLayoutInflater().inflate(
					R.layout.item_flowlayout_image, null);
			ImageView iv = (ImageView) container.findViewById(R.id.iv_image);
			iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					FileUtils.deleteFile(FilePath.getImageFilePath()
							+ "pic1.jpg");// 删除
					flowLayout.removeViewAt(1);
					withPic = false;
				}
			});
			try {
				Bitmap bitmap = BitmapUtils.getPicFromUri(photoUri,
						getContext());
				iv.setImageBitmap(ImageUtils.zoomBitmap(bitmap, 100, 100));
				ImageUtils.saveImageToSD(FilePath.getImageFilePath()
						+ "issue_pic1.jpg",
						ImageUtils.createImageThumbnail(bitmap, 800), 80);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			flowLayout.addView(container);
			withPic = true;
		}
	}
}
