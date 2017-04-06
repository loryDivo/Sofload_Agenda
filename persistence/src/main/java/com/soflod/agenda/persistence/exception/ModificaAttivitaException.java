package com.soflod.agenda.persistence.exception;

public class ModificaAttivitaException extends Exception {

	public ModificaAttivitaException() {
		super("Attivita non modifica");
	}

	public ModificaAttivitaException(String message) {
		super(message);
	}

	public ModificaAttivitaException(Throwable cause) {
		super(cause);
	}

	public ModificaAttivitaException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModificaAttivitaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
