package com.alumnigroup.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.alumnigroup.app.acty.Login;
import com.alumnigroup.app.acty.Main;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.utils.DataPool;
import com.alumnigroup.utils.L;

/**
 * 启动页面
 * 
 * @author Jayin Ton
 * 
 */
public class AppStart extends BaseActivity {
    private long starttime ;
    private long waittime = 1500;
	private BroadcastReceiver mReceiver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_start);
		initReceiver();// 先后顺序不能变
		init();
		starttime = System.currentTimeMillis();
	}

	private void initReceiver() {
		mReceiver = new AppStartReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action_Login_In_Successful);
		filter.addAction(Constants.Action_Login_In_Faild);
		registerReceiver(mReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mReceiver != null)
			unregisterReceiver(mReceiver);
	}

	// 初始化工作
	protected void init() {
		// start up the core service
		Intent service = new Intent(getContext(), CoreService.class);
		service.setAction(Constants.Action_To_Login_In);
		startService(service);
	}

	private boolean checkLoginInfo() {
		DataPool dp = new DataPool(DataPool.SP_Name_User, this);
		if (dp.contains(DataPool.SP_Key_User))
			return true;
		else
			return false;
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initLayout() {

	}

	class AppStartReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constants.Action_Login_In_Successful)) {
				L.i("AppStartReceiver-->Login Successfully");
			} else {
//				toast("请检查你的网络");
				L.i("AppStartReceiver-->Login faild");
			}
			long lasttime = System.currentTimeMillis() - starttime;
			if(waittime>lasttime){
				try {
					Thread.sleep(waittime-lasttime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (checkLoginInfo()) {
				openActivity(Main.class);
			} else {
				openActivity(Login.class);
			}
			closeActivity();
		}
	}

}
