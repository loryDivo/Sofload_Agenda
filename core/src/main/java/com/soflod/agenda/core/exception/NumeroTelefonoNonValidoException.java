package com.soflod.agenda.core.exception;

public class NumeroTelefonoNonValidoException extends Exception {

	public NumeroTelefonoNonValidoException() {
		super("Il numero di telefono non valido.");
	}

	public NumeroTelefonoNonValidoException(String message) {
		super(message);
	}

	public NumeroTelefonoNonValidoException(Throwable cause) {
		super(cause);
	}

	public NumeroTelefonoNonValidoException(String message, Throwable cause) {
		super(message, cause);
	}

	public NumeroTelefonoNonValidoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
