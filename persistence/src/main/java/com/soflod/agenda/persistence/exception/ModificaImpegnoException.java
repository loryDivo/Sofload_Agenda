package com.soflod.agenda.persistence.exception;

public class ModificaImpegnoException extends Exception {

	public ModificaImpegnoException() {
		super("Non è stato possibile modificare l'impegno");
	}

	public ModificaImpegnoException(String message) {
		super(message);
	}

	public ModificaImpegnoException(Throwable cause) {
		super(cause);
	}

	public ModificaImpegnoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModificaImpegnoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
