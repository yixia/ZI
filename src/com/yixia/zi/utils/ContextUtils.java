package com.yixia.zi.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

public class ContextUtils {
	public static int getVersionCode(Context ctx) {
		int version = 0;
		try {
			version = ctx.getPackageManager().getPackageInfo(ctx.getApplicationInfo().packageName, 0).versionCode;
		} catch (Exception e) {
			Log.e("getVersionInt", e);
		}
		return version;
	}

	public static String getDataDir(Context ctx) {
		ApplicationInfo ai = ctx.getApplicationInfo();
		if (ai.dataDir != null)
			return StringUtils.fixLastSlash(ai.dataDir);
		else
			return "/data/data/" + ai.packageName + "/";
	}
}
