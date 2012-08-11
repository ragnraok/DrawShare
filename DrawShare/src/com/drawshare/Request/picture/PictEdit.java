package com.drawshare.Request.picture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.drawshare.Request.Constant;
import com.drawshare.Request.Util;
import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.PictureNotExistException;
import com.drawshare.Request.exceptions.UserNotExistException;

import android.graphics.Bitmap;
import android.util.Log;

public class PictEdit {
	private static final String getPictInfoURL = Constant.SITE + Constant.Picture.GET_PICT_INFO;
	private static final String addNewPictURL = Constant.SITE + Constant.Picture.ADD_NEW_PICT;
	private static final String deletePictURL = Constant.SITE + Constant.Picture.DELETE_PICT;
	
	/**
	 * 获取某张图片的信息
	 * @param pictureId 图片的id
	 * @return 一个JSONObject,格式如下:
	 * {'picture_id':picture_id, 'create_user_id': create_user_id,
     *                   'picture_intro': picture_intro, 'picture_url': picture_url, 
     *                   'picture_thumbnail_url': picture_thumbnail_url, 'picture_score': picture_score,
     *                   'picture_create_date': YYYY/MM/DD, 'create_username': username}
	 * @throws PictureNotExistException, 如果pictureId对应的图片不存在的话,抛出此异常
	 */
	public static JSONObject getPictInfo(String pictureId
			) throws PictureNotExistException {
		Log.d(Constant.LOG_TAG, "in get pict info, the url is " + getPictInfoURL + pictureId + "/");
		
		try {
			URL url = new URL(getPictInfoURL + pictureId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setFollowRedirects(true);
			connection.connect();
			
			Log.d(Constant.LOG_TAG, "in get pict info, the response message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 404) {
				throw new PictureNotExistException("the picture for id " + pictureId + " is not exist");
			}
			else if (connection.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				
				String returnMsg = reader.readLine();
				returnMsg = Util.processJsonString(returnMsg);
				
				JSONObject pictObject = new JSONObject(returnMsg);
				return pictObject;
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 增加一张全新的图片（不是fork别人的图片）
	 * @param userId 用户的id
	 * @param apiKey 用户的apiKey
	 * @param bitmap 新的图片
	 * @param title 新图片的title
	 * @param intro 新图片的介绍
	 * @return true,如果增加成功
	 * @throws UserNotExistException, 如果userId对应的用户不存在,抛出此异常
	 * @throws AuthFailException,如果apiKey不正确,抛出此异常
	 */
	public static boolean addNewPict(String userId, String apiKey, Bitmap bitmap, 
			String title, String intro) throws UserNotExistException, AuthFailException {
		Log.d(Constant.LOG_TAG, "in add new pict, the url is " + addNewPictURL + userId + "/");
		
		try {
			URL url = new URL(addNewPictURL + userId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", apiKey);
			connection.setFollowRedirects(true);
			connection.connect();
			
			// construct the output json
			String bitmapEncodeString = Util.bitmapToBase64String(bitmap);
			JSONObject outpictObject = new JSONObject();
			outpictObject.put("title", title);
			outpictObject.put("intro", intro);
			outpictObject.put("picture_binary", bitmapEncodeString);
			
			PrintWriter writer = new PrintWriter(connection.getOutputStream());
			writer.print(outpictObject.toString());
			writer.flush();
			writer.close();
			
			Log.d(Constant.LOG_TAG, "in add new pict, the response message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 404) {
				throw new UserNotExistException("the user for id " + userId + " is not exist");
			}
			else if (connection.getResponseCode() == 401) {
				throw new AuthFailException("the apiKey " + apiKey + " error");
			}
			else if (connection.getResponseCode() == 200) {
				return true;
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 删除用户的一张图片	
	 * @param userId 用户的id
	 * @param pictureId 图片的id
	 * @param apiKey 用户的apiKey
	 * @return true如果删除成功
	 * @throws AuthFailException, 如果用户的apiKey错误,则抛出此异常
	 * @throws UserNotExistException, 如果pictureId对应的图片不存在,或者改图片不属于用户,则抛出此异常
	 */
	public static boolean delelePict(String userId, String pictureId, 
			String apiKey) throws AuthFailException, UserNotExistException {
		Log.d(Constant.LOG_TAG, "in delete the picture, the url is " + deletePictURL + userId + "/" + pictureId + "/");
		
		try {
			URL url = new URL(deletePictURL + userId + "/" + pictureId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", apiKey);
			connection.setRequestMethod("POST");
			connection.setFollowRedirects(true);
			
			connection.connect();
			
			Log.d(Constant.LOG_TAG, "in delete the picture, the response message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 401) {
				throw new AuthFailException("the apiKey " + apiKey + " is error");
			}
			else if (connection.getResponseCode() == 404) {
				throw new UserNotExistException("the user for id " + userId + " is not exist");
			}
			else if (connection.getResponseCode() == 200) {
				return true;
			}
			return false;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	
}
