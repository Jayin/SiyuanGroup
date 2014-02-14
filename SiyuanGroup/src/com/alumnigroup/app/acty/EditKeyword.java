package com.alumnigroup.app.acty;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;

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
import com.alumnigroup.entity.User;
import com.alumnigroup.utils.JsonUtils;
import com.alumnigroup.utils.L;
import com.alumnigroup.widget.OutoLinefeedLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 修改关键字
 * 
 * @author vector
 * 
 */
public class EditKeyword extends BaseActivity {

	/**
	 * 控件
	 */
	private OutoLinefeedLayout lyContent, lyHot;
	private EditText etAdd;
	private View btnAdd,btnRelease;

	/**
	 * 关键字
	 */
	private String keyworkContent;
	private int backgroupcolor = 0;

	/**
	 * 增加的关键字
	 */
	private ArrayList<Keyword> alMyKeyword;

	/**
	 * 热门关键字
	 */
	private ArrayList<Keyword> alHotKeyword;
	
	private UserAPI api;
	private User myself;
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_edit_keywork);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		alMyKeyword = new ArrayList<EditKeyword.Keyword>();
		api = new UserAPI();
		myself = AppInfo.getUser(EditKeyword.this);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(
				EditKeyword.this);
		builder.setTitle("更新中···");
		dialog = builder.create();
	}

	@Override
	protected void initLayout() {
		lyContent = (OutoLinefeedLayout) _getView(R.id.acty_edit_keywork_ly_content);
		lyContent.setMargin(10);

		lyHot = (OutoLinefeedLayout) _getView(R.id.acty_edit_keywork_ly_hot);
		lyHot.setMargin(10);

		etAdd = (EditText) _getView(R.id.acty_edit_keywork_et_add);
		btnAdd = _getView(R.id.acty_edit_keywork_btn_add);
		btnAdd.setOnClickListener(this);
		
		btnRelease = _getView(R.id.acty_head_btn_release);
		btnRelease.setOnClickListener(this);
	}

	/**
	 * 刷新关键字
	 * 
	 */
	private void updateKeyWord(ArrayList<Keyword> alKeyword,
			OutoLinefeedLayout ly) {
		ly.removeAllViews();
		for (Keyword keyword : alKeyword) {
			ly.addView(keyword.getView());
		}
	}

	/**
	 * 增加一个keyword，增加进list 里面
	 */
	@SuppressLint("NewApi")
	private void addKeyWord(String content, int background,
			ArrayList<Keyword> alKeyword) {

		Keyword keyword = new Keyword();
		keyword.setContent(keyworkContent);
		/**
		 * 装上TextView
		 */
		LayoutInflater inflater = null;
		inflater = LayoutInflater.from(this);
		View convertView = null;

		if (background == 0) {
			convertView = inflater.inflate(
					R.layout.item_outolinefeedlayout_acty_edit_keywork_yellow,
					null);
		} else {
			convertView = inflater.inflate(
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
		alKeyword.add(keyword);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		switch (id) {

		/**
		 * 增加按钮点下
		 */
		case R.id.acty_edit_keywork_btn_add:
			keyworkContent = etAdd.getText().toString();
			if ("".equals(keyworkContent)) {
				toast("请先输入关键字");
				return;
			}
			addKeyWord(keyworkContent, backgroupcolor, alMyKeyword);
			backgroupcolor = (backgroupcolor + 1) % 2;
			updateKeyWord(alMyKeyword, lyContent);
			updateKeyWord(alMyKeyword, lyContent);
			etAdd.setText("");
			break;
		case R.id.acty_head_btn_release:
			dialog.show();
			updateKeyWord();
			break;

		default:
			break;
		}
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
			updateKeyWord(alMyKeyword, lyContent);
		}

	}
	
	private void updateKeyWord(){
		String tags = "";
		for (Keyword keyword : alMyKeyword) {
			tags = keyword.getContent()+",";
		}
		api.updateTag(tags, new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] data, Throwable err) {
				if (data != null)
					L.i(new String(data));
				if (err != null)
					L.i(err.toString());
				toast("更新失败");
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] data) {
				String json = new String(data);// jsonarray
				if (JsonUtils.isOK(json)) {
					toast("更新完成");
					updateSPUser();
					finish();
				} else {
					toast("更新失败"+json);
				}
			}

			@Override
			public void onFinish() {
				dialog.cancel();
			}
		});
		
	}

/**
 * 更新user 数据
 */
private void updateSPUser() {
	api.find(new RequestParams("id", myself.getId()),
			new AsyncHttpResponseHandler() {
				@Override
				public void onFinish() {
					dialog.cancel();
				}

				public void onSuccess(int statusCode, Header[] headers,
						byte[] data) {
					String json = new String(data);
					if (JsonUtils.isOK(json)) {
						try {
							AppInfo.setUser(json, EditKeyword.this);
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
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
