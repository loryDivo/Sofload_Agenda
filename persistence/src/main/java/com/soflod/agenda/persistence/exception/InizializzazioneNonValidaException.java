package com.soflod.agenda.persistence.exception;

public class InizializzazioneNonValidaException extends Exception {

	public InizializzazioneNonValidaException() {
		super();
	}

	public InizializzazioneNonValidaException(String message) {
		super(message);
	}

	public InizializzazioneNonValidaException(Throwable cause) {
		super(cause);
	}

	public InizializzazioneNonValidaException(String message, Throwable cause) {
		super(message, cause);
	}

	public InizializzazioneNonValidaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
