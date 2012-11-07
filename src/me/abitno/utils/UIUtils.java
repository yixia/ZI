/**
 * 
 */

package me.abitno.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;

/**
 * 
 * @author crossle
 */
public class UIUtils {
	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasICS() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static boolean isHoneycombTablet(Context context) {
		return hasHoneycomb() && isTablet(context);
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork == null || !activeNetwork.isConnected()) {
			return false;
		}
		return true;
	}

	public static ImageFetcher getImageFetcher(final FragmentActivity activity) {
		// The ImageFetcher takes care of loading remote images into our ImageView
		ImageFetcher fetcher = new ImageFetcher(activity);
		fetcher.setImageCache(ImageCache.findOrCreateCache(activity, "imageFetcher"));
		return fetcher;
	}
	
	/** 
   * Set the theme of the Activity, and restart it by creating a new Activity 
   * of the same type. 
   */  
  public static void changeToTheme(Activity activity)  
  {  
      activity.finish();
      activity.startActivity(new Intent(activity, activity.getClass()));  
  }  
  
  
  public static TypedValue getAttrValue(Activity activity, int attrId) {
		TypedValue typedValue = new TypedValue(); 
		activity.getTheme().resolveAttribute(attrId, typedValue, true);
		return typedValue;
  }
}
