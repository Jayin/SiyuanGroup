package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

import com.alumnigroup.adapter.BaseOnPageChangeListener;
import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.api.ActivityAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.app.acty.Activities.ActivitiesAdapter;
import com.alumnigroup.entity.MActivity;
import com.alumnigroup.entity.Project;
import com.alumnigroup.widget.PullAndLoadListView;
import com.alumnigroup.widget.PullAndLoadListView.OnLoadMoreListener;
import com.alumnigroup.widget.PullToRefreshListView.OnRefreshListener;

public class Business extends BaseActivity implements OnItemClickListener {
	private List<View> btns = new ArrayList<View>();
	private View btn_back, btn_all, btn_favourite, btn_myjoin, btn_compose;
	private PullAndLoadListView lv_all, lv_myjoin, lv_favourit;
	private ViewPager viewpager;
	private List<Project> data_all, data_myjoin, data_favourite;
	private BusinessAdapter adapter_all, adapter_myjoin, adapter_favourite;
	private int page_all = 1, page_myjoin = 1, page_favourit = 1;
	private ActivityAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_business);
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
		lv_all.setOnItemClickListener(this);
		lv_myjoin.setOnItemClickListener(this);
		lv_favourit.setOnItemClickListener(this);
	}

	@Override
	protected void initData() {
		api = new ActivityAPI();
		data_all = new ArrayList<Project>();
		data_myjoin = new ArrayList<Project>();
		data_favourite = new ArrayList<Project>();
	}

	private void initViewPager() {
		viewpager = (ViewPager) _getView(R.id.acty_business_content);
		View all = getLayoutInflater().inflate(R.layout.frame_acty_business,
				null);
		View myjoin = getLayoutInflater().inflate(R.layout.frame_acty_business,
				null);
		View favourit = getLayoutInflater().inflate(
				R.layout.frame_acty_business, null);
		lv_all = (PullAndLoadListView) all
				.findViewById(R.id.frame_acty_business_listview);
		lv_myjoin = (PullAndLoadListView) myjoin
				.findViewById(R.id.frame_acty_business_listview);
		lv_favourit = (PullAndLoadListView) favourit
				.findViewById(R.id.frame_acty_business_listview);

		adapter_all = new BusinessAdapter(data_all);
		adapter_myjoin = new BusinessAdapter(data_myjoin);
		adapter_favourite = new BusinessAdapter(data_favourite);

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
		btn_all = _getView(R.id.acty_business_footer_all);
		btn_myjoin = _getView(R.id.acty_business_footer_myjoin);
		btn_favourite = _getView(R.id.acty_business_footer_favourite);
		btn_compose = _getView(R.id.acty_head_btn_compose);

		btns.add(btn_all);
		btns.add(btn_myjoin);
		btns.add(btn_favourite);

		btn_back.setOnClickListener(this);
		btn_all.setOnClickListener(this);
		btn_myjoin.setOnClickListener(this);
		btn_favourite.setOnClickListener(this);
		btn_compose.setOnClickListener(this);

		initViewPager();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_head_btn_compose:
			toast("发布");
			break;
		case R.id.acty_business_footer_all:
			viewpager.setCurrentItem(0, true);
			break;
		case R.id.acty_business_footer_myjoin:
			viewpager.setCurrentItem(1, true);
			break;
		case R.id.acty_business_footer_favourite:
			viewpager.setCurrentItem(2, true);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	class BusinessAdapter extends BaseAdapter {
		private List<Project> data;

		public BusinessAdapter(List<Project> data) {
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
			convertView = getLayoutInflater().inflate(
					R.layout.item_lv_acty_business, null);
			return convertView;
		}

	}
}
