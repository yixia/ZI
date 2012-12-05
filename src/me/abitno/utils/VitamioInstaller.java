package me.abitno.utils;

/*
 * Copyright (C) 2011 VOV IO (http://vov.io/)
 */

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import me.abitno.utils.Log;

/**
 * Copy from VitamioLibrary/src/io/vov/vitamio/VitamioInstaller.java
 * 
 */
public class VitamioInstaller {

	public static String getVitamioInfo(Context ctx) {
		StringBuilder sb = new StringBuilder();
		try {
			ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_SHARED_LIBRARY_FILES);
			sb.append(getPair("vitamio_pkg", ai.packageName));
			PackageInfo pi = ctx.getPackageManager().getPackageInfo(ai.packageName, 0);
			sb.append(getPair("vitamio_version_name", pi.versionName));
			sb.append(getPair("vitamio_version_code", "" + pi.versionCode));
			sb.append(getPair("vitamio_first_install_time", "" + pi.firstInstallTime));
			sb.append(getPair("vitamio_last_update_time", "" + pi.lastUpdateTime));
			sb.append(getPair("vitamio_library_path", getLibraryPath(ctx.getPackageName())));
		} catch (Exception e) {
			Log.e("getVitamioInfo[" + ctx.toString() + "]", e);
		}
		return sb.toString();
	}

	/**
	 * Get the absolute path to Vitamio's library
	 * 
	 * @param ctx a Context
	 * @return the absolute path to the libffmpeg.so
	 * @throws VitamioNotCompatibleException
	 * @throws VitamioNotFoundException
	 */
	public static final String getLibraryPath(String packageName) {
		return "/data/data/" + packageName + "/libs/";
	}

	private static String getPair(String key, String value) {
		return "&" + key + "=" + value;
	}
}
