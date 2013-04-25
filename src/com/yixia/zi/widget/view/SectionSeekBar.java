package com.yixia.zi.widget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.widget.SeekBar;

import com.yixia.zi.R;

import java.util.ArrayList;

/**
 * Section Buffer SeekBar
 */
public class SectionSeekBar extends SeekBar {

	private static final int MSG_UPDATE = 42;

	private Rect mBounds;
	private Handler mHandler;
	private int mCurrentProgress;
	private Paint mProgressPaint;

	private ArrayList<Pair<Integer, Integer>> mSectionList;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SectionSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public SectionSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * @param context
	 */
	public SectionSeekBar(Context context) {
		super(context);
		init(context);
	}

	@SuppressLint("HandlerLeak")
	private void init(Context ctx) {
		mSectionList = new ArrayList<Pair<Integer, Integer>>();
		mBounds = new Rect(0, 0, 0, 0);
		mCurrentProgress = 0;
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == MSG_UPDATE) {
					invalidate();
				}
			}
		};

		int progressColor = ctx.getResources().getColor(R.color.pin_progress_default_progress_color);
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

	/**
	 * Only set one section buffer
	 * 
	 * @param startProgress
	 * @param endProgess
	 */
	public void setSectionProgress(int startProgress, int endProgess) {
		mSectionList.clear();
		mSectionList.add(Pair.create(startProgress, endProgess));
		mHandler.removeMessages(MSG_UPDATE);
		mHandler.sendEmptyMessage(MSG_UPDATE);
	}

	/**
	 * Set several section buffer
	 * 
	 * @param sectionList
	 */
	public void setSectionProgress(ArrayList<Pair<Integer, Integer>> sectionList) {
		mSectionList.clear();
		mSectionList = sectionList;
		mHandler.removeMessages(MSG_UPDATE);
		mHandler.sendEmptyMessage(MSG_UPDATE);
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (Pair<Integer, Integer> section : mSectionList) {
			mBounds.left = getWidth() * section.first / getMax();
			mBounds.right = getWidth() * section.second / getMax();
			mBounds.top = getHeight() / 2 - 2;
			mBounds.bottom = getHeight() / 2 + 2;
			mCurrentProgress = section.second;
			canvas.drawRect(mBounds, mProgressPaint);
			Log.e("====================", "==rect.left==" + mBounds.left + "==top== " + mBounds.top + "======right===" + mBounds.right + "========bottom====" + mBounds.bottom);
		}
	}
}
