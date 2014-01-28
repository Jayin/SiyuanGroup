package com.alumnigroup.app.acty;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alumnigroup.api.BusinessAPI;
import com.alumnigroup.api.StarAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Cooperation;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.DataPool;
import com.alumnigroup.widget.CommentView;

public class BusinessDetail extends BaseActivity {
	private Cooperation c;
	private View btn_back, btn_edit, btn_end, btn_delete, btn_space,
			btn_comment, btn_favourite;
	private View owner, common;
	private TextView tv_username, tv_projectname, tv_deadline, tv_description;
	private User user;
	private CommentView commentView;
	private BusinessAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_businessdetail);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {

		c = (Cooperation) getSerializableExtra("cooperation");
		debug(c.toString());
		user = (User) new DataPool(DataPool.SP_Name_User, this)
				.get(DataPool.SP_Key_User);
		api = new BusinessAPI();
	}

	@Override
	protected void initLayout() {
		tv_username = (TextView) _getView(R.id.tv_username);
		tv_projectname = (TextView) _getView(R.id.tv_projectname);
		tv_deadline = (TextView) _getView(R.id.tv_deadline);
		tv_description = (TextView) _getView(R.id.tv_description);

		tv_username.setText(c.getUser().getProfile().getName());
		tv_projectname.setText(c.getName());
		tv_deadline.setText(CalendarUtils.getTimeFromat(c.getDeadline(),
				CalendarUtils.TYPE_TWO));
		tv_description.setText(c.getDescription());

		btn_back = _getView(R.id.acty_head_btn_back);
		btn_edit = _getView(R.id.btn_edit);
		btn_end = _getView(R.id.btn_end);
		btn_delete = _getView(R.id.btn_delete);
		btn_space = _getView(R.id.btn_space);
		btn_comment = _getView(R.id.btn_comment);
		btn_favourite = _getView(R.id.btn_favourite);

		btn_back.setOnClickListener(this);
		btn_edit.setOnClickListener(this);
		btn_end.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
		btn_space.setOnClickListener(this);
		btn_comment.setOnClickListener(this);
		btn_favourite.setOnClickListener(this);

		commentView = (CommentView) _getView(R.id.commentlist);

		// api get comment list
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.btn_edit:

			break;
		case R.id.btn_end:

			break;
		case R.id.btn_delete:

			break;
		case R.id.btn_space:

			break;
		case R.id.btn_comment:

			break;
		case R.id.btn_favourite:
			favourite();
			break;
		default:
			break;
		}
	}
	//收藏的remark默认为期类型名
	private void favourite() {
		 StarAPI starapi = new StarAPI();
		 starapi.star(StarAPI.Item_type_business, c.getId(), "business", new JsonResponseHandler() {
			
			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				 toast("收藏成功");
			}
			
			@Override
			public void onFaild(int errorType, int errorCode) {
			   toast("收藏失败 错误码:"+errorCode);
			}
		});
	}

}
