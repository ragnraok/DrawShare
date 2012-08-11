package com.drawshare.Request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Util {
	
	/**
	 * Let the server return json string can be worked in the fucking org.json package
	 * @param jsonString
	 * @return
	 */
	public static String processJsonString(String jsonString) {
		String replaceString = jsonString.replace("\\", "");
		return replaceString.substring(1, replaceString.length() - 1);
	}
	
	public static String bitmapToBase64String(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] bitmapArray = baos.toByteArray();
		
		String encodeString = Base64.encodeToString(bitmapArray, Base64.DEFAULT);
		
		return encodeString;
	}
	
	/**
	 * 将网址中的内容转换成Bitmap
	 * @param siteUrl
	 * @return
	 */
	public static Bitmap urlToBitmap(String siteUrl) {
		try {
			URL url = new URL(siteUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			
			InputStream inputStream = connection.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			
			return bitmap;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
