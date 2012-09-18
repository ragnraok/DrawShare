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
	private static final String getUnReadMessageURL = Constant.SITE + Constant.Message.GET_UNREAD_MESSAGE_NUM;
	
	/**
	 * 获取用户fork信息
	 * @param userId 用户的id
	 * @param apiKey 用户的apiKey
	 * @param num 获取信息的数量
	 * @return 一个JSONObject,格式如下:
	 * {'fork_messages': 
     * 		[{'sender_id': sender_id, 'sender_name': sender_name, 'origin_pict_id': origin_pict_id, 
     *                      'origin_pict_title': origin_pict_title, 'origin_pict_url': url,  'result_pict_id': result_pict_id, 
     *                             'result_pict_title': result_pict_title, 'send_date': YYYY/MM/DD}, 
     *         ....]
     *  }
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
	
	/**
	 * {'follow_messages':
     *      [{'follow_user_id': follow_user_id, 'follow_username': follow_username, 'follow_date': YYYY/MM/DD,
     *              'follow_user_avatar_url':avatar_url}, 
     *                 ...]
     *   }
	 * @param userId
	 * @param apiKey
	 * @param num
	 * @return
	 * @throws AuthFailException
	 * @throws UserNotExistException
	 */
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
	
	/**
	 * 获取某个用户没有读取的消息条数
	 * @param userId
	 * @param apiKey
	 * @return 一个JSONObject,格式如下:
	 *    {'unread_message_number': number}
	 * @throws AuthFailException 当apiKey错误时，抛出此异常
	 * @throws UserNotExistException 当userId对应的用户不存在的时候，抛出此异常
	 */
	public static JSONObject getUnreadMessageNum(String userId, String apiKey) throws AuthFailException, UserNotExistException {
		Log.d(Constant.LOG_TAG, "in get the unread message num, the url is " + getUnReadMessageURL + userId + "/");
		
		try {
			URL url = new URL(getUnReadMessageURL + userId  + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Authorization", apiKey);
			connection.setRequestMethod("GET");
			connection.setFollowRedirects(true);
			connection.connect();
			
			Log.d(Constant.LOG_TAG, "in get the unread message, the return message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 401) {
				throw new AuthFailException("the apikey " + apiKey + " is error");
			}
			else if (connection.getResponseCode() == 404) {
				throw new UserNotExistException("the user for id " + userId + " is not exist");
			}
			else {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				
				String returnString = reader.readLine();
				returnString = Util.processJsonString(returnString);
				
				JSONObject object = new JSONObject(returnString);
				
				return object;
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
