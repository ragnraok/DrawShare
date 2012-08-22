package com.drawshare.datastore;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 用于设置和获取已经登录的本用户的userId的类，
 * @author ragnarok
 *
 */
public class UserIdHandler {
	private static final String userIdPreferencesName = "userId";
	private static final String userIdKey = "USER_ID";
	
	
	/**
	 * 设置登录用户的userId
	 * @param context
	 * @param userId
	 */
	public static void setUserId(Context context, String userId) {
		SharedPreferences preferences = context.getSharedPreferences(userIdPreferencesName, Context.MODE_WORLD_WRITEABLE);
		Editor editor = preferences.edit();
		editor.clear();
		editor.putString(userIdKey, userId);
		editor.commit();
		
	}
	
	/**
	 * 获取目前登录用户的user id
	 * @param context
	 * @return 用户的user id，如果不存在，返回null
	 */
	public static String getUserId(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(userIdPreferencesName, Context.MODE_WORLD_READABLE);
		String userId  = preferences.getString(userIdKey, null);
		return userId;
	}
}
