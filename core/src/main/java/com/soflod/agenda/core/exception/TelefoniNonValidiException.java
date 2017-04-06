package com.soflod.agenda.core.exception;

public class TelefoniNonValidiException extends Exception {

	public TelefoniNonValidiException() {
		super("I telefoni non sono validi.");
	}

	public TelefoniNonValidiException(String message) {
		super(message);
	}

	public TelefoniNonValidiException(Throwable cause) {
		super(cause);
	}

	public TelefoniNonValidiException(String message, Throwable cause) {
		super(message, cause);
	}

	public TelefoniNonValidiException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
