package com.alumnigroup.app;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
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

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				L.i("CoreService-->onReceive-->" + intent.getAction());
				if (intent != null && context != null) {
					intent.setClass(context, CoreService.class);
					startService(intent);
				}

			};
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action_To_Get_Unread);// 去获取未读消息数目
		registerReceiver(mReceiver, filter);
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
				int unreadCount = MessageCache
						.getUnreadCount(getApplicationContext());
				MessageCache.setUnreadCount(getApplicationContext(),
						unreadCount + count);
				Intent intent = new Intent(Constants.Action_Receive_UnreadCount);
				intent.putExtra("count", mCount);
				sendBroadcast(intent);
				L.i("getUnread-->" + count);
				// if (count > 0) {
				// api.getReceiedMessageList(count, new JsonResponseHandler() {
				//
				// @Override
				// public void onOK(Header[] headers, JSONObject obj) {
				// ArrayList<MMessage> new_messages = MMessage
				// .create_by_jsonarray(obj.toString());
				// ArrayList<MMessage> cache_messages=
				// MessageCache.getReceiveMessages(getApplicationContext());
				// cache_messages.addAll(new_messages);
				// MessageCache.setReceiveMessages(getApplicationContext(),
				// cache_messages);
				//
				// }
				//
				// @Override
				// public void onFaild(int errorType, int errorCode) {
				// L.e("获取消息错误," + ErrorCode.errorList.get(errorCode));
				// }
				// });
				// }
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
		String userid = AppInfo.getUserID(getApplicationContext());
		String psw = AppInfo.getUserPSW(getApplicationContext());
		UserAPI api = new UserAPI();
		api.login(userid, psw, new JsonResponseHandler() {

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
                  L.i("login successfully");
			}

			@Override
			public void onFaild(int errorType, int errorCode) {
				L.e(ErrorCode.errorList.get(errorCode));
			}
		});
	}

}
