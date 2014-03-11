package com.alumnigroup.app.acty;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.api.FeedbackAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.MFeedback;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.widget.FeedbackDialog;
import com.alumnigroup.widget.XListView;
import com.alumnigroup.widget.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 意见反馈
 * 
 * @author Jayin Ton
 * 
 */
public class Feedback extends BaseActivity {
	private XListView lv;
	private ArrayList<MFeedback> data;
	private FeedbackAdapter adapter;
	private FeedbackAPI api;
	private int page = 0;
    private FeedbackDialog dialog;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_feedback);
		initData();
		initLayout();
		initXListView();
	}

	private void initXListView() {
		lv.setAdapter(adapter);
		lv.setPullLoadEnable(true);
		lv.setPullRefreshEnable(true);
		lv.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				api.getFeedbackList(1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						ArrayList<MFeedback> newData = MFeedback
								.create_by_jsonarray(obj.toString());
						if (newData == null) {
							toast("网络异常,解析错误");
						} else if (newData.size() == 0) {
							toast("还没有人反馈");
							lv.setPullLoadEnable(false);
						} else {
							page = 1;
							data.clear();
							data.addAll(newData);
							adapter.notifyDataSetChanged();
							if(newData.size()<10){
								lv.setPullLoadEnable(false);
							}else{
								lv.setPullLoadEnable(true);
							}
							
						}
						lv.stopRefresh();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast(ErrorCode.errorList.get(errorCode));
						lv.stopRefresh();
					}
				});

			}

			@Override
			public void onLoadMore() {
				if (page == 0) {
					lv.stopLoadMore();
					lv.startRefresh();
					return;
				}
				api.getFeedbackList(page + 1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						ArrayList<MFeedback> newData = MFeedback
								.create_by_jsonarray(obj.toString());
						if (newData == null) {
							toast("网络异常,解析错误");
						} else if (newData.size() == 0) {
							toast("没有更多");
							lv.setPullLoadEnable(false);
						} else {
							page++;
							data.addAll(newData);
							adapter.notifyDataSetChanged();
							if(newData.size()<10){
								lv.setPullLoadEnable(false);
							}else{
								lv.setPullLoadEnable(true);
							}
						}
						lv.stopLoadMore();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast(ErrorCode.errorList.get(errorCode));
						lv.stopLoadMore();
					}
				});
			}
		});
		lv.startRefresh();
	}

	@Override
	protected void initData() {
		data = new ArrayList<MFeedback>();
		adapter = new FeedbackAdapter();
		api = new FeedbackAPI();
	}

	@Override
	protected void initLayout() {
		dialog = new FeedbackDialog(getContext());
		lv = (XListView) _getView(R.id.lv_feedback);
		_getView(R.id.acty_head_btn_back).setOnClickListener(this);
		_getView(R.id.btn_feedback).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.btn_feedback:
			dialog.show();
			break;
		default:
			break;
		}
	}

	class FeedbackAdapter extends BaseAdapter {

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
						R.layout.item_lv_acty_feedback, null);
				h.avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
				h.username = (TextView) convertView
						.findViewById(R.id.tv_username);
				h.posttime = (TextView) convertView
						.findViewById(R.id.tv_posttime);
				h.title = (TextView) convertView.findViewById(R.id.tv_title);
				h.body = (TextView) convertView.findViewById(R.id.tv_body);
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}
			MFeedback mf = data.get(position);
			if (mf.getUser().getAvatar() != null) {
				ImageLoader.getInstance().displayImage(
						RestClient.BASE_URL + mf.getUser().getAvatar(),
						h.avatar);
			} else {
				ImageLoader.getInstance().displayImage(
						"drawable://" + R.drawable.ic_image_load_normal,
						h.avatar);
			}
			h.username.setText(mf.getUser().getProfile().getName());
			h.posttime.setText(CalendarUtils.getTimeFromat(mf.getPosttime(),
					CalendarUtils.TYPE_timeline));
			h.title.setText(mf.getType());
			h.body.setText(mf.getBody());
			return convertView;
		}

		class ViewHolder {
			TextView username, posttime, title, body;
			ImageView avatar;
		}

	}
}
