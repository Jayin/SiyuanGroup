package com.alumnigroup.app;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.alumnigroup.api.VersionAPI;
import com.alumnigroup.app.acty.AppUpdate;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.AndroidUtils;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.utils.L;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.IBinder;

/**
 * 后台服务
 * 
 * @author Jayin Ton
 * 
 */
public class CoreService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			String action = intent.getAction();
			if (Constants.Action_checkVersion.equals(action)) {
				checkVersion();
			}
		}
		return Service.START_STICKY;
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
					//发送收到结果的广播
					Intent intent = new Intent(Constants.Action_Receive_VersionInfo);
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
