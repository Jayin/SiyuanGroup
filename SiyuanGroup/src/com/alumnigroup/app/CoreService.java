package com.alumnigroup.app;

import java.util.ArrayList;

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
import com.alumnigroup.api.VersionAPI;
import com.alumnigroup.app.acty.AppUpdate;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.MMessage;
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
				startService(intent);
			};
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action_Get_Unread);// 获取未读消息
		registerReceiver(mReceiver, filter);
	}

	// 任务分发
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			String action = intent.getAction();
			if (Constants.Action_checkVersion.equals(action)) {
				checkVersion();// 版本检测
			} else if (Constants.Action_Get_Unread.equals(action)) {
				getUnread(); // 获取未读消息
			}
		}
		return Service.START_STICKY;
	}

	protected void getUnread() {
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
				if (count > 0) {
					api.getReceiedMessageList(count, new JsonResponseHandler() {

						@Override
						public void onOK(Header[] headers, JSONObject obj) {
							ArrayList<MMessage> new_messages = MMessage
									.create_by_jsonarray(obj.toString());
							ArrayList<MMessage> cache_messages= AppCache.getReceiveMessages(getApplicationContext());
							cache_messages.addAll(new_messages);
							AppCache.setReceiveMessages(getApplicationContext(), cache_messages);
							Intent intent = new Intent(Constants.Action_Get_Unread);
							intent.putExtra("count", mCount);
							sendBroadcast(intent);
						}

						@Override
						public void onFaild(int errorType, int errorCode) {
							L.e("获取消息错误," + ErrorCode.errorList.get(errorCode));
						}
					});
				}
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

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
