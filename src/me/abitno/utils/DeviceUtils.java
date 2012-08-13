package me.abitno.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

@SuppressWarnings("deprecation")
public class DeviceUtils {
	public static int getScreenWidth(Activity ctx) {
		return ctx.getWindowManager().getDefaultDisplay().getWidth();
	}

	public static int getScreenHeight(Activity ctx) {
		return ctx.getWindowManager().getDefaultDisplay().getHeight();
	}
	
	public static double getScreenPhysicalSize(Activity ctx) {
		DisplayMetrics dm = new DisplayMetrics();
		ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
		double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2) + Math.pow(dm.heightPixels, 2));
		return diagonalPixels / (160 * dm.density);
	}
}
