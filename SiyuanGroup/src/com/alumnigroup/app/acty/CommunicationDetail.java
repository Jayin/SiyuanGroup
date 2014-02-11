package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.api.IssuesAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.api.StarAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Comment;
import com.alumnigroup.entity.Issue;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.JsonUtils;
import com.alumnigroup.widget.CommentView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 话题详情
 * 
 * @author Jayin Ton
 * 
 */
public class CommunicationDetail extends BaseActivity {
	private View btn_back, btn_share, btn_favourite, btn_comment, btn_space;
	private TextView tv_title, tv_body, tv_username, tv_time, tv_notify;
	private ImageView iv_avater;
	private Issue issue;
	// private PullAndLoadListView lv_comment;
	private CommentView lv_comment;
	private List<Comment> data_commet;
	private CommentAdapter adapter_commet;
	private IssuesAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_communicationdetail);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		issue = (Issue) getSerializableExtra("issue");
		api = new IssuesAPI();
		data_commet = new ArrayList<Comment>();
	}

	@Override
	protected void initLayout() {
		tv_notify = (TextView) _getView(R.id.acty_communicationdetail_tv_notify);
		tv_username = (TextView) _getView(R.id.acty_communicationdetail_tv_name);
		tv_time = (TextView) _getView(R.id.acty_communicationdetail_tv_posttime);
		tv_title = (TextView) _getView(R.id.item_lv_acty_comminication_title);
		tv_body = (TextView) _getView(R.id.item_lv_acty_comminication_body);
		iv_avater = (ImageView) _getView(R.id.acty_communicationdetail_iv_avater);
		// 头像
		ImageLoader.getInstance().displayImage(
				RestClient.BASE_URL + issue.getUser().getAvatar(), iv_avater);
		tv_username.setText(issue.getUser().getProfile().getName());
		tv_time.setText(CalendarUtils.getTimeFromat(issue.getPosttime(),
				CalendarUtils.TYPE_timeline));
		tv_title.setText(issue.getTitle());
		tv_body.setText(issue.getBody());

		btn_back = _getView(R.id.acty_head_btn_back);
		btn_space = _getView(R.id.acty_communicationdetail_btn_space);
		btn_share = _getView(R.id.acty_communicationdetail_footer_share);
		btn_comment = _getView(R.id.acty_communicationdetail_footer_comment);
		btn_favourite = _getView(R.id.acty_communicationdetail_footer_favourite);

		btn_back.setOnClickListener(this);
		btn_space.setOnClickListener(this);
		btn_share.setOnClickListener(this);
		btn_comment.setOnClickListener(this);
		btn_favourite.setOnClickListener(this);

		lv_comment = (CommentView) _getView(R.id.item_lv_acty_comminication_lv_comment);
		adapter_commet = new CommentAdapter(data_commet);
		lv_comment.setAdapter(adapter_commet);

		api.view(issue.getId(), new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				tv_notify.setText("评论加载中....");
				tv_notify.setVisibility(View.VISIBLE);
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				toast("网络异常 错误码:" + arg0);
				tv_notify.setVisibility(View.GONE);
			}

			@Override
			public void onSuccess(int statusCode, Header[] header, byte[] data) {
				tv_notify.setVisibility(View.GONE);
				boolean canRefresh = true;
				String json = new String(data);
				if (JsonUtils.isOK(json)) {
					List<Comment> newData_comment = Comment
							.create_by_jsonarray(json);
					if (newData_comment != null && newData_comment.size() > 0) {
						data_commet.addAll(newData_comment);
						lv_comment.setAdapter(new CommentAdapter(data_commet));
					} else {
						if (newData_comment == null) {
							tv_notify.setVisibility(View.VISIBLE);
							tv_notify.setText("网络异常,解析错误");
							toast("网络异常,解析错误");
						} else if (newData_comment.size() == 0) {
							toast("还没有人评论!");
							tv_notify.setText("还没有人评论!");
							tv_notify.setVisibility(View.VISIBLE);
						}
					}
				} else {
					toast("Error:" + JsonUtils.getErrorString(json));
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_communicationdetail_btn_space:
			// 去个人空间
			toast("to personal space");
			break;
		case R.id.acty_communicationdetail_footer_share:
			// 分享到圈子
			toast("share");
			break;
		case R.id.acty_communicationdetail_footer_comment:
			// 评论
			Intent comIntent = new Intent(this,CommunicationComment.class);
			comIntent.putExtra("issue", issue);
			openActivity(comIntent);
			break;
		case R.id.acty_communicationdetail_footer_favourite:
			// 收藏
			faviourite();
			break;
		default:
			break;
		}
	}
    //收藏的remark默认为期类型名
	private void faviourite() {
		 StarAPI starapi = new StarAPI();
		 starapi.star(StarAPI.Item_type_issue, issue.getId(), "issue", new JsonResponseHandler() {
			
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

	class CommentAdapter extends BaseAdapter {

		private List<Comment> data;

		public CommentAdapter(List<Comment> data) {
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
