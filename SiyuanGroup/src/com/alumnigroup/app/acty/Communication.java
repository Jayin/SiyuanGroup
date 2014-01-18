package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alumnigroup.adapter.BaseOnPageChangeListener;
import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Issue;
import com.alumnigroup.widget.PullAndLoadListView;
import com.alumnigroup.widget.PullAndLoadListView.OnLoadMoreListener;
import com.alumnigroup.widget.PullToRefreshListView.OnRefreshListener;

public class Communication extends BaseActivity {
	private List<View> btns = new ArrayList<View>();
	private View btn_back, btn_post, btn_all, btn_myjoin, btn_favourite;
	private PullAndLoadListView lv_all, lv_myjoin, lv_favourit;
	private ViewPager viewpager;
	private List<Issue> data_all, data_myjoin, data_favourite;
	private IssueAdapter adapter_all, adapter_myjoin, adapter_favourite;

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

			}
		});
		lv_all.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

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

	}

	@Override
	protected void initData() {
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

		lv_all.setAdapter(new IssueAdapter(data_all));
		lv_myjoin.setAdapter(new IssueAdapter(data_myjoin));
		lv_favourit.setAdapter(new IssueAdapter(data_favourite));

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
			return convertView;
		}

	}

}
