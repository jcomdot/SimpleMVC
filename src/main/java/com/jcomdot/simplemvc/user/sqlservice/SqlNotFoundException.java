package com.jcomdot.simplemvc.user.sqlservice;

public class SqlNotFoundException extends RuntimeException {


	private static final long serialVersionUID = -3355122691899109273L;

	public SqlNotFoundException(String message) {
		super(message);
	}

	public SqlNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
