package com.store.services.exceptions;

public class MethodNotValidException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public MethodNotValidException(String msg) {
		super(msg);
	}
	
	public MethodNotValidException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}