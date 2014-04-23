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
import com.alumnigroup.adapter.IssueAdapter;
import com.alumnigroup.adapter.MyOnPageChangeListener;
import com.alumnigroup.api.IssuesAPI;
import com.alumnigroup.api.StarAPI;
import com.alumnigroup.app.AppCache;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.app.SyncData;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.Issue;
import com.alumnigroup.entity.Starring;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.widget.XListView;
import com.alumnigroup.widget.XListView.IXListViewListener;
import com.astuetz.PagerSlidingTabStrip;

/**
 * 校友交流
 * 
 * @author Jayin Ton
 * 
 */
public class Communication extends BaseActivity implements OnItemClickListener {
	PagerSlidingTabStrip tabs;
	private String[] titles ;
	private XListView lv_all, lv_my, lv_favourit;
	private ViewPager viewpager;
	private ArrayList<Issue> data_all, data_my, data_favourite;
	private IssueAdapter adapter_all, adapter_my, adapter_favourite;
	private int page_all = 0, page_my = 0, page_favourit = 0;
	private IssuesAPI api;
	private StarAPI starAPI;
	private User user;
	private BroadcastReceiver mReceiver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_communication);
		initData();
		initLayout();
		initController();
		openReceiver();
	}

	// 修改话题分享 刷新UI
	private void openReceiver() {
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(Constants.Action_Issue_Edit)) {
					Issue issue = (Issue) intent.getSerializableExtra("issue");
					SyncData.updateEditIssue(data_all, adapter_all, issue);
					SyncData.updateEditIssue(data_favourite, adapter_favourite, issue);
					SyncData.updateEditIssue(data_my, adapter_my, issue);
				}else if(intent.getAction().equals(Constants.Action_Issue_delete)){
					Issue deleteItem = (Issue)intent.getSerializableExtra("issue");
					SyncData.updateDeleteIssue(data_all, adapter_all, deleteItem);
					SyncData.updateDeleteIssue(data_my, adapter_my, deleteItem);
					SyncData.updateDeleteIssue(data_favourite, adapter_favourite, deleteItem);
				}else if(intent.getAction().equals(Constants.Action_Issue_unFavourite)){
					//un fav
					Issue deleteItem = (Issue)intent.getSerializableExtra("issue");
					SyncData.updateDeleteIssue(data_favourite, adapter_favourite, deleteItem);
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action_Issue_Edit);
		filter.addAction(Constants.Action_Issue_delete);
		filter.addAction(Constants.Action_Issue_unFavourite);
		registerReceiver(mReceiver, filter);
	}
	
	private void initController() {
		lv_all.setPullRefreshEnable(true);
		lv_all.setPullLoadEnable(true);
		lv_my.setPullRefreshEnable(true);
		lv_my.setPullLoadEnable(true);
		lv_favourit.setPullRefreshEnable(true);
		lv_favourit.setPullLoadEnable(true);
		lv_all.setXListViewListener(new IXListViewListener() {

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
							toast("还没有任何讨论");
							lv_all.setPullLoadEnable(false);
						} else {
							page_all = 1;
							data_all.clear();
							data_all.addAll(newData_all);
							adapter_all.notifyDataSetChanged();
							if (data_all.size() < 10) {
								lv_all.setPullLoadEnable(false);
							} else {
								lv_all.setPullLoadEnable(true);
							}
							AppCache.setCommunicationAll(getContext(), data_all);
						}
						lv_all.stopRefresh();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast(ErrorCode.errorList.get(errorCode));
						lv_all.stopRefresh();
					}
				});

			}

			@Override
			public void onLoadMore() {
				if (page_all == 0) { // 因为网络原因没有加载到第一页
					lv_all.startRefresh();
					lv_all.stopLoadMore();
					return;
				}
				api.getIssueList(page_all + 1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<Issue> newData_all = Issue.create_by_jsonarray(obj
								.toString());
						if (newData_all == null) {
							toast("网络异常,解析错误");
						} else if (newData_all.size() == 0) {
							toast("没有更多");
							lv_all.setPullLoadEnable(false);
						} else {
							page_all++;
							data_all.addAll(newData_all);
							adapter_all.notifyDataSetChanged();
						}
						lv_all.stopLoadMore();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast(ErrorCode.errorList.get(errorCode));
						lv_all.stopLoadMore();
					}
				});

			}
		});

		lv_my.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				api.getMyIssueList(1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<Issue> newData_my = Issue.create_by_jsonarray(obj
								.toString());
						if (newData_my == null) {
							toast("网络异常 解析错误");
						} else if (newData_my.size() == 0) {
							toast("还没有参与任何讨论");
							lv_my.setPullLoadEnable(false);
						} else {
							page_my = 1;
							data_my.clear();
							data_my.addAll(newData_my);
							adapter_my.notifyDataSetChanged();
							if (data_my.size() < 10) {
								lv_my.setPullLoadEnable(false);
							} else {
								lv_my.setPullLoadEnable(true);
							}
							AppCache.setCommunicationMy(getContext(), data_my);
						}
						lv_my.stopRefresh();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast(ErrorCode.errorList.get(errorCode));
						lv_my.stopRefresh();
					}
				});
			}

			@Override
			public void onLoadMore() {
				if (page_my == 0) { // 因为网络原因没有加载到第一页
					lv_my.startRefresh();
					lv_my.stopLoadMore();
					return;
				}
				api.getMyIssueList(page_my + 1, new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						List<Issue> newData_my = Issue.create_by_jsonarray(obj
								.toString());
						if (newData_my == null) {
							toast("网络异常,解析错误");
						} else if (newData_my.size() == 0) {
							toast("没有更多");
							lv_my.setPullLoadEnable(false);
						} else {
							page_my++;
							data_my.addAll(newData_my);
							adapter_my.notifyDataSetChanged();
						}
						lv_my.stopLoadMore();
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast(ErrorCode.errorList.get(errorCode));
						lv_my.stopLoadMore();
					}
				});
			}
		});

		lv_favourit.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				starAPI.getMyStarList(1, StarAPI.Item_type_issue,
						new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<Starring> stars = Starring
										.create_by_jsonarray(obj.toString());
								List<Issue> newData_faviour = new ArrayList<Issue>();
								if (stars == null) {
									toast("网络异常，解析错误");
								} else {
									for (Starring s : stars) {
										 if(s.getItem() !=null)newData_faviour.add((Issue) s.getItem());
									}
									if (newData_faviour.size() == 0) {
										toast("还没有收藏任何话题");
										lv_favourit.setPullLoadEnable(false);
										data_favourite.clear();
									} else {
										page_favourit = 1;
										data_favourite.clear();
										data_favourite.addAll(newData_faviour);
										adapter_favourite
												.notifyDataSetChanged();
										if (data_favourite.size() < 10) {
											lv_favourit
													.setPullLoadEnable(false);
										} else {
											lv_favourit.setPullLoadEnable(true);
										}
										AppCache.setCommunicationFavourite(
												getContext(), data_favourite);
									}
								}
								lv_favourit.stopRefresh();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast(ErrorCode.errorList.get(errorCode));
								lv_favourit.stopRefresh();
							}
						});
			}

			@Override
			public void onLoadMore() {
				if (page_favourit == 0) {
					lv_favourit.stopLoadMore();
					lv_favourit.startRefresh();
					return;
				}
				starAPI.getMyStarList(page_favourit + 1,
						StarAPI.Item_type_issue, new JsonResponseHandler() {

							@Override
							public void onOK(Header[] headers, JSONObject obj) {
								List<Starring> stars = Starring
										.create_by_jsonarray(obj.toString());
								List<Issue> newData_faviour = new ArrayList<Issue>();
								if (stars == null) {
									toast("网络异常，解析错误");
								} else {
									for (Starring s : stars) {
										 if(s==null){
											 System.out.println("s is null!!");
											 continue;
										 }
										if(s.getItem()!=null)newData_faviour.add((Issue) s.getItem());
									}
									if (newData_faviour.size() == 0) {
										toast("没有更多");
										lv_favourit.setPullLoadEnable(false);
									} else {
										page_favourit++;
										data_favourite.addAll(newData_faviour);
										adapter_favourite
												.notifyDataSetChanged();
									}
								}
								lv_favourit.stopLoadMore();
							}

							@Override
							public void onFaild(int errorType, int errorCode) {
								toast(ErrorCode.errorList.get(errorCode));
								lv_favourit.stopLoadMore();
							}
						});
			}
		});
		lv_all.setOnItemClickListener(this);
		lv_my.setOnItemClickListener(this);
		lv_favourit.setOnItemClickListener(this);

		lv_all.startRefresh();
	}

	@Override
	protected void initData() {
		titles = getResources().getStringArray(R.array.title_allmember);
		user = AppInfo.getUser(getContext());
		if (user == null) {
			toast("无用户信息,请重新登录");
			closeActivity();
		}
		api = new IssuesAPI();
		starAPI = new StarAPI();

		if (AppCache.getCommunicationAll(getContext()) != null) {
			data_all = AppCache.getCommunicationAll(getContext());
		} else {
			data_all = new ArrayList<Issue>();
		}
		if (AppCache.getCommunicationMy(getContext()) != null) {
			data_my = AppCache.getCommunicationMy(getContext());
		} else {
			data_my = new ArrayList<Issue>();
		}
		if (AppCache.getCommunicationFavourite(getContext()) != null) {
			data_favourite = AppCache.getCommunicationFavourite(getContext());
		} else {
			data_favourite = new ArrayList<Issue>();
		}

	}

	private void initViewPager() {
		tabs = (PagerSlidingTabStrip)_getView(R.id.tabs);
		viewpager = (ViewPager) _getView(R.id.acty_comunication_content);
		View all = getLayoutInflater().inflate(
				R.layout.frame_acty_communication_all, null);
		View myjoin = getLayoutInflater().inflate(
				R.layout.frame_acty_communication_join, null);
		View favourit = getLayoutInflater().inflate(
				R.layout.frame_acty_communication_favourite, null);
		lv_all = (XListView) all
				.findViewById(R.id.frame_acty_communication_all_listview);
		lv_my = (XListView) myjoin
				.findViewById(R.id.frame_acty_communication_join_listview);
		lv_favourit = (XListView) favourit
				.findViewById(R.id.frame_acty_communication_favourite_listview);

		adapter_all = new IssueAdapter(getContext(), data_all);
		adapter_my = new IssueAdapter(getContext(), data_my);
		adapter_favourite = new IssueAdapter(getContext(), data_favourite);

		lv_all.setAdapter(adapter_all);
		lv_my.setAdapter(adapter_my);
		lv_favourit.setAdapter(adapter_favourite);

		List<View> views = new ArrayList<View>();
		views.add(all);
		views.add(myjoin);
		views.add(favourit);

		List<XListView> listviews = new ArrayList<XListView>();
		listviews.add(lv_all);
		listviews.add(lv_my);
		listviews.add(lv_favourit);

		viewpager.setAdapter(new BaseViewPagerAdapter(views,titles));
		tabs.setViewPager(viewpager);
		tabs.setOnPageChangeListener(new MyOnPageChangeListener(listviews));
	}

	@Override
	protected void initLayout() {
		_getView(R.id.acty_head_btn_back).setOnClickListener(this);
		_getView(R.id.acty_head_btn_post).setOnClickListener(this);

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
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position - 1 == -1)
			return;
		Intent intent = new Intent(this, CommunicationDetail.class);
		if (parent == lv_all) {
			intent.putExtra("issue", data_all.get(position - 1));
		}
		if (parent == lv_my) {
			intent.putExtra("issue", data_my.get(position - 1));
		}
		if (parent == lv_favourit) {
			intent.putExtra("issue", data_favourite.get(position - 1));
			intent.putExtra("isFavourite", 1);
		}
		openActivity(intent);
	}

}

 
