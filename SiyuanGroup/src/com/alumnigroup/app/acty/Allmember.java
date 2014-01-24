package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.adapter.MemberAdapter;
import com.alumnigroup.api.UserAPI;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.User;
import com.alumnigroup.utils.JsonUtils;
import com.alumnigroup.utils.L;
import com.alumnigroup.widget.PullAndLoadListView;
import com.alumnigroup.widget.PullAndLoadListView.OnLoadMoreListener;
import com.alumnigroup.widget.PullToRefreshListView.OnRefreshListener;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 全站会员
 * 
 * @author create by vector<br>
 *         coded by Jayin Ton
 * 
 */
public class Allmember extends BaseActivity {
	private TextView tv_title;
	private View btn_back, btn_allmenmber, btn_myfriend, btn_pressed;
	private PullAndLoadListView lv_allmember, lv_myfriend;
	private List<User> data_allmember = null, data_myfriend = null;
	private ViewPager viewpager;
	private UserAPI api;
	private int page_allmember = 1, page_myfriend = 1;
	private MemberAdapter adapter_allmember, adapter_myfriend;
	private int currentStatus = 0; // 0代表从底部左边数起第一个处于显示状态

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_allmember);
		initData();
		initLayout();
		initController();
	}

	private void initController() {
		lv_allmember.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// page=1?
				api.getAllMember(1, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] data, Throwable err) {
						toast("网络异常 错误码:" + statusCode);
						if (data != null)
							L.i(new String(data));
						if (err != null)
							L.i(err.toString());
						lv_allmember.onRefreshComplete();
					}

					// page=1?
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] data) {
						// 还要判断是否有error_code
						String json = new String(data);// jsonarray
						if (JsonUtils.isOK(json)) {
							List<User> newData_allmember = User
									.create_by_jsonarray(json);
							if (newData_allmember != null) {
								page_allmember = 1;
								data_allmember.clear();
								data_allmember.addAll(newData_allmember);
								adapter_allmember.notifyDataSetChanged();
							}
						} else {
							toast("Error:" + JsonUtils.getErrorString(json));
						}
						lv_allmember.setCanLoadMore(true);// 因为下拉到最低的时候，再下拉刷新，相当于继续可以下拉刷新
						lv_allmember.onRefreshComplete();
					}
				});
			}
		});

		lv_allmember.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				api.getAllMember(page_allmember + 1,
						new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(int statusCode,
									Header[] headers, byte[] data, Throwable err) {
								toast("网络异常 错误码:" + statusCode);
								if (data != null)
									L.i(new String(data));
								if (err != null)
									L.i(err.toString());
								lv_allmember.onLoadMoreComplete();
							}

							@Override
							public void onSuccess(int statusCode,
									Header[] headers, byte[] data) {

								// L.i(new String(data));
								String json = new String(data);// json array
								if (JsonUtils.isOK(json)) {
									List<User> newData_allmember = User
											.create_by_jsonarray(json);
									if (newData_allmember != null
											&& newData_allmember.size() > 0) {
										page_allmember++;
										data_allmember
												.addAll(newData_allmember);
										adapter_allmember
												.notifyDataSetChanged();
									} else {
										if (newData_allmember == null) {
											toast("网络异常,解析错误");
										} else if (newData_allmember.size() == 0) {
											toast("没有更多了!");
											lv_allmember.setCanLoadMore(false);
										}
									}
								} else {
									toast("Error:"
											+ JsonUtils.getErrorString(json));
								}
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

	@Override
	protected void initData() {
		api = new UserAPI();
		data_allmember = new ArrayList<User>();
		data_myfriend = new ArrayList<User>();
	}

	@Override
	protected void initLayout() {
		initViewPager();
		tv_title = (TextView) _getView(R.id.acty_head_tv_title);
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_allmenmber = _getView(R.id.acty_allmember_footer_allmember);
		btn_myfriend = _getView(R.id.acty_allmember_footer_myfriend);

		btn_pressed = btn_allmenmber;

		btn_back.setOnClickListener(this);
		btn_allmenmber.setOnClickListener(this);
		btn_myfriend.setOnClickListener(this);

		adapter_allmember = new MemberAdapter(data_allmember,getContext());
		adapter_myfriend = new MemberAdapter(data_myfriend,getContext());

		lv_allmember.setAdapter(adapter_allmember);
		lv_myfriend.setAdapter(adapter_myfriend);
	}

	private void initViewPager() {
		viewpager = (ViewPager) _getView(R.id.acty_allmember_content);
		View allmember = getLayoutInflater().inflate(
				R.layout.frame_acty_allmember_allmember, null);
		View myfriend = getLayoutInflater().inflate(
				R.layout.frame_acty_allmember_myfriend, null);

		lv_allmember = (PullAndLoadListView) allmember
				.findViewById(R.id.acty_allmember_lv_allmember);
		lv_myfriend = (PullAndLoadListView) myfriend
				.findViewById(R.id.acty_allmember_lv_myfriend);
		List<View> views = new ArrayList<View>();
		views.add(allmember);
		views.add(myfriend);

		viewpager.setAdapter(new BaseViewPagerAdapter(views));
		viewpager.setCurrentItem(0);

		viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_allmember_footer_allmember:
			viewpager.setCurrentItem(0);
			tv_title.setText("全站会员");
			currentStatus = 0;
			break;
		case R.id.acty_allmember_footer_myfriend:
			viewpager.setCurrentItem(1);
			tv_title.setText("我的好友");
			currentStatus = 1;
			break;
		default:
			break;
		}
	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			if (position == 0) {
				tv_title.setText("全站会员");
				btn_allmenmber.setBackgroundResource(R.color.blue_nav_bg_press);
				btn_myfriend.setBackgroundResource(R.color.blue_nav_bg_nomal);
			}

			if (position == 1) {
				tv_title.setText("我的好友");
				btn_allmenmber.setBackgroundResource(R.color.blue_nav_bg_nomal);
				btn_myfriend.setBackgroundResource(R.color.blue_nav_bg_press);
			}

		}
	}
}
