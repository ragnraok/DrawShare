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

import android.util.Log;

public class PictureComment {
	private static final String getPictCommentsURL = Constant.SITE + Constant.Picture.GET_PICT_COMMENTS;
	private static final String addPictCommentURL = Constant.SITE + Constant.Picture.ADD_PICT_COMMENT;
	
	/**
	 * 获取某张图片的所有评论	
	 * @param pictureId 图片的id
	 * @return 一个JSONObject,格式如下:
	 * {'pict_id': pict_id, 
     *                   {'pict_comments': [{'pict_comment': comment, 'comment_user_id': user_id, 
     *                   'comment_date': YYYY/MM/DD, 'comment_user_name': username, 'comment_user_avatar_url': avatar_url}, 
     *                       ........]
     *                   }}
	 * @throws PictureNotExistException，当picutureId对应的图片不存在的时候,抛出此异常
	 */
	public static JSONObject getPictComments(String pictureId
			) throws PictureNotExistException {
		Log.d(Constant.LOG_TAG, "in get picture comments, the url is " + getPictCommentsURL + pictureId + "/");
		
		try {
			URL url = new URL(getPictCommentsURL + pictureId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setFollowRedirects(true);
			connection.connect();
			
			Log.d(Constant.LOG_TAG, "in get picture comments, the response message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 404) {
				throw new PictureNotExistException("picuture for id " + pictureId + " is not exist");
			}
			else if (connection.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				
				String returnMsg = reader.readLine();
				returnMsg = Util.processJsonString(returnMsg);
				
				JSONObject commentsObject = new JSONObject(returnMsg);
				return commentsObject;
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
	 * 为某张图片添加评论
	 * @param userId 用户的id
	 * @param pictureId 图片的id
	 * @param apiKey 用户的apiKey
	 * @param pictComment 图片的评论
	 * @param evalScore 图片的评分
	 * @return true如果成功添加评论
	 * @throws AuthFailException 如果apiKey错误，抛出此异常
	 * @throws PictureOrUserNotExistException 如果userId对应的用户或者pictureId对应的图片不存在,抛出此异常
	 */
	public static boolean addPictComment(String userId, String pictureId, String apiKey,
			String pictComment, int evalScore) throws AuthFailException, PictureOrUserNotExistException {
		Log.d(Constant.LOG_TAG, "in add pict comment, the url is " + addPictCommentURL + userId + "/" + pictureId + "/");
		
		try {
			URL url  = new URL(addPictCommentURL + userId + "/" + pictureId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", apiKey);
			connection.connect();
			
			PrintWriter writer = new PrintWriter(connection.getOutputStream());
			// construct the output json
			JSONObject outCommentObject = new JSONObject();
			outCommentObject.put("pict_comment", pictComment);
			outCommentObject.put("eval_score", evalScore);
			writer.print(outCommentObject.toString());
			writer.flush();
			writer.close();
			
			Log.d(Constant.LOG_TAG, "in add pict comment, the post data is " + outCommentObject.toString());
			Log.d(Constant.LOG_TAG, "in add pict comment, the response message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 401) {
				throw new AuthFailException("the apiKey " + apiKey + " is error");
			}
			else if (connection.getResponseCode() == 404) {
				throw new PictureOrUserNotExistException("picture for id " + pictureId + " or user for id  " + userId +
						" is not exist");
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
