package com.drawshare.render.netRenderer;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.drawshare.Request.picture.TopPict;
import com.drawshare.datastore.cache.JsonCache;
import com.drawshare.render.object.Picture;

public class HotestPictNetRenderer extends NetRender<Picture> {

	private int num = 0;
	
	public HotestPictNetRenderer(int num) {
		this.num = num;
	}
	
	/**
	 * 只是转换json,对应的bitmap则不进行获取
	 */
	@Override
	public Picture JSONtoObject(JSONObject json) {
		// TODO Auto-generated method stub
		//return super.JSONtoObject(json);
		Picture picture = new Picture();
		try {
			picture.pictureId = json.getString("picture_id");
			picture.createUserId = json.getString("create_user_id");
			picture.creatUserName = json.getString("create_username");
			picture.title = json.getString("title");
			picture.pictURL = json.getString("picture_url");
			picture.avgScore = (float) json.getDouble("picture_score");
			return picture;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return picture;
	}

	/**
	 * 返回对象为ArrayList<Picture>
	 */
	@Override
	public ArrayList<Picture> renderToList()  {
		// TODO Auto-generated method stub
		JSONObject pictsObject = TopPict.getTopScorePict(num);
		if (pictsObject != null) {
			// first cache it
			//JsonCache.getJsonCache().cacheHotestPictJson(pictsObject.toString());
			// then parser it
			try {
				JSONArray pictArray = pictsObject.getJSONArray("pictures");
				ArrayList<Picture> pictList = new ArrayList<Picture>(this.num);
				
				for (int i = 0; i < pictArray.length(); i++) {
					Picture pict = this.JSONtoObject(pictArray.getJSONObject(i));
					pictList.add(pict);
				}
				return pictList;
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

}
