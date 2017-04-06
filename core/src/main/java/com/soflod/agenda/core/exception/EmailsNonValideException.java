package com.soflod.agenda.core.exception;

public class EmailsNonValideException extends Exception {

	public EmailsNonValideException() {
		super("Le emails non sono valide.");
	}

	public EmailsNonValideException(String message) {
		super(message);
	}

	public EmailsNonValideException(Throwable cause) {
		super(cause);
	}

	public EmailsNonValideException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmailsNonValideException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
