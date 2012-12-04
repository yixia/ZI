package me.abitno.utils;

/*
 * Copyright (C) 2011 VOV IO (http://vov.io/)
 */


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.AndroidException;

import me.abitno.utils.AndroidContextUtils;
import me.abitno.utils.CPU;
import me.abitno.utils.Log;
import me.abitno.utils.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * It's recommended to use this class to check if Vitamio Plugin is installed in
 * the device before using any other classes and functions.
 * 
 * copy from /VitamioSDK/src/io/vov/vitamio/VitamioInstaller.java
 */
public class VitamioInstaller {
	public static final int VITAMIO_VERSION_CODE = 200;
	public static final String VITAMIO_VERSION_NAME = "2.0";

	public static final String VITAMIO_PACKAGE = "me.abitno.vplayer.t";

	public static final int VITAMIO_NOT_SUPPORTED = 1;
	public static final int VITAMIO_ARMV6 = 60;
	public static final int VITAMIO_ARMV6_VFP = 61;
	public static final int VITAMIO_ARMV7_VFPV3 = 70;
	public static final int VITAMIO_ARMV7_NEON = 71;

	private static final String[] LIBS = { "libvplayer.so", "libvscanner.so", "libffmpeg.so", "libvao.0.so", "libvvo.0.so", "libvvo.9.so", "libvvo.j.so", "libOMX.9.so", "libOMX.11.so", "libOMX.14.so", "inited.lock" };

	private static final int vitamioType;
	private static String vitamioPackage;
	static {
		int cpu = CPU.getFeature();
		if ((cpu & CPU.FEATURE_ARM_NEON) > 0)
			vitamioType = VITAMIO_ARMV7_NEON;
		else if ((cpu & CPU.FEATURE_ARM_VFPV3) > 0 && (cpu & CPU.FEATURE_ARM_V7A) > 0)
			vitamioType = VITAMIO_ARMV7_VFPV3;
		else if ((cpu & CPU.FEATURE_ARM_VFP) > 0 && (cpu & CPU.FEATURE_ARM_V6) > 0)
			vitamioType = VITAMIO_ARMV6_VFP;
		else if ((cpu & CPU.FEATURE_ARM_V6) > 0)
			vitamioType = VITAMIO_ARMV6;
		else
			vitamioType = VITAMIO_NOT_SUPPORTED;

		if (vitamioType != VITAMIO_NOT_SUPPORTED)
			vitamioPackage = VITAMIO_PACKAGE;
		else
			vitamioPackage = null;
	}

	/**
	 * @return the best compatible Vitamio package name
	 */
	public static String getCompatiblePackage() {
		return vitamioPackage;
	}

	public static int getVitamioType() {
		return vitamioType;
	}

	/**
	 * Check if a Vitamio package is installed
	 * 
	 * @param ctx
	 *          a Context
	 * @return the Vitamio package name installed in this device, it may be the
	 *         most compatible package, check {@link #getCompatiblePackage()}
	 * @throws VitamioNotCompatibleException
	 * @throws VitamioNotFoundException
	 */
	public static String checkVitamioInstallation(Context ctx) throws VitamioNotCompatibleException, VitamioNotFoundException {
		if (vitamioType == VITAMIO_NOT_SUPPORTED)
			throw new VitamioNotCompatibleException();

		PackageManager pm = ctx.getPackageManager();
		try {
			ApplicationInfo ai = pm.getApplicationInfo(vitamioPackage, PackageManager.GET_SHARED_LIBRARY_FILES);
			Log.i("Vitamio installation: %s", ai.toString());
			return vitamioPackage;
		} catch (PackageManager.NameNotFoundException ne) {
			Log.e("checkVitamioInstallation", ne);
		}

		throw new VitamioNotFoundException();
	}

