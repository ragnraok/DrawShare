package com.drawshare.Request.exceptions;

public class AuthFailException extends Exception {
	
	public String msg = "";
	
	public AuthFailException(String msg) {
		this.msg = msg;
	}
	
	public AuthFailException() {
		
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		//return super.getMessage();
		
		if (msg.equals(null)) {
			return "Authentication Failed";
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
