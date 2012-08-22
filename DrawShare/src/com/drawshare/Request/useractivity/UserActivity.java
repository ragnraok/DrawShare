package com.drawshare.Request.useractivity;

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
import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.UserNotExistException;

import android.util.Log;

public class UserActivity {
	private static final String getFollowedActivitiesURL = Constant.SITE + Constant.Activity.GET_FOLLOWED_ACTIVITIES;
	
	/**
	 * 获取你follow的人最新消息	
	 * @param userId 你的id
	 * @param apiKey 你的apikey
	 * @param num 获取消息的数量
	 * @return 一个JSONObject,格式是:
	 * {activities:[
     *        {'userid': userid, 'username': username, 'picture_id': picture_id, 'picture_title': title, 
     *               'picture_url': picture_url, 'avatar_url': 'avatar_url'
     *                   'edit_type': edit_type, 'edit_date': YYYY/MM/DD}, ...
     *                   ]}
     * the edit_type 1 is create, 2 is fork
	 * @throws AuthFailException 如果apiKey错误,抛出此异常
	 * @throws UserNotExistException 如果userId对应的用户不存在,抛出此异常
	 */
	public static JSONObject getFollowActivities(String userId, String apiKey, 
			int num) throws AuthFailException, UserNotExistException {
		Log.d(Constant.LOG_TAG, "in get follow activities, the url is " + getFollowedActivitiesURL + userId + "/" + num + "/");
		
		try {
			URL url = new URL(getFollowedActivitiesURL + userId + "/" + num + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Authorization", apiKey);
			connection.setFollowRedirects(true);
			connection.setRequestMethod("GET");
			connection.connect();
			
			Log.d(Constant.LOG_TAG, "in get follow activities, the response message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 401) {
				throw new AuthFailException("the apiKey " + apiKey + " is error");
			}
			else if (connection.getResponseCode() == 404) {
				throw new UserNotExistException("the user for id " + userId + " is not exist");
			}
			else if (connection.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				
				String returnMsg = reader.readLine();
				returnMsg = Util.processJsonString(returnMsg);
				
				JSONObject activityObject = new JSONObject(returnMsg);
				return activityObject;
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
