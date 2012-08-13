package me.abitno.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Parcelable;
import android.text.Html;

public final class IntentHelper {

	public static final String MEDIA_PATTERN = "(http[s]?://)+([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?";
	private static final Pattern mMediaPattern;

	static {
		mMediaPattern = Pattern.compile(MEDIA_PATTERN);
	}

	public static Uri getIntentUri(Intent intent) {
		Uri result = null;
		if (intent != null) {
			result = intent.getData();
			if (result == null) {
				final String type = intent.getType();
				String sharedUrl = intent.getStringExtra(Intent.EXTRA_TEXT);
				if (!StringUtils.isBlank(sharedUrl)) {
					if ("text/plain".equals(type) && sharedUrl != null) {
						result = getTextUri(sharedUrl);
					} else if ("text/html".equals(type) && sharedUrl != null) {
						result = getTextUri(Html.fromHtml(sharedUrl).toString());
					}
				} else {
					Parcelable parce = intent.getParcelableExtra(Intent.EXTRA_STREAM);
					if (parce != null)
						result = (Uri) parce;
				}
			}
		}
		return result;
	}

	private static Uri getTextUri(String sharedUrl) {
		Matcher matcher = mMediaPattern.matcher(sharedUrl);
		if (matcher.find()) {
			sharedUrl = matcher.group();
			if (!StringUtils.isBlank(sharedUrl)) {
				return Uri.parse(sharedUrl);
			}
		}
		return null;
	}

	public static boolean existPackage(final Context ctx, String packageName) {
		if (!StringUtils.isBlank(packageName)) {
			for (PackageInfo p : ctx.getPackageManager().getInstalledPackages(0)) {
				if (packageName.equals(p.packageName))
					return true;
			}
		}
		return false;
	}

	public static void startApkActivity(final Context ctx, String packageName) {
		PackageManager pm = ctx.getPackageManager();
		PackageInfo pi;
		try {
			pi = pm.getPackageInfo(packageName, 0);
			Intent intent = new Intent(Intent.ACTION_MAIN, null);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setPackage(pi.packageName);

			List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

			ResolveInfo ri = apps.iterator().next();
			if (ri != null) {
				String className = ri.activityInfo.name;
				intent.setComponent(new ComponentName(packageName, className));
				ctx.startActivity(intent);
			}
		} catch (NameNotFoundException e) {
			Log.e("startActivity", e);
		}
	}
}
