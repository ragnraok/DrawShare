package com.drawshare.Request.exceptions;

public class UserNotExistException extends Exception {
	public String msg = null;
	private String defaultMsg = "User is not exist";
	
	public UserNotExistException(String msg) {
		this.msg = msg;
	}
	
	public UserNotExistException() {
		
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		//return super.getMessage();
		
		if (msg.equals(null)) {
			return defaultMsg;
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
