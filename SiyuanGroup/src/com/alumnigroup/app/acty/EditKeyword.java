package com.alumnigroup.app.acty;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.alumnigroup.api.UserAPI;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.JsonUtils;
import com.custom.view.FlowLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 修改关键字
 * 
 * @author vector;restructure by Jayin Ton
 * 
 */
public class EditKeyword extends BaseActivity {

	// /** 控件 */
	private FlowLayout flowlayout;
	private EditText etAdd;
	private View btnAdd, btnRelease, parent;

	/** 关键字 */
	private int backgroupcolor = 0;

	/** 增加的关键字 */
	private ArrayList<Keyword> alMyKeyword;

	private UserAPI api;
	private User myself;
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_edit_keyword);
		initData();
		initLayout();
		initKeyWord();
	}

	@Override
	protected void initData() {
		alMyKeyword = new ArrayList<EditKeyword.Keyword>();
		api = new UserAPI();
		myself = AppInfo.getUser(EditKeyword.this);

		AlertDialog.Builder builder = new AlertDialog.Builder(EditKeyword.this);
		builder.setMessage("更新中···");
		dialog = builder.create();
	}

	@Override
	protected void initLayout() {

		parent = _getView(R.id.acty_edit_keywork_ly_edit);

		flowlayout = (FlowLayout) _getView(R.id.flowlayout_keyword);
		// flowlayout.setMargin(10);

		etAdd = (EditText) _getView(R.id.acty_edit_keywork_et_add);
		btnAdd = _getView(R.id.acty_edit_keywork_btn_add);
		btnAdd.setOnClickListener(this);

		btnRelease = _getView(R.id.acty_head_btn_release);
		btnRelease.setOnClickListener(this);

		_getView(R.id.acty_head_btn_back).setOnClickListener(this);
	}

	private void initKeyWord() {
		if (myself.getProfile().getTag() != null
				&& !myself.getProfile().getTag().equals("")) {
			String tags[] = myself.getProfile().getTag().split(",");
			for (String tag : tags) {
				String content = new String(tag);
				addKeyWord(content);
				backgroupcolor = backgroupcolor == 0 ? 1 : 0;
			}
			updateKeyWord();
		}
	}

	/**
	 * 刷新关键字
	 * 
	 */
	private void updateKeyWord() {
		flowlayout.removeAllViews();
		for (Keyword keyword : alMyKeyword) {
			flowlayout.addView(keyword.getView());
		}
	}

	/**
	 * 增加一个keyword，增加进list 里面
	 */
	@SuppressLint("NewApi")
	private void addKeyWord(String content) {
		Keyword keyword = new Keyword();
		keyword.setContent(content);
		/**
		 * 装上TextView
		 */
		View convertView = null;

		if (backgroupcolor == 0) {
			convertView = LayoutInflater.from(this).inflate(
					R.layout.item_outolinefeedlayout_acty_edit_keywork_yellow,
					null);
		} else {
			convertView = LayoutInflater.from(this).inflate(
					R.layout.item_outolinefeedlayout_acty_edit_keywork_green,
					null);

		}
		TextView tvKeyWord = (TextView) convertView
				.findViewById(R.id.item_keyword_tv_value);
		tvKeyWord.setText(content);

		View btnDelete = convertView
				.findViewById(R.id.item_outolinefeedlayout_acty_edit_keywork_btn_delete);
		btnDelete.setOnClickListener(new OnDeleteButton(keyword));
		/**
		 * 加入新的关键字
		 */
		keyword.setView(convertView);
		alMyKeyword.add(keyword);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/**
		 * 增加按钮点下
		 */
		case R.id.acty_edit_keywork_btn_add:
			add();
			parent.invalidate();
			break;
		case R.id.acty_head_btn_release:
			dialog.show();
			sendKeyWord();
			break;
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		default:
			break;
		}
	}

	private void add() {
		String keyworkContent = etAdd.getText().toString();
		if ("".equals(keyworkContent)) {
			toast("请先输入关键字");
			return;
		}
		addKeyWord(keyworkContent);
		backgroupcolor = backgroupcolor == 0 ? 1 : 0;
		updateKeyWord();
		etAdd.setText("");
	}

	/**
	 * 删除按钮监听器，按下就从list 删除数据，之后刷新界面
	 * 
	 * @author vector
	 * 
	 */
	private class OnDeleteButton implements OnClickListener {

		private Keyword keyword;

		public OnDeleteButton(Keyword keyword) {
			this.keyword = keyword;
		}

		@Override
		public void onClick(View v) {
			alMyKeyword.remove(keyword);
			updateKeyWord();
		}

	}

	private void sendKeyWord() {
		StringBuilder tags = new StringBuilder();
		for (Keyword keyword : alMyKeyword) {
			// tags += keyword.getContent() + ",";
			tags.append(keyword.getContent()).append(",");
		}
		if (tags.length() > 0)
			tags.deleteCharAt(tags.length() - 1);
		api.updateTag(tags.toString(), new JsonResponseHandler() {

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				toast("更新完成");
				updateSPUser();
			}

			@Override
			public void onFaild(int errorType, int errorCode) {
				toast("更新失败 " + ErrorCode.errorList.get(errorCode));
				dialog.cancel();
			}
		});
	}

	/**
	 * 更新user 数据
	 */
	private void updateSPUser() {
		api.getMyInfo(new JsonResponseHandler() {
			
			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				User user;
				try {
					user = User.create_by_json(obj.getJSONObject("user").toString());
					AppInfo.setUser(getContext(),user);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFaild(int errorType, int errorCode) {
				 
			}
			
			@Override
			public void onFinish() {
				dialog.cancel();
				closeActivity();
			}
		});
	}

	/**
	 * 封装一个关键字
	 * 
	 * @author vector
	 * 
	 */
	private class Keyword {
		/**
		 * 关键字的内容
		 */
		private String content;
		/**
		 * 关键字的View
		 */
		private View view;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public View getView() {
			return view;
		}

		public void setView(View view) {
			this.view = view;
		}

	}

}
