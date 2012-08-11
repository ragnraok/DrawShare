package com.drawshare.Request.userprofile;

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
import com.drawshare.Request.exceptions.UserExistException;

import android.util.Log;

public class UserRegister {
	private static final String registerURL = Constant.SITE + Constant.UserProfile.REGISTER;
	
	/**
	 * 用户注册函数
	 * @param username
	 * @param email
	 * @param password
	 * @param shortDescription 简介
	 * @return 一个JSONObject, 格式为：
	 * {'username': username, 'user_id':user_id, 'api_key':api_key}, 其中，user_id和api_key在以后会用到，最好保存下来
	 * @throws UserExistException 如果用户已经存在的话，抛出此异常
	 */
	public static JSONObject register(String username, String email, String password, String shortDescription) throws UserExistException {
		Log.d(Constant.LOG_TAG, "the register url is " + registerURL);
		 
		try {
			URL url = new URL(registerURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.connect();
			
			PrintWriter writer = new PrintWriter(connection.getOutputStream());
			// construct the post json string
			JSONObject registerJson = new JSONObject();
			registerJson.put("username", username);
			registerJson.put("email", email);
			registerJson.put("password", password);
			registerJson.put("short_description", shortDescription);
			
			writer.print(registerJson.toString());
			writer.flush();
			writer.close();
			
			// if the user was already existed, return error code 406
			if (connection.getResponseCode() == 406) {
				throw new UserExistException("User " + username + " was existed");
			}
			else if (connection.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				
				String returnMsg = reader.readLine();
				Log.d(Constant.LOG_TAG, "in register, the return message is " + returnMsg);
				
				String returnString = Util.processJsonString(returnMsg);
				Log.d(Constant.LOG_TAG, "in register, the return string is " + returnString);
				
				JSONObject resultObject = new JSONObject(returnString);
				return resultObject;
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
