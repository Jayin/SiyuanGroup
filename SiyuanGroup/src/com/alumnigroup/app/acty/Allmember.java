package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alumnigroup.api.RestClient;
import com.alumnigroup.api.UserAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.User;
import com.alumnigroup.utils.L;
import com.alumnigroup.widget.PullAndLoadListView;
import com.alumnigroup.widget.PullAndLoadListView.OnLoadMoreListener;
import com.alumnigroup.widget.PullToRefreshListView.OnRefreshListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author create by vector<br>
 *         coded by Jayin Ton
 * 
 */
public class Allmember extends BaseActivity {
	private TextView tv_title;
	private View btn_back, btn_allmenmber, btn_myfriend;
	private PullAndLoadListView lv_allmember, lv_myfriend;
	private List<User> data_allmember = null, data_myfriend = null;
	private ViewFlipper flipper;
	private UserAPI api;
	private int page = 1;
	private MemberAdapter adapter_allmember, adapter_myfriend;
	private int currentStatus = 0; //0代表从底部左边数起第一个处于显示状态

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_allmember);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		api = new UserAPI();
		data_allmember = new ArrayList<User>();
		data_myfriend = new ArrayList<User>();
	}

	@Override
	protected void initLayout() {
		initFlipper();
		tv_title = (TextView) _getView(R.id.acty_head_tv_title);
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_allmenmber = _getView(R.id.acty_allmember_footer_allmember);
		btn_myfriend = _getView(R.id.acty_allmember_footer_myfriend);
		lv_allmember = (PullAndLoadListView) _getView(R.id.acty_allmember_lv_allmember);
		lv_myfriend = (PullAndLoadListView) _getView(R.id.acty_allmember_lv_myfriend);
        
		btn_back.setOnClickListener(this);
		btn_allmenmber.setOnClickListener(this);
		btn_myfriend.setOnClickListener(this);
		
		adapter_allmember = new MemberAdapter(data_allmember);
		adapter_myfriend = new MemberAdapter(data_myfriend);
        
		lv_allmember.setAdapter(adapter_allmember);
		lv_myfriend.setAdapter(adapter_myfriend);
		
		lv_allmember.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// page=1?
				api.getAllMember(1, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] data, Throwable err) {
						toast("网络异常 错误码:" + statusCode);
						if(data!=null)L.i(new String(data));
						if(err!=null)L.i(err.toString());
						lv_allmember.onRefreshComplete();
					}

					// page=1?
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] data) {
						page = 1;
						String json = new String(data);// jsonarray
						List<User> newData_allmember = User
								.create_by_jsonarray(json);
						data_allmember.clear();
						data_allmember.addAll(newData_allmember);
						adapter_allmember.notifyDataSetChanged();
						lv_allmember.onRefreshComplete();
					}
				});
			}
		});

		lv_allmember.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				L.i("load more!!!!");
				api.getAllMember(page + 1, new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] data, Throwable err) {
						toast("网络异常 错误码:" + statusCode);
						if(data!=null)L.i(new String(data));
						if(err!=null)L.i(err.toString());
						lv_allmember.onLoadMoreComplete();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] data) {
						page++;
						// L.i(new String(data));
						String json = new String(data);// json array
						List<User> newData_allmember = User
								.create_by_jsonarray(json);
						data_allmember.addAll(newData_allmember);
						adapter_allmember.notifyDataSetChanged();
						lv_allmember.onLoadMoreComplete();
					}
				});
			}
		});

		lv_myfriend.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

			}
		});

		lv_myfriend.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

			}
		});
	
	}

	private void initFlipper() {
		flipper = (ViewFlipper) _getView(R.id.acty_allmember_content);
		flipper.setInAnimation(getContext(), R.anim.push_right_in);
		flipper.setOutAnimation(getContext(), R.anim.push_right_out);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_allmember_footer_allmember:
			if(currentStatus==0)return;
			tv_title.setText("全站会员");
			flipper.showPrevious();
			currentStatus = 0;
			break;
		case R.id.acty_allmember_footer_myfriend:
			if(currentStatus==1)return;
			tv_title.setText("我的好友");
			flipper.showNext();
			currentStatus = 1;
			break;
		default:
			break;
		}
	}

	/**
	 * 适配器
	 */
	class MemberAdapter extends BaseAdapter {
		private List<User> data;

		public MemberAdapter(List<User> data) {
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
			ViewHolder h;
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.item_lv_allmember, null);
				h = new ViewHolder();
				
				h.avatar = (ImageView) convertView
						.findViewById(R.id.item_lv_allmember_avatar);
				h.online = (ImageView) convertView
						.findViewById(R.id.item_lv_allmember_online);
				h.grade = (TextView) convertView
						.findViewById(R.id.item_lv_allmemeber_grade);
				h.name = (TextView) convertView
						.findViewById(R.id.item_lv_allmemeber_name);
				h.major = (TextView) convertView
						.findViewById(R.id.item_lv_allmemeber_major);
                convertView.setTag(h);
			} else {
                   h = (ViewHolder)convertView.getTag();
			}
			User u = data.get(position);
			ImageLoader loader = ImageLoader.getInstance();
			loader.displayImage(RestClient.BASE_URL+u.getAvatar(), h.avatar);
			h.grade.setText(u.getGrade()+"");
			h.name.setText(u.getName());
			h.major.setText(u.getMajor());
			if(u.isOnline()){
				h.online.setVisibility(View.VISIBLE);
			}else{
				h.online.setVisibility(View.INVISIBLE);
			}		
			return convertView;
		}

		class ViewHolder {
			ImageView avatar, online;
			TextView name, grade, major;
			ImageLoader loader;
		}

	}
}
