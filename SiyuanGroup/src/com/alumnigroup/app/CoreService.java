package com.alumnigroup.app;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.IBinder;

import com.alumnigroup.api.MessageAPI;
import com.alumnigroup.api.UserAPI;
import com.alumnigroup.api.VersionAPI;
import com.alumnigroup.app.acty.AppUpdate;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.AndroidUtils;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.utils.L;

/**
 * 后台服务
 * 
 * @author Jayin Ton
 * 
 */
public class CoreService extends Service {
	private BroadcastReceiver mReceiver = null;
	public static final int Default_PollingTime = 60 * 1000;
	public boolean hasStartPolling = false;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initReceiver();
		startPolling();
	}
    //初始化广播
	private void initReceiver() {
		mReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				L.i("CoreService::onReceive-->" + intent.getAction());
				if (intent != null && context != null) {
					intent.setClass(context, CoreService.class);
					
					//wrap the intent
					//网络状态发生改变
					if(intent.getAction()!=null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
						   if(AndroidUtils.isNetworkConnected(getApplicationContext())){ 
 							   //登录
							   intent.setAction(Constants.Action_To_Login_In);
						   }
					} 
					//start to work
					startService(intent);
				}
			};
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action_To_Get_Unread);// 去获取未读消息数目
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION); 	//网络状态发生改变
		registerReceiver(mReceiver, filter);
	}

	// 开始轮询
	private void startPolling() {
		hasStartPolling = true;
		AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
		Intent intent = new Intent(this, CoreService.class);
		intent.setAction(Constants.Action_To_Get_Unread);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
				0);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				Default_PollingTime, pendingIntent);
	}

	// 停止轮询
	private void stopPolling() {
		hasStartPolling = false;
		AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
		Intent intent = new Intent(this, CoreService.class);
		intent.setAction(Constants.Action_To_Get_Unread);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
				0);
		// am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
		// Default_PollingTime, pendingIntent);
		am.cancel(pendingIntent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
	}

	// 任务分发
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			String action = intent.getAction();
			Runnable runnable = null;
			L.i("onStartCommand-->" + action);
			if (Constants.Action_checkVersion.equals(action)) {
				runnable = new Runnable() {
					@Override
					public void run() {
						checkVersion();// 版本检测

					}
				};
			} else if (Constants.Action_To_Get_Unread.equals(action)) {
				runnable = new Runnable() {
					@Override
					public void run() {
						getUnread(); // 获取未读消息数目
					}
				};
			} else if (Constants.Action_Stop_Receive_UnreadCount.equals(action)) {
				runnable = new Runnable() {
					@Override
					public void run() {
						stopPolling(); // 停止取消息数目
					}
				};
			} else if (Constants.Action_Start_Receive_UnreadCount
					.equals(action)) {
				runnable = new Runnable() {
					@Override
					public void run() {
						if (!hasStartPolling)
							startPolling(); // 开始取消息数目
					}
				};
			}else if(Constants.Action_To_Login_In.equals(action)){
				runnable = new Runnable() {
					
					@Override
					public void run() {
					    login();   //登录
					}
				};
			}
			new Thread(runnable).start();
		}
		return Service.START_STICKY;
	}

	private void getUnread() {

		final MessageAPI api = new MessageAPI();
		api.getUnreadCount(new JsonResponseHandler() {

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				int count = 0;// count of unread message
				try {
					count = obj.getInt("count");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				final int mCount = count;
				// send broadcast after getting all messages
				MessageCache.setUnreadCount(getApplicationContext(), count);
				Intent intent = new Intent(Constants.Action_Receive_UnreadCount);
				intent.putExtra("count", mCount);
				sendBroadcast(intent);
			}

			@Override
			public void onFaild(int errorType, int errorCode) {
				L.e("获取消息错误," + ErrorCode.errorList.get(errorCode));
			}
		});
	}

	private void checkVersion() {
		VersionAPI api = new VersionAPI();
		api.getLatestInfo(new JsonResponseHandler() {

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				try {
					int versioncode = Integer.parseInt(obj
							.getString("versioncode"));
					int currentVersion = AndroidUtils
							.getAppVersionCode(getApplicationContext());
					String updateInfo = obj.getString("description");
					if (versioncode > currentVersion) {
						Intent intent = new Intent(getApplicationContext(),
								AppUpdate.class);
						intent.putExtra("updateInfo", updateInfo);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
					// 发送收到结果的广播
					Intent intent = new Intent(
							Constants.Action_Receive_VersionInfo);
					intent.putExtra("versioncode", versioncode);
					intent.putExtra("description", updateInfo);
					sendBroadcast(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFaild(int errorType, int errorCode) {

			}
		});
	}

	private void login() {
		String username = AppInfo.getUsername(getApplicationContext());
		String psw = AppInfo.getUserPSW(getApplicationContext());
		UserAPI api = new UserAPI();
		api.login(username, psw, new JsonResponseHandler() {

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				sendBroadcast(new Intent(Constants.Action_Login_In_Successful));
				L.i("login successfully");
			}

			@Override
			public void onFaild(int errorType, int errorCode) {
				sendBroadcast(new Intent(Constants.Action_Login_In_Faild));
				L.e(ErrorCode.errorList.get(errorCode));
			}
		});
	}

}
