package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.api.FollowshipAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.Following;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.widget.PullAndLoadListView;
import com.alumnigroup.widget.PullAndLoadListView.OnLoadMoreListener;
import com.alumnigroup.widget.PullToRefreshListView.OnRefreshListener;
import com.alumnigroup.widget.XListView;
import com.alumnigroup.widget.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 用户关注列表<br>
 * 如果有勾选的，则在getIntent().getSeriable("result")获得勾选列表ArrayList< User >
 * @author Jayin Ton
 * 
 */
public class FollowingList extends BaseActivity {
	private XListView lv;
	private View btn_back, btn_comfrim;
	private FollowingAdapter adapter;
	private List<User> data;
	private FollowshipAPI api;
	private int page = 0;
	private int userid;
	private List<Boolean> selected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_followinglist);
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
				api.getFollowingList(1, userid, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<User> newData = Following.getUsesList(Following
								.create_by_jsonarray(obj.toString()));
						if (newData == null) {
							toast("网络异常 解析错误");
						} else if (newData.size() == 0) {
							toast("你还没有关注任何人");
							lv.setPullLoadEnable(false);
						} else {
							page = 1;
							data.clear();
							data.addAll(newData);
							selected.clear();
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
				api.getFollowingList(page + 1, userid,
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<User> newData = Following.getUsesList(Following
										.create_by_jsonarray(obj.toString()));
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
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				selected.set(position-1, !selected.get(position-1));
				if (selected.get(position-1)) {
					((ImageView) view.findViewById(R.id.iv_ischeck))
							.setBackgroundResource(R.drawable.ic_check_green_cheked);
				} else {
					((ImageView) view.findViewById(R.id.iv_ischeck))
							.setBackgroundResource(R.drawable.ic_check_green_uncheked);
				}
			}
		});
		lv.startRefresh();
	}

	// 根据新数据来增长队列
	public void increaseList(List<User> newData) {
		for (int i = 0; i < newData.size(); i++) {
			selected.add(false);
		}
	}

	@Override
	protected void initData() {
		// 如果缓存有数据 则加载
		api = new FollowshipAPI();
		userid = getIntExtra("userid");
		if (userid == -1) {
			toast("不存在该用户");
			closeActivity();
		}
		selected = new ArrayList<Boolean>();
		data = new ArrayList<User>();
		adapter = new FollowingAdapter(data, getContext());
	}

	@Override
	protected void initLayout() {
		lv = (XListView) _getView(R.id.lv_listivew);
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_comfrim = _getView(R.id.acty_head_btn_comfirm);

		btn_back.setOnClickListener(this);
		btn_comfrim.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_head_btn_comfirm:
			if (getIntent() != null) {
				Intent intent = getIntent();
				if(intent!=null){
					//有选择的才算ok
					ArrayList<User> result = wrapSelect();
					if(result.size()>0){
						intent.putExtra("result",result );
						setResult(RESULT_OK, intent);
					}else{
						setResult(RESULT_CANCELED);
					}
				}
			}else{
				setResult(RESULT_CANCELED);
			}
			closeActivity();
			break;
		default:
			break;
		}
	}
	
	//获得勾选的列表
	private ArrayList<User> wrapSelect(){
		ArrayList<User> result = new ArrayList<User>();
		for(int i=0;i<selected.size();i++){
			if(selected.get(i) && data.get(i)!=null){
				result.add(data.get(i));
			}
		}
		return result;
	}

	class FollowingAdapter extends BaseAdapter {
		private List<User> data;
		private Context context;

		public FollowingAdapter(List<User> data, Context context) {
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
			ViewHolder h;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_lv_acty_followlist, null);
				h = new ViewHolder();

				h.avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
				h.ischeck = (ImageView) convertView
						.findViewById(R.id.iv_ischeck);

				h.name = (TextView) convertView.findViewById(R.id.tv_name);
				h.major = (TextView) convertView.findViewById(R.id.tv_major);
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}
			User u = data.get(position);
			if(u.getAvatar()!=null){
				ImageLoader.getInstance().displayImage(
						RestClient.BASE_URL + u.getAvatar(), h.avatar);
			}else{
				ImageLoader.getInstance().displayImage(
						"drawable://"+R.drawable.ic_image_load_normal, h.avatar);
			}
			h.name.setText(u.getProfile().getName());
			h.major.setText(u.getProfile().getMajor());
			h.ischeck.setVisibility(View.VISIBLE);
			if (selected.get(position)) {
				h.ischeck
						.setBackgroundResource(R.drawable.ic_check_green_cheked);
			} else {
				h.ischeck
						.setBackgroundResource(R.drawable.ic_check_green_uncheked);
			}
			return convertView;
		}

		class ViewHolder {
			ImageView avatar, ischeck;
			TextView name, major;
		}

	}

}
