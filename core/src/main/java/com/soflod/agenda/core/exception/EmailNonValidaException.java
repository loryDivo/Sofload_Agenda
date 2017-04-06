package com.soflod.agenda.core.exception;

public class EmailNonValidaException extends Exception {

	public EmailNonValidaException() {
		super("L'email non e' valida.");
	}

	public EmailNonValidaException(String message) {
		super(message);
	}

	public EmailNonValidaException(Throwable cause) {
		super(cause);
	}

	public EmailNonValidaException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmailNonValidaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
