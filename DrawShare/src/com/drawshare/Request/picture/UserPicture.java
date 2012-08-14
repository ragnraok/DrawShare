package com.drawshare.Request.picture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.drawshare.Request.Constant;
import com.drawshare.Request.Util;
import com.drawshare.Request.exceptions.UserNotExistException;

import android.util.Log;

public class UserPicture {
	private static final String getUserPictURL = Constant.SITE + Constant.Picture.GET_USER_PICT;
	
	/**
	 * 获取某个用户的所有图片
	 * @param userId
	 * @return 一个JSONObject，格式为:
	 * 					{'pictures':
     *                           [{'picture_id': picture_id, 'title':title, 'picture_url': picture_url,
     *                              'picture_thumbnail_url': picture_thumbnail_url}, ...]
     *                           }
	 * @throws UserNotExistException
	 */
	public static JSONObject getUserPict(String userId) throws UserNotExistException {
		Log.d(Constant.LOG_TAG, "in get user picture, the url is " + getUserPictURL + userId + "/");
		
		try {
			URL url = new URL(getUserPictURL + userId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setFollowRedirects(true);
			connection.connect();
			
			Log.d(Constant.LOG_TAG, "in get user picture, the response message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 404) {
				throw new UserNotExistException("the user for id " + userId + " is not exist");
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
}
