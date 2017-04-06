package com.soflod.agenda.persistence.exception;

public class AttivitaOrmNonValidaException extends Exception {

	public AttivitaOrmNonValidaException() {
		super();
	}

	public AttivitaOrmNonValidaException(String message) {
		super(message);
	}

	public AttivitaOrmNonValidaException(Throwable cause) {
		super(cause);
	}

	public AttivitaOrmNonValidaException(String message, Throwable cause) {
		super(message, cause);
	}

	public AttivitaOrmNonValidaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
