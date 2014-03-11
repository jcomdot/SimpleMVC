package com.jcomdot.simplemvc.user.sqlservice;

public class SqlRetrievalFailureException extends RuntimeException {

	private static final long serialVersionUID = 6738974321231132481L;

	public SqlRetrievalFailureException(String message) {
		super(message);
	}

	public SqlRetrievalFailureException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
