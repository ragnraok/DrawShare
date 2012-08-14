package com.drawshare.datastore.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;

public class JsonCache extends BaseCache {
	
	private static final String FRIENDS_NEWS_DIR = "/" + CacheConstant.CACHE_BASE_DIR + "/" + CacheConstant.JSON_CACHE +
			"/" + CacheConstant.FRIEND_ACTIVITIES + "/";
	private static final String HOTEST_PICT_DIR = "/" + CacheConstant.CACHE_BASE_DIR + "/" + CacheConstant.JSON_CACHE + 
			"/" + CacheConstant.HOTEST_PICT + "/";

	private static int FRIEND_NEWS = 1;
	private static int HOTEST_PICT = 2;
	
	private static JsonCache jsonCache = null;
	
	public static JsonCache getJsonCache() {
		if (jsonCache != null) {
			return jsonCache;
		}
		else {
			jsonCache = new JsonCache();
			return jsonCache;
		}
	}
	
	private JsonCache() {
		//super(context);
		// TODO Auto-generated constructor stub
	}
	
	private void writeJsonToFile(String json, String filePathName) {
		FileOutputStream fouts = this.getFileOutputStream(filePathName);
		PrintWriter writer = new PrintWriter(fouts);
		writer.print(json);
		writer.flush();
		writer.close();
		try {
			fouts.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private JSONObject readJsonFromFile(String filePathName) throws FileNotFoundException {
		FileInputStream fins = this.getFileInputStream(filePathName);
		InputStreamReader isr = new InputStreamReader(fins);
		BufferedReader reader = new BufferedReader(isr);
		
		String line = null;
		StringBuffer json = new StringBuffer("");
		
		try {
			while ((line = reader.readLine()) != null) {
				json.append(line);
			}
			return new JSONObject(json.toString());
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
	 * the json file name format is: "typename_cache"
	 * @param cacheDate
	 * @param type
	 * @return
	 */
	private String getFileNameFromType(int type) {
		String suffix = "cache";
		if (type == FRIEND_NEWS) { 
			String fileName = CacheConstant.FRIEND_ACTIVITIES + suffix;
			return fileName;
		}
		else if (type == HOTEST_PICT) { 
			String fileName = CacheConstant.HOTEST_PICT + suffix;
			return fileName;
		}
		return null;
	}
	
	/**
	 * 缓存最热门图片的json
	 * @param json 最热门图片的json
	 */
	public void cacheHotestPictJson(String json) {
		writeJsonToFile(json, HOTEST_PICT_DIR + getFileNameFromType(HOTEST_PICT));
	}
	
	/**
	 * 缓存好友最新消息的json
	 * @param json 好友最新消息的json
	 */
	public void cacheFriendNewsJson(String json) {
		writeJsonToFile(json, FRIENDS_NEWS_DIR + getFileNameFromType(FRIEND_NEWS));
	}
	
	/**
	 * 获取缓存中最热门图片的json
	 * @return 一个JSONObject, 格式如下:
	 * {'pictures': 
     *            [{'picture_id': picture_id, 'create_user_id': user_id, 'create_username': username, 
     *                   'create_user_avatar_url': avatar_url, 'title': title, 'picture_url': picture_url, 
     *                      'picture_thumbnail_url': picture_thumbnail_url, 'picture_score': score}, ......]
     *    }
     * 若缓存不存在,返回null;
	 */
	public JSONObject getHotestPictJsonCache() {
		try {
			JSONObject hotestPictObject = this.readJsonFromFile(HOTEST_PICT_DIR + getFileNameFromType(HOTEST_PICT));
			return hotestPictObject;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取缓存中好友最新消息的json缓存
	 * @return 一个JSONObject,格式如下:
	 * {activities:[
     *        {'userid': userid, 'username': username, 'picture_id': picture_id, 'picture_title': title, 
     *               'picture_url': picture_url, 
     *                   'thumbnail_url': thumbnail_url, 'edit_type': edit_type, 'edit_date': YYYY/MM/DD}, ...
     *                   ]}
     * the edit_type 1 is create, 2 is fork
     * 若缓存不存在，返回null
	 */
	public JSONObject getFriendNewsJsonCache() {
		try {
			JSONObject friendNewsObject = this.readJsonFromFile(FRIENDS_NEWS_DIR + getFileNameFromType(FRIEND_NEWS));
			return friendNewsObject;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public boolean ifExistFriendNewsCache() {
		String filePathName = FRIENDS_NEWS_DIR + this.getFileNameFromType(FRIEND_NEWS);
		File file = new File(Environment.getExternalStorageDirectory() + filePathName);
		return file.isFile() && file.exists();
	}
	
	public boolean ifExistHotestPictCache() {
		String filePathName = FRIENDS_NEWS_DIR + this.getFileNameFromType(HOTEST_PICT);
		File file = new File(Environment.getExternalStorageDirectory() + filePathName);
		return file.isFile() && file.exists();
	}
	
}
