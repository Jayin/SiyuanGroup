package com.alumnigroup.app.acty;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.api.MessageAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.MMessage;
import com.alumnigroup.widget.XListView;
import com.alumnigroup.widget.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MessageDetail extends BaseActivity {
	private MMessage message;
	private ArrayList<MMessage> data;
	private XListView lv;
	private ChatAdapter adapter;
	private EditText et_body;
	private MessageAPI api;

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
		lv.setPullRefreshEnable(false);
		lv.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {

			}

			@Override
			public void onLoadMore() {

			}
		});
	}

	@Override
	protected void initData() {
		message = getSerializableExtra("message");
		data = new ArrayList<MMessage>();
		adapter = new ChatAdapter();
		data.add(message);
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
			MMessage mm = new MMessage();
			mm.setSender(AppInfo.getUser(getContext()));
			mm.setBody(et_body.getText().toString());
			data.add(mm);
			adapter.notifyDataSetChanged();
			lv.setSelection(data.size() - 1);
			debug(mm.toString());
			debug(data.size() + "");
			debug(data.toString());
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
			MMessage m = data.get(position);
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
					break;
				case RIGHT:
					convertView = getLayoutInflater().inflate(
							R.layout.item_lv_chat_right, null);
					h.iv_avatar = (ImageView) convertView
							.findViewById(R.id.iv_avatar);
					h.tv_content = (TextView) convertView
							.findViewById(R.id.tv_content);
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
				if (m.getSender().getAvatar() != null) {
					ImageLoader.getInstance().displayImage(
							RestClient.BASE_URL + m.getSender().getAvatar(),
							h.iv_avatar);
				} else {
					ImageLoader.getInstance().displayImage(
							"drawable://" + R.drawable.ic_image_load_normal,
							h.iv_avatar);
				}
				break;
			case RIGHT:
				h.tv_content.setText(m.getBody());
				if (m.getSender().getAvatar() != null) {
					ImageLoader.getInstance().displayImage(
							RestClient.BASE_URL + m.getSender().getAvatar(),
							h.iv_avatar);
				} else {
					ImageLoader.getInstance().displayImage(
							RestClient.BASE_URL + "drawable://"
									+ R.drawable.ic_image_load_normal,
							h.iv_avatar);
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
			return data.get(position).getSender().getId() != AppInfo.getUser(
					getContext()).getId() ? LEFT : RIGHT;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		class ViewHolder {
			TextView tv_content;
			ImageView iv_avatar;
		}
	}

}
