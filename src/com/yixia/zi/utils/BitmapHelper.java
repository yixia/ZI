/*
 * Copyright (C) 2012 YIXIA.COM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yixia.zi.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.http.AndroidHttpClient;

public class BitmapHelper {
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float radius) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, radius, radius, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static boolean saveBitmapToFile(final Bitmap bitmap, String savePath) {
		boolean result = false;
		if (bitmap != null && !bitmap.isRecycled()) {
			File myDrawFile = new File(savePath);
			try {
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myDrawFile));
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				bos.flush();
				bos.close();
				result = true;
			} catch (Exception e) {
				Log.e("saveBitmapToFile", e);
			} finally {
				if (bitmap != null) {
					bitmap.recycle();
				}
			}
		}
		return result;
	}

	public static Bitmap downloadBitmap(String url) {
		Bitmap bm = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			is = AndroidHttpClient.getUngzippedContent(NetHelper.getResponse(null, url, null).getEntity());
			bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(bis);
		} catch (Exception e) {
			Log.e("downloadBitmap", e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return bm;
	}

}
