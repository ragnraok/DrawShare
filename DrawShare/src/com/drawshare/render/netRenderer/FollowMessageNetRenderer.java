package com.drawshare.render.netRenderer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.PictureNotExistException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.Request.message.Message;
import com.drawshare.render.object.FollowMessage;

public class FollowMessageNetRenderer extends NetRender<FollowMessage>{

	private String userId = null;
	private String apiKey = null;
	private int num = 0;
	
	public FollowMessageNetRenderer(String userId, String apiKey, int num) {
		this.userId = userId;
		this.apiKey = apiKey;
		this.num = num;
	}
	
	@Override
	public FollowMessage JSONtoObject(JSONObject json) {
		// TODO Auto-generated method stub
		//return null;
		FollowMessage message = new FollowMessage();
		try {
			message.followUsername = json.getString("follow_username");
			message.followUserId = json.getString("follow_user_id");
			message.followUserAvatarUrl = json.getString("follow_user_avatar_url");
			message.followDate = new SimpleDateFormat("yyyy/MM/dd").parse(json.getString("follow_date"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

	@Override
	public ArrayList<FollowMessage> renderToList() throws AuthFailException,
			UserNotExistException, PictureNotExistException {
		// TODO Auto-generated method stub
		//return null;
		JSONObject followObject = Message.getFollowMessage(this.userId, this.apiKey, num);
		if (followObject != null) {
			try {
				JSONArray followArray = followObject.getJSONArray("follow_messages");
				ArrayList<FollowMessage> messageList = new ArrayList<FollowMessage>();
				for (int i = 0; i < followArray.length(); i++) {
					FollowMessage message = this.JSONtoObject(followArray.getJSONObject(i));
					messageList.add(message);
				}
				return messageList;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public FollowMessage renderToObject() {
		// TODO Auto-generated method stub
		return null;
	}

}
