package me.abitno.utils;

import android.text.TextUtils;

import java.util.Arrays;
import java.util.Iterator;

public class StringUtils {
	public static String join(Object[] elements, CharSequence separator) {
		return join(Arrays.asList(elements), separator);
	}

	public static String join(Iterable<? extends Object> elements, CharSequence separator) {
		StringBuilder builder = new StringBuilder();

		if (elements != null) {
			Iterator<? extends Object> iter = elements.iterator();
			if (iter.hasNext()) {
				builder.append(String.valueOf(iter.next()));
				while (iter.hasNext()) {
					builder.append(separator).append(String.valueOf(iter.next()));
				}
			}
		}

		return builder.toString();
	}

	public static String fixLastSlash(String str) {
		String res = str == null ? "/" : str.trim() + "/";
		if (res.length() > 2 && res.charAt(res.length() - 2) == '/')
			res = res.substring(0, res.length() - 1);
		return res;
	}

	public static int convertToInt(String str) throws NumberFormatException {
		int s, e;
		for (s = 0; s < str.length(); s++)
			if (Character.isDigit(str.charAt(s)))
				break;
		for (e = str.length(); e > 0; e--)
			if (Character.isDigit(str.charAt(e - 1)))
				break;
		if (e > s) {
			try {
				return Integer.parseInt(str.substring(s, e));
			} catch (NumberFormatException ex) {
				Log.e("convertToInt", ex);
				throw new NumberFormatException();
			}
		} else {
			throw new NumberFormatException();
		}
	}

	public static String generateTime(long time) {
		int totalSeconds = (int) (time / 1000);
		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
	}

	public static boolean isBlank(String s) {
		return TextUtils.isEmpty(s);
	}
}
