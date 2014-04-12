package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alumnigroup.api.GroupAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.MGroup;
import com.alumnigroup.entity.MMemberships;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.widget.XListView;
import com.alumnigroup.widget.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 管理申请名单页面
 * 
 * @author Jayin Ton
 * 
 */
public class GroupUsership extends BaseActivity {
	private XListView lv;
	private MGroup group;
	private List<Integer> selected;
	private int nomal=0,onGoing =1,finished=2;// 0:用户未操作 ;1:正在发请求 2:请求已完成
	private List<MMemberships> data;
	private MemberShipsAdapter adapter;
	private GroupAPI api;
	private int page = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_groupuserships);
		initData();
		initLayout();
		initController();

	}

	private void initController() {
		lv.setAdapter(adapter);
		lv.setPullLoadEnable(true);
		lv.setPullRefreshEnable(true);
		lv.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				api.getMembersUnAccepted(1, group.getId(),
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<MMemberships> newData = MMemberships
										.create_by_jsonarray(obj.toString());
								if (newData == null) {
									toast("网络异常 解析错误");
								} else if (newData.size() == 0) {
									toast("还没有人申请参加");
									lv.setPullLoadEnable(false);
								} else {
									page = 1;
									data.clear();
									data.addAll(newData);
									increaseList(newData);
									adapter.notifyDataSetChanged();
									lv.setPullLoadEnable(true);
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
				api.getMembersUnAccepted(page + 1, group.getId(),
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<MMemberships> newData = MMemberships
										.create_by_jsonarray(obj.toString());
								if (newData == null) {
									toast("网络异常 解析错误");
								} else if (newData.size() == 0) {
									toast("没有更多");
									lv.setPullLoadEnable(false);
								} else {
									page++;
									data.addAll(newData);
									increaseList(newData);
									adapter.notifyDataSetChanged();
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

	// 根据新数据来增长队列
	public void increaseList(List<MMemberships> newData) {
		for (int i = 0; i < newData.size(); i++) {
			selected.add(0);
		}
	}

	@Override
	protected void initData() {
		group = (MGroup) getSerializableExtra("group");
		if (group == null) {
			toast("无数据");
			closeActivity();
		}
		selected = new ArrayList<Integer>();
		data = new ArrayList<MMemberships>();
		adapter = new MemberShipsAdapter(getContext(), data);
		api = new GroupAPI();
		
	}

	@Override
	protected void initLayout() {
		_getView(R.id.acty_head_btn_back).setOnClickListener(this);
		lv = (XListView) _getView(R.id.lv_listview);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		default:
			break;
		}
	}

	class MemberShipsAdapter extends BaseAdapter {
		private List<MMemberships> data;
		private Context context;

		public MemberShipsAdapter(Context context, List<MMemberships> data) {
			this.data = data;
			this.context = context;
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
			final ViewHolder h;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_lv_acty_groupusership, null);
				h = new ViewHolder();
				h.avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
				h.status = (TextView) convertView.findViewById(R.id.tv_status);
				h.name = (TextView) convertView.findViewById(R.id.tv_name);
				h.major = (TextView) convertView.findViewById(R.id.tv_major);
				h.accept = convertView.findViewById(R.id.btn_accept);
				h.refuse = convertView.findViewById(R.id.btn_refuse);
				h.pb = (ProgressBar) convertView.findViewById(R.id.progress);
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}
			User u = data.get(position).getUser();
			if (u.getAvatar() != null) {
				ImageLoader.getInstance().displayImage(
						RestClient.BASE_URL + u.getAvatar(), h.avatar);
			} else {
				ImageLoader.getInstance().displayImage(
						"drawable://" + R.drawable.ic_img_avatar_default,
						h.avatar);
			}

			h.name.setText(u.getProfile().getName());
			h.major.setText(u.getProfile().getMajor());

			if(selected.get(position)==finished){
				h.pb.setVisibility(View.GONE);
				h.refuse.setVisibility(View.GONE);
				h.accept.setVisibility(View.GONE);
				h.status.setVisibility(View.VISIBLE);
			}else if(selected.get(position)==onGoing){
				h.pb.setVisibility(View.VISIBLE);
				h.refuse.setVisibility(View.GONE);
				h.accept.setVisibility(View.GONE);
				h.status.setVisibility(View.GONE);
			}else{     //nomal
				h.pb.setVisibility(View.GONE);
				h.refuse.setVisibility(View.VISIBLE);
				h.accept.setVisibility(View.VISIBLE);
				h.status.setVisibility(View.GONE);
			}
			final int p = position;
			h.accept.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					api.accept(data.get(p).getId(), new JsonResponseHandler() {
						@Override
						public void onStart() {
							h.pb.setVisibility(View.VISIBLE);
							h.refuse.setVisibility(View.GONE);
							h.accept.setVisibility(View.GONE);
							h.status.setVisibility(View.GONE);
							selected.set(p, onGoing);
						}

						@Override
						public void onOK(Header[] headers, JSONObject obj) {
							h.pb.setVisibility(View.GONE);
							h.refuse.setVisibility(View.GONE);
							h.accept.setVisibility(View.GONE);
							h.status.setVisibility(View.VISIBLE);
							selected.set(p, finished);
							h.status.setText("已接受");
							h.status.setTextColor(Color.GREEN);
						}

						@Override
						public void onFaild(int errorType, int errorCode) {
							toast(ErrorCode.errorList.get(errorCode));
							h.pb.setVisibility(View.GONE);
							h.refuse.setVisibility(View.VISIBLE);
							h.accept.setVisibility(View.VISIBLE);
							h.status.setVisibility(View.GONE);
							selected.set(p, nomal);
						}
					});

				}
			});
			h.refuse.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					api.reject(data.get(p).getId(), new JsonResponseHandler() {
						@Override
						public void onStart() {
							h.pb.setVisibility(View.VISIBLE);
							h.refuse.setVisibility(View.GONE);
							h.accept.setVisibility(View.GONE);
							h.status.setVisibility(View.GONE);
							selected.set(p, onGoing);
						}

						@Override
						public void onOK(Header[] headers, JSONObject obj) {
							h.pb.setVisibility(View.GONE);
							h.refuse.setVisibility(View.GONE);
							h.accept.setVisibility(View.GONE);
							h.status.setVisibility(View.VISIBLE);
							selected.set(p, finished);
							h.status.setText("已拒绝");
							h.status.setTextColor(Color.RED);
						}

						@Override
						public void onFaild(int errorType, int errorCode) {
							toast(ErrorCode.errorList.get(errorCode));
							h.pb.setVisibility(View.GONE);
							h.refuse.setVisibility(View.VISIBLE);
							h.accept.setVisibility(View.VISIBLE);
							h.status.setVisibility(View.GONE);
							selected.set(p, nomal);
						}
					});
				}
			});
			return convertView;
		}

		class ViewHolder {
			ImageView avatar;
			View accept, refuse;
			TextView name, major, status;
			ProgressBar pb;
		}
	}
}
