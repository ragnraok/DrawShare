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
import com.drawshare.Request.exceptions.PictureOrUserNotExistException;

import android.graphics.Bitmap;
import android.util.Log;

public class PictEditHistory {
	private static final String getEditHistoryURL = Constant.SITE + Constant.Picture.GET_PICT_EDIT_HISTORY;
	private static final String forkPictURL = Constant.SITE + Constant.Picture.FORK_PICT;
	
	/**
	 * 获取一张图片的编辑历史
	 * @param pictureId 图片的id
	 * @return 一个JSONObject,格式如下:
	 * {'origin_picture': 
     *          {'picture_id': pict_id, 'picture_url': url, 
     *                     'edit_date': YYYY/MM/DD,
     *                        'create_username': username, 'title': title},
     *                         'fork_picture': [{'picture_id': picture_id, 'picture_url': url,
     *                          'create_username': username, 'edit_date': YYYY/MM/DD,
     *                           'title': title},
     *                  ....]
     *  }
     *  如果在'origin_picture'中,如果'picture_id'和方法中的pictureId一样,并且'fork_picture'为空,则为全新的图片,即为不是从
     *  任何一张图片fork出来,而是由完全由用户自己创作的
	 * @throws PictureNotExistException
	 */
	public static final JSONObject getPictEditHistory(String pictureId) throws PictureNotExistException {
		Log.d(Constant.LOG_TAG, "in get picture edit history, the url is " + getEditHistoryURL + pictureId + "/");
		
		try {
			URL url = new URL(getEditHistoryURL + pictureId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setFollowRedirects(true);
			connection.connect();
			
			Log.d(Constant.LOG_TAG, "in get picture edit history, the response message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 404) {
				throw new PictureNotExistException("the picture for id " + pictureId + " is not exist");
			}
			else if (connection.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				
				String returnMsg = reader.readLine();
				returnMsg = Util.processJsonString(returnMsg);
				Log.d(Constant.LOG_TAG, "in get picture edit history, the returnMsg is " + returnMsg);
				
				JSONObject historyObject = new JSONObject(returnMsg);
				return historyObject;
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
	
	public static final boolean forkPicture(String userId, String originPictId, 
			String apiKey, String title, String intro, Bitmap newPict) throws AuthFailException, PictureOrUserNotExistException {
		Log.d(Constant.LOG_TAG, "in fork picture, the url is " + forkPictURL + userId + "/" + originPictId + "/");
		
		try {
			URL url = new URL(forkPictURL + userId + "/" + originPictId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setFollowRedirects(true);
			connection.setRequestProperty("Authorization", apiKey);
			connection.setRequestMethod("POST");
			connection.connect();
			
			PrintWriter writer = new PrintWriter(connection.getOutputStream());
			// construst the post data
			String pictEncodeString = Util.bitmapToBase64String(newPict);
			JSONObject pictObject = new JSONObject();
			pictObject.put("title", title);
			pictObject.put("intro", intro);
			pictObject.put("new_picture", pictEncodeString);
			writer.print(pictObject.toString());
			writer.flush();
			writer.close();
			
			Log.d(Constant.LOG_TAG, "in fork picture, the return message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 401) {
				throw new AuthFailException("the apiKey " + apiKey + " is error");
			}
			else if (connection.getResponseCode() == 404) {
				throw new PictureOrUserNotExistException("the picture for id " + originPictId + " or the " +
						"user for id " + userId + " is not exist");
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
	
	
	
	
	
	
}
