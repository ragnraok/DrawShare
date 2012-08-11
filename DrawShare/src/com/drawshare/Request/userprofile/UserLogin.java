package com.drawshare.Request.userprofile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.drawshare.Request.Constant;
import com.drawshare.Request.Util;
import com.drawshare.Request.exceptions.AuthFailException;

import android.util.Log;

public class UserLogin {
	private static final String loginURL = Constant.SITE + Constant.UserProfile.LOGIN;
	
	/**
	 * 用户登录函数
	 * @param nameOrEmail
	 * @param password
	 * @return 一个JSONObject，格式如下：
	 * {'username':username, "user_id":user_id, "api_key": api_key}
	 * 其中user_id,api_key在以后的请求中都会用到，所以最好保存起来。
	 * @throws AuthFailException 如果用户名/邮箱，或者密码错误的时候，抛出此异常
	 */
	public static JSONObject login(String nameOrEmail, String password) throws AuthFailException {
		Log.d(Constant.LOG_TAG, "the loginURL is " + loginURL);
		try {
			URL loginUrl = new URL(loginURL);
			HttpURLConnection connection = (HttpURLConnection) loginUrl.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.connect();
			
			OutputStream out = connection.getOutputStream();
			PrintWriter writer = new PrintWriter(out);
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name_or_email", nameOrEmail);
			jsonObject.put("password", password);
			
			writer.print(jsonObject.toString());
			writer.flush();
			writer.close();
			
			//Log.d(Constant.LOG_TAG, "in login, the response message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 401) {
				throw new AuthFailException("login failed");
			}
			
			else if (connection.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				
				StringBuffer returnMsg = new StringBuffer("");
				String line = "";
				
				returnMsg.append(reader.readLine());
				
				Log.d(Constant.LOG_TAG, "in login, the return msg is " + returnMsg.toString());
				
				String returnString = Util.processJsonString(returnMsg.toString());
				
				Log.d(Constant.LOG_TAG, "the returnString is " + returnString);
				
				JSONObject returnJson = new JSONObject(returnString);
				
				return returnJson;
			}
			

			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Log.d(Constant.LOG_TAG, "Throw MalformedURLException " +  e.getMessage());
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			Log.d(Constant.LOG_TAG, "Throw ProtocolException " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d(Constant.LOG_TAG, "Throw IOException " + e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d(Constant.LOG_TAG, "Throw JSONException " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
