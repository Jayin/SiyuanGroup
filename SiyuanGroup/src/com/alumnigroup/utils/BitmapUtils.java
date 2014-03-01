package com.alumnigroup.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;

public class BitmapUtils {

	/**
	 * 根据所需的宽高 计算缩小比例
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		int height = options.outHeight;
		int width = options.outWidth;
		int inSamleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			if (width > reqHeight) {
				inSamleSize = Math.round(width / reqWidth);
			} else {
				inSamleSize = Math.round(height / reqHeight);
			}
		}
		return inSamleSize;
	}

	/**
	 * 根据所需的宽高 获取放缩后的bitmap
	 * 
	 * @param res
	 * @param resID
	 * @param reqWidth
	 * @param reqHeight
	 * @return bitmap
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resID, int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resID, options);
		// calulate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resID, options);
	}

	/**
	 * 自动缩小bitmap
	 * 
	 * @param bitmap
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap scaleInSampleSize(Bitmap bitmap, int reqWidth,
			int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();

		if (bitmap.getHeight() > reqHeight || bitmap.getWidth() > reqWidth) {
			if (bitmap.getWidth() > reqHeight) {
				options.inSampleSize = Math.round(bitmap.getWidth() / reqWidth);
			} else {
				options.inSampleSize = Math.round(bitmap.getHeight()
						/ reqHeight);
			}
		}
		if (bitmap.getNinePatchChunk() == null)
			L.i("null");

		return BitmapFactory.decodeByteArray(bitmap.getNinePatchChunk(), 0,
				bitmap.getNinePatchChunk().length, options);
	}
	
	/**
	 * 从一个字节数组中读取图片
	 * @param bytes
	 * @param opts
	 * @return
	 */
	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	/**
	 * 从一个uri 中读取图片
	 * @param uri
	 * @param context
	 * @return
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	public static Bitmap getPicFromUri(Uri uri, Context context) throws FileNotFoundException, Exception {
		ContentResolver resolver = context.getContentResolver();
		byte[] data = readStream(resolver.openInputStream(Uri.parse(uri
				.toString())));
		return getPicFromBytes(data, null);
	}

	private static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}
	
	public static InputStream getBitmapInputStream(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte [] data = baos.toByteArray();
		return new ByteArrayInputStream(data);
	}
	
	  /**
     * scale image
     * 
     * @param org
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
        return scaleImage(org, (float)newWidth / org.getWidth(), (float)newHeight / org.getHeight());
    }

    /**
     * scale image
     * 
     * @param org
     * @param scaleWidth sacle of width
     * @param scaleHeight scale of height
     * @return
     */
    public static Bitmap scaleImage(Bitmap org, float scaleWidth, float scaleHeight) {
        if (org == null) {
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
    }

}
