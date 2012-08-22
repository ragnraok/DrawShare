package com.drawshare.render.netRenderer;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.Request.userprofile.UserFollow;
import com.drawshare.render.object.User;

/**
 * 获取用户关注的人的Renderer
 * @author ragnarok
 *
 */
public class UserFollowedNetRenderer extends NetRender<User> {

	private String userId = null;
	
	public UserFollowedNetRenderer(String userId) {
		this.userId = userId;
	}
	
	@Override
	public User JSONtoObject(JSONObject json) {
		// TODO Auto-generated method stub
		User user = new User();
		
		try {
			user.userId = json.getString("user_id");
			user.username = json.getString("user_name");
			user.avatarUrl = json.getString("avatar_url");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public ArrayList<User> renderToList() throws AuthFailException,
			UserNotExistException {
		// TODO Auto-generated method stub
		JSONObject followInfoJsonObject = UserFollow.getFollowInfo(userId);
		
		try {
			JSONArray followInfoArray = followInfoJsonObject.getJSONArray("following");
			ArrayList<User> followInfoList = new ArrayList<User>();
			
			for (int i = 0; i < followInfoArray.length(); i++) {
				User user = JSONtoObject(followInfoArray.getJSONObject(i));
				followInfoList.add(user);
			}
			return followInfoList;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public User renderToObject() {
		// TODO Auto-generated method stub
		return null;
	}

}
