package me.abitno.media.explorer.loader;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;

public class MemoryCache<T> {
	private HashMap<T, SoftReference<Bitmap>> cache = new HashMap<T, SoftReference<Bitmap>>();

	public Bitmap get(T id) {
		if (!cache.containsKey(id))
			return null;
		SoftReference<Bitmap> ref = cache.get(id);
		return ref.get();
	}

	public void put(T id, Bitmap bitmap) {
		cache.put(id, new SoftReference<Bitmap>(bitmap));
	}

	public void clear() {
		cache.clear();
	}
}