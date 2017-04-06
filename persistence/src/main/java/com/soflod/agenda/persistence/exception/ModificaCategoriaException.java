package com.soflod.agenda.persistence.exception;

public class ModificaCategoriaException extends Exception {

	public ModificaCategoriaException() {
		super("Impossibile modificare la categoria.");
	}

	public ModificaCategoriaException(String message) {
		super(message);
	}

	public ModificaCategoriaException(Throwable cause) {
		super(cause);
	}

	public ModificaCategoriaException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModificaCategoriaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
