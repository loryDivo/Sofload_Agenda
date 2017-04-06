package com.soflod.agenda.persistence.exception;

public class AggiuntaImpegnoException extends Exception {

	public AggiuntaImpegnoException() {
		super("Non e' stato possibile aggiungere l'impegno.");
	}

	public AggiuntaImpegnoException(String message) {
		super(message);
	}

	public AggiuntaImpegnoException(Throwable cause) {
		super(cause);
	}

	public AggiuntaImpegnoException(String message, Throwable cause) {
		super(message, cause);
	}

	public AggiuntaImpegnoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
