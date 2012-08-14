package com.drawshare.render.netRenderer;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;

import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.UserNotExistException;
import com.drawshare.render.base.Renderer;

/**
 * The NetRenderer not download the bitmap, just download the json
 * @author ragnarok
 *
 * @param <T>
 */
public abstract class NetRender<T> extends Renderer<T> {

	@Override
	public T JSONtoObject(JSONObject json) {
		// TODO Auto-generated method stub
		return super.JSONtoObject(json);
	}

	@Override
	public ArrayList<T> renderToList() throws AuthFailException, UserNotExistException {
		// TODO Auto-generated method stub
		return super.renderToList();
	}

	protected Context context;
}
