package com.drawshare.datastore.cache;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.drawshare.Request.Constant;
import com.drawshare.Request.Util;
import com.drawshare.util.DrawShareConstant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class ImageCache extends BaseCache{
	

	private static final String IMG_CACHE_DIR = "/" + CacheConstant.CACHE_BASE_DIR + "/" + 
			CacheConstant.IMAGE_CACHE + "/";
	
	private static ImageCache imageCache = null;
	
	private static String LOG_TAG = "ImageCache";
	
	private ImageCache() {
		//super(context);
		// TODO Auto-generated constructor stub
	}
	
	public static ImageCache getImageCache() {
		if (imageCache != null) {
			return imageCache;
		}
		else {
			imageCache = new ImageCache();
			return imageCache;
		}
	}
	
	private boolean writeImageToFile(Bitmap image, String filePathName) {
		FileOutputStream fouts = this.getFileOutputStream(filePathName);
		Log.d(LOG_TAG, "get the output stream of " + filePathName);
		BufferedOutputStream bos = new BufferedOutputStream(fouts);
		boolean success = image.compress(Bitmap.CompressFormat.PNG, 100, bos);
		Log.d(LOG_TAG, "compress the bitmap, return value is " + success); 
		try {
			//fouts.flush();
			bos.flush();
			bos.close();
			fouts.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}
	
	private Bitmap readImageFromFile(String filePathName, int requireSize) throws FileNotFoundException {
		FileInputStream fins = this.getFileInputStream(filePathName);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(fins, null, options);
		
		// determinted the scale
		int scale = 1;
		Log.d(Constant.LOG_TAG, "the outHeight is " + options.outHeight + ", the outWidth is " + options.outWidth);
		
		scale = Util.computeSampleSize(options, -1, requireSize * requireSize);
		options.inJustDecodeBounds = false;
		Log.d(Constant.LOG_TAG, "the scale is " + scale);
		
		options.inSampleSize = scale;
		Bitmap bitmap = BitmapFactory.decodeStream(this.getFileInputStream(filePathName), null, options);
		return bitmap;
		
	}
	
	/**
	 * 将图片的url变成文件名，方法为从"www"开始，将"/"替换成"_"
	 * @param pictureURL
	 * @return
	 */
	private String getPictFileNameFromURL(String pictureURL) {
		String pictFileName = pictureURL.substring(4, pictureURL.length()).replace('/', '_');
		return pictFileName;
	}
	
	public boolean cacheImage(Bitmap bitmap, String pictureURL) {
		String pictFileName = getPictFileNameFromURL(pictureURL);
		Log.d(LOG_TAG, "the cache image path is " + Environment.getExternalStorageDirectory() + IMG_CACHE_DIR + pictFileName);
		boolean success = writeImageToFile(bitmap, IMG_CACHE_DIR + pictFileName);
		Log.d(LOG_TAG, success + " to write the image");
		return success;
	}
	
	public Bitmap getCacheImage(String pictureURL, int requireSize) throws FileNotFoundException {
		String pictFileName = getPictFileNameFromURL(pictureURL);
		Bitmap bitmap = this.readImageFromFile(IMG_CACHE_DIR + pictFileName, requireSize);
		return bitmap;
	}
	
	@Override
	public boolean clearCache() {
		// TODO Auto-generated method stub
		//return super.clearCache();
		if (this.deleteAllUnderPath(IMG_CACHE_DIR)) {
			return true;
		}
		return false;
	}
}
