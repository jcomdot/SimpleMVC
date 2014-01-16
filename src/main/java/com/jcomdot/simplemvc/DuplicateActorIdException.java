package com.jcomdot.simplemvc;

public class DuplicateActorIdException extends RuntimeException {

	private static final long serialVersionUID = 3121126639856526449L;

	public DuplicateActorIdException(Throwable cause) {
		super(cause);
	}
}
