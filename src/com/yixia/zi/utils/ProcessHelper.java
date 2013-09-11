package com.yixia.zi.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

public class ProcessHelper {
	public static boolean isProcessRunning(Context ctx, String name) {
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> apps = am.getRunningAppProcesses();
		for (RunningAppProcessInfo app : apps) {
			if (app.processName.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isTopActivity(Context ctx) {
		ActivityManager am = (ActivityManager) ctx.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
		if (list != null && list.isEmpty())
			return false;
		for (ActivityManager.RunningAppProcessInfo process : list) {
			if (process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && process.processName.equals(ctx.getPackageName())) {
				return true;
			}
		}
		return false;
	}
}
