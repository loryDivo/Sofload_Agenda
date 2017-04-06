package com.soflod.agenda.persistence.exception;

public class RimozioneAttivitaException extends Exception {

	public RimozioneAttivitaException() {
		super("Non e' stato possibile rimuovere l'attivita.");
	}

	public RimozioneAttivitaException(String message) {
		super(message);
	}

	public RimozioneAttivitaException(Throwable cause) {
		super(cause);
	}

	public RimozioneAttivitaException(String message, Throwable cause) {
		super(message, cause);
	}

	public RimozioneAttivitaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
