package com.drawshare.Request.message;

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

public class Message {
	private static final String getFollowMessageURL = Constant.SITE + Constant.Message.GET_FOLLOW_MESSAGE;
	private static final String getForkMessageURL = Constant.SITE + Constant.Message.GET_FORK_MESSAGE;
	
	/**
	 * 获取用户fork信息
	 * @param userId 用户的id
	 * @param apiKey 用户的apiKey
	 * @param num 获取信息的数量
	 * @return 一个JSONObject,格式如下:
	 * {'fork_messages': 
     *     [{'sender_id': sender_id, 'sender_name': sender_name, 'origin_pict_id': origin_pict_id, 
     *             'origin_pict_title': origin_pict_title, 'result_pict_id': result_pict_id, 
     *                   'result_pict_title': result_pict_title, 'send_date': YYYY/MM/DD}, 
     *                      ....]
     * }
	 * @throws AuthFailException
	 * @throws UserNotExistException
	 */
	public static JSONObject getForkMessage(String userId, String apiKey, 
			int num) throws AuthFailException, UserNotExistException {
		Log.d(Constant.LOG_TAG, "in get fork message, the url is " + getForkMessageURL + userId + "/" + num + "/");
		
		try {
			URL url = new URL(getForkMessageURL + userId + "/" + num + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Authorization", apiKey);
			connection.setFollowRedirects(true);
			connection.setRequestMethod("GET");
			connection.connect();
			
			Log.d(Constant.LOG_TAG, "in get fork message, the response message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 401) {
				throw new AuthFailException("the apikey " + apiKey + " is error");
			}
			else if (connection.getResponseCode() == 404) {
				throw new UserNotExistException("the user for id " + userId + " is not exist");
			}
			else if (connection.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				
				String returnMsg = reader.readLine();
				returnMsg = Util.processJsonString(returnMsg);
				
				JSONObject forkObject = new JSONObject(returnMsg);
				return forkObject;
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
	
	public static final JSONObject getFollowMessage(String userId, String apiKey, 
			int num) throws AuthFailException, UserNotExistException {
		Log.d(Constant.LOG_TAG, "in get follow message, the url is " + getFollowMessageURL + userId + "/" + num + "/");
		
		try {
			URL url = new URL(getFollowMessageURL + userId + "/" + num + "/");
			HttpURLConnection connection  = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Authorization", apiKey);
			connection.setFollowRedirects(true);
			connection.setRequestMethod("GET");
			connection.connect();
			
			Log.d(Constant.LOG_TAG, "in get follow message, the response message is " + connection.getResponseMessage());
			
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
				Log.d(Constant.LOG_TAG, "in get follow message, the returnMsg is " + returnMsg);
				
				JSONObject followObject = new JSONObject(returnMsg);
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
	
	
	
	
	
	
}
