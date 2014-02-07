package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.adapter.MemberAdapter;
import com.alumnigroup.api.FollowshipAPI;
import com.alumnigroup.api.UserAPI;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Following;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.DataPool;
import com.alumnigroup.widget.MenuDialog;
import com.alumnigroup.widget.PullAndLoadListView;
import com.alumnigroup.widget.PullAndLoadListView.OnLoadMoreListener;
import com.alumnigroup.widget.PullToRefreshListView.OnRefreshListener;

/**
 * 全站会员:<br>
 * 第一次进来，自动刷新,如果没有数据提示：没有更多数据，保存20最多条记录<br>
 * 第二次进来，拿缓存数据，需要主动刷新，刷新后更新缓存数据为最新，最新定义为列表顶部20条数据<br>
 * 底部加载不更改缓存<br>
 * 点击item 进入空间
 * 
 * @author create by vector<br>
 *         coded by Jayin Ton
 * 
 */
public class Allmember extends BaseActivity implements OnItemClickListener {
	private TextView tv_title;
	private View btn_back, btn_allmenmber, btn_myfriend, btn_pressed;
	private PullAndLoadListView lv_allmember, lv_myfriend;
	private List<User> data_allmember = null, data_myfriend = null;
	private DataPool dp;
	private ViewPager viewpager;
	private UserAPI api;
	private int page_allmember = 1, page_myfriend = 1;
	private MemberAdapter adapter_allmember, adapter_myfriend;
	private int currentStatus = 0; // 0代表从底部左边数起第一个处于显示状态
	private MenuDialog dialog;
	private User user;
	private FollowshipAPI followshipAPI;

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
				api.getAllMember(1, new JsonResponseHandler() {
					
					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<User> newData_allmember = User
								.create_by_jsonarray(obj.toString());
						if(newData_allmember==null){
							toast("网络异常，解析错误");
						}else if(newData_allmember.size()==0){
							toast("没有更多");
							page_allmember = 1;
						}else{
							page_allmember = 1;
							data_allmember.clear();
							data_allmember.addAll(newData_allmember);
							adapter_allmember.notifyDataSetChanged();
							//saveAllMemberData2SP(newData_allmember);
						}
						lv_allmember.onRefreshComplete();
						lv_allmember.setCanLoadMore(true);
					}
					
					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("网络异常 错误码:" + errorCode);
						lv_allmember.onRefreshComplete();
					}
				});
			}
		});

		lv_allmember.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				api.getAllMember(page_allmember+1, new JsonResponseHandler() {
					
					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						boolean canLoadMore = true;
						List<User> newData_allmember = User
								.create_by_jsonarray(obj.toString());
						if(newData_allmember==null){
							toast("网络异常，解析错误");
						}else if(newData_allmember.size()==0){
							toast("没有更多");
							canLoadMore = false;
						}else{
							page_allmember++;
							data_allmember.addAll(newData_allmember);
							adapter_allmember.notifyDataSetChanged();
						}
						lv_allmember.onLoadMoreComplete();
						if (!canLoadMore)
							lv_allmember.setCanLoadMore(false);
					}
					
					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("网络异常 错误码:" + errorCode);
						lv_allmember.onLoadMoreComplete();
					}
				});
			}
		});

		lv_myfriend.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				followshipAPI.getFollowingList(1, user.getId(),
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<User> newData = Following
										.getUsesList(Following
												.create_by_jsonarray(obj
														.toString()));
								if (newData == null) {
									toast("网络异常 解析错误");
								} else if (newData.size() == 0) {
									toast("你还没有关注任何人");
									page_myfriend = 1;
								} else {
									page_myfriend = 1;
									data_myfriend.clear();
									data_myfriend.addAll(newData);
									adapter_myfriend.notifyDataSetChanged();
									//saveMyFriendData2SP(newData_myfriend);
								}
								//lv_myfriend.setCanRefresh(false, false);  //好友应该加载一次就ok？
								lv_myfriend.onRefreshComplete();
								lv_myfriend.setCanLoadMore(true);
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast("网络异常 错误码:" + errorCode);
								lv_myfriend.onRefreshComplete();
							}
						});
			}
		});

		lv_myfriend.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				followshipAPI.getFollowingList(page_myfriend + 1, user.getId(),
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								boolean canLoadMore = true;
								debug(obj.toString());
								List<User> newData = Following
										.getUsesList(Following
												.create_by_jsonarray(obj
														.toString()));
								if (newData == null) {
									toast("网络异常 解析错误");
								} else if (newData.size() == 0) {
									toast("没有更多");
									canLoadMore = false;
								} else {
									page_myfriend++;
									data_myfriend.addAll(newData);
									adapter_myfriend.notifyDataSetChanged();
								}
								lv_myfriend.onLoadMoreComplete();
								if (!canLoadMore)
									lv_myfriend.setCanLoadMore(false);
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast("网络异常 错误码:" + errorCode);
								lv_myfriend.onLoadMoreComplete();
							}
						});

			}
		});
		lv_allmember.setOnItemClickListener(this);
		lv_myfriend.setOnItemClickListener(this);
	}

	@Override
	protected void initData() {
		user = AppInfo.getUser(getContext());
		if (user == null) {
			toast("无用户信息,请重新登录!");
			closeActivity();
		}
		dp = new DataPool(this);
		followshipAPI = new FollowshipAPI();
		api = new UserAPI();
		data_allmember = new ArrayList<User>();
		//loadAllMemberData(data_allmember);
		data_myfriend = new ArrayList<User>();
		// loadMyFriendData(data_myfriend);
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

		adapter_allmember = new MemberAdapter(data_allmember, getContext());
		adapter_myfriend = new MemberAdapter(data_myfriend, getContext());

		lv_allmember.setAdapter(adapter_allmember);
		lv_myfriend.setAdapter(adapter_myfriend);
		initMenuDialog();
	}

	private void initMenuDialog() {
		dialog = new MenuDialog(getContext());
		List<String> strings = new ArrayList<String>();
		strings.add("关注");
		strings.add("进入空间");
		dialog.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				dialog.dismiss();
				if (position == 0) {
					FollowshipAPI api = new FollowshipAPI();
					api.follow(data_allmember.get(dialog.getParentPosition())
							.getId(), "following", new JsonResponseHandler() {

						@Override
						public void onOK(Header[] headers, JSONObject obj) {
							toast("已关注");
						}

						@Override
						public void onFaild(int errorType, int errorCode) {
							toast("关注失败,错误码:" + errorCode);
						}
					});
				}
				if (position == 1) {
					Intent intent =new Intent(Allmember.this,SpaceOther.class);
					intent.putExtra("user", data_allmember.get(dialog.getParentPosition()));
					openActivity(intent);
				}
			}
		});
		dialog.setData(strings);
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

	/**
	 * 保存缓存数据进sharepreferenses 中,最多保存20 条记录
	 */
	private void saveAllMemberData2SP(List<User> users) {
		if (users == null || users.size() == 0) {
			return;
		}
		for (int i = 1; i <= 20 && users.size() >= i
				&& users.get(i - 1) != null; i++) {
			dp.remove(DataPool.SP_KEY_ALLMEMBER + i);
			dp.put(DataPool.SP_KEY_ALLMEMBER + i, users.get(i - 1));
		}
	}

	/**
	 * 加载缓存数据进users 中,最多20 条记录
	 */
	private void loadAllMemberData(List<User> users) {
		if (users == null) {
			return;
		}
		for (int i = 1; i <= 20; i++) {
			User user = (User) dp.get(DataPool.SP_KEY_ALLMEMBER + i);
			if (user != null) {
				users.add(user);
			}
		}
	}

	/**
	 * 保存缓存数据进sharepreferenses 中,最多保存20 条记录
	 */
	private void saveMyFriendData2SP(List<User> users) {
		if (users == null || users.size() == 0) {
			return;
		}
		for (int i = 1; i <= 20 && users.size() >= i
				&& users.get(i - 1) != null; i++) {
			dp.remove(DataPool.SP_KEY_FRIEND_MEMBER + i);
			dp.put(DataPool.SP_KEY_FRIEND_MEMBER + i, users.get(i - 1));
		}
	}

	/**
	 * 加载缓存数据进users 中,最多20 条记录
	 */
	private void loadMyFriendData(List<User> users) {
		if (users == null) {
			return;
		}
		for (int i = 1; i <= 20; i++) {
			User user = (User) dp.get(DataPool.SP_KEY_FRIEND_MEMBER + i);
			if (user != null) {
				users.add(user);
			}
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

	/**
	 * 单击每一个item 进入空间，传入一个user 对象，代表要进入谁的空间
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == lv_allmember) {
			dialog.show(position - 1);
		}
		if (parent == lv_myfriend) {
			Intent intent = new Intent(this, SpaceOther.class);
			intent.putExtra("user", data_myfriend.get(position - 1));
			openActivity(intent);
		}
	}
}
