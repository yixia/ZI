package me.abitno.utils;

import android.database.Cursor;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.Closeable;

public class Utils {
	
	private static final String TAG = "Utils";
	
	public static <T> int indexOf(T[] array, T s) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(s)) {
				return i;
			}
		}
		return -1;
	}

	public static void closeSilently(Closeable c) {
		if (c == null)
			return;
		try {
			c.close();
		} catch (Throwable t) {
			Log.w(TAG, "fail to close", t);
		}
	}

	public static void closeSilently(ParcelFileDescriptor c) {
		if (c == null)
			return;
		try {
			c.close();
		} catch (Throwable t) {
			Log.w(TAG, "fail to close", t);
		}
	}
	
	public static void closeSilently(Cursor cursor) {
    try {
    	if (cursor != null) cursor.close();
     } catch (Throwable t) {
    	 Log.w(TAG, "fail to close", t);
     }
	 }
}
