package com.drawshare.render.cacheRenderer;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.drawshare.datastore.cache.ImageCache;
import com.drawshare.datastore.cache.JsonCache;
import com.drawshare.render.object.FriendActivity;

public class FriendsNewsCacheRenderer extends CacheRender<FriendActivity> {

	@Override
	public FriendActivity JSONtoObject(JSONObject json) {
		// TODO Auto-generated method stub
		FriendActivity activity = new FriendActivity();
		try {
			activity.userName = json.getString("username");
			activity.userId = json.getString("userid");
			activity.pictureId = json.getString("pictureId");
			activity.pictureTitle = json.getString("picture_title");
			activity.pictureURL = json.getString("picture_url");
			activity.editType = json.getInt("edit_type");
			activity.editDate = new SimpleDateFormat("yyyy/MM/dd").parse(json.getString("edit_date"));
			activity.userAvatarUrl = json.getString("avatar_url");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		try {
			activity.picture = ImageCache.getImageCache().getCacheImage(activity.pictureURL);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			activity.picture = null;
		}**/ catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return activity;
	}

	/**
	 * 返回对象为ArrayList<FriendActivity>，获得对象属性的时候务必检查是否为null或者为默认值
	 * 若缓存不存在，返回null;
	 */
	@Override
	public ArrayList<FriendActivity> renderToList() {
		// TODO Auto-generated method stub
		//return super.render();
		JSONObject friendJsonObject = JsonCache.getJsonCache().getFriendNewsJsonCache();
		if (friendJsonObject != null) {
			try {
				JSONArray newsArray = friendJsonObject.getJSONArray("activities");
				ArrayList<FriendActivity> news = new ArrayList<FriendActivity>();
				for (int i = 0; i < newsArray.length(); i++) {
					JSONObject activity = newsArray.getJSONObject(i);
					FriendActivity friendActivity = (FriendActivity) this.JSONtoObject(activity);
					news.add(friendActivity);
				}
				return news;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public FriendActivity renderToObject() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
