package com.drawshare.datastore;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 用于获取和设置apikey的类，在以后每个设置请求中都会用到
 * @author ragnarok
 * apiKey存放在名为apikey的sharedpreference中，key为userId，value为apiKey
 */
public class ApiKeyHandler {
	private static final String apiPreferencesName = "apikey";
	//private static final String KEY_USERID = "userId";
	//private static final String KEY_APIKEY = "apiKey";
	
	/**
	 * 设置用户的apiKey
	 * @param context
	 * @param userId
	 * @param apiKey
	 */
	public static void setApiKey(Context context, String userId, String apiKey) {
		
		SharedPreferences preferences = context.getSharedPreferences(apiPreferencesName, Context.MODE_WORLD_WRITEABLE);
		Editor editor = preferences.edit();
		editor.clear(); // first clear the old data, if it had
		editor.putString(userId, apiKey);
		editor.commit();
	}
	
	/**
	 * 获取用户的apiKey
	 * @param context
	 * @param userId
	 * @return apiKey, 若对应的值不存在, 返回null
	 */
	public static String getApiKey(Context context, String userId) {
		SharedPreferences preferences = context.getSharedPreferences(apiPreferencesName, Context.MODE_WORLD_READABLE);
		String apiKey = preferences.getString(userId, null);
		return apiKey;
	}
}
