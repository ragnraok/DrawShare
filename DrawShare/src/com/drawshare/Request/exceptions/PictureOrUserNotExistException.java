package com.drawshare.Request.exceptions;

public class PictureOrUserNotExistException extends Exception {

	public String msg = "";
	
	public PictureOrUserNotExistException(String msg) {
		this.msg = msg;
	}
	
	public PictureOrUserNotExistException() {
		
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		//return super.getMessage();
		
		if (msg.equals(null)) {
			return "picture or user is not exist";
		}
		else {
			return this.msg;
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		//return super.toString();
		return this.getMessage();
	}
}
