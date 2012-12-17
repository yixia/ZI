package com.yixia.zi.widget;


import com.yixia.zi.R;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;

public class Toast {
	public static int LENGTH_LONG = android.widget.Toast.LENGTH_LONG;
	public static int LENGTH_SHORT = android.widget.Toast.LENGTH_SHORT;

	public static void showText(Context context, CharSequence text, int duration) {
		android.widget.Toast t = android.widget.Toast.makeText(context, text, duration);
		View v = t.getView();
		if (v != null)
			v.setBackgroundResource(R.drawable.toast_frame);
		t.show();
	}

	public static void showText(Context context, int resId, int duration) throws Resources.NotFoundException {
		showText(context, context.getResources().getText(resId), duration);
	}
}
