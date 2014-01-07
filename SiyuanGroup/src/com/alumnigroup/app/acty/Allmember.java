package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alumnigroup.api.UserAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.User;
import com.alumnigroup.utils.L;
import com.alumnigroup.widget.PullAndLoadListView;
import com.alumnigroup.widget.PullAndLoadListView.OnLoadMoreListener;
import com.alumnigroup.widget.PullToRefreshListView.OnRefreshListener;
import com.loopj.android.http.AsyncHttpResponseHandler;

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

		adapter_allmember = new MemberAdapter(data_allmember);
		adapter_myfriend = new MemberAdapter(data_myfriend);
		
		lv_allmember.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// page=1?
				api.getAllMember(1, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] data, Throwable arg3) {
						toast("网络异常 错误码:" + statusCode);
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
				api.getAllMember(page + 1, new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] data, Throwable arg3) {
						toast("网络异常 错误码:" + statusCode);
						lv_allmember.onLoadMoreComplete();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] data) {
						page++;
						// L.i(new String(data));
						String json = new String(data);// jsonarray
						List<User> newData_allmember = User
								.create_by_jsonarray(json);
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

		lv_allmember.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

			}
		});
		btn_back.setOnClickListener(this);
		btn_allmenmber.setOnClickListener(this);
		btn_myfriend.setOnClickListener(this);
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
			break;
		case R.id.acty_allmember_footer_myfriend:
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
			return  data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			return null;
		}

	}
}
