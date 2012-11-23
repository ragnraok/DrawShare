package com.drawshare.render.netRenderer;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.Request.picture.UserPicture;
import com.drawshare.render.object.Picture;

/**
 * 获取用户的所有照片的Renderer
 * @author ragnarok
 *
 */
public class UserPictureNetRenderer extends NetRender<Picture>{

	private String userId = null;
	
	public UserPictureNetRenderer(String userId) {
		this.userId = userId;
	}
	
	@Override
	public Picture renderToObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Picture JSONtoObject(JSONObject json) {
		// TODO Auto-generated method stub
		//return super.JSONtoObject(json);
		Picture picture = new Picture();
		
		try {
			picture.pictureId = json.getString("picture_id");
			picture.title = json.getString("title");
			picture.pictURL = json.getString("picture_url");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return picture;
	}

	@Override
	public ArrayList<Picture> renderToList() throws 
			UserNotExistException {
		// TODO Auto-generated method stub
		//return super.renderToList();
		JSONObject pictureJsonObject = UserPicture.getUserPict(userId);
		try {
			JSONArray pictureArray = pictureJsonObject.getJSONArray("pictures");
			ArrayList<Picture> pictureList = new ArrayList<Picture>();
			
			for (int i = 0; i < pictureArray.length(); i++) {
				Picture picture = JSONtoObject(pictureArray.getJSONObject(i));
				pictureList.add(picture);
			}
			return pictureList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
