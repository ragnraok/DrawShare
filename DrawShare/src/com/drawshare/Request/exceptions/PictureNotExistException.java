package com.drawshare.Request.exceptions;

public class PictureNotExistException extends Exception {

	public String msg = "";
	
	public PictureNotExistException(String msg) {
		this.msg = msg;
	}
	
	public PictureNotExistException() {
		
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		//return super.getMessage();
		
		if (msg.equals(null)) {
			return "Picture is not exist";
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