	public static String checkVitamioInstallation(Context ctx, int desireVersion) throws VitamioNotCompatibleException, VitamioNotFoundException, VitamioOutdateException {
		String p = checkVitamioInstallation(ctx);
		try {
			int version = ctx.getPackageManager().getPackageInfo(p, 0).versionCode;
			if (version < desireVersion)
				throw new VitamioOutdateException();
		} catch (PackageManager.NameNotFoundException ne) {
			Log.e("checkVitamioInstallation", ne);
			throw new VitamioNotFoundException();
		}
		return p;
	}

	public static String getVitamioInfo(Context ctx) {
		StringBuilder sb = new StringBuilder();
		try {
			ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo(vitamioPackage, PackageManager.GET_SHARED_LIBRARY_FILES);
			sb.append(getPair("vitamio_pkg", ai.packageName));
			PackageInfo pi = ctx.getPackageManager().getPackageInfo(ai.packageName, 0);
			sb.append(getPair("vitamio_version_name", pi.versionName));
			sb.append(getPair("vitamio_version_code", "" + pi.versionCode));
			sb.append(getPair("vitamio_first_install_time", "" + pi.firstInstallTime));
			sb.append(getPair("vitamio_last_update_time", "" + pi.lastUpdateTime));
			sb.append(getPair("vitamio_library_path", getLibraryPath()));
		} catch (Exception e) {
			Log.e("getVitamioInfo[" + ctx.toString() + "]", e);
		}
		return sb.toString();
	}

	/**
	 * Get the absolute path to Vitamio's library
	 * 
	 * @param ctx
	 *          a Context
	 * @return the absolute path to the libffmpeg.so
	 * @throws VitamioNotCompatibleException
	 * @throws VitamioNotFoundException
	 */
	public static final String getLibraryPath() {
		return "/data/data/" + vitamioPackage + "/libs/";
	}

	public static boolean isNativeLibsInited(Context context) {
		File dir = new File(VitamioInstaller.getLibraryPath());
		Log.i("isNativeLibsInited: %s, %b, %b", dir.getAbsolutePath(), dir.exists(), dir.isDirectory());
		if (dir.exists() && dir.isDirectory()) {
			String[] libs = dir.list();
			if (libs != null) {
				Arrays.sort(libs);
				for (String L : LIBS) {
					if (Arrays.binarySearch(libs, L) < 0) {
						Log.e("Native libs %s not exists!", L);
						return false;
					}
				}
				File lock = new File(getLibraryPath() + "/inited.lock");
				FileReader fr = null;
				try {
					fr = new FileReader(lock);
					int appVersion = AndroidContextUtils.getVersionCode(context);
					int libVersion = fr.read();
					Log.i("isNativeLibsInited, APP VERSION: %d, Vitamio Library version: %d", appVersion, libVersion);
					if (libVersion == appVersion)
						return true;
				} catch (IOException e) {
					Log.e("isNativeLibsInited", e);
				} finally {
					IOUtils.closeSilently(fr);
				}
			}
		}
		return false;
	}

	/**
	 * This exception will be thrown when Vitamio not compatible with the device.
	 * I feel sorry for this.
	 */
	public static class VitamioNotCompatibleException extends AndroidException {
		private static final long serialVersionUID = 2072357560688644354L;

		public VitamioNotCompatibleException() {
		}

		public VitamioNotCompatibleException(String name) {
			super(name);
		}
	}

	/**
	 * This exception will be thrown when Vitamio not found in the device. May
	 * popup a dialog to guide the user install a compatible Vitamio.
	 * 
	 * @see io.vov.vitamio.VitamioInstaller#showInstallerDialog(String, String)
	 */
	public static class VitamioNotFoundException extends AndroidException {
		private static final long serialVersionUID = 5842552425092114126L;

		public VitamioNotFoundException() {
		}

		public VitamioNotFoundException(String name) {
			super(name);
		}
	}

	public static class VitamioOutdateException extends AndroidException {
		private static final long serialVersionUID = -8736652909744594894L;

		public VitamioOutdateException() {
		}

		public VitamioOutdateException(String name) {
			super(name);
		}
	}

	private static String getPair(String key, String value) {
		return "&" + key + "=" + value;
	}
}
