package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alumnigroup.adapter.CommunicationAdapter;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Issue;

/**
 * 话题页面
 * @author vector
 *
 */
public class Communication extends BaseActivity implements OnItemClickListener {

	/**
	 * header
	 */
	private View btnBack, btnRelease;
	
	/**
	 * content
	 */
	private ViewPager vpIssueContent;
	private IssueViewPagerAdapter aptIssueContent;
	private ArrayList<View> alIssueContentView;
	private ListView lvAllIssue,lvJoinIssue,lvEnshrineIssue;
	private CommunicationAdapter aptAllIssue,aptJoinIssue,aptEnshrineIssue;
	private LayoutInflater inflater;
	private ArrayList<Issue> alCommunications;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_communication);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		alCommunications = new ArrayList<Issue>(){{
			add(new Issue());
			add(new Issue());
			add(new Issue());
			add(new Issue());
			add(new Issue());
			add(new Issue());
			add(new Issue());
			add(new Issue());
		}};
		
		alIssueContentView = new ArrayList<View>();
		
		inflater = LayoutInflater.from(this);
	}

	@Override
	protected void initLayout() {
		/**
		 * header
		 */
		btnBack = _getView(R.id.acty_head_btn_back);
		btnBack.setOnClickListener(this);

		btnRelease = _getView(R.id.acty_head_btn_release);
		btnRelease.setOnClickListener(this);
		
		/**
		 * content
		 */
		vpIssueContent = (ViewPager) _getView(R.id.acty_comunication_vp_content);
		
		/**
		 * 初始化listview
		 */
		lvAllIssue = (ListView) inflater.inflate(R.layout.frame_acty_communication_viewpager_lv, null);
		aptAllIssue = new CommunicationAdapter(alCommunications, this);
		lvAllIssue.setAdapter(aptAllIssue);
		lvAllIssue.setOnItemClickListener(this);
		
		lvJoinIssue = (ListView) inflater.inflate(R.layout.frame_acty_communication_viewpager_lv, null);
		aptJoinIssue = new CommunicationAdapter(alCommunications, this);
		lvJoinIssue.setAdapter(aptJoinIssue);
		
		lvEnshrineIssue = (ListView) inflater.inflate(R.layout.frame_acty_communication_viewpager_lv, null);
		aptEnshrineIssue = new CommunicationAdapter(alCommunications, this);
		lvEnshrineIssue.setAdapter(aptEnshrineIssue);
		
		/**
		 * 加入ViewPager
		 */
		alIssueContentView.add(lvAllIssue);
		alIssueContentView.add(lvJoinIssue);
		alIssueContentView.add(lvEnshrineIssue);
		aptIssueContent = new IssueViewPagerAdapter(alIssueContentView);
		vpIssueContent.setAdapter(aptIssueContent);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		Intent intent = new Intent();

		switch (id) {
		case R.id.acty_head_btn_back:
			finish();
			break;

		case R.id.acty_head_btn_release:
			intent.setClass(Communication.this, CommunicationPublish.class);
			startActivity(intent);
			break;

		default:
			break;
		}

	}
	
	
	/**
	 * ViewPager 的适配器
	 * 
	 * @author vector
	 * 
	 */
	class IssueViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public IssueViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;// 构造方法，参数是我们的页卡，这样比较方便。
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));// 删除页卡
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
			container.addView(mListViews.get(position), 0);// 添加页卡
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();// 返回页卡的数量
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// 官方提示这样写
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		/**
		 * 传issue 对象过去
		 */
		Intent intent = new Intent(Communication.this,CommunicationDetail.class);
		startActivity(intent);
	}

}