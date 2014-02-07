package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.PSource;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.adapter.BaseOnPageChangeListener;
import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.api.IssuesAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Issue;
import com.alumnigroup.imple.ImageLoadingListenerImple;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.JsonUtils;
import com.alumnigroup.utils.L;
import com.alumnigroup.widget.PullAndLoadListView;
import com.alumnigroup.widget.PullAndLoadListView.OnLoadMoreListener;
import com.alumnigroup.widget.PullToRefreshListView.OnRefreshListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 校友交流
 * 
 * @author Jayin Ton
 * 
 */
public class Communication extends BaseActivity implements OnItemClickListener {
	private List<View> btns = new ArrayList<View>();
	private View btn_back, btn_post, btn_all, btn_myjoin, btn_favourite;
	private PullAndLoadListView lv_all, lv_myjoin, lv_favourit;
	private ViewPager viewpager;
	private List<Issue> data_all, data_myjoin, data_favourite;
	private IssueAdapter adapter_all, adapter_myjoin, adapter_favourite;
	private int page_all = 1, page_myjoin = 1, page_favourit = 1;
	private IssuesAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_communication);
		initData();
		initLayout();
		initController();
	}

	private void initController() {
		lv_all.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				api.getIssueList(1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<Issue> newData_all = Issue.create_by_jsonarray(obj
								.toString());
						if (newData_all == null) {
							toast("网络异常 解析错误");
						} else if (newData_all.size() == 0) {
							toast("没有更多");
							page_all = 1;
						} else {
							page_all = 1;
							data_all.clear();
							data_all.addAll(newData_all);
							adapter_all.notifyDataSetChanged();
						}
						lv_all.onRefreshComplete();
						lv_all.setCanLoadMore(true);

					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("网络异常 错误码:" + errorCode);
						lv_all.onRefreshComplete();
					}
				});

			}
		});
		lv_all.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				api.getIssueList(page_all + 1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						boolean canloadmore = true;
						List<Issue> newData_all = Issue.create_by_jsonarray(obj
								.toString());
						if (newData_all == null) {
							toast("网络异常,解析错误");
						} else if (newData_all.size() == 0) {
							toast("没有更多了!");
							canloadmore = false;
						} else {
							page_all++;
							data_all.addAll(newData_all);
							adapter_all.notifyDataSetChanged();
						}
						lv_all.onLoadMoreComplete();
						if (!canloadmore)
							lv_all.setCanLoadMore(false);
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("网络异常 错误码:" + errorCode);
						lv_all.onLoadMoreComplete();

					}
				});
			}
		});

		lv_myjoin.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

			}
		});
		lv_myjoin.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

			}
		});
		lv_favourit.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

			}
		});
		lv_favourit.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

			}
		});
		lv_all.setOnItemClickListener(this);
		lv_myjoin.setOnItemClickListener(this);
		lv_favourit.setOnItemClickListener(this);
	}

	@Override
	protected void initData() {
		api = new IssuesAPI();
		data_all = new ArrayList<Issue>();
		data_myjoin = new ArrayList<Issue>();
		data_favourite = new ArrayList<Issue>();
	}

	private void initViewPager() {
		viewpager = (ViewPager) _getView(R.id.acty_comunication_content);
		View all = getLayoutInflater().inflate(
				R.layout.frame_acty_communication_all, null);
		View myjoin = getLayoutInflater().inflate(
				R.layout.frame_acty_communication_join, null);
		View favourit = getLayoutInflater().inflate(
				R.layout.frame_acty_communication_favourite, null);
		lv_all = (PullAndLoadListView) all
				.findViewById(R.id.frame_acty_communication_all_listview);
		lv_myjoin = (PullAndLoadListView) myjoin
				.findViewById(R.id.frame_acty_communication_join_listview);
		lv_favourit = (PullAndLoadListView) favourit
				.findViewById(R.id.frame_acty_communication_favourite_listview);

		adapter_all = new IssueAdapter(data_all);
		adapter_myjoin = new IssueAdapter(data_myjoin);
		adapter_favourite = new IssueAdapter(data_favourite);

		lv_all.setAdapter(adapter_all);
		lv_myjoin.setAdapter(adapter_myjoin);
		lv_favourit.setAdapter(adapter_favourite);

		List<View> views = new ArrayList<View>();
		views.add(all);
		views.add(myjoin);
		views.add(favourit);
		viewpager.setAdapter(new BaseViewPagerAdapter(views));
		viewpager.setOnPageChangeListener(new BaseOnPageChangeListener(btns));
	}

	@Override
	protected void initLayout() {

		btn_back = _getView(R.id.acty_head_btn_back);
		btn_post = _getView(R.id.acty_head_btn_post);
		btn_all = _getView(R.id.acty_comunication_footer_all);
		btn_myjoin = _getView(R.id.acty_comunication_footer_myjoin);
		btn_favourite = _getView(R.id.acty_comunication_footer_favourite);

		btns.add(btn_all);
		btns.add(btn_myjoin);
		btns.add(btn_favourite);

		btn_back.setOnClickListener(this);
		btn_post.setOnClickListener(this);
		btn_all.setOnClickListener(this);
		btn_myjoin.setOnClickListener(this);
		btn_favourite.setOnClickListener(this);

		initViewPager();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_head_btn_post:
			// post here
			openActivity(CommunicationPublish.class);
			break;
		case R.id.acty_comunication_footer_all:
			viewpager.setCurrentItem(0, true);
			break;
		case R.id.acty_comunication_footer_myjoin:
			viewpager.setCurrentItem(1, true);
			break;
		case R.id.acty_comunication_footer_favourite:
			viewpager.setCurrentItem(2, true);
			break;
		default:
			break;
		}
	}

	class IssueAdapter extends BaseAdapter {
		private List<Issue> data;

		public IssueAdapter(List<Issue> data) {
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
						R.layout.item_lv_acty_communication, null);
				h.name = (TextView) convertView
						.findViewById(R.id.item_lv_acty_comminication_name);
				h.major = (TextView) convertView
						.findViewById(R.id.item_lv_acty_comminication_major);
				h.posttime = (TextView) convertView
						.findViewById(R.id.item_lv_acty_comminication_posttime);
				h.title = (TextView) convertView
						.findViewById(R.id.item_lv_acty_comminication_title);
				h.body = (TextView) convertView
						.findViewById(R.id.item_lv_acty_comminication_body);
				h.numComment = (TextView) convertView
						.findViewById(R.id.item_lv_acty_comminication_numComment);
				h.favourite = (TextView) convertView
						.findViewById(R.id.item_lv_acty_comminication_favourite);
				h.avatar = (ImageView) convertView
						.findViewById(R.id.item_lv_acty_comminication_avatar);
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}
			h.name.setText(data.get(position).getUser().getProfile().getName());
			h.major.setText(data.get(position).getUser().getProfile()
					.getMajor());
			h.posttime.setText(CalendarUtils.getTimeFromat(data.get(position)
					.getPosttime(), CalendarUtils.TYPE_timeline));
			h.title.setText(data.get(position).getTitle());
			h.body.setText(data.get(position).getBody());
			h.numComment.setText(data.get(position).getNumComments() + "");
			//h.favourite.setText(data.get(position).getFavourite()+"");
			if(data.get(position).getUser().getAvatar()!=null){
				ImageLoader.getInstance().displayImage(
						RestClient.BASE_URL
								+ data.get(position).getUser().getAvatar(),
						h.avatar);
			}else{
				ImageLoader.getInstance().displayImage(
						"drawable://" + R.drawable.ic_image_load_normal, h.avatar);
			}
		
			return convertView;
		}

		class ViewHolder {
			TextView name, major, posttime, title, body, numComment, favourite;
			ImageView avatar;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, CommunicationDetail.class);
		if (parent == lv_all) {
			intent.putExtra("issue", data_all.get(position - 1));
		}
		if (parent == lv_myjoin) {
			intent.putExtra("issue", data_myjoin.get(position - 1));
		}
		if (parent == lv_favourit) {
			intent.putExtra("issue", data_favourite.get(position - 1));
		}
		openActivity(intent);
	}

}
