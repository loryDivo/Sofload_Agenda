package com.soflod.agenda.core.exception;

public class DataInizioNonValidaException extends Exception {

	public DataInizioNonValidaException() {
		super("La data di inizio deve essere minore di quella di fine.");
	}

	public DataInizioNonValidaException(String message) {
		super(message);
	}

	public DataInizioNonValidaException(Throwable cause) {
		super(cause);
	}

	public DataInizioNonValidaException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataInizioNonValidaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
