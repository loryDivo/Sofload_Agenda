package com.soflod.agenda.persistence.exception;

public class ModificaContattoException extends Exception {

	public ModificaContattoException() {
		super("Non e' stato possibile modificare il contatto.");
	}

	public ModificaContattoException(String message) {
		super(message);
	}

	public ModificaContattoException(Throwable cause) {
		super(cause);
	}

	public ModificaContattoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModificaContattoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
