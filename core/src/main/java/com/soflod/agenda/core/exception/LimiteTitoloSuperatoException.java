package com.soflod.agenda.core.exception;

public class LimiteTitoloSuperatoException extends Exception {

	public LimiteTitoloSuperatoException() {
		super("Il numero massimo di caratteri per il titolo è stato superato.");
	}

	public LimiteTitoloSuperatoException(String message) {
		super(message);
	}

	public LimiteTitoloSuperatoException(Throwable cause) {
		super(cause);
	}

	public LimiteTitoloSuperatoException(String message, Throwable cause) {
		super(message, cause);
	}

	public LimiteTitoloSuperatoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
