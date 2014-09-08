package com.jcomdot.simplemvc.user.sqlservice;

public class SqlUpdateFailureException extends RuntimeException {

	private static final long serialVersionUID = -7520925324356672054L;

	public SqlUpdateFailureException(String message) {
		super(message);
	}

	public SqlUpdateFailureException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
