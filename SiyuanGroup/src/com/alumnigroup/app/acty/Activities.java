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
import android.widget.ImageView;
import android.widget.TextView;
import com.alumnigroup.adapter.BaseOnPageChangeListener;
import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.api.ActivityAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.MActivity;
import com.alumnigroup.imple.ImageLoadingListenerImple;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.widget.PullAndLoadListView;
import com.alumnigroup.widget.PullAndLoadListView.OnLoadMoreListener;
import com.alumnigroup.widget.PullToRefreshListView.OnRefreshListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 活动页面
 * 
 * @author Jayin Ton
 * 
 */
public class Activities extends BaseActivity implements OnItemClickListener {
	private List<View> btns = new ArrayList<View>();
	private View btn_back, btn_all, btn_favourite, btn_myjoin;
	private PullAndLoadListView lv_all, lv_myjoin, lv_favourit;
	private ViewPager viewpager;
	private List<MActivity> data_all, data_myjoin, data_favourite;
	private ActivitiesAdapter adapter_all, adapter_myjoin, adapter_favourite;
	private int page_all = 1, page_myjoin = 1, page_favourit = 1;
	private ActivityAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_activities);
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
		data_all = new ArrayList<MActivity>();
		data_myjoin = new ArrayList<MActivity>();
		data_favourite = new ArrayList<MActivity>();
	}

	private void initViewPager() {
		viewpager = (ViewPager) _getView(R.id.acty_activities_content);
		View all = getLayoutInflater().inflate(
				R.layout.frame_acty_activities_all, null);
		View myjoin = getLayoutInflater().inflate(
				R.layout.frame_acty_activities_myjoin, null);
		View favourit = getLayoutInflater().inflate(
				R.layout.frame_acty_activities_favourite, null);
		lv_all = (PullAndLoadListView) all
				.findViewById(R.id.frame_acty_activities_all_listview);
		lv_myjoin = (PullAndLoadListView) myjoin
				.findViewById(R.id.frame_acty_activities_myjoin_listview);
		lv_favourit = (PullAndLoadListView) favourit
				.findViewById(R.id.frame_acty_activities_favourite_listview);

		adapter_all = new ActivitiesAdapter(data_all);
		adapter_myjoin = new ActivitiesAdapter(data_myjoin);
		adapter_favourite = new ActivitiesAdapter(data_favourite);

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
		btn_all = _getView(R.id.acty_activities_footer_all);
		btn_myjoin = _getView(R.id.acty_activities_footer_myjoin);
		btn_favourite = _getView(R.id.acty_activities_footer_favourite);

		btns.add(btn_all);
		btns.add(btn_myjoin);
		btns.add(btn_favourite);

		btn_back.setOnClickListener(this);
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
		case R.id.acty_activities_footer_all:
			viewpager.setCurrentItem(0, true);
			break;
		case R.id.acty_activities_footer_myjoin:
			viewpager.setCurrentItem(1, true);
			break;
		case R.id.acty_activities_footer_favourite:
			viewpager.setCurrentItem(2, true);
			break;
		default:
			break;
		}
	}

	class ActivitiesAdapter extends BaseAdapter {
		private List<MActivity> data;

		public ActivitiesAdapter(List<MActivity> data) {
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
						R.layout.item_lv_acty_activities, null);
				h.actyName = (TextView) convertView
						.findViewById(R.id.item_lv_acty_activities_actyname);
				h.site = (TextView) convertView
						.findViewById(R.id.item_lv_acty_activities_site);
				h.starttime = (TextView) convertView
						.findViewById(R.id.item_lv_acty_activities_starttime);
				h.ownername = (TextView) convertView
						.findViewById(R.id.item_lv_acty_activities_ownername);
				h.applyCount = (TextView) convertView
						.findViewById(R.id.item_lv_acty_activities_applyCount);
				h.status = (ImageView) convertView
						.findViewById(R.id.item_lv_acty_activities_iv_status);
				h.avater = (ImageView) convertView
						.findViewById(R.id.item_lv_acty_activities_iv_avater);
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}
			MActivity acty = data.get(position);
			h.actyName.setText(acty.getName());
			h.site.setText(acty.getSite());
			h.starttime.setText("活动时间:"
					+ CalendarUtils.getTimeFromat(acty.getStarttime(),
							CalendarUtils.TYPE_ONE));
			h.ownername.setText("name" + acty.getOwnerid());
			h.applyCount.setText(acty.getUserships().size() + "人报名");
			h.status.setImageResource(acty.getStatus().getId() == 0 ? R.drawable.ic_image_status_on
					: R.drawable.ic_image_status_off);
			// h.favourite.setText(data.get(position).getFavourite()+"");
			ImageLoader.getInstance().displayImage(
					RestClient.BASE_URL + acty.getAvater(), h.avater);
			return convertView;
		}

		class ViewHolder {
			ImageView avater, status;
			TextView actyName, site, starttime, ownername, applyCount;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int real_position = position;
	}

}
