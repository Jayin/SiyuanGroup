package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.api.BusinessAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.api.StarAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Cocomment;
import com.alumnigroup.entity.Cooperation;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.DataPool;
import com.alumnigroup.widget.CommentView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BusinessDetail extends BaseActivity {
	private Cooperation c;
	private View btn_back, btn_edit, btn_end, btn_space, btn_comment,
			btn_favourite;
	private View owner, common;
	private TextView tv_username, tv_projectname, tv_deadline, tv_description,
			tv_notify;
	private ImageView iv_avatar;
	private User user;
	private CommentView commentView;
	private BusinessAPI api;
	private List<Cocomment> data;
	private CocommentAdapter adatper;

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
		user = (User) new DataPool(DataPool.SP_Name_User, this)
				.get(DataPool.SP_Key_User);
		api = new BusinessAPI();

		data = new ArrayList<Cocomment>();
		adatper = new CocommentAdapter(data);
	}

	@Override
	protected void initLayout() {
		tv_username = (TextView) _getView(R.id.tv_username);
		tv_projectname = (TextView) _getView(R.id.tv_projectname);
		tv_deadline = (TextView) _getView(R.id.tv_deadline);
		tv_description = (TextView) _getView(R.id.tv_description);
		tv_notify = (TextView) _getView(R.id.tv_notify);

		tv_username.setText(c.getUser().getProfile().getName());
		tv_projectname.setText(c.getName());
		tv_deadline.setText(CalendarUtils.getTimeFromat(c.getDeadline(),
				CalendarUtils.TYPE_TWO));
		tv_description.setText(c.getDescription());

		btn_back = _getView(R.id.acty_head_btn_back);
		btn_edit = _getView(R.id.btn_edit);
		btn_end = _getView(R.id.btn_end);
		btn_space = _getView(R.id.btn_space);
		btn_comment = _getView(R.id.btn_comment);
		btn_favourite = _getView(R.id.btn_favourite);

		btn_back.setOnClickListener(this);
		btn_edit.setOnClickListener(this);
		btn_end.setOnClickListener(this);
		btn_space.setOnClickListener(this);
		btn_comment.setOnClickListener(this);
		btn_favourite.setOnClickListener(this);

		iv_avatar = (ImageView) _getView(R.id.iv_avatar);
		ImageLoader.getInstance().displayImage(
				RestClient.BASE_URL + c.getUser().getAvatar(), iv_avatar);
		commentView = (CommentView) _getView(R.id.commentlist);

		// api get comment list
		api.view(c.getId(), new JsonResponseHandler() {

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				List<Cocomment> newData = null;
				try {
					newData = Cocomment.create_by_jsonarray(obj.getJSONObject(
							"cooperation").toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (newData == null) {
					toast("网络异常 解析错误");
				} else if (newData.size() == 0) {
					tv_notify.setVisibility(View.VISIBLE);
					tv_notify.setText("还没有人询问");
				} else {
					data.addAll(newData);
					commentView.setAdapter(new CocommentAdapter(data));
				}
			}

			@Override
			public void onFaild(int errorType, int errorCode) {
				toast("网络异常 错误码:" + errorCode);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.btn_edit:
			toast("edit");
			break;
		case R.id.btn_end:
			toast("end");
			end();
			break;
		case R.id.btn_space:
			toast("to space");
			break;
		case R.id.btn_comment:
			toast("comment");
			break;
		case R.id.btn_favourite:
			favourite();
			break;
		default:
			break;
		}
	}

	private void end() {
		api.end(c.getId(), new JsonResponseHandler() {

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				toast("项目一结束");
			}

			@Override
			public void onFaild(int errorType, int errorCode) {
				toast("结束项目失败 错误码:" + errorCode);
			}
		});

	}

	// 收藏的remark默认为期类型名
	private void favourite() {
		StarAPI starapi = new StarAPI();
		starapi.star(StarAPI.Item_type_business, c.getId(), "business",
				new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						toast("收藏成功");
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("收藏失败 错误码:" + errorCode);
					}
				});
	}

	class CocommentAdapter extends BaseAdapter {

		private List<Cocomment> data;

		public CocommentAdapter(List<Cocomment> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder h = null;
			if (convertView == null) {
				h = new ViewHolder();
				convertView = getLayoutInflater().inflate(
						R.layout.item_lv_acty_communicationdetail, null);
				h.name = (TextView) convertView
						.findViewById(R.id.item_lv_acty_communicationdetai_name);
				h.positime = (TextView) convertView
						.findViewById(R.id.item_lv_acty_communicationdetai_posttime);
				h.body = (TextView) convertView
						.findViewById(R.id.item_lv_acty_communicationdetai_body);
				h.avater = (ImageView) convertView
						.findViewById(R.id.item_lv_acty_communicationdetai_avater);
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}
			h.name.setText(data.get(position).getUser().getProfile().getName());
			h.positime.setText(CalendarUtils.getTimeFromat(data.get(position)
					.getPosttime(), CalendarUtils.TYPE_timeline));
			h.body.setText(data.get(position).getBody());
			ImageLoader.getInstance().displayImage(
					RestClient.BASE_URL
							+ data.get(position).getUser().getAvatar(),
					h.avater);
			return convertView;
		}

		class ViewHolder {
			TextView name, positime, body;
			ImageView avater;
		}
	}

}
