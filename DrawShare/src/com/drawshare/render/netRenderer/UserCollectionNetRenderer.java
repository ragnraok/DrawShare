package com.drawshare.render.netRenderer;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.Request.userprofile.UserProfile;
import com.drawshare.render.object.Picture;

/**
 * 获取用户收藏的所有照片的Renderer
 * @author ragnarok
 *
 */
public class UserCollectionNetRenderer extends NetRender<Picture>{
	
	private String userId = null;
	
	public UserCollectionNetRenderer(String userId) {
		this.userId = userId;
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
			picture.creatUserName = json.getString("create_username");
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
		JSONObject userPictObject = UserProfile.getCollectPicture(userId);
		
		try {
			JSONArray userPictArray = userPictObject.getJSONArray("collect_pictures");
			ArrayList<Picture> userPictList = new ArrayList<Picture>();
			
			for (int i = 0; i < userPictArray.length(); i++) {
				Picture picture = JSONtoObject(userPictArray.getJSONObject(i));
				userPictList.add(picture);
			}
			return userPictList;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Picture renderToObject() {
		// TODO Auto-generated method stub
		return null;
	}

}
