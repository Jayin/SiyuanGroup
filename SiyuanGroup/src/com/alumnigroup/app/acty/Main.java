package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.CoreService;
import com.alumnigroup.app.R;
import com.alumnigroup.app.fragment.MainAll;
import com.alumnigroup.app.fragment.MessageCenter;
import com.alumnigroup.app.fragment.Mine;
import com.alumnigroup.utils.Constants;

/**
 * 主界面
 * 
 * @author Jayin Ton
 * @since 2013.12.11;
 * 
 */
public class Main extends BaseActivity implements OnClickListener {
	private BroadcastReceiver mRecevier;
	Bitmap backgroudBitmap = null;
	ViewPager viewpager;
	List<Fragment> fragments = new ArrayList<Fragment>();
	View btn_main, btn_message, btn_mine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_main);
		initData();
		initLayout();
		checkVerison();
		startPolling();
		openReceiver();
		initActionBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_settings) {
			openActivity(Setting.class);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initActionBar() {
		ActionBar mActionBar = getActionBar();
		mActionBar.setIcon(R.drawable.icon);
		// mActionBar.setSubtitle(R.string.subtitle);
	}

	private void startPolling() {
		Intent service = new Intent(getContext(), CoreService.class);
		service.setAction(Constants.Action_Start_Receive_UnreadCount);
		startService(service);
	}

	// 接收到
	private void openReceiver() {
		mRecevier = new MainRecevier();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action_Receive_UnreadCount);// 有未读消息
		filter.addAction(Constants.Action_Backgroud_switch);// 背景图切换
		filter.addAction(Constants.Action_User_Login_Out);// 用户登出
		registerReceiver(mRecevier, filter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mRecevier != null) {
			unregisterReceiver(mRecevier);
		}
		if (backgroudBitmap != null)
			backgroudBitmap.recycle();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// onUnreadChange(MessageCache.getUnreadCount(getContext()));
	}

	private void checkVerison() {
		Intent service = new Intent(getContext(), CoreService.class);
		service.setAction(Constants.Action_checkVersion);
		startService(service);
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initLayout() {
		// getSupportFragmentManager().beginTransaction()
		// .replace(R.id.container, new MainAll(), "MainAll").commit();
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		fragments.add(new MainAll());
		fragments.add(new MessageCenter());
		fragments.add(new Mine());
		viewpager.setAdapter(new MainFragmentPagerAdapter(
				getSupportFragmentManager(), fragments));

		btn_main = _getView(R.id.btn_main);
		btn_message = _getView(R.id.btn_message);
		btn_mine = _getView(R.id.btn_mine);

		btn_main.setOnClickListener(this);
		btn_message.setOnClickListener(this);
		btn_mine.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_main:
			viewpager.setCurrentItem(0,true);
			break;
		case R.id.btn_message:
			viewpager.setCurrentItem(1,true);
			break;
		case R.id.btn_mine:
			viewpager.setCurrentItem(2,true);
			break;

		default:
			break;
		}
	}

	class MainRecevier extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constants.Action_Receive_UnreadCount)) {// got
				// onUnreadChange(MessageCache.getUnreadCount(getContext()));
			} else if (intent.getAction().equals(
					Constants.Action_Backgroud_switch)) {
				// if (AppInfo.getBackgroudPath(getContext()) != null) {
				// iv_backgroud
				// .setImageBitmap(BitmapFactory.decodeFile(AppInfo
				// .getBackgroudPath(getContext())));
				// }
			} else if (intent.getAction().equals(
					Constants.Action_User_Login_Out)) {
				closeActivity();
			}
		}
	}

	class MainFragmentPagerAdapter extends FragmentStatePagerAdapter {
		private List<Fragment> fragments;

		public MainFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public MainFragmentPagerAdapter(FragmentManager fm,
				List<Fragment> fragments) {
			this(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

	}
}
