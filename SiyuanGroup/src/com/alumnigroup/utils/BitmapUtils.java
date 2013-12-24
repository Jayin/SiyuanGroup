package com.alumnigroup.utils;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;


public class BitmapUtils {
   
	/**
	 * 根据所需的宽高 计算缩小比例
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
		int height = options.outHeight;
		int width  = options.outWidth;
		int inSamleSize = 1;
		if(height > reqHeight || width >reqWidth){
			if(width > reqHeight){
				inSamleSize = Math.round(width / reqWidth);	
			}else{
				inSamleSize = Math.round(height / reqHeight);
			}
		}
		return inSamleSize;
	}
	
	/**
	 * 根据所需的宽高  获取放缩后的bitmap
	 * @param res
	 * @param resID
	 * @param reqWidth
	 * @param reqHeight
	 * @return bitmap
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res,int resID,int reqWidth,int reqHeight){
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resID,options);
		// calulate inSampleSize
		options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
		// decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resID,options);
	}
	
	/**
	 * 自动缩小bitmap 
	 * @param bitmap
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap scaleInSampleSize(Bitmap bitmap,int reqWidth,int reqHeight){
		final BitmapFactory.Options options = new BitmapFactory.Options();

		if(bitmap.getHeight() > reqHeight || bitmap.getWidth() >reqWidth){
			if(bitmap.getWidth() > reqHeight){
				options.inSampleSize = Math.round( bitmap.getWidth() / reqWidth);	
			}else{
				options.inSampleSize = Math.round(bitmap.getHeight() / reqHeight);
			}
		}
		if(bitmap.getNinePatchChunk()==null)L.i("null");
	 
		return BitmapFactory.decodeByteArray(bitmap.getNinePatchChunk(), 0, bitmap.getNinePatchChunk().length, options);		
	}
 
}

