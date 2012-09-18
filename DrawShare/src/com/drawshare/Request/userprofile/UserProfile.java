package com.drawshare.Request.userprofile;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
import com.drawshare.Request.exceptions.UserNotExistException;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

@TargetApi(8)
public class UserProfile {
	private static final String getProfileURL = Constant.SITE + Constant.UserProfile.GET_PROFILE;
	private static final String setProfileURL = Constant.SITE + Constant.UserProfile.SET_PROFILE;
	private static final String setAvatarURL = Constant.SITE + Constant.UserProfile.SET_AVATAR;
	private static final String getCollectURL = Constant.SITE + Constant.UserProfile.GET_COLLECT_PICTURE;
	private static final String addCollectURL = Constant.SITE + Constant.UserProfile.ADD_COLLECT_PICTURE;
	private static final String deleteCollectURL = Constant.SITE + Constant.UserProfile.DELETE_COLLECT_PICTURE;
	private static final String getFollowStatusURL = Constant.SITE + Constant.UserProfile.GET_FOLLOW_STATUS;
	private static final String getCollectStatusURL = Constant.SITE + Constant.UserProfile.GET_COLLECT_STATUS;
	
	/**
	 * 获取用户资料的方法
	 * @param userId 用户的id（服务器端的ID）
	 * @return 一个JSONObject，格式如下：
	 *                         {'username':username, 'email':email, 'short_description':short_description, 
     *                   'user_id': user_id, 'avatar_url': avatar_url, 
     *                   'collect_num': collect_num}
     * 其中，avatar_url和thumbnail_url即为头像，以及头像缩略图的地址，可以通过这个地址下载到相应的图片,
     * 而collect_num为用户收藏图片的数目
	 * @throws UserNotExistException
	 */
	public static JSONObject getProfile(String userId) throws UserNotExistException {
		Log.d(Constant.LOG_TAG, "the get profile url is " + getProfileURL + userId + "/");
		
		try {
			URL url = new URL(getProfileURL + userId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setFollowRedirects(true);
			connection.connect();
			
			if (connection.getResponseCode() == 404) {
				throw new UserNotExistException("user for id " + userId + " is not exist");
			}
			else if (connection.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				
				String returnMsg = reader.readLine();
				String returnJsonString = Util.processJsonString(returnMsg);
				Log.d(Constant.LOG_TAG, "in get profile, the returnJsonString is " + returnJsonString);
				
				JSONObject profileJson = new JSONObject(returnJsonString);
				return profileJson;
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
			Log.d(Constant.LOG_TAG, "throw exeception when init the profileJson");
		}
		return null;
	}
	
	/**
	 * 设置用户资料的方法（不包含头像，目前就只能设置short_description）
	 * @param userId 用户的id，服务端的id
	 * @param shortDescription 用户的简介
	 * @param apiKey 该用户的api key
	 * @return 一个JSONObject，格式如下：
	 * {'user_name':username, 'short_description':short_description,
     *                           'email': email, 'avatar_url': avatar_url, 
     *                           }
	 * @throws UserNotExistException 如果该userId对应的用户不存在的话，抛出此异常
	 * @throws AuthFailException 如果apiKey错误的话，抛出此异常
	 */
	public static final JSONObject setProfile(String userId, String shortDescription, String apiKey
			) throws UserNotExistException, AuthFailException {
		Log.d(Constant.LOG_TAG, "the set profile url is " + setProfileURL + userId + "/");
		
		try {
			URL url = new URL(setProfileURL + userId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", apiKey);
			connection.setFollowRedirects(true);
			connection.connect();
			
			Log.d(Constant.LOG_TAG, "success connect");
			
			// construct the post json
			JSONObject profilePostJson = new JSONObject();
			profilePostJson.put("short_description", shortDescription);
			
			Log.d(Constant.LOG_TAG, "the post json is " + profilePostJson.toString());
			
			PrintWriter writer = new PrintWriter(connection.getOutputStream());
			writer.print(profilePostJson.toString());
			writer.flush();
			writer.close();
			
			Log.d(Constant.LOG_TAG, "success post the data");
			
			Log.d(Constant.LOG_TAG, "the response message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 404) {
				// return 404 if the user is not exist
				throw new UserNotExistException("User for userId " + userId + " is not exist");
			}
			else if (connection.getResponseCode() == 401) {
				throw new AuthFailException("the apikey " + apiKey + " is error");
			}
			else if (connection.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				
				String returnMsg = reader.readLine();
				String profileInfoString = Util.processJsonString(returnMsg);
				
				Log.d(Constant.LOG_TAG, "the profileInfoString is " + profileInfoString);
				
				JSONObject profileInfoObject = new JSONObject(profileInfoString);
				
				return profileInfoObject;
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
	 * 设置用户头像的函数
	 * @param userId 用户的id（服务端的id）
	 * @param bitmap 用户的新头像
	 * @param apiKey 用户的apiKey
	 * @return 一个JSONObject，格式如下:
	 * 	{'user_name':username, 'short_description':short_description,
     *                     'email': email, 'avatar_url': avatar_url}
	 * @throws UserNotExistException 如果userId对应的用户不存在的话，抛出此异常
	 * @throws JSONException
	 * @throws AuthFailException 如果apiKey错误的话，抛出此异常
	 */
	public static JSONObject setAvatar(String userId, Bitmap bitmap, String apiKey
			) throws UserNotExistException, JSONException, AuthFailException {
		Log.d(Constant.LOG_TAG, "the set avatar url is " + setAvatarURL + userId + "/");
		
		try {
			URL url = new URL(setAvatarURL + userId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Authorization", apiKey);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setFollowRedirects(true);
			connection.connect();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos)) {
				byte[] bitmapArray = baos.toByteArray();
				// output the bitmapArray
				//OutputStream os = connection.getOutputStream();
				//os.write(bitmapArray);
				//os.flush();
				//os.close();
				String encodeString = Base64.encodeToString(bitmapArray, Base64.DEFAULT);
				PrintWriter writer = new PrintWriter(connection.getOutputStream());
				writer.print(encodeString);
				writer.flush();
				writer.close();
				Log.d(Constant.LOG_TAG, "success post the btye array");
			}
			
			
			if (connection.getResponseCode() == 404) {
				throw new UserNotExistException("the user for id " + userId + " is not exist");
			}
			else if (connection.getResponseCode() == 401) {
				throw new AuthFailException("the apikey " + apiKey + " is error");
			}
			else if (connection.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				
				String returnMsg = reader.readLine();
				String userInfoString = Util.processJsonString(returnMsg);
				
				Log.d(Constant.LOG_TAG, "in set avatar, the userInfoString is " + userInfoString);
				
				JSONObject userInfoObject = new JSONObject(userInfoString);
				
				return userInfoObject;
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取用户收藏的所有图片 
	 * @param userId 改用户的id
	 * @return 一个JSONObject, 格式如下:
	 * {collect_pictures:
     *            [{'picture_id': picture_id, 'title':title, 'create_username': username,
     *                         'picture_url':picture_url},
     *                      ...]
     * }
     *
	 * @throws UserNotExistException 如果用户不存在,抛出此异常
	 */
	public static JSONObject getCollectPicture(String userId) throws UserNotExistException {
		Log.d(Constant.LOG_TAG, "in get user collect picture, the url is " + getCollectURL + userId + "/");
		
		try {
			URL url = new URL(getCollectURL + userId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setFollowRedirects(true);
			connection.connect();
			
			Log.d(Constant.LOG_TAG, "in get user collect picture, the response message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 404) {
				throw new UserNotExistException("the user for id " + userId + " is not exist");
			}
			else if (connection.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				
				String responseMsg = reader.readLine();
				responseMsg = Util.processJsonString(responseMsg);
				Log.d(Constant.LOG_TAG, "in get user collect picture, the response message is " + responseMsg);
				
				JSONObject collectObject = new JSONObject(responseMsg);
				return collectObject;
				
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
	 * 收藏一张图片	
	 * @param userId 用户ID
	 * @param pictureId 图片ID
	 * @param apiKey 用户的apiKey
	 * @return	true 如果收藏成功
	 * @throws AuthFailException 如果apiKey错误，抛出此异常
	 * @throws UserNotExistException 如果用户不存在，或者图片不存在，跑出此异常
	 */
	public static boolean collectPicture(String userId, String pictureId, 
			String apiKey) throws AuthFailException, UserNotExistException {
		Log.d(Constant.LOG_TAG, "in collect picture, the url is " + addCollectURL + userId + "/" + pictureId + "/");
		
		try {
			URL url = new URL(addCollectURL + userId + "/" + pictureId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setFollowRedirects(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", apiKey);
			connection.connect();
			
			Log.d(Constant.LOG_TAG, "in collect picture, the response message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 401) {
				throw new AuthFailException("the apiKey " + apiKey + " is error");
			}
			else if (connection.getResponseCode() == 404) {
				throw new UserNotExistException("the user for id  " + userId  + " is not exist");
			}
			else if (connection.getResponseCode() == 200) {
				return true;
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
	
	/**
	 * 取消收藏一张照片
	 * @param apiKey
	 * @param userId
	 * @param pictureId
	 * @return true如果取消成功
	 * @throws AuthFailException 当apiKey错误时,抛出此异常
	 * @throws UserNotExistException 当用户不存在时,抛出此异常
	 */
	public static boolean deleteCollectPicture(String apiKey, String userId, String pictureId) throws AuthFailException, UserNotExistException {
		Log.d(Constant.LOG_TAG, "in delete collect picture, the url is " + deleteCollectURL + userId + "/" + pictureId + "/");
		
		try {
			URL url = new URL(deleteCollectURL + userId + "/" + pictureId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", apiKey);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setFollowRedirects(true);
			connection.connect();
			
			Log.d(Constant.LOG_TAG, "in delete collect picture, the response message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 401) {
				throw new AuthFailException("the apikey " + apiKey + " is error");
			}
			else if (connection.getResponseCode() == 404) {
				throw new UserNotExistException("the user for id  " + userId + " is not exist");
			}
			else if (connection.getResponseCode() == 200) {
				return true;
			}
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
	
	/**
	 * 判断userId 是否关注了 followUserId
	 * @param userId
	 * @param followUserId
	 * @return true如果userId关注了followUserId
	 * @throws UserNotExistException 当用户不存在的时候，抛出此异常
	 */
	public static boolean getFollowStatus(String userId, String followUserId) throws UserNotExistException {
		Log.d(Constant.LOG_TAG, "in get follow status, the url is " + getFollowStatusURL + userId + "/" + followUserId + "/");
		try {
			URL url = new URL(getFollowStatusURL + userId + "/" + followUserId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setFollowRedirects(true);
			connection.connect();
			
			Log.d(Constant.LOG_TAG, "in get follow status, the response message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 404) {
				throw new UserNotExistException();
			}
			else if (connection.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				
				String returnString = reader.readLine();
				returnString = Util.processJsonString(returnString);
				Log.d(Constant.LOG_TAG, "in get follow status, the return json is " + returnString);
				
				JSONObject object = new JSONObject(returnString);
				boolean ifFollow = object.getBoolean("follow_status");
				return ifFollow;
				
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
	
	/**
	 * 判断用户是否收藏了某图片
	 * @param userId
	 * @param pictId
	 * @return true如果收藏了某图片
	 * @throws PictureNotExistException 
	 */
	public static boolean getCollectStatus(String userId, String pictId) throws PictureNotExistException {
		Log.d(Constant.LOG_TAG, "in get collect status, the url is " + getCollectStatusURL + userId + "/" + pictId + "/");
		try {
			URL url = new URL(getCollectStatusURL + userId + "/" + pictId + "/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setFollowRedirects(true);
			connection.connect();
			
			Log.d(Constant.LOG_TAG, "in get collect status, the return message is " + connection.getResponseMessage());
			
			if (connection.getResponseCode() == 404) {
				throw new PictureNotExistException();
			}
			else if (connection.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				
				String returnMsg = reader.readLine();
				returnMsg = Util.processJsonString(returnMsg);
				Log.d(Constant.LOG_TAG, "in get collect status, the return json is " + returnMsg);
				
				JSONObject object = new JSONObject(returnMsg);
				boolean ifCollect = object.getBoolean("collect_status");
				return ifCollect;
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
