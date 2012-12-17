package com.yixia.zi.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceHelper {
	public static boolean isServiceRunning(Context ctx, String name) {
		ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (name.equals(service.service.getClassName()))
				return true;
		}
		return false;
	}
}
