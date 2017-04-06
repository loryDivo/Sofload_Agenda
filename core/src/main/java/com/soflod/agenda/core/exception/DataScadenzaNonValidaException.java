package com.soflod.agenda.core.exception;

public class DataScadenzaNonValidaException extends Exception {

	public DataScadenzaNonValidaException() {
		super("La data di scadenza non puo' essere precendente la data di inizio.");
	}

	public DataScadenzaNonValidaException(String message) {
		super(message);
	}

	public DataScadenzaNonValidaException(Throwable cause) {
		super(cause);
	}

	public DataScadenzaNonValidaException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataScadenzaNonValidaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
