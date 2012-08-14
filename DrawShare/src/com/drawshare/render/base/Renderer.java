package com.drawshare.render.base;

import java.util.ArrayList;

import org.json.JSONObject;

import com.drawshare.Request.exceptions.AuthFailException;
import com.drawshare.Request.exceptions.UserNotExistException;

public abstract class Renderer<T> {
	
	/**
	 * 将JSONObject转换成对应的类型
	 * @param json
	 * @return
	 */
	public T JSONtoObject(JSONObject json) {
		return null;
	}
	
	/**
	 * 进行实际的render，返回的是单独的一个对象
	 * @return
	 */
	public T renderToObject() {
		return null;
	}
	
	/**
	 * 进行实际的render, 返回ArrayList
	 * @return
	 * @throws UserNotExistException 
	 * @throws AuthFailException 
	 */
	public ArrayList<T> renderToList() throws AuthFailException, UserNotExistException {
		return null;
	}
}
