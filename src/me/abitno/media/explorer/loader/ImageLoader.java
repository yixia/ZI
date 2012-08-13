package me.abitno.media.explorer.loader;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.ImageView;

public abstract class ImageLoader<T> {
	private AtomicBoolean mStopped = new AtomicBoolean(Boolean.FALSE);
	private ThreadPoolExecutor mQueue;
	private MemoryCache<T> mCacheBitmap = new MemoryCache<T>();

	public ImageLoader() {
		if (Build.VERSION.SDK_INT > 8) {
			mQueue = new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), sThreadFactory);
			mQueue.allowCoreThreadTimeOut(true);
		} else {
			mQueue = new ThreadPoolExecutor(2, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), sThreadFactory);
		}
	}

	public abstract Bitmap getBitmap(final T id);

	public void displayImage(final T id, final Activity activity, final ImageView imageView) {
		Bitmap bitmap = mCacheBitmap.get(id);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setTag(id);
			mQueue.execute(new Runnable() {

				@Override
				public void run() {
					final Bitmap bitmap = getBitmap(id);
					if (bitmap != null && !bitmap.isRecycled()) {
						mCacheBitmap.put(id, bitmap);
						Object tag = imageView.getTag();
						if (tag.equals(id)) {
							if (activity != null) {
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										imageView.setImageBitmap(bitmap);
									}
								});
							}
						}
					}
				}
			});
		}
	}

	public void stopThread() {
		if (!mStopped.get()) {
			mQueue.shutdownNow();
			mCacheBitmap.clear();
			mStopped.set(Boolean.TRUE);
		}
	}

	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "ImageLoader #" + mCount.getAndIncrement());
		}
	};
}
