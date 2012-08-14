package com.drawshare.render.cacheRenderer;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.drawshare.datastore.cache.ImageCache;
import com.drawshare.datastore.cache.JsonCache;
import com.drawshare.render.object.Picture;

public class HotestPictCacheRenderer extends CacheRender<Picture> {

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
			picture.pictThumbnailURL = json.getString("picture_thumbnail_url");
			picture.avgScore = (float) json.getDouble("picture_score");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			picture.pict = ImageCache.getImageCache().getCacheImage(picture.pictURL);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			picture.pict = null;
		}
		
		try {
			picture.pictThumbnail = ImageCache.getImageCache().getCacheImage(picture.pictThumbnailURL);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			picture.pictThumbnail = null;
		}
		return picture;
	}

	/**
	 * 返回对象为ArrayList<Picture>，使用时务必检查其中的属性是否为null
	 * 若缓存不存在，返回null;
	 */
	@Override
	public ArrayList<Picture> renderToList() {
		// TODO Auto-generated method stub
		//return super.render();
		JSONObject hotestPictObject = JsonCache.getJsonCache().getHotestPictJsonCache();
		if (hotestPictObject != null) {
			try {
				JSONArray hotestPictArray = hotestPictObject.getJSONArray("pictures");
				ArrayList<Picture> hotestPictList = new ArrayList<Picture>();
				for (int i = 0; i < hotestPictArray.length(); i++) {
					Picture pict = this.JSONtoObject(hotestPictArray.getJSONObject(i));
					hotestPictList.add(pict);
				}
				return hotestPictList;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
