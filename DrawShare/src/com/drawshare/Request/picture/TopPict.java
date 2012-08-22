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

import android.util.Log;

public class TopPict {
	private static final String getTopPictURL = Constant.SITE + Constant.Picture.GET_TOP_PICT;
	
	/**
	 * 获取评分最高的一组图片
	 * @param num 获取图片的数量
	 * @return 一个JSONObject,格式如下:
	 *	 {'pictures': 
     *            [{'picture_id': picture_id, 'create_user_id': user_id, 'create_username': username, 
     *                   'create_user_avatar_url': avatar_url, 'title': title, 'picture_url': picture_url, 
     *                      'picture_score': score}, ......]
     *    }
	 */
	public static JSONObject getTopScorePict(int num) {
		Log.d(Constant.LOG_TAG, "in get the top score picture, the url is " + getTopPictURL + num + "/");
		
		try {
			URL url = new URL(getTopPictURL + num + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setFollowRedirects(true);
			connection.connect();
			
			Log.d(Constant.LOG_TAG, "in get the top score picture, the response message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				
				String returnMsg = reader.readLine();
				returnMsg = Util.processJsonString(returnMsg);
				
				JSONObject topPictObject = new JSONObject(returnMsg);
				return topPictObject;
				
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
