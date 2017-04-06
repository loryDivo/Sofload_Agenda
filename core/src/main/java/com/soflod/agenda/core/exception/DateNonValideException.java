package com.soflod.agenda.core.exception;

public class DateNonValideException extends Exception {

	public DateNonValideException() {
		super("Le date di inizio e fine dell'impergno inserite non sono valide.");
	}

	public DateNonValideException(String message) {
		super(message);
	}

	public DateNonValideException(Throwable cause) {
		super(cause);
	}

	public DateNonValideException(String message, Throwable cause) {
		super(message, cause);
	}

	public DateNonValideException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
