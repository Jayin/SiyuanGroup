package com.alumnigroup.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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
		return Service.START_REDELIVER_INTENT;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
