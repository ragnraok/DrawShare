package com.drawshare.Request.exceptions;

public class UserExistException extends Exception {
	public String msg = "";
	private String defaultMsg = "User was already existed";
	
	public UserExistException(String msg) {
		this.msg = msg;
	}
	
	public UserExistException() {
		
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
