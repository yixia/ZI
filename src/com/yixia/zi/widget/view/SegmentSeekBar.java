package com.yixia.zi.widget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.yixia.zi.R;

/**
 * Section Buffer SeekBar
 */
public class SegmentSeekBar extends SeekBar {

	private static final int MSG_UPDATE = 42;

	private RectF mBounds;
	private Handler mHandler;
	private float mCurrentProgress;
	private Paint mProgressPaint;

	private long[] mSegments;
	private long mLength;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SegmentSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public SegmentSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * @param context
	 */
	public SegmentSeekBar(Context context) {
		super(context);
		init(context);
	}

	@SuppressLint("HandlerLeak")
	private void init(Context ctx) {
		mBounds = new RectF(0, 0, 0, 0);
		mCurrentProgress = 0;
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == MSG_UPDATE) {
					invalidate();
				}
			}
		};

		int progressColor = ctx.getResources().getColor(R.color.seekbar_buffer_color);
		mProgressPaint = new Paint();
		mProgressPaint.setColor(progressColor);
		mProgressPaint.setAntiAlias(true);
	}

	@Override
	public void onLayout(boolean f, int l, int t, int r, int b) {
		mBounds.left = 0;
		mBounds.right = (r - l) * mCurrentProgress / getMax();
		mBounds.top = 0;
		mBounds.bottom = b - t;
	}

	public void setSegmentProgress(long[] segments, long length) {
		if (segments != null && segments.length > 0) {
			this.mSegments = segments;
			this.mLength = length;
			mHandler.removeMessages(MSG_UPDATE);
			mHandler.sendEmptyMessage(MSG_UPDATE);
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mSegments == null || mLength == 0) {
			return;
		}

		Rect progressRect = getProgressDrawable().getBounds();
		int progressRange = progressRect.right - progressRect.left;

		for (int i = 0; i < mSegments.length; i += 2) {
			long begin = mSegments[i];
			long end = mSegments[i + 1];

			long xx = begin * getMax() / mLength;
			long yy = end * getMax() / mLength;
			mBounds.left = progressRange * xx / getMax() + getThumbOffset();
			mBounds.right = progressRange * yy / getMax() + getThumbOffset();

			mBounds.top = getHeight() / 2 - 3.5f;
			mBounds.bottom = getHeight() / 2 + 2;
			mCurrentProgress = end;
			canvas.drawRect(mBounds, mProgressPaint);
		}
	}
}
