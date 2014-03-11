package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alumnigroup.api.MessageAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.MMessage;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.widget.XListView;
import com.alumnigroup.widget.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 消息详情
 * 
 * @author Jayin Ton
 * 
 */
public class MessageDetail extends BaseActivity {
	private MMessage message;
	// private ArrayList<MMessage> data;
	private XListView lv;
	private ChatAdapter adapter;
	private EditText et_body;
	private MessageAPI api;
	// private ArrayList<Integer> status;
	private final int Finished = 0;
	private final int Sending = 1;
	private final int Faild = 2;
	private int page = 0;
	private LinkedList<MMessage> msgs;
	private LinkedList<Integer> status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_messagedetail);
		initData();
		initLayout();
		initCotroller();
	}

	private void initCotroller() {
		lv.setAdapter(adapter);
		lv.setPullLoadEnable(false);
		lv.setPullRefreshEnable(true);
		lv.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				api.getChatRecord(page + 1, message.getSenderid(),
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								int position = 0;
								ArrayList<MMessage> messages = MMessage
										.create_by_jsonarray(obj.toString());
								for (MMessage m : messages) {
									msgs.push(m);
									status.push(Finished);
								}
								page++;
								adapter.notifyDataSetChanged();
								lv.setSelection(position);
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

			}
		});
		lv.startRefresh();
	}

	@Override
	protected void initData() {
		message = getSerializableExtra("message");
		api = new MessageAPI();
		// data = new ArrayList<MMessage>();
		adapter = new ChatAdapter();
		// data.add(message);
		// status = new ArrayList<Integer>();
		// status.add(Finished);
		status = new LinkedList<Integer>();
		msgs = new LinkedList<MMessage>();
	}

	@Override
	protected void initLayout() {
		_getView(R.id.acty_head_btn_back).setOnClickListener(this);
		_getView(R.id.btn_send).setOnClickListener(this);
		et_body = (EditText) _getView(R.id.et_body);
		lv = (XListView) _getView(R.id.lv_content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.btn_send:
			if (et_body.getText().toString().equals("")) {
				toast("不能为空");
				return;
			}
			api.send(message.getSenderid(), "title", et_body.getText()
					.toString(), new JsonResponseHandler() {
				int position = 0;

				@Override
				public void onStart() {
					position = status.size();
					MMessage mm = new MMessage();
					// stop here
					// mm.setSender(AppInfo.getUser(getContext()));
					mm.setSenderid(AppInfo.getUser(getContext()).getId());
					mm.setSenderavatar(AppInfo.getUser(getContext())
							.getAvatar());
					mm.setSendername(AppInfo.getUser(getContext()).getProfile()
							.getName());
					mm.setBody(et_body.getText().toString());
					// data.add(mm);
					msgs.add(mm);
					status.add(Sending);
					adapter.notifyDataSetChanged();
					lv.setSelection(msgs.size() - 1);
					debug("onstart---pos-"+position);
				}

				@Override
				public void onOK(Header[] headers, JSONObject obj) {
					status.set(position, Finished);
					adapter.notifyDataSetInvalidated();
					lv.setSelection(msgs.size() - 1);
					et_body.setText("");
					debug("on ok---pos-"+position);
				}

				@Override
				public void onFaild(int errorType, int errorCode) {
					status.set(position, Finished);
					adapter.notifyDataSetInvalidated();
					lv.setSelection(msgs.size() - 1);
					et_body.setText("");
					debug("onFaild---pos-"+position);
				}
			});

			break;
		default:
			break;
		}
	}

	class ChatAdapter extends BaseAdapter {
		private final int LEFT = 0;
		private final int RIGHT = 1;

		private ChatAdapter() {
		}

		@Override
		public int getCount() {
			return msgs.size();
		}

		@Override
		public Object getItem(int position) {
			return msgs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder h = null;
			MMessage m = msgs.get(position);
			if (convertView == null) {
				h = new ViewHolder();
				switch (getItemViewType(position)) {
				case LEFT:
					convertView = getLayoutInflater().inflate(
							R.layout.item_lv_chat_left, null);
					h.iv_avatar = (ImageView) convertView
							.findViewById(R.id.iv_avatar);
					h.tv_content = (TextView) convertView
							.findViewById(R.id.tv_content);
					h.iv_faild = (ImageView) convertView
							.findViewById(R.id.iv_faild);
					h.progress = (ProgressBar) convertView
							.findViewById(R.id.progress_loading);
					break;
				case RIGHT:
					convertView = getLayoutInflater().inflate(
							R.layout.item_lv_chat_right, null);
					h.iv_avatar = (ImageView) convertView
							.findViewById(R.id.iv_avatar);
					h.tv_content = (TextView) convertView
							.findViewById(R.id.tv_content);
					h.iv_faild = (ImageView) convertView
							.findViewById(R.id.iv_faild);
					h.progress = (ProgressBar) convertView
							.findViewById(R.id.progress_loading);
					break;
				default:
					break;
				}
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}
			switch (getItemViewType(position)) {
			case LEFT:
				h.tv_content.setText(m.getBody());
				if (m.getSenderavatar() != null) {
					ImageLoader.getInstance().displayImage(
							RestClient.BASE_URL + m.getSenderavatar(),
							h.iv_avatar);
				} else {
					ImageLoader.getInstance().displayImage(
							"drawable://" + R.drawable.ic_image_load_normal,
							h.iv_avatar);
				}
				if (status.get(position) == Finished) {
					h.iv_faild.setVisibility(View.GONE);
					h.progress.setVisibility(View.GONE);
				} else if (status.get(position) == Sending) {
					h.iv_faild.setVisibility(View.GONE);
					h.progress.setVisibility(View.VISIBLE);
				} else { // faild
					h.iv_faild.setVisibility(View.VISIBLE);
					h.progress.setVisibility(View.GONE);
				}
				break;
			case RIGHT:
				h.tv_content.setText(m.getBody());
				if (m.getSenderavatar() != null) {
					ImageLoader.getInstance().displayImage(
							RestClient.BASE_URL + m.getSenderavatar(),
							h.iv_avatar);
				} else {
					ImageLoader.getInstance().displayImage(
							RestClient.BASE_URL + "drawable://"
									+ R.drawable.ic_image_load_normal,
							h.iv_avatar);
				}
				
				debug( m.getSenderavatar());
				if (status.get(position) == Finished) {
					h.iv_faild.setVisibility(View.INVISIBLE);
					h.progress.setVisibility(View.INVISIBLE);
				} else if (status.get(position) == Sending) {
					h.iv_faild.setVisibility(View.INVISIBLE);
					h.progress.setVisibility(View.VISIBLE);
				} else { // faild
					h.iv_faild.setVisibility(View.VISIBLE);
					h.progress.setVisibility(View.INVISIBLE);
				}
				break;
			default:
				break;
			}

			return convertView;
		}

		/**
		 * 根据数据源的position返回需要显示的的layout的type
		 * 
		 * type的值必须从0开始
		 * 
		 * */
		@Override
		public int getItemViewType(int position) {
			return msgs.get(position).getSenderid() != AppInfo.getUser(
					getContext()).getId() ? LEFT : RIGHT;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		class ViewHolder {
			TextView tv_content;
			ImageView iv_avatar, iv_faild;
			ProgressBar progress;
		}
	}

}
