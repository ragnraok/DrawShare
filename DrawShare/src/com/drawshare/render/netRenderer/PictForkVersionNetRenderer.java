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
import com.drawshare.Request.picture.PictEditHistory;
import com.drawshare.render.object.Picture;

public class PictForkVersionNetRenderer extends NetRender<Picture> {

	private JSONObject renderJson = null;
	private String pictureId = null;
	
	public PictForkVersionNetRenderer(String pictureId) {
		this.pictureId = pictureId;
	}
	
	@Override
	public Picture JSONtoObject(JSONObject json) {
		// TODO Auto-generated method stub
		Picture picture = new Picture();
		
		try {
			picture.pictureId = json.getString("picture_id");
			picture.pictURL = json.getString("picture_url");
			picture.createDate = new SimpleDateFormat("yyyy/MM/dd").parse(json.getString("edit_date"));
			picture.creatUserName = json.getString("create_username");
			picture.title = json.getString("title");
			picture.createUserAvatarUrl = json.getString("avatar_url");
			picture.createUserId = json.getString("user_id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			picture.createDate = null;
		}
		return picture;
	}

	/**
	 * 获取所有的修改版本
	 */
	@Override
	public ArrayList<Picture> renderToList() throws AuthFailException,
			UserNotExistException, PictureNotExistException {
		// TODO Auto-generated method stub
		try {
			JSONArray forkArray = this.getRenderJson().getJSONArray("fork_picture");
			ArrayList<Picture> forkList = new ArrayList<Picture>();
			
			for (int i = 0; i < forkArray.length(); i++) {
				Picture picture = JSONtoObject(forkArray.getJSONObject(i));
				forkList.add(picture);
			}
			return forkList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取原图信息
	 */
	@Override
	public Picture renderToObject() {
		// TODO Auto-generated method stub
		try {
			JSONObject originPictJsonObject = this.getRenderJson().getJSONObject("origin_picture");
			Picture picture = JSONtoObject(originPictJsonObject);
			return picture;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PictureNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 判断是否为新的照片
	 * @return
	 */
	public boolean ifNewPict() {
		try {
			JSONObject json = this.getRenderJson().getJSONObject("origin_picture");
			if (this.pictureId.equals(json.getString("picture_id"))) {
				return true;
			}
			else {
				return false;
			}
		} catch (PictureNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private JSONObject getRenderJson() throws PictureNotExistException {
		if (this.renderJson == null) {
			this.renderJson = PictEditHistory.getPictEditHistory(pictureId);
			return this.renderJson;
		}
		else {
			return this.renderJson;
		}
	}

}
