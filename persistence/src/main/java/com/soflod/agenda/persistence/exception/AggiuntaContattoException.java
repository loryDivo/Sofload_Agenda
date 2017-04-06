package com.soflod.agenda.persistence.exception;

public class AggiuntaContattoException extends Exception {

	public AggiuntaContattoException() {
		super("Non e' stato possibile aggiungere il contatto.");
	}

	public AggiuntaContattoException(String message) {
		super(message);
	}

	public AggiuntaContattoException(Throwable cause) {
		super(cause);
	}

	public AggiuntaContattoException(String message, Throwable cause) {
		super(message, cause);
	}

	public AggiuntaContattoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
