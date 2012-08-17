package com.drawshare.datastore;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserNameHandler {
	private static final String usernamePreferenceName = "username";
	
	private static final String KEY = "user_name";
	
	/**
	 * 设置已登录用户的username
	 * @param context
	 * @param username
	 */
	public static void setUserName(Context context, String username) {
		SharedPreferences preferences = context.getSharedPreferences(usernamePreferenceName, Context.MODE_WORLD_WRITEABLE);
		Editor editor = preferences.edit();
		editor.clear(); // first clear the old data, if it had
		editor.putString(KEY, username);
		editor.commit();
	}
	
	/**
	 * 获取已登录用户的usernmae
	 * @param context
	 * @return username, 若不存在，返回null
	 */
	public static String getUserName(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(usernamePreferenceName, Context.MODE_WORLD_READABLE);
		String username = preferences.getString(KEY, null);
		return username;
	}
}
