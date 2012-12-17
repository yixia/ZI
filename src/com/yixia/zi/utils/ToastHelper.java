package com.yixia.zi.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.yixia.zi.R;

public class ToastHelper {
	public static void showToast(Context ctx, int resID) {
		showToast(ctx, Toast.LENGTH_SHORT, resID);
	}

	public static void showToast(Context ctx, String text) {
		showToast(ctx, Toast.LENGTH_SHORT, text);
	}

	public static void showToast(Context ctx, int duration, int resID) {
		showToast(ctx, duration, ctx.getString(resID));
	}

	public static void showToast(Context ctx, int duration, String text) {
		Toast toast = Toast.makeText(ctx, text, duration);
		View mNextView = toast.getView();
		if (mNextView != null)
			mNextView.setBackgroundResource(R.drawable.toast_frame);
		toast.show();
	}
}
