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
import com.drawshare.render.object.ForkMessage;

public class ForkMessageNetRenderer extends NetRender<ForkMessage> { 

	private String userId = null;
	private String apiKey = null;
	private int num = 0;
	
	public ForkMessageNetRenderer(String userId, String apiKey, int num) {
		this.userId = userId;
		this.apiKey = apiKey;
		this.num = num;
	}
	
	@Override
	public ForkMessage JSONtoObject(JSONObject json) {
		// TODO Auto-generated method stub
		ForkMessage message = new ForkMessage();
		try {
			message.senderId = json.getString("sender_id");
			message.senderName = json.getString("sender_name");
			message.originPictId = json.getString("origin_pict_id");
			message.originPictTitle = json.getString("origin_pict_title");
			message.originPictURL = json.getString("origin_pict_url");
			message.resultPictId = json.getString("result_pict_id");
			message.resultPictTitle = json.getString("result_pict_title");
			message.sendDate = new SimpleDateFormat("yyyy/MM/dd").parse(json.getString("send_date"));
			return message;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<ForkMessage> renderToList() throws AuthFailException,
			UserNotExistException, PictureNotExistException {
		// TODO Auto-generated method stub
		JSONObject object = Message.getForkMessage(userId, apiKey, num);
		if (object != null) {
			try {
				JSONArray messageJsonArray = object.getJSONArray("fork_messages");
				ArrayList<ForkMessage> messageList = new ArrayList<ForkMessage>();
				for (int i = 0; i < messageJsonArray.length(); i++) {
					messageList.add(JSONtoObject(messageJsonArray.getJSONObject(i)));
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
	public ForkMessage renderToObject() {
		// TODO Auto-generated method stub
		return null;
	}

}
