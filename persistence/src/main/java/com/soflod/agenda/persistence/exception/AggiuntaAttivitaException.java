package com.soflod.agenda.persistence.exception;

public class AggiuntaAttivitaException extends Exception {

	public AggiuntaAttivitaException() {
		super("Non e' stato possibile aggiungere l'attivita");
	}

	public AggiuntaAttivitaException(String message) {
		super(message);
	}

	public AggiuntaAttivitaException(Throwable cause) {
		super(cause);
	}

	public AggiuntaAttivitaException(String message, Throwable cause) {
		super(message, cause);
	}

	public AggiuntaAttivitaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
