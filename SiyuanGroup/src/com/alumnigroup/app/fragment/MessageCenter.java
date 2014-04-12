package com.alumnigroup.app.fragment;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import com.alumnigroup.api.MessageAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.MessageCache;
import com.alumnigroup.app.R;
import com.alumnigroup.app.acty.MessageDetail;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.MMessage;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.widget.XListView;
import com.alumnigroup.widget.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 消息中心
 * @author Jayin Ton
 *
 */
public class MessageCenter extends BaseFragment {
	private XListView lv_message;
	private ArrayList<MMessage> data_message;
	private MessageAdapter adapter;
	private MessageAPI api;
	private int page = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		data_message = new ArrayList<MMessage>();
		adapter = new MessageAdapter();
		api = new MessageAPI();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(lv_message!=null){
			lv_message.startRefresh();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.fragment_messagecenter, container, false);
		lv_message = (XListView) v.findViewById(R.id.lv_messages);
		initCotroller();
		return v;
	}
	private void initCotroller() {
		lv_message.setAdapter(adapter);
		lv_message.setPullLoadEnable(true);
		lv_message.setPullRefreshEnable(true);
		lv_message.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				api.getMessageList(1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						ArrayList<MMessage> newData = MMessage
								.create_by_jsonarray(obj.toString());
						if (newData == null) {
							toast("网络异常 解析错误");
						} else if (newData.size() == 0) {
							toast("没有消息");
						} else {
							page = 1;
							data_message.clear();
							data_message.addAll(newData);
							adapter.notifyDataSetChanged();
							if(data_message.size()<10){
								lv_message.setPullLoadEnable(false);
							}else{
								lv_message.setPullLoadEnable(true);
							}
						}
						lv_message.stopRefresh();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast(ErrorCode.errorList.get(errorCode));
						lv_message.stopRefresh();
					}
				});
			}

			@Override
			public void onLoadMore() {
				if (page == 0) {
					lv_message.stopLoadMore();
					lv_message.startRefresh();
					return;
				}
				
				api.getMessageList(page + 1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						ArrayList<MMessage> newData = MMessage
								.create_by_jsonarray(obj.toString());
						if (newData == null) {
							toast("网络异常 解析错误");
						} else if (newData.size() == 0) {
							toast("没有消息");
						} else {
							page++;
							data_message.addAll(newData);
							adapter.notifyDataSetChanged();
						}
						lv_message.stopLoadMore();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast(ErrorCode.errorList.get(errorCode));
						lv_message.stopLoadMore();
					}
				});
			}
		});

		lv_message.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position - 1 == -1)
					return;
				//刷新已读书
				int unreadCount =  MessageCache.getUnreadCount(getActivity())-data_message.get(position-1).getUnreadcount();
				MessageCache.setUnreadCount(getActivity(),unreadCount <0?0:unreadCount);
				getActivity().sendBroadcast(new Intent(Constants.Action_Receive_UnreadCount));
				view.findViewById(R.id.tv_unreadcount).setVisibility(View.GONE);
				data_message.get(position-1).setUnreadcount(0);
				Intent intent = new Intent(getActivity(), MessageDetail.class);
				intent.putExtra("message", data_message.get(position - 1));
				startActivity(intent);
			}
		});
		lv_message.startRefresh();
		
	}
	class MessageAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return data_message.size();
		}

		@Override
		public Object getItem(int position) {
			return data_message.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder h = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.item_lv_acty_messagecenter, null);
				h = new ViewHolder();
				h.iv_avatar = (ImageView) convertView
						.findViewById(R.id.iv_avatar);
				h.tv_username = (TextView) convertView
						.findViewById(R.id.tv_username);
				h.tv_content = (TextView) convertView
						.findViewById(R.id.tv_content);
				h.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				h.tv_unreadCount = (TextView)convertView.findViewById(R.id.tv_unreadcount);
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}
			MMessage mm = data_message.get(position);

			if (mm.getSenderavatar() != null) {
				ImageLoader.getInstance()
						.displayImage(
								RestClient.BASE_URL + mm.getSenderavatar(),
								h.iv_avatar);
			} else {
				ImageLoader.getInstance().displayImage(
						"drawalbe://" + R.drawable.ic_image_load_normal,
						h.iv_avatar);
			}
			h.tv_content.setText(mm.getBody());
			h.tv_username.setText(mm.getSendername());
			h.tv_time.setText(CalendarUtils.getTimeFromat(
					data_message.get(position).getSendtime(),
					CalendarUtils.TYPE_timeline));
			h.tv_unreadCount.setVisibility(View.GONE);
			
			if(mm.getSenderid() == 8){ //系统消息
				h.tv_username.setTextColor(getResources().getColor(R.color.blue_lv_two));
			}else{
				h.tv_username.setTextColor(getResources().getColor(R.color.black));
			}
			if(mm.getUnreadcount()>0){
				h.tv_unreadCount.setText(mm.getUnreadcount()+"");
				h.tv_unreadCount.setVisibility(View.VISIBLE);
			}
			return convertView;
		}

		class ViewHolder {
			ImageView iv_avatar;
			TextView tv_time, tv_username, tv_content,tv_unreadCount;
		}

	}
	@Override
	public void onClick(View v) {
	
	}
}
