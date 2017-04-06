package com.soflod.agenda.persistence.exception;

public class ContattoOrmNonValidoException extends Exception {

	public ContattoOrmNonValidoException() {
		super();
	}

	public ContattoOrmNonValidoException(String message) {
		super(message);
	}

	public ContattoOrmNonValidoException(Throwable cause) {
		super(cause);
	}

	public ContattoOrmNonValidoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ContattoOrmNonValidoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
