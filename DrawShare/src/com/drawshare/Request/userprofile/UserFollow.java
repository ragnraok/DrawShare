package com.drawshare.Request.userprofile;

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

public class UserFollow {
	private static final String getFollowURL = Constant.SITE + Constant.UserProfile.GET_FOLLOW_INFO;
	private static final String followUserURL = Constant.SITE + Constant.UserProfile.FOLLOW_USER;
	private static final String unfollowUserURL = Constant.SITE + Constant.UserProfile.UNFOLLOW_USER;
	
	
	public static final JSONObject getFollowInfo(String userId) throws UserNotExistException {
		Log.d(Constant.LOG_TAG, "the get follow info url is " + getFollowURL + userId);
		try {
			URL url = new URL(getFollowURL + userId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setFollowRedirects(true);
			connection.setRequestMethod("GET");
			connection.connect();
			
			if (connection.getResponseCode() == 404) {
				throw new UserNotExistException("the user for id " + userId + " is not exist");
			}
			else if (connection.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				
				String returnMsg = reader.readLine();
				String followInfoString = Util.processJsonString(returnMsg);
				
				Log.d(Constant.LOG_TAG, "in get the follow info, the followInfoString is " + followInfoString);
				
				JSONObject followObject = new JSONObject(followInfoString);
				return followObject;
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
	
	public static final boolean followUser(String curUserId, String followUserId, 
			String apiKey) throws UserNotExistException, AuthFailException {
		Log.d(Constant.LOG_TAG, "the set follow info url is " + followUserURL + curUserId + "/" + followUserId + "/");
		
		try {
			URL url = new URL(followUserURL + curUserId + "/" + followUserId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", apiKey);
			connection.connect();
			
			if (connection.getResponseCode() == 404) { 
				throw new UserNotExistException("the user for id " + curUserId + " or the user for id " + 
						followUserId + " is not exist");
			}
			else if (connection.getResponseCode() == 401) {
				throw new AuthFailException("the api key " + apiKey + " is error");
			}
			else if (connection.getResponseCode() == 200) {
				Log.d(Constant.LOG_TAG, "the user for id " + curUserId + " sucess follow the user for id " + followUserId);
				return true;
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * unfollow 一个用户
	 * @param curUserId, 你自己的用户id
	 * @param unfollowUserId, 被你取消关注的人的id
	 * @param apiKey, 你自己的apikey
	 * @return
	 * @throws AuthFailException 如果apiKey错误的话,抛出此异常
	 * @throws UserNotExistException 如果curUserId或者unfollowUserId对应的用户不存在,则抛出此异常
	 */
	public static final boolean unfollowUser(String curUserId, String unfollowUserId, 
			String apiKey) throws AuthFailException, UserNotExistException {
		Log.d(Constant.LOG_TAG, "in unfollow user, the url is " + unfollowUserURL + curUserId + "/" + unfollowUserId + "/");
		
		try {
			URL url = new URL(unfollowUserURL + curUserId + "/" + unfollowUserId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setFollowRedirects(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", apiKey);
			connection.connect();
			
			Log.d(Constant.LOG_TAG, "in unfollow user, the response message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 200) {
				return true;
			}
			else if (connection.getResponseCode() == 404) {
				throw new UserNotExistException("the user for " + curUserId + 
						" or the user for " + unfollowUserId + " is not exist");
			}
			else if (connection.getResponseCode() == 401) {
				throw new AuthFailException("the apikey " + apiKey + " is error");
			}
			else
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
