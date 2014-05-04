package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.PendingIntent;
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
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.CoreService;
import com.alumnigroup.app.MessageCache;
import com.alumnigroup.app.R;
import com.alumnigroup.app.fragment.MainAll;
import com.alumnigroup.app.fragment.MessageCenter;
import com.alumnigroup.app.fragment.Mine;
import com.alumnigroup.utils.BaseNotification;
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
	ImageView iv_new, iv_message, iv_my, iv_main;

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
		updateUnreadChange(MessageCache.getUnreadCount(getContext()));
	}

	private void updateUnreadChange(int unreadCount) {
		if (unreadCount > 0) {
			iv_new.setVisibility(View.VISIBLE);
		} else {
			iv_new.setVisibility(View.INVISIBLE);
		}
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
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		fragments.add(new MainAll());
		fragments.add(new MessageCenter());
		fragments.add(new Mine());
		viewpager.setAdapter(new MainFragmentPagerAdapter(
				getSupportFragmentManager(), fragments));
		viewpager.setOnPageChangeListener(new MyPageChangeLinster());

		btn_main = _getView(R.id.btn_main);
		btn_message = _getView(R.id.btn_message);
		btn_mine = _getView(R.id.btn_mine);

		iv_message = (ImageView) _getView(R.id.iv_message);
		iv_my = (ImageView) _getView(R.id.iv_my);
		iv_main = (ImageView) _getView(R.id.iv_main);
		iv_new = (ImageView) _getView(R.id.iv_new);
		iv_main.setBackgroundResource(R.drawable.ic_img_discover_pressed);

		btn_main.setOnClickListener(this);
		btn_message.setOnClickListener(this);
		btn_mine.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_main:
			viewpager.setCurrentItem(0, true);
			changeSelected(0);
			break;
		case R.id.btn_message:
			viewpager.setCurrentItem(1, true);
			changeSelected(1);
			break;
		case R.id.btn_mine:
			viewpager.setCurrentItem(2, true);
			changeSelected(2);
			break;
		default:
			break;
		}
	}

	private void changeSelected(int selected) {
		switch (selected) {
		case 0:
			iv_main.setBackgroundResource(R.drawable.ic_img_discover_pressed);
			iv_message.setBackgroundResource(R.drawable.ic_img_message_namal);
			iv_my.setBackgroundResource(R.drawable.ic_img_my_namal);
			break;
		case 1:
			iv_main.setBackgroundResource(R.drawable.ic_img_discover_namal);
			iv_message.setBackgroundResource(R.drawable.ic_img_message_pressed);
			iv_my.setBackgroundResource(R.drawable.ic_img_my_namal);
			break;
		case 2:
			iv_main.setBackgroundResource(R.drawable.ic_img_discover_namal);
			iv_message.setBackgroundResource(R.drawable.ic_img_message_namal);
			iv_my.setBackgroundResource(R.drawable.ic_img_my_pressed);
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
				updateUnreadChange(MessageCache.getUnreadCount(getContext()));
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
	
	class MyPageChangeLinster implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int position) {
			changeSelected(position);
		}
		
	}
}
