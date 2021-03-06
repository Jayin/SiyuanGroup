package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.adapter.MemberAdapter;
import com.alumnigroup.adapter.MyOnPageChangeListener;
import com.alumnigroup.api.FollowshipAPI;
import com.alumnigroup.api.UserAPI;
import com.alumnigroup.app.AppCache;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.app.SyncData;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.Follower;
import com.alumnigroup.entity.Following;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.widget.XListView;
import com.alumnigroup.widget.XListView.IXListViewListener;
import com.astuetz.PagerSlidingTabStrip;

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
	PagerSlidingTabStrip tabs;
	private String[] titles ;
	private XListView lv_allmember, lv_following, lv_followers;
	private ArrayList<User> data_allmember = null, data_following = null,
			data_followers = null;
	private ViewPager viewpager;
	private UserAPI api;
	private int page_allmember = 0, page_following = 0, page_followers = 0;
	private MemberAdapter adapter_allmember, adapter_following,
			adapter_followers;
	private User mUser;
	private FollowshipAPI followshipAPI;
	private BroadcastReceiver mReceiver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_allmember);
		initData();
		initLayout();
		initController();
		openReceiver();
	}

	private void openReceiver() {
		mReceiver  = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getAction().equals(Constants.Action_User_unfollow)){
					User user = (User)intent.getSerializableExtra("user");
					SyncData.updateUserDelete(data_following, adapter_following, user);
				}else if(intent.getAction().equals(Constants.Action_User_edit)){
					User user = (User)intent.getSerializableExtra("user");
					SyncData.updateUserEdit(data_allmember, adapter_allmember, user);
					SyncData.updateUserEdit(data_followers, adapter_followers, user);
					SyncData.updateUserEdit(data_following, adapter_following, user);
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action_User_unfollow);
		filter.addAction(Constants.Action_User_edit);
		registerReceiver(mReceiver, filter);
	}
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if(mReceiver != null){
    		unregisterReceiver(mReceiver);
    	}
    }
	private void initController() {
		lv_allmember.setPullLoadEnable(true);
		lv_allmember.setPullRefreshEnable(true);
		lv_following.setPullLoadEnable(true);
		lv_following.setPullRefreshEnable(true);
		lv_followers.setPullLoadEnable(true);
		lv_followers.setPullRefreshEnable(true);

		lv_allmember.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				api.getAllMember(1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<User> newData = User
								.create_by_jsonarray(obj.toString());
						if (newData == null) {
							toast("网络异常，解析错误");
						} else if (newData.size() == 0) {
							toast("还没有任何会员");
							lv_allmember.setPullLoadEnable(false);
						} else {
							page_allmember = 1;
							data_allmember.clear();
							data_allmember.addAll(newData);
							adapter_allmember.notifyDataSetChanged();
							AppCache.setAllmemberAll(getContext(),
									data_allmember);
							if(newData.size()<10){
								lv_allmember.setPullLoadEnable(false);
							}else{
								lv_allmember.setPullLoadEnable(true);
							}
						}
						lv_allmember.stopRefresh();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast(ErrorCode.errorList.get(errorCode));
						lv_allmember.stopRefresh();
					}
				});

			}

			@Override
			public void onLoadMore() {
				if (page_allmember == 0) {
					lv_allmember.stopLoadMore();
					lv_allmember.startRefresh();
					return;
				}
				api.getAllMember(page_allmember + 1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<User> newData_allmember = User
								.create_by_jsonarray(obj.toString());
						if (newData_allmember == null) {
							toast("网络异常，解析错误");
						} else if (newData_allmember.size() == 0) {
							toast("没有更多");
							lv_allmember.setPullLoadEnable(false);
						} else {
							page_allmember++;
							data_allmember.addAll(newData_allmember);
							adapter_allmember.notifyDataSetChanged();
						}
						lv_allmember.stopLoadMore();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast(ErrorCode.errorList.get(errorCode));
						lv_allmember.stopLoadMore();
					}
				});

			}
		});
		lv_following.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				followshipAPI.getFollowingList(1, mUser.getId(),
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
									lv_following.setPullLoadEnable(false);
								} else {
									page_following = 1;
									data_following.clear();
									data_following.addAll(newData);
									adapter_following.notifyDataSetChanged();
									AppCache.setAllmemberFollowing(
											getContext(), data_following);
									if(newData.size()<10){
										lv_following.setPullLoadEnable(false);
									}else{
										lv_following.setPullLoadEnable(true);
									}
								}
								lv_following.stopRefresh();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast(ErrorCode.errorList.get(errorCode));
								lv_following.stopRefresh();
							}
						});
			}

			@Override
			public void onLoadMore() {
				if (page_following == 0) {
					lv_following.stopLoadMore();
					lv_following.startRefresh();
					return;
				}
				followshipAPI.getFollowingList(page_following + 1,
						mUser.getId(), new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								debug(obj.toString());
								List<User> newData = Following
										.getUsesList(Following
												.create_by_jsonarray(obj
														.toString()));
								if (newData == null) {
									toast("网络异常 解析错误");
								} else if (newData.size() == 0) {
									toast("没有更多");
									lv_following.setPullLoadEnable(false);
								} else {
									page_following++;
									data_following.addAll(newData);
									adapter_following.notifyDataSetChanged();
								}
								lv_following.stopLoadMore();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast(ErrorCode.errorList.get(errorCode));
								lv_following.stopLoadMore();
							}
						});
			}
		});
		lv_followers.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				followshipAPI.getFollowerList(1, mUser.getId(),
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								debug(obj.toString());
								List<User> newData = Follower
										.getUsesList(Follower
												.create_by_jsonarray(obj
														.toString()));
								if (newData == null) {
									toast("网络异常 解析错误");debug("网络异常 解析错误");
								} else if (newData.size() == 0) {
									toast("还没有粉丝");
									lv_followers.setPullLoadEnable(false);
								} else {
									page_followers = 1;
									data_followers.clear();
									data_followers.addAll(newData);
									adapter_followers.notifyDataSetChanged();
									AppCache.setAllmemberFollowers(
											getContext(), data_followers);
									if(newData.size()<10){
										lv_followers.setPullLoadEnable(false);
									}else{
										lv_followers.setPullLoadEnable(true);
									}
								}
								// //好友应该加载一次就ok？
								lv_followers.stopRefresh();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast(ErrorCode.errorList.get(errorCode));
								lv_followers.stopRefresh();
							}
						});

			}

			@Override
			public void onLoadMore() {
				if (page_followers == 0) {
					lv_followers.stopLoadMore();
					lv_followers.startRefresh();
					return;
				}
				followshipAPI.getFollowerList(page_followers + 1, mUser.getId(),
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								debug(obj.toString());
								List<User> newData = Follower
										.getUsesList(Follower
												.create_by_jsonarray(obj
														.toString()));
								if (newData == null) {
									toast("网络异常 解析错误");
								} else if (newData.size() == 0) {
									toast("没有更多");
									lv_followers.setPullLoadEnable(false);
								} else {
									page_followers++;
									data_followers.addAll(newData);
									adapter_followers.notifyDataSetChanged();
								}
								lv_followers.stopLoadMore();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast(ErrorCode.errorList.get(errorCode));
								lv_followers.stopLoadMore();
							}
						});
			}
		});
		lv_allmember.setOnItemClickListener(this);
		lv_following.setOnItemClickListener(this);
		lv_followers.setOnItemClickListener(this);
		lv_allmember.startRefresh();
	}
	
	@Override
	protected void initData() {
		titles = getResources().getStringArray(R.array.title_allmember);
		mUser = AppInfo.getUser(getContext());
		if (mUser == null) {
			toast("无用户信息,请重新登录!");
			closeActivity();
		}
		followshipAPI = new FollowshipAPI();
		api = new UserAPI();
		if (AppCache.getAllmemberAll(getContext()) != null) {
			data_allmember = AppCache.getAllmemberAll(getContext());
		} else {
			data_allmember = new ArrayList<User>();
		}
		if (AppCache.getAllmemberFollowing(getContext()) != null) {
			data_following = AppCache.getAllmemberFollowing(getContext());
		} else {
			data_following = new ArrayList<User>();
		}
		if (AppCache.getAllmemberFollowers(getContext()) != null) {
			data_followers = AppCache.getAllmemberFollowers(getContext());
		} else {
			data_followers = new ArrayList<User>();
		}
	}

	@Override
	protected void initLayout() {
		_getView(R.id.acty_head_btn_back).setOnClickListener(this);
		initViewPager();
	}

	private void initViewPager() {
		tabs = (PagerSlidingTabStrip)_getView(R.id.tabs);
		viewpager = (ViewPager) _getView(R.id.acty_allmember_content);
		View allmember = getLayoutInflater().inflate(
				R.layout.frame_acty_allmember_allmember, null);
		View following = getLayoutInflater().inflate(
				R.layout.frame_acty_allmember_following, null);
		View followers = getLayoutInflater().inflate(
				R.layout.frame_acty_allmember_followers, null);

		lv_allmember = (XListView) allmember
				.findViewById(R.id.acty_allmember_lv_allmember);
		lv_following = (XListView) following
				.findViewById(R.id.acty_allmember_lv_following);
		lv_followers = (XListView) followers
				.findViewById(R.id.acty_allmember_lv_followers);

		adapter_allmember = new MemberAdapter(data_allmember, getContext());
		adapter_following = new MemberAdapter(data_following, getContext());
		adapter_followers = new MemberAdapter(data_followers, getContext());

		lv_allmember.setAdapter(adapter_allmember);
		lv_following.setAdapter(adapter_following);
		lv_followers.setAdapter(adapter_followers);

		List<View> views = new ArrayList<View>();
		views.add(allmember);
		views.add(following);
		views.add(followers);

		List<XListView> listviews = new ArrayList<XListView>();
		listviews.add(lv_allmember);
		listviews.add(lv_following);
		listviews.add(lv_followers);

		viewpager.setAdapter(new BaseViewPagerAdapter(views,titles));
		viewpager.setCurrentItem(0);
		tabs.setViewPager(viewpager);
		tabs.setOnPageChangeListener(new MyOnPageChangeListener(listviews));
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		default:
			break;
		}
	}

	/**
	 * 单击每一个item 进入空间，传入一个user 对象，代表要进入谁的空间
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = null;
		User user = null;
		if (parent == lv_allmember) {
			user = data_allmember.get(position - 1);
		}
		if (parent == lv_following) {
			user = data_following.get(position - 1);
		}
		if (parent == lv_followers) {
			user = data_followers.get(position - 1);

		}
		intent = user.getId() == AppInfo.getUser(getContext()).getId() ? new Intent(
				this, SpacePersonal.class) : new Intent(this, SpaceOther.class);
		intent.putExtra("user", user);
		openActivity(intent);
	}
}
