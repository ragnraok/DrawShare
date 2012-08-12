package com.drawshare.datastore.cache;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageCache extends BaseCache{
	
	private static final String IMG_CACHE_DIR = "/" + CacheConstant.CACHE_BASE_DIR + "/" + 
			CacheConstant.IMAGE_CACHE + "/";
	
	private static ImageCache imageCache = null;
	
	private ImageCache(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public static ImageCache getImageCacheHandler(Context context) {
		if (imageCache != null) {
			return imageCache;
		}
		else {
			imageCache = new ImageCache(context);
			return imageCache;
		}
	}
	
	private boolean writeImageToFile(Bitmap image, String filePathName) {
		FileOutputStream fouts = this.getFileOutputStream(filePathName);
		boolean success = image.compress(Bitmap.CompressFormat.JPEG, 100, fouts);
		try {
			fouts.flush();
			fouts.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}
	
	private Bitmap readImageFromFile(String filePathName) throws FileNotFoundException {
		FileInputStream fins = this.getFileInputStream(filePathName);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		try {
			while ((len = fins.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			byte[] imageByte = baos.toByteArray();
			Bitmap image = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
			fins.close();
			baos.close();
			return image;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
		boolean success = writeImageToFile(bitmap, IMG_CACHE_DIR + pictureURL);
		return success;
	}
	
	public Bitmap getCacheImage(String pictureURL) throws FileNotFoundException {
		String pictFileName = getPictFileNameFromURL(pictureURL);
		Bitmap bitmap = this.readImageFromFile(IMG_CACHE_DIR + pictureURL);
		return bitmap;
	}
}
