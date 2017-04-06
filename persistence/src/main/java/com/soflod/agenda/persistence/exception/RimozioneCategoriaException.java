package com.soflod.agenda.persistence.exception;

public class RimozioneCategoriaException extends Exception {

	public RimozioneCategoriaException() {
		super("Non e' stato possibile rimuovere la categoria.");
	}

	public RimozioneCategoriaException(String message) {
		super(message);
	}

	public RimozioneCategoriaException(Throwable cause) {
		super(cause);
	}

	public RimozioneCategoriaException(String message, Throwable cause) {
		super(message, cause);
	}

	public RimozioneCategoriaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
