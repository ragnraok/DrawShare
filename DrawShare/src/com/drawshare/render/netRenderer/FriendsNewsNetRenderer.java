package com.drawshare.render.netRenderer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.Request.useractivity.UserActivity;
import com.drawshare.datastore.ApiKeyHandler;
import com.drawshare.datastore.UserIdHandler;
import com.drawshare.datastore.cache.JsonCache;
import com.drawshare.render.object.FriendActivity;

public class FriendsNewsNetRenderer extends NetRender<FriendActivity> {

	private String userId = null;
	private String apiKey = null;
	private int num = 10;
	
	public FriendsNewsNetRenderer(String userId, String apiKey, int num) {
		this.userId = userId;
		this.num = num;
	}
	
	
	public FriendsNewsNetRenderer(Context context, int num) {
		this.context = context;
		this.userId = UserIdHandler.getUserId(context);
		this.apiKey = ApiKeyHandler.getApiKey(context);
		this.num = num;
	}
	
	/**
	 * 将对应的JSONObject转换成FriendActivity对象，但是并不会下载图片
	 * 只是json
	 */
	@Override
	public FriendActivity JSONtoObject(JSONObject json) {
		// TODO Auto-generated method stub
		//return super.JSONtoObject(json);
		FriendActivity activity = new FriendActivity();
		try {
			activity.userName = json.getString("username");
			activity.userId = json.getString("userid");
			activity.pictureId = json.getString("pictureId");
			activity.pictureTitle = json.getString("picture_title");
			activity.pictureURL = json.getString("picture_url");
			activity.editType = json.getInt("edit_type");
			SimpleDateFormat dateFormat = new SimpleDateFormat(json.getString("yyyy/MM/dd"));
			activity.editDate = dateFormat.getCalendar();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return activity;
	}

	/**
	 * 返回对象为ArrayList<FriendActivity>
	 * @throws AuthFailException, 当apiKey错误时,抛出此异常
	 * @throws UserNotExistException, 当userId对应的用户不存在时,抛出此异常
	 */
	@Override
	public ArrayList<FriendActivity> renderToList() throws AuthFailException, UserNotExistException {
		// TODO Auto-generated method stub
		//return super.render();
		JSONObject activityObject = UserActivity.getFollowActivities(userId, apiKey, num);
		if (activityObject != null) {
			// first cache the json
			//JsonCache.getJsonCache().cacheFriendNewsJson(activityObject.toString());
			// then parser it
			JSONArray activityArray;
			try {
				activityArray = activityObject.getJSONArray("activities");
				ArrayList<FriendActivity> activityList = new ArrayList<FriendActivity>();
				for (int i = 0; i < activityArray.length(); i++) {
					FriendActivity activity = this.JSONtoObject(activityArray.getJSONObject(i));
					activityList.add(activity);
				}
				return activityList;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

}
