package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.alumnigroup.api.ActivityAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.MActivity;
import com.alumnigroup.entity.User;
import com.alumnigroup.entity.Userships;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.widget.XListView;
import com.alumnigroup.widget.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 用户列表
 * 
 * @author Jayin Ton
 * 
 */
public class ActivitiesUserShip extends BaseActivity {
	private View btn_back, btn_accept;
	private XListView lv;
	private MActivity acty;
	private List<Integer> selected;// 0:not accept ;1:accepting 2:accepted
	private List<Userships> data;
	private UserShipAdapter adapter;
	private ActivityAPI api;
	private int page = 0;
	private ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_activitiesuserships);
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
				api.getUserList(1, acty.getId(), new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<Userships> newData = Userships
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
				if(page==0){
					lv.stopLoadMore();
					lv.startRefresh();
					return;
				}
				api.getUserList(page + 1, acty.getId(),
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<Userships> newData = Userships
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

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					final int position, long id) {
				if (selected.get(position - 1) == 2
						|| data.get(position - 1).getIsaccepted() != 0) {
					return;
				}
				api.accept(data.get(position - 1).getId(), acty.getId(),
						new JsonResponseHandler() {
							@Override
							public void onStart() {
								selected.set(position - 1, 1);
								((ProgressBar) view.findViewById(R.id.progress))
										.setVisibility(View.VISIBLE);
								((ImageView) view.findViewById(R.id.iv_ischeck))
										.setVisibility(View.GONE);
							}

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								selected.set(position - 1, 2);
								((ProgressBar) view.findViewById(R.id.progress))
										.setVisibility(View.GONE);
								((ImageView) view.findViewById(R.id.iv_ischeck))
										.setVisibility(View.GONE);
								(view.findViewById(R.id.tv_status))
										.setVisibility(View.VISIBLE);
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								selected.set(position - 1, 0);
								((ProgressBar) view.findViewById(R.id.progress))
										.setVisibility(View.GONE);
								((ImageView) view.findViewById(R.id.iv_ischeck))
										.setVisibility(View.VISIBLE);
							}
						});
			}
		});
		
		lv.startRefresh();
	}

	// 根据新数据来增长队列
	public void increaseList(List<Userships> newData) {
		for (int i = 0; i < newData.size(); i++) {
			selected.add(0);
		}
	}

	@Override
	protected void initData() {
		acty = (MActivity) getSerializableExtra("activities");
		if (acty == null) {
			toast("无数据");
			closeActivity();
		}
		selected = new ArrayList<Integer>();
		data = new ArrayList<Userships>();
		adapter = new UserShipAdapter(getContext(), data);
		api = new ActivityAPI();
	}

	@Override
	protected void initLayout() {
		btn_accept = _getView(R.id.btn_accept);
		btn_back = _getView(R.id.acty_head_btn_back);
		lv = (XListView) _getView(R.id.lv_listview);

		btn_accept.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_accept:
			closeActivity();
			break;
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		default:
			break;
		}
	}

	class UserShipAdapter extends BaseAdapter {
		private List<Userships> data;
		private Context context;

		public UserShipAdapter(Context context, List<Userships> data) {
			this.data = data;
			this.context = context;
			;
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
			ViewHolder h;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_lv_activitiesusership, null);
				h = new ViewHolder();
				h.avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
				h.status = (TextView) convertView.findViewById(R.id.tv_status);
				h.name = (TextView) convertView.findViewById(R.id.tv_name);
				h.major = (TextView) convertView.findViewById(R.id.tv_major);
				h.isCheck = (ImageView) convertView
						.findViewById(R.id.iv_ischeck);
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
						"drawable://" + R.drawable.ic_image_load_normal,
						h.avatar);
			}

			h.name.setText(u.getProfile().getName());
			h.major.setText(u.getProfile().getMajor());
			if (data.get(position).getIsaccepted() != 0
					|| selected.get(position) == 2) {// 用户被接受参加
				h.status.setVisibility(View.VISIBLE);
				h.isCheck.setVisibility(View.GONE);
				h.pb.setVisibility(View.GONE);
			} else {
				h.status.setVisibility(View.GONE);
				if (selected.get(position) == 0) { // not accept
					h.isCheck.setVisibility(View.VISIBLE);
					h.isCheck
							.setBackgroundResource(R.drawable.ic_check_green_uncheked);
					h.pb.setVisibility(View.GONE);
				} else { // accepting
					h.pb.setVisibility(View.VISIBLE);
					h.isCheck.setVisibility(View.GONE);
				}
			}

			return convertView;
		}

		class ViewHolder {
			ImageView avatar, isCheck;
			TextView name, major, status;
			ProgressBar pb;
		}

	}

}
