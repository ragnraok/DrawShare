package com.drawshare.render.netRenderer;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.PictureNotExistException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.Request.picture.PictureComment;
import com.drawshare.render.object.Comment;

/**
 * 获取某张图片的所有评论的Renderer
 * @author ragnarok
 *
 */
public class PictCommentNetRenderer extends NetRender<Comment> {

	private String pictureId = null;
	
	public PictCommentNetRenderer(String pictureId) {
		this.pictureId = pictureId;
	}
	
	@Override
	public Comment JSONtoObject(JSONObject json) {
		// TODO Auto-generated method stub
		Comment comment = new Comment();
		
		try {
			comment.pictureComment = new String(json.getString("pict_comment").getBytes("UTF-8"), "UTF-8");
			comment.pictureId = this.pictureId;
			comment.commentDate = new SimpleDateFormat("yyyy/MM/dd").parse(json.getString("comment_date"));
			comment.commentUserName = json.getString("comment_user_name");
			comment.commentUserId = json.getString("comment_user_id");
			comment.commentUserAvatarUrl = json.getString("comment_user_avatar_url");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			comment.commentDate = null;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return comment;
	}

	@Override
	public ArrayList<Comment> renderToList() throws PictureNotExistException {
		JSONObject commentJsonObject = PictureComment.getPictComments(pictureId);
		
		try {
			JSONArray commentArray = commentJsonObject.getJSONArray("pict_comments");
			ArrayList<Comment> commentList = new ArrayList<Comment>();
			
			for (int i = 0; i < commentArray.length(); i++) {
				Comment comment = JSONtoObject(commentArray.getJSONObject(i));
				commentList.add(comment);
			}
			return commentList;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Comment renderToObject() {
		// TODO Auto-generated method stub
		return null;
	}

}
