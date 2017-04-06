package com.soflod.agenda.persistence.exception;

public class AggiuntaCategoriaException extends Exception {

	public AggiuntaCategoriaException() {
		super("Non e' stato possibile aggiungere la nuova categoria.");
	}

	public AggiuntaCategoriaException(String message) {
		super(message);
	}

	public AggiuntaCategoriaException(Throwable cause) {
		super(cause);
	}

	public AggiuntaCategoriaException(String message, Throwable cause) {
		super(message, cause);
	}

	public AggiuntaCategoriaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
